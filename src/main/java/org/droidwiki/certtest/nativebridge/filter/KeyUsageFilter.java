package org.droidwiki.certtest.nativebridge.filter;

import java.util.List;

import org.droidwiki.certtest.KeyUsage;
import org.droidwiki.certtest.nativebridge.Certificate;

public class KeyUsageFilter extends AbstractListFilter<KeyUsage> {
	/**
	 * Constructs a new KeyUsageFilter where the oneOfKeyUsages parameter is a
	 * list of {@link KeyUsage} values, where at least one need to exist in the
	 * key usages of the certificate.
	 * 
	 * @param oneOfKeyUsages
	 */
	public KeyUsageFilter(List<KeyUsage> oneOfKeyUsages) {
		setOneOfList(oneOfKeyUsages);
	}

	@Override
	protected List<KeyUsage> getList(Certificate certificate) {
		return certificate.getKeyUsage();
	}
}
