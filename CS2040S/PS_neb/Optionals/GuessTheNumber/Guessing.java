import org.junit.Test;

public class Guessing {
    int max = 1001;
    int min = 0;
    int lastCall;

    @Test
    public int guess() {
        lastCall = min + (max - min) / 2;
        System.out.println(lastCall);
        return lastCall;
    }

    public void update(int answer) {
        if (answer == -1) {
            // lastCall too low
            min = lastCall + 1;
        } else if (answer == 1) {
            // lastCall too high
            max = lastCall;
        } else {
            throw new IllegalArgumentException(String.format("Illegal Input: %d", answer));
        }
    }
}