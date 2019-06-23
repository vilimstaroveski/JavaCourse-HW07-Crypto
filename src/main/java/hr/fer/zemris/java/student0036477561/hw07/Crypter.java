package hr.fer.zemris.java.student0036477561.hw07;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class used for encrypting files. 
 * 
 * @author Vilim Staroveški
 *
 */
public class Crypter {

	/**
	 * Cipher.
	 */
	private Cipher cipher;
	
	/**
	 * Alphabet for hex numbers.
	 */
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	/**
	 * Creates new crypter
	 * @param keyText
	 * @param ivText
	 * @param encrypt
	 */
	public Crypter(String keyText, String ivText, boolean encrypt) {
		
		SecretKeySpec keySpec = new SecretKeySpec(hextobyte(keyText), "AES");
		
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(hextobyte(ivText));
		
		try {
			
			this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Non existing algorithm for encryption/decryption selected.");
		} catch (NoSuchPaddingException e) {
			System.err.println("Non existing padding for encryption/decryption selected.");
		}

		try {
			
			this.cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
			
		} catch (InvalidKeyException e) {
			System.err.println("Wrong key for encryption/decryption selected.");
		} catch (InvalidAlgorithmParameterException e) {
			System.err.println("Wrong algorithm for encryption/decryption selected.");
		}
		
	}
	
	/**
	 * Returns byte representation of hex input given in string.
	 * @param s bytes
	 * @return hex string
	 */
	public static byte[] hextobyte(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	    	
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}


	/**
	 * Crypts the file.
	 * @param original file to crypt
	 * @param crypted crypted file
	 * @throws FileNotFoundException if file is not found
	 * @throws IOException if an IO error occures
	 * @throws IllegalBlockSizeException if error in writing occures
	 * @throws BadPaddingException if error in padding occures
	 */
	public void crypt(String original, String crypted) throws FileNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException {

		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream("files/"+original));
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("files/"+crypted))) {
			
			byte[] bytes = new byte[4*1024];
			while(true) {
				
				int r = bis.read(bytes);
				if(r < 1) {
					break;
				}
				bos.write(this.cipher.update(bytes));
			}
		}
	}

	/**
	 * Returns hex representation of bytes given as argument.
	 * @param bytes bytes to convert ro hex
	 * @return hex
	 */
	public static String bytetohex(byte[] bytes) {
		
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	    	
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
