package org.droidwiki.certtest;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import org.droidwiki.certtest.nativebridge.Certificate;
import org.droidwiki.certtest.nativebridge.CertificateStore;
import org.droidwiki.certtest.natives.CryptUINativeFunctions;
import org.droidwiki.certtest.natives.Kernel32NativeFunctions;
import org.droidwiki.certtest.structures.CERT_CONTEXT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		App app = new App();
		app.run();
	}

	public void run() {
		logger.debug("Initializing native objects");
		CertificateStore mySystemStore = CertificateStore.getMySystemCertStore();
		CertificateStore temporaryStore = CertificateStore.newCachedCertStore();
		fillTemporaryCertStore(mySystemStore, temporaryStore);
		logger.debug("Prompting the user to select a certificate");
		Certificate selectedCertContext = selectCertificateWithDialog(temporaryStore);
		if (selectedCertContext == null) {
			logger.debug(
					"The user has aborted the certificate selection or something else went wrong in a native method.");
			return;
		}

		X509Certificate cert;
		try {
			cert = selectedCertContext.getX509Certificate();
		} catch (CertificateException e) {
			logger.debug("Could not create X509Certificate from CERT_CONTEXT.", e);
			return;
		}

		String certificateAlias = selectedCertContext.getAlias();
		logger.info("Selected certificate is {}", certificateAlias);

		// release the native things here, we don't need it here anymore
		mySystemStore.free();
		temporaryStore.free();
		selectedCertContext.free();

		// building a new KeyStore from the selected certificate
		KeyStore ks;
		try {
			ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(null, null);
			ks.setCertificateEntry(certificateAlias, cert);
			logger.info("The public key of the selected certificate is: {}", cert.getPublicKey());
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			logger.error("Could not initialize a new keystore.", e);
		}

	}

	/**
	 * Prompts the user with a certificate selection prompt with a list of
	 * certificates in the passed certificate store handle.
	 * 
	 * The CERT_CONTEXT of the certificate selected by the user will be returned
	 * by this function. Null is returned if the user aborted the selection
	 * dialog or if another error occurred.
	 * 
	 * @param temporaryStore
	 * @return
	 */
	private Certificate selectCertificateWithDialog(CertificateStore temporaryStore) {
		Object[] argsCryptUIDlgSelectCertificateFromStore = new Object[] { temporaryStore.getNative(), null, null, null,
				0, 0, null };
		CERT_CONTEXT selectedCertContext;
		try {
			selectedCertContext = (CERT_CONTEXT) CryptUINativeFunctions.CryptUIDlgSelectCertificateFromStore
					.invoke(CERT_CONTEXT.class, argsCryptUIDlgSelectCertificateFromStore);
		} catch (Error err) {
			int error = Kernel32NativeFunctions.GetLastError.invokeInt(null);
			System.out.println(error);
			return null;
		}
		if (selectedCertContext == null) {
			return null;
		}
		return new Certificate(selectedCertContext);
	}

	/**
	 * Iterates over all certificates of the passed HANDLE mySystemStore, which
	 * should be an already populated certificate store, and links these
	 * certificates to the temporary certificate store.
	 * 
	 * This method filters the certificate based on the following criteria,
	 * which a certificate need to fulfill to be used as a client login
	 * certificate:
	 * 
	 * <ul>
	 * <li>Needs to have at least the keyUsage
	 * {@link KeyUsage#digitalSignature}</li>
	 * <li>Needs one of the following OIDs in the Extended Key Usage extension:
	 * {@link CertificateConstants.ExtendedKeyUsage#SmartcardLogon}</li>
	 * </ul>
	 * 
	 * That the certificate needs a private key (e.g. checked with the
	 * CryptAcquireCertificatePrivateKey native method) is explicitly not a
	 * requirement for a certificate ending up in the temporary certificate
	 * store. Checking this beforehand may result in a CSP requesting a hard
	 * token or password to open the private key, if that is required by the
	 * certificate iterated over in the system certificate store.
	 * 
	 * @param mySystemStore
	 * @param temporaryStore
	 */
	private void fillTemporaryCertStore(CertificateStore mySystemStore, CertificateStore temporaryStore) {
		mySystemStore.forEach(certificate -> {
			List<KeyUsage> kuList = certificate.getKeyUsage();
			List<String> ekuList = certificate.getExtendedKeyUsages();
			boolean kuOk = !kuList.isEmpty() && kuList.contains(KeyUsage.digitalSignature);
			boolean ekuOk = !ekuList.isEmpty()
					&& (ekuList.contains(CertificateConstants.ExtendedKeyUsage.SmartcardLogon));
			if (ekuOk && kuOk) {
				temporaryStore.addCertificateToStore(certificate);
			}
		});
	}
}
