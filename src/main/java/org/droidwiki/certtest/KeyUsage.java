package org.droidwiki.certtest;

public enum KeyUsage {
	digitalSignature, // 0
	nonRepudiation, // 1
	keyEncipherment, // 2
	dataEncipherment, // 3
	keyAgreement, // 4
	keyCertSign, // 5
	cRLSign, // 6
	encipherOnly, // 7
	decipherOnly // 8
}
