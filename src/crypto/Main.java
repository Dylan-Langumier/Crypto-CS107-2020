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
	public static void main(String[] args) throws IOException {
		byte[] plainBytes= {98, 111, 110, 110, 101, 32};
		byte[] pad= { 1,   2,   3};
		byte[] cipherBytes= Encrypt.cbc(plainBytes, pad);
		System.out.println(Arrays.toString(cipherBytes));
/*
		
		String inputMessage = Helper.readStringFromFile("text_one.txt");
		String key = "2cF%5";
		
		String messageClean = cleanString(inputMessage);

		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = stringToBytes(key);
		
		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();
		
		System.out.println("------Caesar------");
		testCaesar(messageBytes, keyBytes[0]);
		System.out.println("------Vigenere------");
		testVigenere(new byte[]{98, 111, 110, 110, 101, 32, 106, 111, 117, 114, 110, -23, 101}, new byte[]{1, 2, 3});
		System.out.println("------XOR------");
		testXor(new byte[]{1}, new byte[]{2});
		// TODO: TO BE COMPLETED
    
    // SHELL
		BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
		String[] input;
		boolean isFinished = false;
		try {
			while(!isFinished) {
				System.out.println("Voulez vous terminer votre programme? [Oui/Non]");
				String s = reader.readLine();
				if (s.equals("Oui")) {
					isFinished = true;
				} else {
					System.out.println("Le programme ne se termine pas.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

*/

	}

	//Run the Encoding and Decoding using the caesar pattern 
	public static void testCaesar(byte[] string , byte key) {
		//Encoding
		byte[] result = Encrypt.caesar(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);
		
		//Decoding with key
		String sD = bytesToString(Encrypt.caesar(result, (byte) (-key)));
		System.out.println("Decoded knowing the key : " + sD);
		
		//Decoding without key
		byte[][] bruteForceResult = Decrypt.caesarBruteForce(result);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		Helper.writeStringToFile(sDA, "bruteForceCaesar.txt");

		byte decodingKey = Decrypt.caesarWithFrequencies(result);
		String sFD = bytesToString(Encrypt.caesar(result, (byte) (-decodingKey)));
		System.out.println("Decoded without knowing the key : " + sFD);
	}

	public static void testVigenere(byte[] string, byte[] key) {
		String plainText = "bonne journée";
		byte[] plainBytes = Helper.stringToBytes(plainText);//98, 111, 110, 110, 101, 32, 106, 111, 117, 114, 110, -23, 101
		byte[] keypad = {(byte) 1, (byte) 2, (byte) 3 };
		byte[] cipherBytes = Encrypt.vigenere(plainBytes, keypad, true);
		String cipherText = Helper.bytesToString(cipherBytes);
		System.out.println(cipherText);
		// todo: various improvements
	}

	public static void testXor(byte[] string, byte[] key) {
		byte[] plainBytes = {98, 111, 110, 110, 101, 32, 106, 111, 117, 114, 110, -23, 101};
		byte[] cipherBytes = Encrypt.xor(plainBytes, (byte) 7, true);
		System.out.println(
				Arrays.toString(plainBytes) + "\n" +
				Arrays.toString(cipherBytes) + "\n" +
				Helper.bytesToString(cipherBytes));
		// todo: various improvements
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
		if (!end) {
			result = Arrays.copyOfRange(array, bound, array.length);
		} else {
			result = Arrays.copyOfRange(array, 0, bound);
		}
		return result;
	}
	public static byte[] trim(byte[] array, int bound) {
		return trim(array, bound, false);
	}
//TODO : TO BE COMPLETED
}
