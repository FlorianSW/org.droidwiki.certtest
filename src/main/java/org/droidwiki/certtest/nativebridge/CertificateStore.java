package org.droidwiki.certtest.nativebridge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.droidwiki.certtest.natives.Crypt32NativeFunctions;
import org.droidwiki.certtest.structures.CERT_CONTEXT;

import com.sun.jna.platform.win32.WinNT.HANDLE;

public class CertificateStore extends AbstractNativeObject implements Iterable<Certificate> {
	private HANDLE storeHandle;

	public CertificateStore(HANDLE store) {
		this.storeHandle = store;
	}

	/**
	 * Opens the Windows-MY certificate store, which contains personal
	 * certificates with private keys owned by the user.
	 * 
	 * @return
	 */
	public static CertificateStore getMySystemCertStore() {
		return new CertificateStore(Crypt32NativeFunctions.CertOpenSystemStore(0, "MY"));
	}

	/**
	 * Creates a temporary certificate store in the cache/memory to store
	 * certificates for short time of period during the application lifecycle.
	 * The store is not saved automatically somewhere else and is lost after the
	 * application exits.
	 * 
	 * @return
	 */
	public static CertificateStore newCachedCertStore() {
		return new CertificateStore(Crypt32NativeFunctions.CertOpenStore(2, 0, 0, 0, 0));
	}

	/**
	 * Get a list of certificates in this store.
	 * 
	 * @return
	 */
	public List<Certificate> getCertificatesInStore() {
		checkFreed();
		List<Certificate> certContextList = new ArrayList<>();
		forEach(certContext -> {
			CERT_CONTEXT newCertContext = Crypt32NativeFunctions.CertCreateCertificateContext(1,
					certContext.getEncodedByteArray(), certContext.getEncodedLength());
			certContextList.add(new Certificate(newCertContext));
		});

		return certContextList;
	}

	@Override
	public Object getNative() {
		checkFreed();
		return storeHandle;
	}

	/**
	 * Adds the given certificate to this store.
	 * 
	 * @param certificate
	 */
	public void addCertificateToStore(Certificate certificate) {
		checkFreed();
		Crypt32NativeFunctions.CertAddCertificateLinkToStore(storeHandle, (CERT_CONTEXT) certificate.getNative(), 3,
				null);
	}

	@Override
	public void free() {
		super.free();
		Crypt32NativeFunctions.CertCloseStore(storeHandle, 0);
	}

	/**
	 * Iterates over the list of certificates in this store and executes the
	 * passed Consumer on it.
	 * 
	 * Be aware: The passed certificate is freed before the next certificate
	 * will be passed to the Consumer. Make sure, that you do not reference it
	 * anymore after that or create a new certificate from the encoded version
	 * (e.g. with {@link Crypt32NativeFunctions#CertCreateCertificateContext} or
	 * use {@link #getCertificatesInStore()}.
	 */
	@Override
	public void forEach(Consumer<? super Certificate> action) {
		checkFreed();
		CERT_CONTEXT toTestCert = Crypt32NativeFunctions.CertEnumCertificatesInStore(storeHandle, null);
		do {
			action.accept(new Certificate(toTestCert));
			toTestCert = Crypt32NativeFunctions.CertEnumCertificatesInStore(storeHandle, toTestCert);
		} while (toTestCert != null);
	}

	@Override
	public Iterator<Certificate> iterator() {
		checkFreed();
		return getCertificatesInStore().iterator();
	}

}
