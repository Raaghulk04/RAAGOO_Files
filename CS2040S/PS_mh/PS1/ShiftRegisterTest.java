import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.text.html.Option;

/**
 * ShiftRegisterTest
 * @author dcsslg
 * Description: set of tests for a shift register implementation
 */
public class ShiftRegisterTest {
    /**
     * Returns a shift register to test.
     * @param size
     * @param tap
     * @return a new shift register
     */
    ILFShiftRegister getRegister(int size, int tap) {
        return new ShiftRegister(size, tap);
    }

    /**
     * Tests shift with simple example.
     */
    @Test
    public void testShift1() {
        ILFShiftRegister r = getRegister(9, 7);
        int[] seed = { 0, 1, 0, 1, 1, 1, 1, 0, 1 };
        r.setSeed(seed);
        int[] expected = { 1, 1, 0, 0, 0, 1, 1, 1, 1, 0 };
        for (int i = 0; i < 10; i++) {
            assertEquals(expected[i], r.shift());
        }
    }

    /**
     * Tests generate with simple example.
     */
    @Test
    public void testGenerate1() {
        ILFShiftRegister r = getRegister(9, 7);
        int[] seed = { 0, 1, 0, 1, 1, 1, 1, 0, 1 };
        r.setSeed(seed);
        int[] expected = { 6, 1, 7, 2, 2, 1, 6, 6, 2, 3 };
        for (int i = 0; i < 10; i++) {
            assertEquals("GenerateTest", expected[i], r.generate(3));
        }
    }

    /**
     * Tests register of length 1.
     */
    @Test
    public void testOneLength() {
        ILFShiftRegister r = getRegister(1, 0);
        int[] seed = { 1 };
        r.setSeed(seed);
        int[] expected = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };
        for (int i = 0; i < 10; i++) {
            assertEquals(expected[i], r.generate(3));
        }
    }

    /**
     * Tests with erroneous seed.
     */
    @Test
    public void testError() {
        ILFShiftRegister r = getRegister(4, 1);
        int[] seed = { 1, 0, 0, 0, 1, 1, 0 };
        /*
        Modification, along with the IllegalArgumentException modification of setSeed() in Problem 2a:
        assertThrows(IllegalArgumentException.class, () -> {
            r.setSeed(seed);
        });
         */
    }

    /**
     * Tests all-zero seed.
     */
    @Test
    public void testAllZeroSeed() {
        ILFShiftRegister r = getRegister(5, 2);
        int[] seed = { 0, 0, 0, 0, 0 };
        r.setSeed(seed);
        for (int i = 0; i < 10; i++) {
            assertEquals(0, r.shift());
        }
    }

    /**
     * Tests all-one seed.
     */
    @Test
    public void testAllOneSeed() {
        ILFShiftRegister r = getRegister(6, 3);
        int[] seed = { 1, 1, 1, 1, 1, 1 };
        r.setSeed(seed);
        for (int i = 0; i < 10; i++) {
            int res = r.shift();
            assertTrue(res == 0 || res == 1);
        }
    }

    /**
     * Tests tap at index 0.
     */
    @Test
    public void testTapAtZero() {
        ILFShiftRegister r = getRegister(5, 0);
        int[] seed = { 1, 0, 1, 0, 0 };
        r.setSeed(seed);
        // Just verify it runs and outputs valid bits.
        for (int i = 0; i < 10; i++) {
            int res = r.shift();
            // The output should always be either 0 or 1.
            assertTrue(res == 0 || res == 1);
        }
    }

    /**
     * Tests tap at last index.
     */
    @Test
    public void testTapAtLastIndex() {
        ILFShiftRegister r = getRegister(5, 4);
        int[] seed = { 1, 0, 1, 0, 1 };
        r.setSeed(seed);
        // Just verify it runs and outputs valid bits.
        for (int i = 0; i < 10; i++) {
            int res = r.shift();
            // The output should always be either 0 or 1.
            assertTrue(res == 0 || res == 1);
        }
    }

    /**
     * Tests generate(k) with k = 1.
     */
    @Test
    public void testGenerateOneBit() {
        ILFShiftRegister r = getRegister(5, 2);
        int[] seed = { 0, 1, 0, 1, 0 };
        r.setSeed(seed);
        // Just verify it runs.
        for (int i = 0; i < 10; i++) {
            int res = r.generate(1);
            assertTrue(res == 0 || res == 1);
        }
    }

    /**
     * Tests generate(k) with maximum k (k = 31).
     */
    @Test
    public void testGenerateMaxBit() {
        ILFShiftRegister r = getRegister(10, 3);
        int[] seed = { 1, 0, 1, 1, 0, 1, 0, 0, 1, 1 };
        r.setSeed(seed);
        // Just verify it runs.
        for (int i = 0; i < 10; i++) {
            int res = r.generate(31);
            assertTrue(res >= 0);
        }
    }
}
