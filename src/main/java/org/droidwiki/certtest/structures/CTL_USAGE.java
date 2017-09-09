package org.droidwiki.certtest.structures;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class CTL_USAGE extends Structure {
	public int cUsageIdentifier;
	/** C type : LPSTR* */
	public Pointer[] rgpszUsageIdentifier = new Pointer[255];

	public CTL_USAGE() {
		super();
	}

	protected List<String> getFieldOrder() {
		return Arrays.asList("cUsageIdentifier", "rgpszUsageIdentifier");
	}

	/**
	 * @param rgpszUsageIdentifier
	 *            C type : LPSTR*
	 */
	public CTL_USAGE(int cUsageIdentifier, Pointer[] rgpszUsageIdentifier) {
		super();
		this.cUsageIdentifier = cUsageIdentifier;
		this.rgpszUsageIdentifier = rgpszUsageIdentifier;
	}

	protected CTL_USAGE.ByReference newByReference() {
		return new ByReference();
	}

	protected CTL_USAGE.ByValue newByValue() {
		return new ByValue();
	}

	protected CTL_USAGE newInstance() {
		return new CTL_USAGE();
	}

	public static class ByReference extends CTL_USAGE implements Structure.ByReference {

	};

	public static class ByValue extends CTL_USAGE implements Structure.ByValue {

	};
}
