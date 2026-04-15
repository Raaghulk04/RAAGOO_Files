import java.io.FileInputStream;
import java.util.LinkedList;

/**
 * This class is used to generated text using a Markov Model
 */
public class TextGenerator {
    private static LinkedList<String> queue = new LinkedList<>();

    // For testing, we will choose different seeds
    private static long seed;

    // Sets the random number generator seed
    public static void setSeed(long s) {
        seed = s;
    }

    /**
     * Reads in the file and builds the MarkovModel.
     *
     * @param order the order of the Markov Model
     * @param fileName the name of the file to read
     * @param model the Markov Model to build
     * @return the first {@code order} characters of the file to be used as the seed text
     */
    public static String buildModel(int order, String fileName, MarkovModel model) {
        // Get ready to parse the file.
        // StringBuffer is used instead of String as appending character to String is slow
        StringBuilder text = new StringBuilder();
        StringBuilder words = new StringBuilder();
        int numOfNO = 0;

        // Loop through the text
        try {
            FileInputStream inputStream = new FileInputStream(fileName);

            // Determine the size of the file, in bytes
            int fileSize = inputStream.available();

            // Read in the file, one character at a time.
            for (int i = 0; i < fileSize; i++) {
                // Read a character
                char c = (char) inputStream.read();
                if (c == ' ') {
                    numOfNO++;
                    if (numOfNO <= order) {
                        queue.add(words.toString());
                        words.setLength(0);
                        text.append(c);
                        continue;
                    } else {
                        queue.add(words.toString());
                        words.setLength(0);
                        queue.pollFirst();
                        text.append(c);
                        continue;
                    }
                }
                text.append(c);
                words.append(c);
            }
            queue.add(words.toString());
            words.setLength(0);
            queue.pollFirst();
            numOfNO++;

            // Make sure that length of input text is longer than requested Markov order
            if (numOfNO < order) {
                System.out.println("Text is shorter than specified Markov Order.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Problem reading file " + fileName + ".");
            return null;
        }

        // Build Markov Model of order from text
        model.initializeText(text.toString());
        return text.substring(0, order);
    }

    /**
     * generateText outputs to stdout text of the specified length based on the specified seedText
     * using the given Markov Model.
     *
     * @param model the Markov Model to use
     * @param seedText the initial kgram used to generate text
     * @param order the order of the Markov Model
     * @param length the length of the text to generate
     */
    public static void generateText(MarkovModel model, String seedText, int order, int length) {
        // Use the first order characters of the text as the starting string
        StringBuffer output = new StringBuffer();

        int outLength = 0;
        while (outLength < length) {
            System.out.println(queue);
            StringBuilder str = new StringBuilder();
            for (String s : queue) {
                str.append(s).append(" ");
            }
            str.setLength(str.length() - 1);

            String stringToAppend = model.nextString(str.toString());

            // If there is no next string, restart generation with initial kgram value which
            // Starts from 0th position.
            if (!stringToAppend.isEmpty()) {
                output.append(stringToAppend).append(" ");
                outLength++;
                queue.pollFirst();
                queue.add(stringToAppend);
            } else {
                // This prefix has never appeared in the text.
                // Give up?
                System.out.println(output);
                return;
            }
        }

        // Output the generated characters, not including the initial seed.
        System.out.println(output);
    }

    /**
     * The main routine.  Takes 3 arguments:
     * args[0]: the order of the Markov Model
     * args[1]: the length of the text to generate
     * args[2]: the filename for the input text
     */
    public static void main(String[] args) {
        // Check that we have three parameters
        if (args.length != 3) {
            System.out.println("Number of input parameters are wrong.");
        }

        // Get the input:
        int order = Integer.parseInt(args[0]);
        int length = Integer.parseInt(args[1]);
        String fileName = args[2];

        // Create the model
        MarkovModel markovModel = new MarkovModel(order, seed);
        String seedText = buildModel(order, fileName, markovModel);

        // Generate text
        generateText(markovModel, seedText, order, length);
    }
}