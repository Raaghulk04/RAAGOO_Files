import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};
	private Maze maze;
	private boolean[][][] visited;
	private int endRow, endCol, startingPower;
	private HashMap<Integer, Integer> reachableRooms;

	private class Point {
		int row;
		int col;
		int step;
		int power;
		Point prev;

		private Point(int row, int col, int power) {
			this.row = row;
			this.col= col;
			this.step = 0;
			this.prev = null;
			this.power = power;
		}
	}

	public MazeSolverWithPower() {
		this.maze = null;
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;

	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		return pathSearch(startRow, startCol, endRow, endCol, 0);
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		return reachableRooms.getOrDefault(k,0);
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol, int startingPower) throws Exception {
		// Edge Case 1: no maze
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		// Edge Case 2: invalid coordinate
		validate(startRow, startCol, endRow, endCol);
		
		// Initialize visited
		this.visited = new boolean[this.maze.getRows()][this.maze.getColumns()][startingPower + 1];

		// set all onpath back to false
		for (int i = 0; i < this.maze.getRows(); ++i) {
			for (int j = 0; j < this.maze.getColumns(); ++j) {
				maze.getRoom(i, j).onPath = false;
			}
		}

		// set End and startingPower
		this.endRow = endRow;
		this.endCol = endCol;
		this.startingPower = startingPower;

		// Initialize a new queue for BFS
		Queue<Point> q = new ArrayDeque<Point>();
		q.add(new Point(startRow, startCol, startingPower));

		// Initialize hashmap
		reachableRooms = new HashMap<>();

		// Update source
		visited[startRow][startCol][startingPower] = true;
		reachableRooms.putIfAbsent(q.peek().step, 1);

		return solve(q);
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("/home/kopiosiewdai/NUS/CS2040S/ps/ps7/ps7_part1/maze-sample.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 4, 3, 2));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Integer solve(Queue<Point> q) {
		Integer res = null;

		while (q.peek() != null) {
			Point currPoint = q.poll();

			int row = currPoint.row;
			int col = currPoint.col;
			int power = currPoint.power;

			// Found end
			if (row == endRow && col == endCol) {
				// Edge Case: ignore if already "reached" before
				if (!maze.getRoom(row, col).onPath) {
					res = getPath(currPoint);
				}
			}

			// Check Neighbours
			for (int direction = 0; direction < 4; direction++) {
				// Edge Case: cant go in that direction
				if (!canGoSuper(row, col, power, direction)) {
					continue;
				}

				// Only traverse if neighbour is unvisited
				Point neighPoint = makePoint(row, col, power, direction);
				if (!this.visited[neighPoint.row][neighPoint.col][neighPoint.power]) {
					// update neighbour
					this.visited[neighPoint.row][neighPoint.col][neighPoint.power] = true;	
					q.add(neighPoint);
					neighPoint.prev = currPoint;

					// update steps
					neighPoint.step = neighPoint.prev.step + 1;
					
					// update reachable if not visited before
					if (!visitedBefore(neighPoint)) {
						reachableRooms.put(neighPoint.step, reachableRooms.getOrDefault(neighPoint.step, 0) + 1);					
					}

					// set all power lower than current to visited, does not make sense to visit same room with lesser power
					for (int i = neighPoint.power - 1; i > -1; i--){
						this.visited[neighPoint.row][neighPoint.col][i] = true;
					}
				}
			}
		}

		// Edge Case: traverse finish and cant find end, res will be null
		return res;
	}

	private boolean visitedBefore(Point neighPoint) {
		// check if visitedBefore by higher power or lower power, then set all lower power to visited afterwards
		for (int i = 0; i <= startingPower; i++) {
			if (i == neighPoint.power) {
				continue;
			}

			// update if visited before
			if (this.visited[neighPoint.row][neighPoint.col][i]) {
				return true;
			}
		}
		return false;
	}

	private Point makePoint(int row, int col, int power, int dir) {
		// Case 1: never use power, Case 2: use power
		Point noPower = new Point(row + DELTAS[dir][0], col + DELTAS[dir][1], power);
		Point Power = new Point(row + DELTAS[dir][0], col + DELTAS[dir][1], power - 1);
		
		switch (dir) {
			case NORTH:
				return !maze.getRoom(row, col).hasNorthWall() ? noPower : Power;
			case SOUTH:
				return !maze.getRoom(row, col).hasSouthWall() ? noPower : Power;
			case EAST:
				return !maze.getRoom(row, col).hasEastWall() ? noPower : Power;
			case WEST:
				return !maze.getRoom(row, col).hasWestWall() ? noPower : Power;
			default:
				return null; // Dummy , should never occur.
		}
	}

	private boolean canGoSuper(int row, int col, int power, int dir) {
		// Edge Case: no more power :/
		if (power <= 0) {
			return canGo(row, col, dir);
		}

		// Edge Case: phase out of the wall
		if (row + DELTAS[dir][0] < 0 || row + DELTAS[dir][0] >= maze.getRows()) return false;
		if (col + DELTAS[dir][1] < 0 || col + DELTAS[dir][1] >= maze.getColumns()) return false;
		
		// Else: just go brrrr
		return true;
 	}

	private boolean canGo(int row, int col, int dir) {
		// not needed since our maze has a surrounding block of wall
		// but Joe the Average Coder is a defensive coder!
		if (row + DELTAS[dir][0] < 0 || row + DELTAS[dir][0] >= maze.getRows()) return false;
		if (col + DELTAS[dir][1] < 0 || col + DELTAS[dir][1] >= maze.getColumns()) return false;

		switch (dir) {
			case NORTH:
				return !maze.getRoom(row, col).hasNorthWall();
			case SOUTH:
				return !maze.getRoom(row, col).hasSouthWall();
			case EAST:
				return !maze.getRoom(row, col).hasEastWall();
			case WEST:
				return !maze.getRoom(row, col).hasWestWall();
		}

		return false;
	}

	private Integer getPath(Point currPoint) {
		// update path
		maze.getRoom(currPoint.row, currPoint.col).onPath = true;

		// Edge Case: reach startPoint
		if (currPoint.prev == null) {
			return 0;
		}

		return 1 + getPath(currPoint.prev);
	}

	private void validate(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}
	}
}
