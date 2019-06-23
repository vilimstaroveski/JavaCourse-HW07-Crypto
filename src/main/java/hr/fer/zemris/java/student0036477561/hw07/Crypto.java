package hr.fer.zemris.java.student0036477561.hw07;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;


/**
 * Program that allows user to encrypt/decrypt given file using
 * the AES crypto-algorithm and the 128-bit encryption key or calculate and check the SHA-256 file digest. 
 * 
 * @author Vilim Staroveški
 *
 */
public class Crypto {

	/**
	 * Method called at the start of program.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {

		if(args.length != 2 && args.length != 3) {
			System.err.println("Input is wrong!");
			System.exit(1);
		}
		
		if(args[0].equalsIgnoreCase("checksha")) {
			
			try {
				
				checksha(args[1]);
				
			} catch(IOException | NoSuchAlgorithmException e) {
				System.err.println(e.getMessage());
				System.out.println("Program will shutdown...");
			}
		}
		else if(args[0].equalsIgnoreCase("encrypt") || args[0].equalsIgnoreCase("decrypt")) {
			
			try {
				
				crypto(args[1], args[2], args[0].equalsIgnoreCase("encrypt"));
				
			}catch(IllegalBlockSizeException| BadPaddingException | IOException e ) {
				System.err.println(e.getMessage());
				System.err.println("Program will shutdown...");
			}
		}
		else {
			
			System.err.println("Unknown mode selected! Possible modes are: checksha, encrypt and decrypt.");
			System.exit(1);
		}
		
	}
	
	/**
	 * Method used for taking users password and initialisation vector.
	 * 
	 * @param original file to crypt
	 * @param crypted crypted file
	 * @throws FileNotFoundException if file is not found
	 * @throws IOException if an IO error occures
	 * @throws IllegalBlockSizeException if error in writing occures
	 * @throws BadPaddingException if error in padding occures
	 */
   	private static void crypto(String original, String crypted, boolean encrypt) throws IllegalBlockSizeException, BadPaddingException, IOException {
		
		Scanner scan = new Scanner(System.in);
		
		System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n>");
		String key = scan.next();
		System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n>");
		String vector = scan.next();
		
		Crypter crypter = new Crypter(key, vector, encrypt);
		
		crypter.crypt(original, crypted);
		System.out.print(encrypt ? "Encryption" : "Decryption");
		System.out.println(" completed. Generated file " + crypted + " based on file "+ original+".");
		scan.close();
	}

   	/**
   	 * Method that checks SHA file digest.
   	 * @param original file for which we calculate digest
   	 * @throws IOException if IO error ocures
   	 * @throws NoSuchAlgorithmException if the specified algorithm is invalid.
   	 */
	private static void checksha(String original) throws IOException, NoSuchAlgorithmException {

		Scanner scan = new Scanner(System.in);
		System.out.print("Please provide expected sha-256 digest for "+original+":\n>");
		String expected = scan.next();
		scan.close();

		MessageDigest digester = MessageDigest.getInstance("SHA-256");
		
		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream("files/"+original))) {
			
			byte[] bytes = new byte[4096];
			int read;
			
			while ((read = bis.read(bytes)) > 0) {
	            digester.update(bytes, 0, read);
	        }
		}
		
		byte[] digested = digester.digest();

		boolean equals = true;
		
		if(expected.length() != 64 ) {
			
			equals = false;
		}
		else {
			
			byte[] expectedBytes = Crypter.hextobyte(expected);
			equals = MessageDigest.isEqual(digested, expectedBytes);
		}
		
		System.out.println("Digesting completed. Digest of " + original
				+ (equals ? " matches expected digest." : 
					" does not match the expected digest. \nDigest was: " + Crypter.bytetohex(digested)  ));
		
		    
	}
	
}
