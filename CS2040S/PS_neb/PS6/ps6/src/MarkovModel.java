import java.util.Random;
import java.util.HashMap;

/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {
	private int order;
	private HashMap<String, int[]> hash = new HashMap<>();

	// Use this to generate random numbers as needed
	private Random generator = new Random();

	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;

	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
		this.order = order;

		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		// Build the Markov model here
		for (int i = order; i < text.length(); i++) {
			if (text.charAt(i) == NOCHARACTER) {
				continue;
			}

			String key = text.substring(i - order, i);
			// Create an array of int to contain all possible charas
			if (!hash.containsKey(key)) {
				int[] newArr = new int[256];
				for (int j = 0; j < newArr.length; j++) {
					newArr[j] = 0;
				}
				hash.put(key, newArr);
			}
			// Return address of array and then increment index and totalFreq
			hash.get(key)[text.charAt(i) - NOCHARACTER]++;
			// Since index 0 is never used (NOCHAR), use it as a totalFreq
			hash.get(key)[0]++;
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		if (kgram.length() != this.order || !hash.containsKey(kgram)) {
			return 0;
		}
		return hash.get(kgram)[0];
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		if (kgram.length() != this.order || !hash.containsKey(kgram)) {
			return 0;
		}
		return hash.get(kgram)[c - NOCHARACTER];
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		// See the problem set description for details
		// on how to make the random selection.
		if (kgram.length() != this.order || getFrequency(kgram) == 0) {
			return NOCHARACTER;
		}
		int rand = generator.nextInt(getFrequency(kgram));
		int freq = 0;
		for (int i = 1; i < 256; i++) {
			freq = hash.get(kgram)[i];
			if (freq == 0) {
				continue;
			}
			rand -= freq;
			if (rand < 0) {
				return (char) i;
			}
		}
		return NOCHARACTER;
	}
}
