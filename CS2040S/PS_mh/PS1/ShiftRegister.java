///////////////////////////////////
// This is the main shift register class.
// Notice that it implements the ILFShiftRegister interface.
// You will need to fill in the functionality.
///////////////////////////////////

/**
 * class ShiftRegister
 * @author
 * Description: implements the ILFShiftRegister interface.
 */
public class ShiftRegister implements ILFShiftRegister {
    ///////////////////////////////////
    // Create your class variables here
    ///////////////////////////////////
    // TODO: an integer array to store the bits, the size of an array, the index of the tap bit
    int[] register;
    int size;
    int tap;
    ///////////////////////////////////
    // Create your constructor here:
    ///////////////////////////////////
    ShiftRegister(int size, int tap) {
        // TODO: initialise the register, store size and tap
        // Don't set the seed here. Set it in setSeed.
        this.register = new int[size];
        this.size = size;
        this.tap = tap;
    }

    ///////////////////////////////////
    // Create your class methods here:
    ///////////////////////////////////
    /**
     * setSeed
     * @param seed
     * Description: Copies the values from seed into the internal register
     */
    @Override
    public void setSeed(int[] seed) {
        // TODO: Copy values element-by-element
        /* The following is actually needed. But since Problem 2.a. PROMISES me that "subsequent calls of setSeed should have seed.length = size",
           I'll just comment it out.
        if (seed.length != size) {
            throw new IllegalArgumentException("Seed length must match register size.");
        }
         */

        /* Originally I wrote like this:
        for (int i = 0; i < this.size; i++) {
            this.register[i] = seed[i];
        }
        But then the system tells me it's a manual array copy and can be done another way:
        */
        System.arraycopy(seed, 0, this.register, 0, this.size);
        // It's just a quick way to clone an array.
    }

    /**
     * shift
     * @return {int} The least significant bit of the resulting register.
     * Description: Computes the feedback bit, shifts everything right,
     *              sets the least significant bit to the feedback bit,
     *              returns the least significant bit
     */
    @Override
    public int shift() {
        // TODO: Shift everything right using for loop.
        int feedbackBit = this.register[this.tap] ^ this.register[this.size - 1];
        for (int i = this.size - 1; i > 0; i--) {
            this.register[i] = this.register[i - 1];
        }
        // I remember that we can combine assignment with return statement in another language,
        // so I just try it in Java,
        // and it works!
        return this.register[0] = feedbackBit;
    }

    /**
     * generate
     * @param k
     * @return {int} The decimal number of k bits from the shift register.
     * Description: Calls shift() exactly k times, collects the returned bits, interprets them as a binary number.
     */
    @Override
    public int generate(int k) {
        // TODO: Append the returned bits into a result, and convert the result into decimal number
        // I was trying to figure out what shift register does with visual aids,
        // so I searched it in YouTube...
        // It led me to this video: https://youtu.be/Ks1pw1X22y4?si=iXlLLrKVtIaDqE2j
        // In the video, the code is written in a way that I've never expected.
        // Although it is written in Python, I found out that these symbols exist in Java as well.
        // I remember in Java, OR is represented as ||, AND is &&.
        // So what's | and &?
        // It's a whole new kind of syntax!
        // So I did my research... They are the bitwise operators: https://www.geeksforgeeks.org/java/bitwise-operators-in-java/
        // Surprisingly, it will automatically convert the input numbers into binary numbers,
        // treat them as binary numbers in operations,
        // and output a decimal number! AUTOMATICALLY!
        // Which saves me a lot of work.
        // I think that's why the toDecimal method can be ignored, as mentioned in Problem 2.a..

        // Set the result into 0 for further operations (initialisation)
        int result = 0;
        // This for loop calls shift() k times
        for (int i = 0; i < k; i++) {
            // Shift the bits of a number left (therefore multiplying the whole (decimal) number by 2),
            // and then put the returned bit of shift() at the units place.
            // Using bitwise OR (|), the places other than units place are all preserved.
            // The units place of the intermediate result before OR operations is 0 (after left shift),
            // therefore using OR can just simply insert the returned bit of shift() at the units place,
            // cuz 0 OR anything just return that anything (which is 0 or 1).
            // Eg: bits returned are 1, 1, 0
            // 0 -> 00 | 1 = 1 -> 10 | 1 = 11 -> 110 | 0 = 110 -> 6
            // But actually, it will AUTOMATICALLY convert it into decimal number during the process.
            // So, in fact, it should be 0 -> 0 | 1 -> 2 (10) | 1 -> 6 (110) | 0 -> 6
            result = result << 1 | shift();
        }
        // The decimal number of the result.
        return result;
    }

    /**
     * Returns the integer representation for a binary int array.
     * @param array
     * @return
     */
    private int toDecimal(int[] array) {
        // TODO: ignored
        return 0;
    }
}
