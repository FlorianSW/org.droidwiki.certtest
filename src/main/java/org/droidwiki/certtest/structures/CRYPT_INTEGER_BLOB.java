package org.droidwiki.certtest.structures;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class CRYPT_INTEGER_BLOB extends Structure {
	public int cbData;
	/** C type : BYTE* */
	public Pointer pbData;

	public CRYPT_INTEGER_BLOB() {
		super();
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList("cbData", "pbData");
	}

	/**
	 * @param pbData
	 *            C type : BYTE*
	 */
	public CRYPT_INTEGER_BLOB(int cbData, Pointer pbData) {
		super();
		this.cbData = cbData;
		this.pbData = pbData;
	}

	public static class ByReference extends CRYPT_INTEGER_BLOB implements Structure.ByReference {

	};

	public static class ByValue extends CRYPT_INTEGER_BLOB implements Structure.ByValue {

	};
}
