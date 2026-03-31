import java.util.*;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private Maze maze;
	private boolean[][][] visited;
	private int[][][][] parent;
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};
	private int endRow;
	private int endCol;
	private int startRow;
	private int startCol;
	private boolean solved;
	private int powers;
	private boolean[][] visitedRoom;
	HashMap<Integer, Integer> map = new HashMap<>();

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

	public MazeSolverWithPower() {
		maze = null;
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
		return map.get(k) == null ? 0 : map.get(k);
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow,
							  int endCol, int startingPower) throws Exception {
		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		// initialize the power
		this.powers = startingPower;

		// set all visited flag to false
		// before we begin our search
		this.visited = new boolean[maze.getRows()][maze.getColumns()][this.powers + 1];
		this.parent = new int[maze.getRows()][maze.getColumns()][this.powers + 1][3];
		for (int i = 0; i < maze.getRows(); ++i) {
			for (int j = 0; j < maze.getColumns(); ++j) {
				for (int k = 0; k <= this.powers; k++) {
					this.visited[i][j][k] = false;
				}
				maze.getRoom(i, j).onPath = false;
			}
		}
		this.visitedRoom = new boolean[maze.getRows()][maze.getColumns()];
		for (int i = 0; i < maze.getRows(); ++i) {
			for (int j = 0; j < maze.getColumns(); ++j) {
				this.visitedRoom[i][j] =false;
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
		int k = this.powers;
		for (boolean[][] surface : this.visited) {
			for (boolean[] x : surface) {
				Arrays.fill(x, false);
			}
		}

		int ans = Integer.MAX_VALUE; // to be returned after updating
		// store {row, col, depth} in the queue
		Queue<int[]> q = new ArrayDeque<>();
		q.add(new int[]{row, col, 0, k});
		visited[row][col][k] = true;
		parent[row][col][k] = new int[]{-1, -1, this.powers};

		while(!q.isEmpty()) {
			int[] curr = q.poll();
			int currRow = curr[0];
			int currCol = curr[1];
			int depth = curr[2];
			int power = curr[3];

			// maintaining the hashmap
			maintainMap(depth, currRow, currCol);

			// found the end
			ans = updatePath(currRow, currCol, depth, ans, power);

			// mark as visited
			visited[currRow][currCol][power] = true;
			// get neighbors from the current room
			for (int i = 0; i < 4; i++) {
				// if it is a neighbor
				if (canGo(currRow, currCol, i)) {
					// if already visited, skip this iteration
					int newRow = currRow + DELTAS[i][0];
					int newCol = currCol + DELTAS[i][1];
					if (visited[newRow][newCol][power]) {
						continue;
					}
					visited[newRow][newCol][power] = true;
					parent[newRow][newCol][power] = new int[]{currRow, currCol, power};
					q.add(new int[]{newRow, newCol, depth + 1, power});
				} else {
					int newRow = currRow + DELTAS[i][0];
					int newCol = currCol + DELTAS[i][1];
					// if neighbor cannot be accessed, check if power is available
					if (power > 0 && inRange(newRow, maze.getRows()) && inRange(newCol, maze.getColumns())) {
						if (visited[newRow][newCol][power - 1]) {
							continue;
						}
						visited[newRow][newCol][power - 1] = true;
						parent[newRow][newCol][power - 1] = new int[]{currRow, currCol, power};
						q.add(new int[]{newRow, newCol, depth + 1, power - 1});
					}
				}
			}
		}
		return ans != Integer.MAX_VALUE ? ans : null;
	}

	private boolean inRange(int x, int y) {
		return x < 0
				? false
				: x < y
				? true
				: false;
	}

	private void maintainMap(int key, int currRow, int currCol) {
		// if the room (without the state) is already visited, then the current depth (key in this case) cannot
		// be the shortest path to the room
		if (visitedRoom[currRow][currCol]) {
			return;
		} else {
			visitedRoom[currRow][currCol] = true;
		}
		// else, this room is first visited now. In BFS, the node being visited currently ensures that
		// the current depth is the shorted path's depth, thus we can update the map
		if (map.containsKey(key)) {
			map.put(key, map.get(key) + 1);
		} else {
			map.put(key, 1);
		}
	}

	private int updatePath(int currRow, int currCol, int depth, int ans, int power) {
		// only updatePath if this is the shortest path
		// the same node can be visited multiple times due to the inclusion of the states.
		if (currRow == endRow && currCol == endCol && depth < ans) {
			//backtrack through parent array to reconstruct path
			int[] node = {endRow, endCol, power};
			while (node[0] != -1) {
				maze.getRoom(node[0], node[1]).onPath = true;
				node = parent[node[0]][node[1]][node[2]];
			}
			// return the number of nodes visited so far
			return depth;
		}

		return ans;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 2, 3, 4));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
