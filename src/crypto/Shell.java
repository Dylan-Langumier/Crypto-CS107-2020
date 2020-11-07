package crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static crypto.Helper.*;
import static crypto.Encrypt.*;
import static crypto.Decrypt.*;

public class Shell {

	/**
	 * Method to return a BufferedReader that acts like a scanner.
	 * @return an instance of a BufferedReader
	 */
	private static BufferedReader reader() {
		return new BufferedReader(new InputStreamReader(System.in));
	}

	/**
	 * Method called in Main.java to start the interactive shell
	 */
	public static void main() {
		System.out.println("\n----- Welcome to the crypto shell -----\n");
		System.out.println("Please note that all commands are case insensitive. However when you will input your text to cipher/decipher, the capitalization will be kept.\n");
		try {
			introduction();
		} catch (IOException ignored) {}
	}

	/**
	 * Interactive introduction menu - everything starts with here
	 */
	private static void introduction() throws IOException {
		boolean passed = false;
		System.out.println("What menu would you like to see? Help (h), encrypt (e) or decrypt (d)?");
		String[] command = reader().readLine().toLowerCase().split(" ");
		while (!passed) {
			switch (command[0]) {
				case "encrypt":
				case "e":
					encrypt();
					passed = true;
					break;
				case "decrypt":
				case "d":
					decrypt();
					passed = true;
					break;
				case "help":
				case "h":
					help();
					passed = true;
					break;
				default:
					System.out.println("Wrong command input.");
					introduction();
			}
		}
	}

	/**
	 * Method used to call the interactive encrypt menu
	 */
	private static void encrypt() throws IOException {
		String plainText = null;
		String key = null;
		int algCode = 0;
		boolean passed = false;
		while (!passed) {
			System.out.println("Which algorithm do you want to use to cipher your text?");
			System.out.println("\tPossible inputs: caesar (ca | 0), vigenere (vi | 1), xor (2), oneTimePad (otp | 3), simplified Cipher Block Chaining (sCBC | 4)");
			String algorithm = reader().readLine().toLowerCase();
			switch (algorithm) {
				case "caesar": case "ca": case "0":
					algCode = CAESAR; // 0
					passed = true;
					break;
				case "vigenere": case "vi": case "1":
					algCode = VIGENERE; // 1
					passed = true;
					break;
				case "xor": case "2":
					algCode = XOR; // 2
					passed = true;
					break;
				case "onetimepad": case "otp": case "3":
					algCode = ONETIME; // 3
					passed = true;
					break;
				case "sCBC": case "4":
					algCode = CBC; // 4
					passed = true;
					break;
				default:
					break;
			}
		}
		passed = false;
		while (!passed) {
			System.out.println("What do you want to cipher? (Enter a text)");
			plainText = reader().readLine();
			if (plainText == null) return;
			else passed = true;
		}
		passed = false;
		while (!passed) {
			System.out.println("What key do you want to use? (Enter a text)");
			key = reader().readLine();
			if(algCode == ONETIME) key = bytesToString(generatePad(plainText.length()));
			if (key == null) return;
			else if (key.equals("none")) {
				if (algCode == VIGENERE || algCode == CBC) {
					int keyLength;
					System.out.println("As you did not want to give a key, we will generate one for you. Please type the length of the key.");
					try {
						keyLength = Integer.parseInt(reader().readLine().split(" ")[0]);
					} catch (NumberFormatException ignored) {
						return;
					}
					key = bytesToString(generatePad(keyLength));
					passed = true;
				} else key = bytesToString(generatePad(1));
			} else if (key.equals("random")) {
				key = bytesToString(generatePad(rand.nextInt(plainText.length())));
				passed = true;
			} else passed = true;
		}
		System.out.println("Ciphering your text...");
		System.out.println(Encrypt.encrypt(plainText, key, algCode));
	}

