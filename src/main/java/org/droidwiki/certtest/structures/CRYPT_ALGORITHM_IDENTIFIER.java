package org.droidwiki.certtest.structures;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WTypes.LPSTR;

public class CRYPT_ALGORITHM_IDENTIFIER extends Structure {
	/** C type : LPSTR */
	public LPSTR pszObjId;
	/** C type : CRYPT_OBJID_BLOB */
	public CRYPT_INTEGER_BLOB Parameters;

	public CRYPT_ALGORITHM_IDENTIFIER() {
		super();
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList("pszObjId", "Parameters");
	}

	/**
	 * @param pszObjId
	 *            C type : LPSTR<br>
	 * @param Parameters
	 *            C type : CRYPT_OBJID_BLOB
	 */
	public CRYPT_ALGORITHM_IDENTIFIER(LPSTR pszObjId, CRYPT_INTEGER_BLOB Parameters) {
		super();
		this.pszObjId = pszObjId;
		this.Parameters = Parameters;
	}

	public static class ByReference extends CRYPT_ALGORITHM_IDENTIFIER implements Structure.ByReference {

	};

	public static class ByValue extends CRYPT_ALGORITHM_IDENTIFIER implements Structure.ByValue {

	};
}
