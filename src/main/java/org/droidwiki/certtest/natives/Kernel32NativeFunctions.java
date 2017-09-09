package org.droidwiki.certtest.natives;

import com.sun.jna.Function;
import com.sun.jna.NativeLibrary;

public class Kernel32NativeFunctions {
	private final static NativeLibrary kernel32 = NativeLibrary.getInstance("Kernel32");

	public final static Function GetLastError = kernel32.getFunction("GetLastError");
}
