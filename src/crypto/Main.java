package crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
	public static void main(String args[]) {
		
		
		String inputMessage = Helper.readStringFromFile("text_one.txt");
		String key = "2cF%5";
		
		String messageClean = cleanString(inputMessage);

		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = stringToBytes(key);
		
		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();
		
		System.out.println("------Caesar------");
		testCaesar(messageBytes, keyBytes[0]);
		
		// TODO: TO BE COMPLETED
    
    // SHELL
		BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
		String input[];
		try {
			System.out.println("yo it's the crypto");
			input = reader.readLine().split(" ");
			while (!input[0].equalsIgnoreCase("stop")) {
				input = reader.readLine().split(" ");

				// vigenere
				// todo : entrée en .txt
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

				// stop
				} else if(input[0].equalsIgnoreCase("stop")) {
					System.out.println("Exiting program...");
					break;

				} else {
					System.out.println(input[0] + " isn't a command.");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
	}
	
	
	//Run the Encoding and Decoding using the caesar pattern 
	public static void testCaesar(byte[] string , byte key) {
		//Encoding
		byte[] result = Encrypt.caesar(string, key);
		System.out.println("ceaser: " + result);
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
		System.out.println("Decoded without knowing the key : " + sFD);
	}
	
//TODO : TO BE COMPLETED
	
}
