package org.droidwiki.certtest.nativebridge;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.droidwiki.certtest.CertificateConstants;
import org.droidwiki.certtest.KeyUsage;
import org.droidwiki.certtest.natives.Crypt32Library;
import org.droidwiki.certtest.structures.CERT_CONTEXT;
import org.droidwiki.certtest.structures.CTL_USAGE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.DWORDByReference;

public class Certificate extends AbstractNativeObject {
	private final static Logger logger = LoggerFactory.getLogger(Certificate.class);

	private CERT_CONTEXT certContext;

	public Certificate(CERT_CONTEXT certContext) {
		this.certContext = certContext;
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
	public X509Certificate getX509Certificate() throws CertificateException {
		checkFreed();
		Objects.requireNonNull(certContext.pbCertEncoded);
		Objects.requireNonNull(certContext.cbCertEncoded);

		CertificateFactory fac = CertificateFactory.getInstance(CertificateConstants.X509CertificateType);
		X509Certificate cert = (X509Certificate) fac.generateCertificate(
				new ByteArrayInputStream(certContext.pbCertEncoded.getByteArray(0, certContext.cbCertEncoded)));
		return cert;
	}

	/**
	 * The name/alias of the certificate as returned by the CertGetNameString
	 * method.
	 * 
	 * @return
	 */
	public String getAlias() {
		checkFreed();
		char[] ptrName = new char[128];
		Crypt32Library.INSTANCE.CertGetNameStringW(certContext, 5, 0, 0, ptrName, 128);
		return new String(ptrName);
	}

	/**
	 * The length of the array returned by {@link #getEncodedByteArray()}.
	 * 
	 * @return
	 */
	public int getEncodedLength() {
		checkFreed();
		Objects.requireNonNull(certContext.pbCertEncoded);
		Objects.requireNonNull(certContext.cbCertEncoded);

		return certContext.cbCertEncoded;
	}

	/**
	 * Returns the certificate as an encoded byte array, which represents the
	 * whole certificate with all of it's attributes.
	 * 
	 * @return
	 */
	public byte[] getEncodedByteArray() {
		checkFreed();
		Objects.requireNonNull(certContext.pbCertEncoded);
		Objects.requireNonNull(certContext.cbCertEncoded);

		return certContext.pbCertEncoded.getByteArray(0, certContext.cbCertEncoded);
	}

	/**
	 * Returns a list of KeyUsages of this certificate. Unlike the
	 * {@link X509Certificate#getKeyUsage()} method, it returns a {@link List}
	 * of {@link KeyUsage} instead of an array of Booleans.
	 * 
	 * This method returns an empty list of KeyUsage if the CERT_CONTEXT does
	 * not have the pbCertEncoded or cbCertEncoded fields be set.
	 * 
	 * @return
	 */
	public List<KeyUsage> getKeyUsage() {
		checkFreed();
		List<KeyUsage> keyUsagesList = new ArrayList<>();
		// if the certificate can not be encoded, return an empty list
		if (certContext.pbCertEncoded == null || certContext.cbCertEncoded == 0) {
			return keyUsagesList;
		}
		try {
			// Convert native type to X509Certificate, which allows an easier
			// access to the KeyUsage extension
			X509Certificate cert = getX509Certificate();
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
	 * Returns a list of OIDs of the enhanced/extended key usage extension of
	 * the passed CERT_CONTEXT structure. The implementation, at the moment,
	 * relies on communication with the native crypto API.
	 * 
	 * TODO: Can probably just use {@link #getCertFromCertContext(CERT_CONTEXT)}
	 * {@link X509Certificate#getExtendedKeyUsage()}.
	 * 
	 * @return
	 */
	public List<String> getExtendedKeyUsages() {
		checkFreed();
		Objects.requireNonNull(certContext);
		List<String> extendedKeyUsages = new ArrayList<>();
		DWORDByReference lengthCertGetEnhancedKeyUsage = new DWORDByReference();
		boolean result = Crypt32Library.INSTANCE.CertGetEnhancedKeyUsage(certContext, 0, null,
				lengthCertGetEnhancedKeyUsage);
		if (!result) {
			return extendedKeyUsages;
		}
		logger.debug("Got the following length for the enhanced key usage array: "
				+ lengthCertGetEnhancedKeyUsage.getValue());

		CTL_USAGE data = new CTL_USAGE();
		result = Crypt32Library.INSTANCE.CertGetEnhancedKeyUsage(certContext, 0, data, lengthCertGetEnhancedKeyUsage);
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

	@Override
	public Object getNative() {
		checkFreed();
		return certContext;
	}

	@Override
	public void free() {
		super.free();
		Crypt32Library.INSTANCE.CertFreeCertificateContext(certContext);
	}

}
