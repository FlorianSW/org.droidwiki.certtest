package org.droidwiki.certtest.natives;

import org.droidwiki.certtest.structures.CERT_CONTEXT;
import org.droidwiki.certtest.structures.CTL_USAGE;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef.DWORDByReference;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.win32.StdCallLibrary;

public interface Crypt32Library extends StdCallLibrary {
	Crypt32Library INSTANCE = (Crypt32Library) Native.loadLibrary("Crypt32", Crypt32Library.class);

	void CertAddCertificateLinkToStore(HANDLE hCertStore, CERT_CONTEXT pCertContext, int dwAddDisposition,
			CERT_CONTEXT ppStoreContext);

	CERT_CONTEXT CertEnumCertificatesInStore(HANDLE hCertStore, CERT_CONTEXT pPrevCertContext);

	void CertGetNameStringW(CERT_CONTEXT pCertContext, int dwType, int dwFlags, int pvTypePara, char[] pszNameString,
			int cchNameString);

	void CertFreeCertificateContext(CERT_CONTEXT pCertContext);

	void CertCloseStore(HANDLE hCertStore, int dwFlags);

	boolean CertGetEnhancedKeyUsage(CERT_CONTEXT pCertContext, int dwFlags, CTL_USAGE pUsage,
			DWORDByReference pcbUsage);

	CERT_CONTEXT CertCreateCertificateContext(int dwCertEncodingType, byte[] pbCertEncoded, int cbCertEncoded);

	HANDLE CertOpenSystemStoreA(int hprov, String szSubsystemProtocol);

	HANDLE CertOpenStore(int lpszStoreProvider, int dwMsgAndCertEncodingType, int hCryptProv, int dwFlags, int pvPara);
}
