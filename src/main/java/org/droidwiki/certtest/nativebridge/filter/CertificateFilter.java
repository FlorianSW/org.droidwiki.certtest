package org.droidwiki.certtest.nativebridge.filter;

import org.droidwiki.certtest.nativebridge.Certificate;

public interface CertificateFilter {
	/**
	 * The main filter function. If called, the return value should be a boolean
	 * where true means, that the certificate should be included and false that
	 * it should not be included in a newly created certificate store.
	 * 
	 * @param certificate
	 * @return
	 */
	boolean accept(Certificate certificate);
}
