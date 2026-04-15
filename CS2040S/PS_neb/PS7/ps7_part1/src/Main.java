public class Main {
    public static void main(String[] args) {
        args = new String[]{"12", "12"};
        int rows = 0;
        for (int i = 0; i < args[0].length(); i++) {
            rows *= 10;
            rows += args[0].charAt(0) - '0';
        }
        int cols = 0;
        for (int i = 0; i < args[0].length(); i++) {
            cols *= 10;
            cols += args[1].charAt(0) - '0';
        }

        MazePrinter.printMaze(MazeGenerator.generateMaze(rows, cols));
    }
}
