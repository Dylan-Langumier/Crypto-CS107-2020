package crypto;

import java.io.IOException;
import java.util.Arrays;

import static crypto.Helper.*;

/*
 * Part 1: Encode (with note that one can reuse the functions to decode)
 * Part 2: bruteForceDecode (caesar, xor) and CBCDecode
 * Part 3: frequency analysis and key-length search
 * Bonus: CBC with encryption, shell
 */
public class Main {

	//---------------------------MAIN---------------------------
	public static void main(String[] args) {
		// Shell.main();

		String key = "2cF%5";
		byte[] keyBytes = stringToBytes(key);

		String[] plainMessage = new String[3];
		String[] cleanedMessage = new String[3];
		byte[][] bytesMessage = new byte[3][];

		plainMessage[0] = readStringFromFile("text_one.txt");
		plainMessage[1] = readStringFromFile("text_two.txt");
		plainMessage[2] = readStringFromFile("text_three.txt");

		for (int i = 0; i <= 2; i++) {
			cleanedMessage[i] = cleanString(plainMessage[i]);
			bytesMessage[i] = stringToBytes(cleanedMessage[i]);
		}

		// Testing all algorithms with there respective methods
		for (int i = 0; i <= 2; i++) {
			System.out.println("------ TEXT " + i + " ------");
			System.out.println("Original input sanitized : " + cleanedMessage[i]);
			testCaesar(bytesMessage[i], keyBytes[0],i);
			testVigenere(bytesMessage[i], keyBytes);
			testXor(bytesMessage[i], keyBytes[0],i);
			testOTP(bytesMessage[i], Encrypt.generatePad(bytesMessage[i].length));
			testCBC(bytesMessage[i], keyBytes);
		}

	}

	//Run the Encoding and Decoding using the caesar pattern 
	public static void testCaesar(byte[] string , byte key, int index) {
		System.out.println("------ Caesar ------");

		byte[] result = Encrypt.caesar(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);
		
		//Decoding with key
		String sD = bytesToString(Encrypt.caesar(result, (byte) (-key)));
		System.out.println("Decoded knowing the key : " + sD);
		
		//Decoding without key
		byte[][] bruteForceResult = Decrypt.caesarBruteForce(result);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		Helper.writeStringToFile(sDA, "bruteForceCaesar-" + index + ".txt");

		byte decodingKey = Decrypt.caesarWithFrequencies(result);
		String sFD = bytesToString(Encrypt.caesar(result, (byte) (-decodingKey)));
		System.out.println("Decoded without knowing the key : " + sFD);
	}

	//Run the Encoding and Decoding using the vigenere pattern
	public static void testVigenere(byte[] string, byte[] key) {
		System.out.println("------ Vigenere ------");

		//Encoding
		byte[] result = Encrypt.vigenere(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);

		//Decoding with key
		String sD = bytesToString(Decrypt.vigenereWithFrequencies(result));
		System.out.println("Decoded with frequencies : " + sD);
	}

	//Run the Encoding and Decoding using the xor pattern
	public static void testXor(byte[] string, byte key, int index) {
		System.out.println("------ XOR ------");
		//Encoding
		byte[] result = Encrypt.xor(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);

		//Decoding with key
		String sD = bytesToString(Encrypt.xor(result, key));
		System.out.println("Decoded knowing the key : " + sD);


		//Decoding without key
		byte[][] bruteForceResult = Decrypt.xorBruteForce(result);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		Helper.writeStringToFile(sDA, "bruteForceXor-" + index + ".txt");
	}

	//Run the Encoding and Decoding using the one time pad pattern
	public static void testOTP(byte[] string, byte[] key) {
		System.out.println("------ One Time Pad ------");

		//Encoding
		byte[] result = Encrypt.oneTimePad(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);

		//Decoding with key
		String sD = bytesToString(Encrypt.oneTimePad(result, key));
		System.out.println("Decoded knowing the key : " + sD);
	}

	//Run the Encoding and Decoding using the cipher block chaining pattern
	public static void testCBC(byte[] string, byte[] key) {
		System.out.println("------ Cipher Block Chaining ------");

		//Encoding
		byte[] result = Encrypt.cbc(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);

		//Decoding with key
		String sD = bytesToString(Decrypt.decryptCBC(result, key));
		System.out.println("Decoded knowing key : " + sD);
	}

	/**
	 * Method to remove the first (last) elements of an array
	 * @param array the array to trim
	 * @param bound the amount of elements you want to remove from the array at the beginning (end)
	 * @param end   if true, the method removes the last elements of the array
	 *
	 * @return a trimmed array
	 */
	public static byte[] trim(byte[] array, int bound, boolean end) {
		byte[] result;
		if (!end) result = Arrays.copyOfRange(array, bound, array.length);
		else result = Arrays.copyOfRange(array, 0, bound);
		return result;
	}
	public static byte[] trim(byte[] array, int bound) {
		return trim(array, bound, false);
	}
//TODO : TO BE COMPLETED
}
