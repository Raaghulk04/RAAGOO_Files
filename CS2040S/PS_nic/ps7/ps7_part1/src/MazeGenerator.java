import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MazeGenerator {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][]{
			{-1, 0}, // North
			{1, 0},  // South
			{0, 1},  // East
			{0, -1}  // West
	};
	private Integer[] directions = new Integer[] {
		0, 1, 2, 3
	};

	private MazeGenerator() {}

	// Method: Recursive Backtracking
	public Maze generateMaze(int rows, int columns) {
		Room[][] rooms = new Room[rows][columns];
		Random rng = new Random();

		// Choose start point
		int startRow = rng.nextInt(rows);
		int startCol = rng.nextInt(columns);

		carvePath(startRow, startCol, rooms);
		return new Maze(rooms);
	}

	public void main(String[] args) {
		Maze maze = generateMaze(5, 5);
		ImprovedMazePrinter.printMaze(maze, 0, 0);
	}

	private void carvePath(int currRow, int currCol, Room[][] rooms) {
		// randomly shuffle directions
		shuffleDirections(this.directions);

		// randomly choose 
		for (int direction: directions) {
			int newRow = getNewRow(currRow, direction);
			int newCol = getNewCol(currCol, direction);

			if ((newCol >= 0 && newCol < rooms.length) && (newRow >= 0 && newRow < rooms[0].length) && rooms[newRow][newCol] == null) {
				rooms[currCol][currRow] = getDir(direction);
				rooms[newCol][newRow] = getOppDir(direction);
				carvePath(newRow, newCol, rooms);
			}
		}
		
	}

	private void shuffleDirections(Integer[] directions) {
		List<Integer> l = Arrays.asList(directions);
		Collections.shuffle(l);
		directions = l.toArray(new Integer[0]);
	}

	private int getNewRow(int row, int dir) {
		return row + DELTAS[dir][0];
	}

	private int getNewCol(int col, int dir) {
		return col + DELTAS[dir][1];
	}

	private Room getDir(int dir) {
		switch (dir) {
			case NORTH:
				return new Room(true, false, false, false);
			case SOUTH:
				return new Room(false, true, false, false);
			case EAST:
				return new Room(false, false, true, false);
			case WEST:
				return new Room(false, false, false, true);
		}
		return null;
	}

	private Room getOppDir(int dir) {
		switch (dir) {
			case NORTH:
				return new Room(false, true, false, false);
			case SOUTH:
				return new Room(true, false, false, false);
			case EAST:
				return new Room(false, false, false, true);
			case WEST:
				return new Room(false, false, true, false);
		}
		return null;
	}
}
