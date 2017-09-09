package org.droidwiki.certtest.structures;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WTypes.LPSTR;

public class CERT_EXTENSION extends Structure {
	/** C type : LPSTR */
	public LPSTR pszObjId;
	public boolean fCritical;
	/** C type : CRYPT_OBJID_BLOB */
	public CRYPT_INTEGER_BLOB Value;

	public CERT_EXTENSION() {
		super();
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList("pszObjId", "fCritical", "Value");
	}

	/**
	 * @param pszObjId
	 *            C type : LPSTR<br>
	 * @param Value
	 *            C type : CRYPT_OBJID_BLOB
	 */
	public CERT_EXTENSION(LPSTR pszObjId, boolean fCritical, CRYPT_INTEGER_BLOB Value) {
		super();
		this.pszObjId = pszObjId;
		this.fCritical = fCritical;
		this.Value = Value;
	}

	public static class ByReference extends CERT_EXTENSION implements Structure.ByReference {

	};

	public static class ByValue extends CERT_EXTENSION implements Structure.ByValue {

	};
}
