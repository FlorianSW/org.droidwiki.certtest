package org.droidwiki.certtest.nativebridge;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinNT.HANDLE;

public interface NativeObject {
	/**
	 * Returns the nearest native representation of this object, may it be a
	 * {@link HANDLE} or a subclass of {@link Structure} or whatever.
	 * 
	 * @return
	 */
	Object getNative();

	/**
	 * Frees the native object so that it can't be accessed anymore. Make sure,
	 * that you do not reference any functions on this instance anymore, as it
	 * will throw an {@link IllegalAccessError}.
	 */
	void free();
}
