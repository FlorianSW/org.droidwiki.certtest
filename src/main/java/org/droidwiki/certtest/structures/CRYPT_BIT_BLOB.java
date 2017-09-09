package org.droidwiki.certtest.structures;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class CRYPT_BIT_BLOB extends Structure {
	public int cbData;
	/** C type : BYTE* */
	public Pointer pbData;
	public int cUnusedBits;

	public CRYPT_BIT_BLOB() {
		super();
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList("cbData", "pbData", "cUnusedBits");
	}

	/**
	 * @param pbData
	 *            C type : BYTE*
	 */
	public CRYPT_BIT_BLOB(int cbData, Pointer pbData, int cUnusedBits) {
		super();
		this.cbData = cbData;
		this.pbData = pbData;
		this.cUnusedBits = cUnusedBits;
	}

	public static class ByReference extends CRYPT_BIT_BLOB implements Structure.ByReference {

	};

	public static class ByValue extends CRYPT_BIT_BLOB implements Structure.ByValue {

	};
}
