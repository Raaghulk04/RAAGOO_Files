import java.util.*;

public class MazeSolver implements IMazeSolver {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	private Maze maze;
	private boolean solved = false;
	private boolean[][] visited;
	private int endRow, endCol;
	private int startRow, startCol;
	HashMap<Integer, Integer> map = new HashMap<>();
	private int[][][] parent;

	public MazeSolver() {
		solved = false;
		maze = null;
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		visited = new boolean[maze.getRows()][maze.getColumns()];
		solved = false;
		parent = new int[maze.getRows()][maze.getColumns()][3];
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


	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		// set all visited flag to false
		// before we begin our search
		for (int i = 0; i < maze.getRows(); ++i) {
			for (int j = 0; j < maze.getColumns(); ++j) {
				this.visited[i][j] = false;
				maze.getRoom(i, j).onPath = false;
			}
		}

		this.endRow = endRow;
		this.endCol = endCol;

		// most recent pathsearch coord for numReachable
		this.startRow = startRow;
		this.startCol = startCol;

		solved = true;

		return solve(startRow, startCol, 0);
	}

	private Integer solve(int row, int col, int rooms){
		// - will be doing BFS to get the shortest path
		// - Initialize the queue containing all the coordinates of the rooms yet to be visited
		// - coordinates are represented in the form an array size 2

		// - changes for numReachable, now traverse every possible room, don't return after finding row
		//   maintain a hashmap where depth is key and no.of rooms is the value

		// reset everything
		map.clear();
		for (boolean [] r : visited) Arrays.fill(r, false);

		int ans = Integer.MAX_VALUE; // to be returned after updating
		// store {row, col, depth} in the queue
		Queue<int[]> q = new ArrayDeque<>();
        q.add(new int[]{row, col, 0});
		visited[row][col] = true;
		parent[row][col] = new int[]{-1, -1};

		while(!q.isEmpty()) {
			int[] curr = q.poll();
			int currRow = curr[0];
			int currCol = curr[1];
			int depth = curr[2];

			// maintaining the hashmap
			int key = curr[2];
			if (map.containsKey(key)) {
				// increment the value if it is already in the map
				map.put(key, map.get(key) + 1);
			} else {
				// insert the key and value into the map
				map.put(key, 1);
			}

			// found the end
			if (currRow == endRow && currCol == endCol) {
				//backtrack through parent array to reconstruct path
				int[] node = {endRow, endCol};
				while (node[0] != -1) {
					maze.getRoom(node[0], node[1]).onPath = true;
					node = parent[node[0]][node[1]];
				}
				// return the number of nodes visited so far
				ans = depth < ans ? depth: ans;
			}

			//mark as visited
			visited[currRow][currCol] = true;

			// get neighbors from the current room
			for (int i = 0; i < 4; i++) {
				// if it is a neighbor
				if (canGo(currRow, currCol, i)) {
					// if already visited, skip this iteration
					int newRow = currRow + DELTAS[i][0];
					int newCol = currCol + DELTAS[i][1];
					if (visited[newRow][newCol]) {
						continue;
					}
					visited[newRow][newCol] = true;
					parent[newRow][newCol] = new int[]{currRow, currCol};
					q.add(new int[]{newRow, newCol, depth + 1});
				}
			}
		}
		return ans != Integer.MAX_VALUE ? ans : null;
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		return map.get(k) == null ? 0 : map.get(k);
	}

	public static void main(String[] args) {
		// Do remember to remove any references to ImprovedMazePrinter before submitting
		// your code!
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
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
}
