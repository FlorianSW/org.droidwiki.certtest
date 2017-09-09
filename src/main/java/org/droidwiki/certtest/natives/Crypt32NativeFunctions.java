package org.droidwiki.certtest.natives;

import org.droidwiki.certtest.structures.CERT_CONTEXT;
import org.droidwiki.certtest.structures.CTL_USAGE;

import com.sun.jna.Function;
import com.sun.jna.NativeLibrary;
import com.sun.jna.platform.win32.WinDef.DWORDByReference;
import com.sun.jna.platform.win32.WinNT.HANDLE;

public class Crypt32NativeFunctions {
	private final static NativeLibrary crypt32 = NativeLibrary.getInstance("Crypt32");

	public final static Function CertOpenSystemStore = crypt32.getFunction("CertOpenSystemStoreA");
	public final static Function CertOpenStore = crypt32.getFunction("CertOpenStore");
	public final static Function CertAddCertificateLinkToStore = crypt32.getFunction("CertAddCertificateLinkToStore");
	public final static Function CertEnumCertificatesInStore = crypt32.getFunction("CertEnumCertificatesInStore");
	public final static Function CertGetNameStringW = crypt32.getFunction("CertGetNameStringW");
	public final static Function CertFreeCertificateContext = crypt32.getFunction("CertFreeCertificateContext");
	public final static Function CertCloseStore = crypt32.getFunction("CertCloseStore");
	public final static Function CertGetEnhancedKeyUsage = crypt32.getFunction("CertGetEnhancedKeyUsage");

	public static HANDLE CertOpenSystemStore(int hprov, String szSubsystemProtocol) {
		Object[] argsCertOpenSystemStore = new Object[] { hprov, szSubsystemProtocol };
		return (HANDLE) Crypt32NativeFunctions.CertOpenSystemStore.invoke(HANDLE.class, argsCertOpenSystemStore);
	}

	public static HANDLE CertOpenStore(int lpszStoreProvider, int dwMsgAndCertEncodingType, int hCryptProv, int dwFlags,
			int pvPara) {
		Object[] argsCertOpenStore = new Object[] { lpszStoreProvider, dwMsgAndCertEncodingType, hCryptProv, dwFlags,
				pvPara };
		return (HANDLE) Crypt32NativeFunctions.CertOpenStore.invoke(HANDLE.class, argsCertOpenStore);
	}

	public static void CertAddCertificateLinkToStore(HANDLE hCertStore, CERT_CONTEXT pCertContext, int dwAddDisposition,
			CERT_CONTEXT ppStoreContext) {
		Object[] argsCertAddCertificateLinkToStore = new Object[] { hCertStore, pCertContext, dwAddDisposition,
				ppStoreContext };
		Crypt32NativeFunctions.CertAddCertificateLinkToStore.invoke(argsCertAddCertificateLinkToStore);
	}

	public static CERT_CONTEXT CertEnumCertificatesInStore(HANDLE hCertStore, CERT_CONTEXT pPrevCertContext) {
		Object[] argsCertEnumCertificatesInStore = new Object[] { hCertStore, pPrevCertContext };
		return (CERT_CONTEXT) Crypt32NativeFunctions.CertEnumCertificatesInStore.invoke(CERT_CONTEXT.class,
				argsCertEnumCertificatesInStore);
	}

	public static String CertGetNameStringW(CERT_CONTEXT pCertContext, int dwType, int dwFlags, int pvTypePara,
			char[] pszNameString, int cchNameString) {
		Object[] argsCertGetNameString = new Object[] { pCertContext, dwType, dwFlags, pvTypePara, pszNameString,
				cchNameString };
		Crypt32NativeFunctions.CertGetNameStringW.invoke(argsCertGetNameString);
		return new String(pszNameString);
	}

	public static void CertFreeCertificateContext(CERT_CONTEXT pCertContext) {
		Object[] argsCertFreeCertificateContext = new Object[] { pCertContext };
		Crypt32NativeFunctions.CertFreeCertificateContext.invoke(argsCertFreeCertificateContext);
	}

	public static void CertCloseStore(HANDLE hCertStore, int dwFlags) {
		Object[] argsCertCloseStore = new Object[] { hCertStore, dwFlags };
		Crypt32NativeFunctions.CertCloseStore.invoke(argsCertCloseStore);
	}

	public static boolean CertGetEnhancedKeyUsage(CERT_CONTEXT pCertContext, int dwFlags, CTL_USAGE pUsage,
			DWORDByReference pcbUsage) {
		Object[] argsCertGetEnhancedKeyUsage = new Object[] { pCertContext, dwFlags, pUsage, pcbUsage };
		Boolean result = (Boolean) Crypt32NativeFunctions.CertGetEnhancedKeyUsage.invoke(Boolean.class,
				argsCertGetEnhancedKeyUsage);

		return result.booleanValue();
	}
}
