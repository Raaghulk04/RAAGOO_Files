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
    private int size;
    private int tap;
    private int[] seed;
    ///////////////////////////////////
    // TODO:

    ///////////////////////////////////
    // Create your constructor here:
    ///////////////////////////////////
    ShiftRegister(int size, int tap) {
        // TODO:
        this.size = size;
        this.tap = tap;
    }

    ///////////////////////////////////
    // Create your class methods here:
    ///////////////////////////////////
    /**
     * setSeed
     * @param seed
     * Description:
     */
    @Override
    public void setSeed(int[] seed) {
        // TODO:
        this.seed = seed;
    }

    /**
     * shift
     * @return
     * Description:
     */
    @Override
    public int shift() {
        // TODO:
        // step 1: calculate feedback bit as XOR of most significant and tap
        int feedback_bit = this.seed[this.size - 1]^this.seed[this.tap];
        for(int i = this.size - 2; i >= 0; i--){
            // ignore step 2, as most significant will be overwritten
            // step 3: every bit is moved one position down (i => i+1)
            this.seed[i+1] = this.seed[i];
        }
        // step 4: least significant set to feedback bit
        this.seed[0] = feedback_bit;
        // return least significant bit of resulting register
        return feedback_bit;
    }

    /**
     * generate
     * @param k
     * @return
     * Description:
     */
    @Override
    public int generate(int k) {
        // TODO:
        int v = 0;
        for(int i = k-1; i >= 0; i--){
            v*=2;
            v+=shift();
        }
        return v;
    }

    // String to Binary String Method
    public String toBinStr (String str) {
        int strlen = str.length();
        StringBuilder bStr = new StringBuilder();
        for (int i = 0; i < strlen; ++i){
            // each character as a binary string
            // as a bit of Google tells us Java characters are 16 bit (2 bytes)
            String temp = Integer.toBinaryString((int) str.charAt(i));
            for (int j = 0; j < 16-temp.length(); j++){
                bStr.append('0');
            }
            bStr.append(temp);
        }
        return new String(bStr);
    }

    /**
     * Returns the integer representation for a binary int array.
     * @param array
     * @return
     */
    private int toDecimal(int[] array) {
        // TODO:
        return 0;
    }
}
