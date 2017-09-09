package org.droidwiki.certtest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.droidwiki.certtest.natives.Crypt32NativeFunctions;
import org.droidwiki.certtest.natives.CryptUINativeFunctions;
import org.droidwiki.certtest.natives.Kernel32NativeFunctions;
import org.droidwiki.certtest.structures.CERT_CONTEXT;
import org.droidwiki.certtest.structures.CTL_USAGE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.DWORDByReference;
import com.sun.jna.platform.win32.WinNT.HANDLE;

public class App {
	private final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		App app = new App();
		app.run();
	}

	public void run() {
		logger.debug("Initializing native objects");
		HANDLE mySystemStore = getMySystemCertStore();
		HANDLE temporaryStore = getIntermediateCertStore();
		fillTemporaryCertStore(mySystemStore, temporaryStore);
		logger.debug("Prompting the user to select a certificate");
		CERT_CONTEXT selectedCertContext = selectCertificateWithDialog(temporaryStore);
		if (selectedCertContext == null) {
			logger.debug(
					"The user has aborted the certificate selection or something else went wrong in a native method.");
			return;
		}

		X509Certificate cert;
		try {
			cert = getCertFromCertContext(selectedCertContext);
		} catch (CertificateException e) {
			logger.debug("Could not create X509Certificate from CERT_CONTEXT.", e);
			return;
		}

		char[] ptrName = new char[128];
		String certificateAlias = Crypt32NativeFunctions.CertGetNameStringW(selectedCertContext, 5, 0, 0, ptrName, 128);
		logger.info("Selected certificate is {}", certificateAlias);

		// release the native things here, we don't need it here anymore
		releaseCertContextAndStore(mySystemStore, temporaryStore, selectedCertContext);

		// building a new KeyStore from the selected certificate
		KeyStore ks;
		try {
			ks = KeyStore.getInstance(KeyStore.getDefaultType());
			ks.load(null, null);
			ks.setCertificateEntry(certificateAlias, cert);
			logger.info("The public key of the selected certificate is: {}", cert.getPublicKey());
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Frees the native, passed certificate store HANDLE and the CERT_CONTEXT.
	 * 
	 * @param mySystemStore
	 * @param selectedCertContext
	 */
	private void releaseCertContextAndStore(HANDLE mySystemStore, HANDLE temporaryStore,
			CERT_CONTEXT selectedCertContext) {
		Crypt32NativeFunctions.CertFreeCertificateContext(selectedCertContext);

		Crypt32NativeFunctions.CertCloseStore(temporaryStore, 0);
		Crypt32NativeFunctions.CertCloseStore(mySystemStore, 0);
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
	private CERT_CONTEXT selectCertificateWithDialog(HANDLE temporaryStore) {
		Object[] argsCryptUIDlgSelectCertificateFromStore = new Object[] { temporaryStore, null, null, null, 0, 0,
				null };
		CERT_CONTEXT selectedCertContext;
		try {
			selectedCertContext = (CERT_CONTEXT) CryptUINativeFunctions.CryptUIDlgSelectCertificateFromStore
					.invoke(CERT_CONTEXT.class, argsCryptUIDlgSelectCertificateFromStore);
		} catch (Error err) {
			int error = Kernel32NativeFunctions.GetLastError.invokeInt(null);
			System.out.println(error);
			return null;
		}
		return selectedCertContext;
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
	private void fillTemporaryCertStore(HANDLE mySystemStore, HANDLE temporaryStore) {
		CERT_CONTEXT toTestCert = Crypt32NativeFunctions.CertEnumCertificatesInStore(mySystemStore, null);
		do {
			List<KeyUsage> kuList = getKeyUsage(toTestCert);
			List<String> ekuList = getExtendedKeyUsages(toTestCert);
			boolean kuOk = !kuList.isEmpty() && kuList.contains(KeyUsage.digitalSignature);
			boolean ekuOk = !ekuList.isEmpty()
					&& (ekuList.contains(CertificateConstants.ExtendedKeyUsage.SmartcardLogon));
			if (ekuOk && kuOk) {
				Crypt32NativeFunctions.CertAddCertificateLinkToStore(temporaryStore, toTestCert, 3, null);
			}
			toTestCert = Crypt32NativeFunctions.CertEnumCertificatesInStore(mySystemStore, toTestCert);
		} while (toTestCert != null);
	}

	/**
	 * Opens the Windows-MY certificate store, which contains personal
	 * certificates with private keys owned by the user.
	 * 
	 * @return
	 */
	private HANDLE getMySystemCertStore() {
		return Crypt32NativeFunctions.CertOpenSystemStore(0, "MY");
	}

	/**
	 * Creates a temporary certificate store in the cache/memory to store
	 * certificates for short time of period during the application lifecycle.
	 * The store is not saved automatically somewhere else and is lost after the
	 * application exits.
	 * 
	 * @return
	 */
	private HANDLE getIntermediateCertStore() {
		return Crypt32NativeFunctions.CertOpenStore(2, 0, 0, 0, 0);
	}

	/**
	 * Returns a list of KeyUsages of this certificate. Unlike the
	 * {@link X509Certificate#getKeyUsage()} method, it returns a {@link List}
	 * of {@link KeyUsage} instead of an array of Booleans.
	 * 
	 * This method returns an empty list of KeyUsage if the CERT_CONTEXT does
	 * not have the pbCertEncoded or cbCertEncoded fields be set.
	 * 
	 * @param certContext
	 * @return
	 */
	private List<KeyUsage> getKeyUsage(CERT_CONTEXT certContext) {
		List<KeyUsage> keyUsagesList = new ArrayList<>();
		// if the certificate can not be encoded, return an empty list
		if (certContext.pbCertEncoded == null || certContext.cbCertEncoded == 0) {
			return keyUsagesList;
		}
		try {
			// Convert native type to X509Certificate, which allows an easier
			// access to the KeyUsage extension
			X509Certificate cert = getCertFromCertContext(certContext);
			boolean[] certKeyUsages = cert.getKeyUsage();
			for (int i = 0; i < KeyUsage.values().length; i++) {
				KeyUsage usage = KeyUsage.values()[i];
				if (certKeyUsages[i]) {
					keyUsagesList.add(usage);
				}
			}

		} catch (CertificateException e) {
			logger.error("Could not load key usage of certificate", e);
		}

		return keyUsagesList;
	}

	/**
	 * Creates a Java X509Certificate object out of a native CERT_CONTEXT
	 * structure from Windows MS Crypto API.
	 * 
	 * This method requires both, the pbCertEncoded and cbCertEncoded, values of
	 * the CERT_CONTEXT structure to be non-null.
	 * 
	 * @param certContext
	 * @return
	 * @throws CertificateException
	 */
	private X509Certificate getCertFromCertContext(CERT_CONTEXT certContext) throws CertificateException {
		Objects.requireNonNull(certContext.pbCertEncoded);
		Objects.requireNonNull(certContext.cbCertEncoded);

		CertificateFactory fac = CertificateFactory.getInstance(CertificateConstants.X509CertificateType);
		X509Certificate cert = (X509Certificate) fac.generateCertificate(
				new ByteArrayInputStream(certContext.pbCertEncoded.getByteArray(0, certContext.cbCertEncoded)));
		return cert;
	}

	/**
	 * Returns a list of OIDs of the enhanced/extended key usage extension of
	 * the passed CERT_CONTEXT structure. The implementation, at the moment,
	 * relies on communication with the native crypto API.
	 * 
	 * TODO: Can probably just use {@link #getCertFromCertContext(CERT_CONTEXT)}
	 * {@link X509Certificate#getExtendedKeyUsage()}.
	 * 
	 * @param certContext
	 * @return
	 */
	private List<String> getExtendedKeyUsages(CERT_CONTEXT certContext) {
		List<String> extendedKeyUsages = new ArrayList<>();
		DWORDByReference lengthCertGetEnhancedKeyUsage = new DWORDByReference();
		boolean result = Crypt32NativeFunctions.CertGetEnhancedKeyUsage(certContext, 0, null,
				lengthCertGetEnhancedKeyUsage);
		if (!result) {
			return extendedKeyUsages;
		}
		logger.debug("Got the following length for the enhanced key usage array: "
				+ lengthCertGetEnhancedKeyUsage.getValue());

		CTL_USAGE data = new CTL_USAGE();
		result = Crypt32NativeFunctions.CertGetEnhancedKeyUsage(certContext, 0, data, lengthCertGetEnhancedKeyUsage);
		for (Pointer pointer : data.rgpszUsageIdentifier) {
			try {
				String ekuOid = pointer.getString(0);
				extendedKeyUsages.add(ekuOid);
			} catch (Error | Exception ex) {
				// The array of Pointers has a fixed size, this exception/error
				// happens, when the end is reached or something else went
				// wrong, just go ahead and take the next array position
			}
		}
		if (!extendedKeyUsages.isEmpty() && extendedKeyUsages.size() > 1) {
			// the first OID is not an OID, but all OIDs plus some weird
			// character, I haven't found a workaround for that, so just
			// remove it.
			extendedKeyUsages.remove(0);
		}

		return extendedKeyUsages;
	}
}
