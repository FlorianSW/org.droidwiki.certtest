package org.droidwiki.certtest.natives;

import org.droidwiki.certtest.structures.CERT_CONTEXT;

import com.sun.jna.Function;
import com.sun.jna.NativeLibrary;
import com.sun.jna.platform.win32.WinNT.HANDLE;

public class CryptUINativeFunctions {
	private final static NativeLibrary cryptUI = NativeLibrary.getInstance("Cryptui");

	public final static Function CryptUIDlgSelectCertificateFromStore = cryptUI
			.getFunction("CryptUIDlgSelectCertificateFromStore");

	public static CERT_CONTEXT CryptUIDlgSelectCertificateFromStore(HANDLE hCertStore, HANDLE hwnd, String pwszTitle,
			String pwszDisplayString, int dwDontUseColumn, int dwFlags, int pvReserved) {
		Object[] argsCryptUIDlgSelectCertificateFromStore = new Object[] { hCertStore, hwnd, pwszTitle,
				pwszDisplayString, dwDontUseColumn, dwFlags, pvReserved };
		return (CERT_CONTEXT) CryptUINativeFunctions.CryptUIDlgSelectCertificateFromStore.invoke(CERT_CONTEXT.class,
				argsCryptUIDlgSelectCertificateFromStore);
	}
}
