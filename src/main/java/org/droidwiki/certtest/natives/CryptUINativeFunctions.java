package org.droidwiki.certtest.natives;

import com.sun.jna.Function;
import com.sun.jna.NativeLibrary;

public class CryptUINativeFunctions {
	private final static NativeLibrary cryptUI = NativeLibrary.getInstance("Cryptui");

	public final static Function CryptUIDlgSelectCertificateFromStore = cryptUI
			.getFunction("CryptUIDlgSelectCertificateFromStore");
}
