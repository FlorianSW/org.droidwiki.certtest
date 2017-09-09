package org.droidwiki.certtest.nativebridge.filter;

import java.util.ArrayList;
import java.util.List;

import org.droidwiki.certtest.nativebridge.Certificate;

public class ExtendedKeyUsageFilter implements CertificateFilter {
	private List<String> oneOfKeyUsages;

	/**
	 * Constructs a new KeyUsageFilter where the oneOfKeyUsages parameter is a
	 * list of {@link String} values, where at least one need to exist in the
	 * certificate.
	 * 
	 * @param oneOfKeyUsages
	 */
	public ExtendedKeyUsageFilter(List<String> oneOfKeyUsages) {
		this.oneOfKeyUsages = oneOfKeyUsages;
	}

	@Override
	public boolean accept(Certificate certificate) {
		List<String> certKeyUsages = certificate.getExtendedKeyUsages();
		List<String> remainingKeyUsage = new ArrayList<>(oneOfKeyUsages);
		remainingKeyUsage.removeAll(certKeyUsages);

		return remainingKeyUsage.size() != oneOfKeyUsages.size();
	}

}
