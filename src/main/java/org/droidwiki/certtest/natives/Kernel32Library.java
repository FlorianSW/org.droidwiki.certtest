package org.droidwiki.certtest.natives;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface Kernel32Library extends StdCallLibrary {
	Kernel32Library INSTANCE = (Kernel32Library) Native.loadLibrary("kernel32", Kernel32Library.class);

	int GetLastError();
}
