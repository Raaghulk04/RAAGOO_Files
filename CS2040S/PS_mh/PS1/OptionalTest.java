import static org.junit.Assert.*;

import org.junit.Test;

public class OptionalTest {
    public static int[] stringToSeed(String password) {
        // Each character in a string is represented as 8-bit number (extended ASCII)
        int[] seed = new int[password.length() * 8];
        int index = 0;

        for (char c : password.toCharArray()) {
            int value = (int) c; // ASCII value

            // reverse the ASCII bit order
            for (int i = 0; i < 8; i++) {
                // shift the order to the right and preserve the units place using & 1.
                seed[index++] = (value >> i) & 1;
            }
        }

        return seed;
    }

    @Test
    public void testZeroesAndOnes() {
        String password = "TheCowJumpedOverTheMoon";
        int[] seed = stringToSeed(password);

        // seed.length / 2 is always an integer since seed.length is a multiple of 8, so no math floor needed.
        ILFShiftRegister r = new ShiftRegister(seed.length, seed.length / 2);
        r.setSeed(seed);

        // I actually don't know if it's zeroes or zeros.
        int zeroes = 0;
        int ones = 0;
        int iterations = 10000;

        for (int i = 0; i < iterations; i++) {
            int value = r.shift();
            if (value == 0) {
                zeroes++;
            } else {
                ones++;
            }
        }

        System.out.printf("Zeroes: %d (%.2f%%)%n", zeroes, ((double) zeroes / iterations) * 100);
        System.out.printf("Ones: %d (%.2f%%)%n", ones, ((double) ones / iterations) * 100);

        int difference = Math.abs(zeroes - ones);
        assertTrue(difference < iterations * 0.05);
    }
}
