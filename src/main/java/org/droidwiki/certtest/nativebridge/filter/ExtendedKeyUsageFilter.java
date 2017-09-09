package org.droidwiki.certtest.nativebridge.filter;

import java.util.List;

import org.droidwiki.certtest.nativebridge.Certificate;

public class ExtendedKeyUsageFilter extends AbstractListFilter<String> {
	/**
	 * Constructs a new ExtendedKeyUsageFilter where the oneOfKeyUsages
	 * parameter is a list of {@link String} values, where at least one need to
	 * exist in the extendedKeyUsage extension of the certificate.
	 * 
	 * @param oneOfKeyUsages
	 */
	public ExtendedKeyUsageFilter(List<String> oneOfKeyUsages) {
		setOneOfList(oneOfKeyUsages);
	}

	@Override
	protected List<String> getList(Certificate certificate) {
		return certificate.getExtendedKeyUsages();
	}

}
