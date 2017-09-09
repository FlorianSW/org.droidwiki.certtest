package org.droidwiki.certtest.structures;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class CERT_PUBLIC_KEY_INFO extends Structure {
	/** C type : CRYPT_ALGORITHM_IDENTIFIER */
	public CRYPT_ALGORITHM_IDENTIFIER Algorithm;
	/** C type : CRYPT_BIT_BLOB */
	public CRYPT_BIT_BLOB PublicKey;

	public CERT_PUBLIC_KEY_INFO() {
		super();
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList("Algorithm", "PublicKey");
	}

	/**
	 * @param Algorithm
	 *            C type : CRYPT_ALGORITHM_IDENTIFIER<br>
	 * @param PublicKey
	 *            C type : CRYPT_BIT_BLOB
	 */
	public CERT_PUBLIC_KEY_INFO(CRYPT_ALGORITHM_IDENTIFIER Algorithm, CRYPT_BIT_BLOB PublicKey) {
		super();
		this.Algorithm = Algorithm;
		this.PublicKey = PublicKey;
	}

	public static class ByReference extends CERT_PUBLIC_KEY_INFO implements Structure.ByReference {

	};

	public static class ByValue extends CERT_PUBLIC_KEY_INFO implements Structure.ByValue {

	};
}
