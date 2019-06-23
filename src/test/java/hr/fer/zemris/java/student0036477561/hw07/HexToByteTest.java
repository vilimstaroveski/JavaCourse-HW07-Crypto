package hr.fer.zemris.java.student0036477561.hw07;

import static org.junit.Assert.*;

import org.junit.Test;
/**
 * Test class for {@link Crypter#hextobyte(String)} method.
 * @author Vilim Staroveški
 *
 */
public class HexToByteTest {

	@Test
	public void testHextobyte() {
		
		byte[] expected = {1};
		
		assertEquals(expected[0], Crypter.hextobyte("01")[0]);
		
	}
	
	@Test
	public void testHextobyte2() {
		
		byte[] expected = {42};
		
		assertEquals(expected[0], Crypter.hextobyte("2A")[0]);
		
	}
	
	@Test
	public void testHextobyte3() {
		
		byte[] expected = {-82};
		
		assertEquals(expected[0], Crypter.hextobyte("AE")[0]);
		
	}
	
	@Test
	public void testHextobyte4() {
		
		byte[] expected = {-81, 0, 5};
		
		for(int i = 0; i < 3; i++) {
			
			assertEquals(expected[i], Crypter.hextobyte("AF0005")[i]);
		}
		
	}
}
