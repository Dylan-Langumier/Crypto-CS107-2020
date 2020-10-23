package crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Arrays;

import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;
import static crypto.Helper.bytesToString;

/*
 * Part 1: Encode (with note that one can reuse the functions to decode)
 * Part 2: bruteForceDecode (caesar, xor) and CBCDecode
 * Part 3: frequency analysis and key-length search
 * Bonus: CBC with encryption, shell
 */
public class Main {
	
	
	//---------------------------MAIN---------------------------
	public static void main(String[] args) {
		
		
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
		try {
			System.out.println("yo it's the crypto");
			input = reader.readLine().split(" ");
			while (!input[0].equalsIgnoreCase("stop")) {
				input = reader.readLine().split(" ");

				// vigenere
				// todo : .txt entry & inverse key and string + trim array to remove elements 0, 1 and 2
				if (input[0].equalsIgnoreCase("vigenere")) {
					// decrypt
					if (input[1].equalsIgnoreCase("decrypt")) {
						if (input[3] != null && input[3].length() > 1) {
							byte[] result = Encrypt.vigenere(Helper.stringToBytes(input[2]), Helper.stringToBytes(input[3]), false);
							String output = bytesToString(result);
							System.out.println(output);
						} else {
							System.out.println("you have to enter a key larger than 1 character.");
						}
					}
				// XOR
				/*
				if(input[0].equalsIgnoreCase("xor")) {
					// decrypt
					if(input[1].equalsIgnoreCase("encrypt")) {
						if(input[3].length()>1) {

						}
					}
				}
				*/
					
				// stop
				} else if(input[0].equalsIgnoreCase("stop")) {
					System.out.println("Exiting program...");
					break;

				} else {
					System.out.println(input[0] + " isn't a command.");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
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
		String sFD = bytesToString(Encrypt.caesar(result, decodingKey));
		System.out.println("\n(absurd result due to a function not done yet)\nDecoded without knowing the key : " + sFD);
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
	 * Method to remove the first elements of an array
	 * @param array the array to trim
	 * @param startingBound the amount of elements you want to remove
	 *                      at the beginning of the array
	 *
	 * @return a trimmed array
	 */
	public static Object[] trim(Object[] array, int startingBound) {
		return Arrays.copyOfRange(array, startingBound, array.length);
	}
//TODO : TO BE COMPLETED
}
