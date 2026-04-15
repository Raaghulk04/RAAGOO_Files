import java.util.Random;
import java.util.HashMap;
import java.util.LinkedList;

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
	private HashMap<String, HashMap<String, Integer>> hash = new HashMap<>();
	private LinkedList<String> queue = new LinkedList<>();

	// Use this to generate random numbers as needed
	private Random generator = new Random();

	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = ' ';

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
		int numOfNO = 0;
		int idx = 0;
		StringBuilder str = new StringBuilder();

		// spaces => stop after order-th space OR when idx >= text.length()
		// ideal usage => stop after order-th space
		while (numOfNO <= this.order && idx < text.length()) {
			if (text.charAt(idx++) == NOCHARACTER) {
				String key = str.toString();
				queue.add(key);
				str.setLength(0);
				numOfNO++;
			} else {
				str.append(text.charAt(idx++));
			}
		}

		while (idx < text.length() - 1) {
			StringBuilder hashStrB = new StringBuilder();
			if (text.charAt(idx++) == NOCHARACTER) {
				for (String s : queue) {
					hashStrB.append(s).append(NOCHARACTER);
				}
				hashStrB.setLength(str.length() - 1);
				String hashKey = hashStrB.toString();

				String newKey = str.toString();
				str.setLength(0);

				if (!hash.containsKey(hashKey)) {
					HashMap<String, Integer> newHash = new HashMap<>();
					// Initialise index 0 to totalFreq
					newHash.put(null, 0);
					hash.put(hashKey, newHash);
				}
				HashMap<String, Integer> smallHash = hash.get(hashKey);
				if (smallHash.containsKey(newKey)) {
					smallHash.put(hashKey, smallHash.get(hashKey) + 1);
					smallHash.put(null, smallHash.get(hashKey) + 1);
				} else {
					smallHash.put(hashKey, 1);
					smallHash.put(null, smallHash.get(hashKey) + 1);
				}
				queue.add(newKey);
				queue.pollFirst();
			} else {
				str.append(text.charAt(idx++));
			}
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		if (!hash.containsKey(kgram)) {
			return 0;
		}
		return hash.get(kgram).get(null);
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, String s) {
		if (!hash.containsKey(kgram)) {
			return 0;
		}
		return hash.get(kgram).get(s);
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public String nextString(String kgram) {
		// See the problem set description for details
		// on how to make the random selection.
		if (getFrequency(kgram) == 0) {
			return "";
		}
		int rand = generator.nextInt(getFrequency(kgram));
		int freq = 0;
		for (String s : hash.get(kgram).keySet()) {
			freq = hash.get(kgram).get(s);
			if (freq == 0) {
				continue;
			}
			rand -= freq;
			if (rand < 0) {
				return s;
			}
		}
		return "";
	}
}
