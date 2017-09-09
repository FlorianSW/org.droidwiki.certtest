package org.droidwiki.certtest.natives;

import org.droidwiki.certtest.structures.CERT_CONTEXT;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.win32.StdCallLibrary;

public interface CryptUILibrary extends StdCallLibrary {
	CryptUILibrary INSTANCE = Native.loadLibrary("Cryptui", CryptUILibrary.class);

	CERT_CONTEXT CryptUIDlgSelectCertificateFromStore(HANDLE hCertStore, HANDLE hwnd, String pwszTitle,
			String pwszDisplayString, int dwDontUseColumn, int dwFlags, int pvReserved);
}
