package vwg.process.certtest.nativebridge;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;

import org.droidwiki.certtest.nativebridge.Certificate;
import org.droidwiki.certtest.natives.Crypt32NativeFunctions;
import org.droidwiki.certtest.structures.CERT_CONTEXT;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Crypt32NativeFunctions.class)
public class CertificateTest {
	private static final String expectedExceptionMessage = "This instance of " + Certificate.class.getName()
			+ " has already been freed.";

	private Certificate testCertificate;
	private Certificate freedTestCertificate;

	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(Crypt32NativeFunctions.class);

		PowerMockito.doNothing().when(Crypt32NativeFunctions.class, "CertFreeCertificateContext", any());
		testCertificate = new Certificate(new CERT_CONTEXT());
		freedTestCertificate = new Certificate(new CERT_CONTEXT());
		freedTestCertificate.free();
	}

	@Test
	public void testGetX509CertificateFreedThrows() throws Exception {
		try {
			freedTestCertificate.getX509Certificate();
			fail("Expected error was not thrown!");
		} catch (Error e) {
			assertThat(e, instanceOf(IllegalAccessError.class));
			assertEquals(expectedExceptionMessage, e.getMessage());
		}
	}

	@Test
	public void testGetAliasFreedThrows() throws Exception {
		try {
			freedTestCertificate.getAlias();
			fail("Expected error was not thrown!");
		} catch (Error e) {
			assertThat(e, instanceOf(IllegalAccessError.class));
			assertEquals(expectedExceptionMessage, e.getMessage());
		}
	}

	@Test
	public void testGetEncodedLengthFreedThrows() throws Exception {
		try {
			freedTestCertificate.getEncodedLength();
			fail("Expected error was not thrown!");
		} catch (Error e) {
			assertThat(e, instanceOf(IllegalAccessError.class));
			assertEquals(expectedExceptionMessage, e.getMessage());
		}
	}

	@Test
	public void testGetEncodedByteArrayFreedThrows() throws Exception {
		try {
			freedTestCertificate.getEncodedByteArray();
			fail("Expected error was not thrown!");
		} catch (Error e) {
			assertThat(e, instanceOf(IllegalAccessError.class));
			assertEquals(expectedExceptionMessage, e.getMessage());
		}
	}

	@Test
	public void testGetKeyUsageFreedThrows() throws Exception {
		try {
			freedTestCertificate.getKeyUsage();
			fail("Expected error was not thrown!");
		} catch (Error e) {
			assertThat(e, instanceOf(IllegalAccessError.class));
			assertEquals(expectedExceptionMessage, e.getMessage());
		}
	}

	@Test
	public void testGetExtendedKeyUsageFreedThrows() throws Exception {
		try {
			freedTestCertificate.getExtendedKeyUsages();
			fail("Expected error was not thrown!");
		} catch (Error e) {
			assertThat(e, instanceOf(IllegalAccessError.class));
			assertEquals(expectedExceptionMessage, e.getMessage());
		}
	}

	@Test
	public void testGetNativeFreedThrows() throws Exception {
		try {
			freedTestCertificate.getNative();
			fail("Expected error was not thrown!");
		} catch (Error e) {
			assertThat(e, instanceOf(IllegalAccessError.class));
			assertEquals(expectedExceptionMessage, e.getMessage());
		}
	}
}
