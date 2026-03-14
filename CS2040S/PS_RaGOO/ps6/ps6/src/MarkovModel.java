
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
	// order of k
	private int order;

	// Build HashMap
	HashMap<String, Integer[]> map = new HashMap<>();

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
        // clear the map, since this method can be called multiple times and is expected to build a new Markov Model each time
        map.clear();
		// Build the Markov model here
		int length = text.length();
		int k = order;

		// next char
		char next;

		// iterate through the entire text, until length - k, to obtain all kgrams
		for (int i = 0; i <= length - k; i++) {
			String kgram = text.substring(i, i + k);
			next = (i + k) < length ? text.charAt(i + k) : '\0';

			if (map.containsKey(kgram)) {
				Integer[] array = map.get(kgram);
				if (next != '\0') {
					array[(int) next]++;
					array[256]++;
				}
			} else {
				Integer[] array = new Integer[257];
				for (int r = 0; r < 257; r++) {
					array[r] = 0;
				}
				if (next != '\0') {
					array[(int) next] = 1;
					array[256] = 1;
				}
				map.put(kgram, array);
			}
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		Integer[] array = map.get(kgram);
        return array == null
                ? 0
                : array[256];
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		Integer[] array = map.get(kgram);
        return array == null
                ? 0
                : array[(int) c];
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		// See the problem set description for details
		// on how to make the random selection.
        int length = kgram.length();
        String str = kgram.substring(length - order, length);
		// 1. Grab pre-calculated frequency array in a single O(1) lookup
		Integer[] freqs = map.get(str);

		// 2. Early exit if kgram isn't found or has no following characters
		if (freqs == null || freqs[256] == 0) {
			return NOCHARACTER;
		}


		int totalSum = freqs[256];
		int number = generator.nextInt(totalSum);

		// 4. Calculate the prefix sum on the fly to find the character
		int runningSum = 0;
		for (int i = 0; i < 256; i++) {
			// Only process characters that actually appeared
			if (freqs[i] > 0) {
				runningSum += freqs[i];
				// If our random number falls into this character's "bucket"
				if (number < runningSum) {
					return (char) i;
				}
			}
		}

		return NOCHARACTER;
	}
}
