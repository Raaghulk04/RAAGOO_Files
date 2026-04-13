import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

public class MazeSolver implements IMazeSolver {
	private static final int TRUE_WALL = Integer.MAX_VALUE;
	private static final int EMPTY_SPACE = 0;
	private static final List<Function<Room, Integer>> WALL_FUNCTIONS = Arrays.asList(
			Room::getNorthWall,
			Room::getEastWall,
			Room::getWestWall,
			Room::getSouthWall
	);
	private static final int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 0, 1 }, // East
			{ 0, -1 }, // West
			{ 1, 0 } // South
	};
	private boolean [][] visited;
	private int endRow, endCol, fearCache;
	private Maze maze;

	private class Point {
		int row;
		int col;
		int fear;

		private Point(int row, int col) {
			this.row = row;
			this.col = col;
			this.fear = 0;
		}
	}

	public MazeSolver() {
		this.maze = null;
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// Edge Case 1: no maze
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		// Edge Case 2: invalid coordinate
		validate(startRow, startCol, endRow, endCol);

		// Initialize visited
		this.visited = new boolean[this.maze.getRows()][this.maze.getColumns()];

		// set End
		this.endRow = endRow;
		this.endCol = endCol;

		// Initialze a min heap for Dijkstra
		PriorityQueue<Point> pq = new PriorityQueue<>(
			(a, b) -> Integer.compare(a.fear, b.fear)
		);
		pq.add(new Point(startRow, startCol));

		return solve(pq);
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// Edge Case 1: no maze
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		// Edge Case 2: invalid coordinate
		validate(startRow, startCol, endRow, endCol);

		// Initialize visited
		this.visited = new boolean[this.maze.getRows()][this.maze.getColumns()];

		// set End
		this.endRow = endRow;
		this.endCol = endCol;

		// Initialze a min heap for Dijkstra
		PriorityQueue<Point> pq = new PriorityQueue<>(
			(a, b) -> Integer.compare(a.fear, b.fear)
		);
		pq.add(new Point(startRow, startCol));

		return solveBonus(pq);
	}

	private Integer bonusSpecial(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// Edge Case 1: no maze
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		// Edge Case 2: invalid coordinate
		validate(startRow, startCol, endRow, endCol);

		// Initialize visited
		this.visited = new boolean[this.maze.getRows()][this.maze.getColumns()];

		// set End
		this.endRow = endRow;
		this.endCol = endCol;

		// Initialze a min heap for Dijkstra
		PriorityQueue<Point> pq = new PriorityQueue<>(
			(a, b) -> Integer.compare(a.fear, b.fear)
		);
		Point specialPoint = new Point(startRow, startCol);
		specialPoint.fear = -1;
		pq.add(specialPoint);

		return solveBonus(pq);
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// Search path from Start to End
		Integer normalSearch = bonusSearch(startRow, startCol, endRow, endCol);

		// Search path from Special To End
		Integer specialSearch = bonusSpecial(sRow, sCol, endRow, endCol);

		// Edge Case 1: normal search dont exist
		if (normalSearch == null) {
			return null;
		}

		// Edge Case 2: special search dont exist
		if (specialSearch == null) {
			return normalSearch;
		}

		// Take the shorter path, normalSearch or specialSearch
		return Math.min(normalSearch, specialSearch);
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("/home/kopiosiewdai/NUS/CS2040S/ps/ps7/ps7_part3/haunted-maze-sample.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.bonusSearch(0, 1, 0, 5));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Integer solve(PriorityQueue<Point> pq) {
		Integer res = null;

		while (pq.peek() != null) {
			Point currPoint = pq.poll();

			int row = currPoint.row;
			int col = currPoint.col;
			int fear = currPoint.fear;
			
			// Skip if current room is visited
			if (visited[row][col]) {
				continue;
			}
			
			// Marked current room as visited
			visited[row][col] = true;

			// Found end
			if (row == endRow && col == endCol) {
				return fear;
			}

			// Check Potential Neighbours
			for (int direction = 0; direction < 4; direction++) {
				// Edge Case: cant go in that direction
				if (!canGo(row, col, direction)) {
					continue;
				}

				// Add in potential neighbour
				Point neighPoint = makePoint(row, col, direction);

				// update fear; fear will never be TRUE WALL since it wouldnt reach here
				neighPoint.fear = fear + this.fearCache;

				// add in min-heap based on new fear
				pq.add(neighPoint);
			}
		}

		// Edge Case: traverse finish and cant find end, res will be null
		return res;
	}

	private Integer solveBonus(PriorityQueue<Point> pq) {
		Integer res = null;

		while (pq.peek() != null) {
			Point currPoint = pq.poll();

			int row = currPoint.row;
			int col = currPoint.col;
			int fear = currPoint.fear;
			
			// Skip if current room is visited
			if (visited[row][col]) {
				continue;
			}
			
			// Marked current room as visited
			visited[row][col] = true;

			// Found end
			if (row == endRow && col == endCol) {
				return fear;
			}

			// Check Potential Neighbours
			for (int direction = 0; direction < 4; direction++) {
				// Edge Case: cant go in that direction
				if (!canGo(row, col, direction)) {
					continue;
				}

				// Add in potential neighbour
				Point neighPoint = makePoint(row, col, direction);

				// update fear; fear will never be TRUE WALL since it wouldnt reach here
				neighPoint.fear = (this.fearCache == EMPTY_SPACE) ? fear + 1: Math.max(fear,this.fearCache);

				// add in min-heap based on new fear
				pq.add(neighPoint);
			}
		}

		// Edge Case: traverse finish and cant find end, res will be null
		return res;
	}

	private boolean canGo(int row, int col, int dir) {
		// not needed since our maze has a surrounding block of wall
		// but Joe the Average Coder is a defensive coder!
		if (row + DELTAS[dir][0] < 0 || row + DELTAS[dir][0] >= maze.getRows()) return false;
		if (col + DELTAS[dir][1] < 0 || col + DELTAS[dir][1] >= maze.getColumns()) return false;
		
		Integer wall = WALL_FUNCTIONS.get(dir).apply(maze.getRoom(row, col));
		
		// update fearCache
		this.fearCache = wall;

		return wall != TRUE_WALL;
	}

	private Point makePoint(int row, int col, int dir) {
		return new Point(row + DELTAS[dir][0], col + DELTAS[dir][1]);
	}

	private void validate(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}
	}
}
