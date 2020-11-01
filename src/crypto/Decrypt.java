package crypto;

import static crypto.Helper.bytesToString;
import static crypto.Helper.stringToBytes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decrypt {
	
	
	public static final int ALPHABETSIZE = Byte.MAX_VALUE - Byte.MIN_VALUE + 1 ; //256
	public static final int APOSITION = 97 + ALPHABETSIZE/2; 
	
	//source : https://en.wikipedia.org/wiki/Letter_frequency
	public static final double[] ENGLISHFREQUENCIES = {0.08497,0.01492,0.02202,0.04253,0.11162,0.02228,0.02015,0.06094,0.07546,0.00153,0.01292,0.04025,0.02406,0.06749,0.07507,0.01929,0.00095,0.07587,0.06327,0.09356,0.02758,0.00978,0.0256,0.0015,0.01994,0.00077};
	
	/**
	 * Method to break a string encoded with different types of cryptosystems
	 * @param type the integer representing the method to break : 0 = Caesar, 1 = Vigenere, 2 = XOR
	 * @return the decoded string or the original encoded message if type is not in the list above.
	 */
	public static String breakCipher(String cipher, int type) {
		//TODO : COMPLETE THIS METHOD
		
		return null; //TODO: to be modified
	}
	
	
	/**
	 * Converts a 2D byte array to a String
	 * @param bruteForceResult a 2D byte array containing the result of a brute force method
	 */
	public static String arrayToString(byte[][] bruteForceResult) {
		StringBuilder decipherText = new StringBuilder();
		for(int i = 0; i < bruteForceResult.length; i++) {
			decipherText.append(Helper.bytesToString(bruteForceResult[i])).append("\n");
		}
		return decipherText.toString();


	}
	
	
	//-----------------------Caesar-------------------------
	
	/**
	 *  Method to decode a byte array  encoded using the Caesar scheme
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return a 2D byte array containing all the possibilities
	 */
	public static byte[][] caesarBruteForce(byte[] cipher) {

		assert(cipher != null);
		byte[][] results = new byte[256][cipher.length];
		for(int i = 0; i < 256; i++) {
			results[i] = Encrypt.caesar(cipher, (byte) i);
		}
		return results; 
	}	
	
	
	/**
	 * Method that finds the key to decode a Caesar encoding by comparing frequencies
	 * @param cipherText the byte array representing the encoded text
	 * @return the encoding key
	 */
	public static byte caesarWithFrequencies(byte[] cipherText) {
		float[] frequencies = computeFrequencies(cipherText);
		byte key = caesarFindKey(frequencies);
		return key;
	}
	
	/**
	 * Method that computes the frequencies of letters inside a byte array corresponding to a String
	 * @param cipherText the byte array 
	 * @return the character frequencies as an array of float
	 */
	public static float[] computeFrequencies(byte[] cipherText) {

		float[] frequencies = new float[256];
		// count each character
		for (int i = 0; i < cipherText.length; i++) {
			int value = cipherText[i];
			frequencies[value+128]++;
		}

		// get frequencies
		for (int i = 0; i < 256; i++) {
			if (frequencies[i] != 0) {
				frequencies[i] /= cipherText.length;
			}
		}
		return frequencies;
	}
	
	
	/**
	 * Method that finds the key used by a  Caesar encoding from an array of character frequencies
	 * @param charFrequencies the array of character frequencies
	 * @return the key
	 */
	public static byte caesarFindKey(float[] charFrequencies) {

		// frequencies analysis
		float[] iterations = new float[256];
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 256; j++) {
				int index = i + j;
				if (index > 255) {
					index = index - 256;
				}
				iterations[j] += ENGLISHFREQUENCIES[i] * charFrequencies[index];
			}
		}

		// find highest value
		int highestIndex = 0;
		float highest = 0;
		for (int i = 0; i < 256; i++) {
			if (iterations[i] > highest) {
				highest = iterations[i];
				highestIndex = i;
			}
		}

		return (byte) (highestIndex - 97 - 128);
	}
	
	
	
	//-----------------------XOR-------------------------
	
	/**
	 * Method to decode a byte array encoded using a XOR 
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return the array of possibilities for the clear text
	 */
	public static byte[][] xorBruteForce(byte[] cipher) {
		//TODO : COMPLETE THIS METHOD

		return null; //TODO: to be modified
	}
	
	
	
	//-----------------------Vigenere-------------------------
	// Algorithm : see  https://www.youtube.com/watch?v=LaWp_Kq0cKs	
	/**
	 * Method to decode a byte array encoded following the Vigenere pattern, but in a clever way, 
	 * saving up on large amounts of computations
	 * @param cipher the byte array representing the encoded text
	 * @return the byte encoding of the clear text
	 */
	public static byte[] vigenereWithFrequencies(byte[] cipher) {
		int keyLength = vigenereFindKeyLength(removeSpaces(cipher));
		byte[] key = vigenereFindKey(removeSpaces(cipher), keyLength);
		byte[] decryptedCipher = Encrypt.vigenere(cipher, key);

		return decryptedCipher;
	}
	
	
	
	/**
	 * Helper Method used to remove the space character in a byte array for the clever Vigenere decoding
	 * @param array the array to clean
	 * @return a List of bytes without spaces
	 */
	public static List<Byte> removeSpaces(byte[] array){
		List<Byte> cipher = new ArrayList<Byte>();
		for (int i = 0; i < array.length; i++) {
			if (array[i] != (byte) 32) {
				cipher.add(array[i]);
			}
		}
		return cipher;
	}
	
	
	/**
	 * Method that computes the key length for a Vigenere cipher text.
	 * @param cipher the byte array representing the encoded text without space
	 * @return the length of the key
	 */
	public static int vigenereFindKeyLength(List<Byte> cipher) {

		// TODO: negative bytes ? i think
		// find coincidences
		int[] coincidences = new int[cipher.size()];
		for (int i = 0; i < cipher.size(); i++) {
			coincidences[i] = 0;
		}
		for (int i = 0; i < cipher.size(); i++) {
			for (int index = 0; index < i; index++) {
				if (cipher.get(i) == cipher.get(index)) {
					coincidences[i - index - 1]++;
				}
			}
		}

		// find local maximums
		// todo: ceil only for tables with odd size I think
		List<Integer> maximumsIndex = new ArrayList<Integer>();
		for (int i = 0; i < Math.ceil(coincidences.length / 2.0); i++) {
			if (i == 0) {
				if (coincidences[i] >= coincidences[1] && coincidences[i] >= coincidences[2]) {
					maximumsIndex.add(i);
				}
			} else if (i == 1) {
				if (coincidences[i] >= coincidences[2] && coincidences[i] >= coincidences[3] && coincidences[i] >= coincidences[0]) {
					maximumsIndex.add(i);
				}
			} else if (coincidences[i] >= coincidences[i + 1] && coincidences[i] >= coincidences[i + 2] && coincidences[i] >= coincidences[i - 1] && coincidences[i] >= coincidences[i - 2]) {
				maximumsIndex.add(i);
			}
		}

		// TODO: cleanup this mess done at 1am lmao
		if (maximumsIndex.size() > 1) {
			// set distances in a hashmap
			Map<Integer, Integer> distance = new HashMap<>();
			for (int i = 0; i < maximumsIndex.size() - 1; i++) {
				int dist = maximumsIndex.get(i+1) - maximumsIndex.get(i);
				if (dist == 1) {
					maximumsIndex.remove(i);
				}
				if (!distance.containsKey(dist) && dist != 1) {
					distance.put(dist, 1);
				} else {
					for (Map.Entry<Integer, Integer> item : distance.entrySet()) {
						if (item.getKey() == dist) {
							item.setValue(item.getValue() + 1);
						}
					}
				}
			}

			// get key length
			return Helper.getHighest(distance);
		} else {
			// return distance to the origin when there is only one maximums
			return maximumsIndex.get(0)+1;
		}
	}

	
	
	/**
	 * Takes the cipher without space, and the key length, and uses the dot product with the English language frequencies 
	 * to compute the shifting for each letter of the key
	 * @param cipher the byte array representing the encoded text without space
	 * @param keyLength the length of the key we want to find
	 * @return the inverse key to decode the Vigenere cipher text
	 */
	public static byte[] vigenereFindKey(List<Byte> cipher, int keyLength) {

		// TODO: optimiser because the code pue la merde and is not beau but well I didn't had le temps de finir
		byte[][] caesarKey = new byte[keyLength][cipher.size()/keyLength];
		for (int i = 0; i < keyLength; i++) {
			for (int j = 0; j < (cipher.size()/keyLength); j++) {
				caesarKey[i][j] = cipher.get((keyLength * j) + i);
			}
		}

		byte[] keys = new byte[keyLength];
		for (int i = 0; i < keyLength; i++) {
			keys[i] = (byte) -(caesarWithFrequencies(caesarKey[i]));
		}
		return keys;
	}
	
	
	//-----------------------Basic CBC-------------------------
	
	/**
	 * Method used to decode a String encoded following the CBC pattern
	 * @param cipher the byte array representing the encoded text
	 * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return the clear text
	 */
	public static byte[] decryptCBC(byte[] cipher, byte[] iv) {
		//TODO : COMPLETE THIS METHOD	
		return null; //TODO: to be modified
	}
	
	
	

		
		
		
		
		
}
