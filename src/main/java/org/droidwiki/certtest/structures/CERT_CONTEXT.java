package org.droidwiki.certtest.structures;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinNT.HANDLE;

public class CERT_CONTEXT extends Structure {
	public int dwCertEncodingType;
	/** C type : BYTE* */
	public Pointer pbCertEncoded;
	public int cbCertEncoded;
	/** C type : PCERT_INFO */
	public CERT_INFO pCertInfo;
	/** C type : HCERTSTORE */
	public HANDLE hCertStore;

	public CERT_CONTEXT() {
		super();
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList("dwCertEncodingType", "pbCertEncoded", "cbCertEncoded", "pCertInfo", "hCertStore");
	}

	/**
	 * @param pbCertEncoded
	 *            C type : BYTE*<br>
	 * @param pCertInfo
	 *            C type : PCERT_INFO<br>
	 * @param hCertStore
	 *            C type : HCERTSTORE
	 */
	public CERT_CONTEXT(int dwCertEncodingType, Pointer pbCertEncoded, int cbCertEncoded, CERT_INFO pCertInfo,
			HANDLE hCertStore) {
		super();
		this.dwCertEncodingType = dwCertEncodingType;
		this.pbCertEncoded = pbCertEncoded;
		this.cbCertEncoded = cbCertEncoded;
		this.pCertInfo = pCertInfo;
		this.hCertStore = hCertStore;
	}

	public static class ByReference extends CERT_CONTEXT implements Structure.ByReference {

	};

	public static class ByValue extends CERT_CONTEXT implements Structure.ByValue {

	};
}
