import static org.junit.Assert.*;

import org.junit.Test;

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
     * Tests shift with other scenario
     */
    @Test
    public void testShift2() {
        ILFShiftRegister r = getRegister(6, 1);
        int[] seed = { 1, 1, 1, 1, 1, 1 };
        r.setSeed(seed);
        int[] expected = { 0, 0, 1, 1, 0, 0, 0, 0, 1, 1 };
        for (int i = 0; i < 10; i++) {
            assertEquals(expected[i], r.shift());
        }
    }

    /**
     * Tests generate with other scenario
     */
    @Test
    public void testGenerate2() {
        ILFShiftRegister r = getRegister(6, 1);
        int[] seed = { 1, 1, 1, 1, 1, 1 };
        r.setSeed(seed);
        int[] expected = { 3, 0, 15, 12, 12, 3, 15, 3, 0, 15 };
        for (int i = 0; i < 10; i++) {
            assertEquals(expected[i], r.generate(4));
        }
    }


    /**
     * Tests register of seed comprised of only 0
     * seed will infinitely loop; i.e. no change in seed
     * register return value should always output 0
     */
    @Test
    public void testZeroSeed() {
        ILFShiftRegister r = getRegister(9, 7);
        int[] seed = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        r.setSeed(seed);
        int[] expected = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < 10; i++) {
            assertEquals(expected[i], r.generate(3));
        }
    }

    /**
     * Tests shift of tap = most significant bit
     * i.e. XOR with itself; shift return value should always output 0
     * Note: is mathematically already tested by testOneLength
     */
    @Test
    public void testXORSelf() {
        ILFShiftRegister r = getRegister(9, 8);
        int[] seed = { 0, 1, 0, 1, 1, 1, 1, 0, 1 };
        r.setSeed(seed);
        int[] expected = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < 10; i++) {
            assertEquals(expected[i], r.shift());
        }
    }

    /**
     * Tests with erroneous seed.
     */
    @Test
    public void testError() {
        ILFShiftRegister r = getRegister(4, 1);
        int[] seed = { 1, 0, 0, 0, 1, 1, 0 };
        r.setSeed(seed);
        r.shift();
        r.generate(4);
    }
}

/**
 * 1. The easiest solution is to have the program fail and deliver an error message, e.g. System.out.println("Error: Length of array provided for seed does not match specified register size."). A return code that would not naturally occur, e.g. -1 could be returned as the output of the program. This would be the safest possible route and also the most convenient to implement, ensuring that a seed is not mistakenly entered wrongly. However, should there be a large amount of data being processed (e.g. user needs to input a hundred seeds and their taps and their sizes), we could consider a scenario whereby the seed was inputted correctly and instead the register size was inputted wrongly, where this would be less than ideal. (The user would have to update all the sizes manually in the input file.)
 * 2. Alternatively, we could have the program update the size automatically, e.g. this.size = seed.length;. This would allow the program to run without any issue. However, consider that the seed was in fact entered wrongly; this would result in false results, which depending on the usage of this program may have significant consequences. The user would be unaware of any issue with the seed, as there would be no error message, and this would really render the size field of the ShiftRegister class redundant.
 * 3. A proper response would likely depend on the context in which the program is implemented. Ideally, a combination of the two solutions would be ideal (a prompt to update the sizes of the ShiftRegister class), but may be overly complicated to implement in a simple program such as this that reads from another java file rather than a terminal window as standard input.
 * 4. In case (1), where the program would be expected to provide an error message, then the right way to test the case would be to expect the result to r.shift() to be the aforementioned error code, e.g. -1, and assign the appropriate int[] expected.
 * 5. In case (2), where the program would be expected to run regardless, then the right way to test the case would be to expect the result that would have originated from the inputted tap, the seed and the correct size of the seed inputted. For instance, in the testError() test, the result would have been identical to those should the object r be constructed using getRegister(7, 1);.
 * 6. In case (3), the test would be significantly more complicated as it would have to test both a 'yes' and 'no' response to the prompt to update the sizes of the ShiftRegister class, and hence involve extra steps to respond to the prompt. With a 'yes' response, it would test according to case (5), whereas with a 'no' resposne, it should test according to case (4).
 */