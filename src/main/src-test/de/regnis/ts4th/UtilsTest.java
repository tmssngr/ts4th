package de.regnis.ts4th;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author Thomas Singer
 */
public class UtilsTest {

	@Test
	public void testToHex() {
		assertEquals("9", Utils.toHex(9, 1));
		assertEquals("a", Utils.toHex(10, 1));
		assertEquals("f", Utils.toHex(15, 1));
		assertEquals("0", Utils.toHex(16, 1));
		assertEquals("10", Utils.toHex(16, 2));
	}
}
