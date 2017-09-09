package org.droidwiki.certtest.nativebridge.filter;

import java.util.ArrayList;
import java.util.List;

import org.droidwiki.certtest.KeyUsage;
import org.droidwiki.certtest.nativebridge.Certificate;

public class KeyUsageFilter implements CertificateFilter {
	private List<KeyUsage> oneOfKeyUsages;

	/**
	 * Constructs a new KeyUsageFilter where the oneOfKeyUsages parameter is a
	 * list of {@link KeyUsage} values, where at least one need to exist in the
	 * certificate.
	 * 
	 * @param oneOfKeyUsages
	 */
	public KeyUsageFilter(List<KeyUsage> oneOfKeyUsages) {
		this.oneOfKeyUsages = oneOfKeyUsages;
	}

	@Override
	public boolean accept(Certificate certificate) {
		List<KeyUsage> certKeyUsages = certificate.getKeyUsage();
		List<KeyUsage> remainingKeyUsage = new ArrayList<>(oneOfKeyUsages);
		remainingKeyUsage.removeAll(certKeyUsages);

		return remainingKeyUsage.size() != oneOfKeyUsages.size();
	}

}