	/**
	 * Method to call the interactive decrypt menu
	 */
	private static void decrypt() {
		String cipherText = null;
		String key = null;
		String messageConfirmation = null;
		String plainText = null;
		String algorithm = null;
		int algCode = 0;
		boolean passed = false;
		while (!passed) {
			System.out.println("Which algorithm do you want to use to decipher your text?");
			System.out.println("\tPossible inputs: caesar (ca | 0), vigenere (vi | 1), xor (2), simplified Cipher Block Chaining (sCBC | 4)");
			System.out.println("\t\tIMPORTANT: To decipher a text ciphered by a sCBC algorithm, the program requires the key, if you do not have the key, the cipher cannot be brute forced without using a lot of resources.");
			try {
				algorithm = reader().readLine().toLowerCase();
			} catch (IOException ignored) {}
			if(algorithm == null) return;
			switch (algorithm) {
				case "caesar": case "ca": case "0":
					algCode = CAESAR; // 0
					passed = true;
					break;
				case "vigenere": case "vi": case "1":
					algCode = VIGENERE; // 1
					passed = true;
					break;
				case "xor": case "2":
					algCode = XOR; // 2
					passed = true;
					break;
				case "sCBC": case "4":
					algCode = CBC; // 4
					passed = true;
					break;
				default:
					break;
			}
		}
		passed = false;
		while (!passed) {
			System.out.println("What do you want to decipher? (Enter a text)");
			try {
				cipherText = reader().readLine();
			} catch (IOException ignored) {}
			if (cipherText == null) return;
			else passed = true;
		}
		passed = false;
		while (!passed) {
			System.out.println("What key do you want to use? (Enter a text)");
			try {
				key = reader().readLine();
			} catch (IOException ignored) {}
			if (key == null) return;
			else if (key.equals("none")) {
				plainText = breakCipher(cipherText, algCode);
				switch (algCode) {
					case CAESAR: case VIGENERE:
						messageConfirmation = "Your ciphered text has been broken using english frequencies.";
						passed = true;
						break;
					case XOR:
						messageConfirmation = "Your ciphered text has been brute forced.";
						passed = true;
						break;
					case CBC:
						System.out.println("Cannot brute force sCBC. Starting decipher process again...\n");
						decrypt();
						break;
				}
			} else {
				messageConfirmation = "Your text has been deciphered using your custom key.";
				switch (algCode) {
					case CAESAR:
						plainText = bytesToString(caesar(stringToBytes(cipherText), stringToBytes(key)[0]));
						passed = true;
						break;
					case VIGENERE:
						plainText = bytesToString(vigenere(stringToBytes(cipherText), stringToBytes(key)));
						passed = true;
						break;
					case XOR:
						plainText = bytesToString(xor(stringToBytes(cipherText), stringToBytes(key)[0]));
						passed = true;
						break;
					case CBC:
						plainText = bytesToString(decryptCBC(stringToBytes(cipherText), stringToBytes(key)));
						passed = true;
						break;
				}
			}
		}
		System.out.println(messageConfirmation + "\n" + plainText);
	}

	/**
	 * Method to call the help menu - which calls the introduction menu
	 */
	private static void help() {
		System.out.println("--- HELP---" + "\n" +
				"Help:" + "\n" +
				"\t" + "help | h" + "\n" +
				"Encryption:" + "\n" +
				"\t" + "encrypt | e" + "\n" +
				"Decryption:" + "\n" +
				"\t" + "decrypt | d" + "\n\n" +
				"After sending an encryption/decryption command, you will be prompted to type the algorithm you want to use to cipher/decipher your text." + "\n\n" +
				"Types of algorithms:" + "\n" +
				"\t" + "(0) Caesar:" + "\t\t\t" + "1 character-long key \t\t- shifts every character of your text (converted to bytes) by the key" + "\n" +
				"\t" + "(1) Vigenere:" + "\t\t" + "multiple character-long key - periodically shifts every character of your text (converted to bytes) by the key" + "\n" +
				"\t" + "(2) XOR:" + "\t\t\t" + "1 character-long key \t\t- uses bit-wise XOR operations to cipher every character of your text by the key" + "\n" +
				"\t" + "(3) OneTimePad:" + "\t\t" + "text-long key \t\t\t\t- uses bit-wise XOR operations to cipher every character of your text. The key HAS, at least, to be as long as your text" + "\n" +
				"\t" + "(4) Simplified CBC:" + "\t" + "multiple character-long key - cuts your text in blocks of the key-size, then uses bit-wise XOR operations through all of the blocks" + "\n\n" +
				"Possible types of key:" + "\n" +
				"\t" + "Encryption:" + "\n" +
				"\t\t" + "none" + "\t" + "- will choose a random key depending on the chosen algorithm (if chosen algorithm is Vigenere (1) or sCBC (4), you will be prompted to enter a length for the key" + "\n" +
				"\t\t" + "random" + "\t" + "- generate a random key, with a random length if chosen algorithm is Vigenere (1) or sCBC (4)" + "\n" +
				"\t\t" + "key" + "\t\t" + "- any input as long as it is not null"+ "\n" +
				"\t" + "Decryption:" + "\n" +
				"\t\t" + "none" + "\t" + "- will act as brute forcing if applicable" + "\n" +
				"\t\t" + "key" + "\t\t" + "- any input as long as it is not null" + "\n"
		);
		try {
			introduction();
		} catch (IOException ignored) {}
	}
}
/*

 */