import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

public class MazeSolver implements IMazeSolver {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};
	private Maze maze;
	private boolean solved;
	private boolean[][] visited;
	private int endRow, endCol;
	private HashMap<Integer, Integer> reachableRooms;

	private class Point {
		int x;
		int y;
		int step;
		Point prev;

		private Point(int row, int col) {
			this.x = row;
			this.y = col;
			this.step = 0;
			this.prev = null;
		}
	}

	public MazeSolver() {
		solved = false;
		maze = null;
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.visited = new boolean[maze.getRows()][maze.getColumns()];
		this.solved = false;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// Goal: shortest path
		// Edge Case 1: no maze
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		} 

		// Edge Case 2: invalid coordinate
		validate(startRow, startCol, endRow, endCol);
		
		// set all visited flag to false
		// before we begin our search
		for (int i = 0; i < maze.getRows(); ++i) {
			for (int j = 0; j < maze.getColumns(); ++j) {
				this.visited[i][j] = false;
				maze.getRoom(i, j).onPath = false;
			}
		}

		// set End
		this.endRow = endRow;
		this.endCol = endCol;

		// Initialize a new queue for BFS
		Queue<Point> q = new ArrayDeque<Point>();
		q.add(new Point(startRow, startCol));

		// Initialized hashmap
		reachableRooms = new HashMap<>();

		// Update source
		visited[startRow][startCol] = true;
		maze.getRoom(startRow, startCol).onPath = true;
		reachableRooms.putIfAbsent(q.peek().step, 1);

		return solve(q);
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		return reachableRooms.getOrDefault(k,0);
	}

	public static void main(String[] args) {
		// Do remember to remove any references to ImprovedMazePrinter before submitting
		// your code!
		try {
			Maze maze = Maze.readMaze("/home/kopiosiewdai/NUS/CS2040S/ps/ps7/ps7_part1/maze-sample.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 2, 3));
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

			int row = currPoint.x;
			int col = currPoint.y;

			// Found end
			if (row == endRow && col == endCol) {
				res = getPath(currPoint);
			}

			// Check Neighbours
			for (int direction = 0; direction < 4; direction++) {
				// Edge Case: cant go in that direction
				if (!canGo(row, col, direction)) {
					continue;
				} 

				// Only traverse if neighbour is unvisited
				Point neighPoint = makePoint(row, col, direction);
				if (!visited[neighPoint.x][neighPoint.y]) {
					// update neighbour
					visited[neighPoint.x][neighPoint.y] = true;
					q.add(neighPoint);
					neighPoint.prev = currPoint;

					// update steps needed
					neighPoint.step = neighPoint.prev.step + 1;
					reachableRooms.put(neighPoint.step, reachableRooms.getOrDefault(neighPoint.step, 0) + 1);
				}
			}
		}
		
		// Edge Case: traverse finish and cant find End
		return res;
	}

	private Integer getPath(Point currPoint) {
		// Edge Case: reach startPoint
		if (currPoint.prev == null) {
			return 0;
		}

		// update path
		maze.getRoom(currPoint.x, currPoint.y).onPath = true;

		return 1 + getPath(currPoint.prev);
	}

	private Point makePoint(int row, int col, int dir) {
		return new Point(row + DELTAS[dir][0], col + DELTAS[dir][1]);
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

	private void validate(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}
	}
}
