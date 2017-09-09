package org.droidwiki.certtest.structures;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinBase.FILETIME;

public class CERT_INFO extends Structure {
	public int dwVersion;
	/** C type : CRYPT_INTEGER_BLOB */
	public CRYPT_INTEGER_BLOB SerialNumber;
	/** C type : CRYPT_ALGORITHM_IDENTIFIER */
	public CRYPT_ALGORITHM_IDENTIFIER SignatureAlgorithm;
	/** C type : CERT_NAME_BLOB */
	public CRYPT_INTEGER_BLOB Issuer;
	/** C type : FILETIME */
	public FILETIME NotBefore;
	/** C type : FILETIME */
	public FILETIME NotAfter;
	/** C type : CERT_NAME_BLOB */
	public CRYPT_INTEGER_BLOB Subject;
	/** C type : CERT_PUBLIC_KEY_INFO */
	public CERT_PUBLIC_KEY_INFO SubjectPublicKeyInfo;
	/** C type : CRYPT_BIT_BLOB */
	public CRYPT_BIT_BLOB IssuerUniqueId;
	/** C type : CRYPT_BIT_BLOB */
	public CRYPT_BIT_BLOB SubjectUniqueId;
	public int cExtension;
	/** C type : PCERT_EXTENSION */
	public CERT_EXTENSION rgExtension;

	public CERT_INFO() {
		super();
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList("dwVersion", "SerialNumber", "SignatureAlgorithm", "Issuer", "NotBefore", "NotAfter",
				"Subject", "SubjectPublicKeyInfo", "IssuerUniqueId", "SubjectUniqueId", "cExtension", "rgExtension");
	}

	protected ByReference newByReference() {
		return new ByReference();
	}

	protected ByValue newByValue() {
		return new ByValue();
	}

	protected CERT_INFO newInstance() {
		return new CERT_INFO();
	}

	public static class ByReference extends CERT_INFO implements Structure.ByReference {

	};

	public static class ByValue extends CERT_INFO implements Structure.ByValue {

	};
}
