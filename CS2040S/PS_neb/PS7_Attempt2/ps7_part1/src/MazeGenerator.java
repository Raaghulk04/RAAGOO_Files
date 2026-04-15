import java.util.Random;

public class MazeGenerator {
	private static Random rng = new Random();
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][]{
			{-1, 0}, // North
			{1, 0},  // South
			{0, 1},  // East
			{0, -1}  // West
	};

	private MazeGenerator() { }

	// TODO: Feel free to modify the method parameters.
	public static Maze generateMaze(int rows, int columns) {
		Random rng = new Random();
		boolean[][] hash = new boolean[rows][columns];
		int numOfmisleadOpens = rows * columns / 2;
		Room[][] rooms = new Room[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < rows; j++) {
				rooms[i][j] = new Room(true, true, true, true);
			}
		}
		while (numOfmisleadOpens > 0) {
			int i = rng.nextInt(rows);
			int j = rng.nextInt(columns);
			if (!hash[i][j]) {
				boolean n = rng.nextBoolean();
				boolean s = rng.nextBoolean();
				boolean e = rng.nextBoolean();
				boolean w = rng.nextBoolean();
				rooms[i][j] = new Room(n, s, e, w);
				numOfmisleadOpens--;
			}
		}
		hash = new boolean[rows][columns];
		int currRow = 0;
		int currCol = 0;
		int softLock = 0;
		while (currRow != rows - 1 || currCol != columns - 1) {
			int dir = rng.nextInt(4);
			if (currRow + DELTAS[dir][0] < 0 || currRow + DELTAS[dir][0] >= rows) {
				continue;
			}
			if (currCol + DELTAS[dir][1] < 0 || currCol + DELTAS[dir][1] >= columns) {
				continue;
			}
			if (softLock > 10) {
				hash[currRow + DELTAS[dir][0]][currCol + DELTAS[dir][1]] = false;
				softLock = 0;
			}
			if (hash[currRow + DELTAS[dir][0]][currCol + DELTAS[dir][1]]) {
				softLock++;
				continue;
			}
			softLock = 0;
			rooms[currRow][currCol].changeWall(dir);
			hash[currRow + DELTAS[dir][0]][currCol + DELTAS[dir][1]] = true;
			currRow = currRow + DELTAS[dir][0];
			currCol = currCol + DELTAS[dir][1];
			int oppDir = dir == 0 || dir == 1 ? dir == 0 ? 1 : 0
					     : dir == 2 ? 3 : 2;
			rooms[currRow][currCol].changeWall(oppDir);
		}
		for (int i = 0; i < columns; i++) {
			rooms[0][i].fixWall(NORTH);
			rooms[rows - 1][i].fixWall(SOUTH);
		}
		for (int i = 0; i < rows; i++) {
			rooms[i][0].fixWall(WEST);
			rooms[i][columns - 1].fixWall(EAST);
		}
		return new Maze(rooms);
	}
}
