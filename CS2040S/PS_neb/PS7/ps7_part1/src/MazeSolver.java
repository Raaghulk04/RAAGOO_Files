import java.util.ArrayList;

public class MazeSolver implements IMazeSolver {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	private Maze maze;
	private boolean[][] visited;
	private int endRow, endCol, startCol, startRow;

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.maze = maze;
		this.visited = new boolean[maze.getRows()][maze.getColumns()];
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find shortest path.
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		this.startCol = startCol;
		this.startRow = startRow;

		this.endRow = endRow;
		this.endCol = endCol;
		return solve(startRow, startCol, 0);
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

	private Integer solve(int row, int col, int rooms) {
		for (int i = 0; i < maze.getRows(); i++) {
			for (int j = 0; j < maze.getColumns(); j++) {
				maze.getRoom(i, j).onPath = false;
			}
		}

		// edge case: curr node is endpoint
		maze.getRoom(row, col).onPath = true;
		if (row == endRow && col == endCol) {
			return 0;
		}

		// AUTHOR'S NOTE: REALISED TOO LATE THIS IS BFS
		// AUTHOR'S NOTE 2: YES, AFTER RECI I REALISE I SHLD USE QUEUE

		// invariant: always visit the least dist nodes first
		// idea: instead of a heap, use 2 stacks;
		// when curr stack is not empty, pop "curr node" off stack
		// update NON-VISITED neighbours, since visited neighbours will
		// by invariant, be already of shortest dist
		// and push neighbours onto next stack
		// when curr stack is empty, swap the two stacks

		// K: [r, c], V: [prev_r, prev_c, d] => int[r][c][V]
		this.visited = new boolean[maze.getRows()][maze.getColumns()];
		int[][][] hash = new int[maze.getRows()][maze.getColumns()][3];
		ArrayList<int[]> curr = new ArrayList<>();
		int currIdx = 0;
		ArrayList<int[]> next = new ArrayList<>();
		int nextIdx = -1;
		ArrayList<int[]> tmp = null; // for swap *claps hands*

		hash[row][col] = new int[] {row, col, 0};
		int currDist = 0;
		int[] currNode = new int[] {row, col};
		curr.add(currNode);
		visited[currNode[0]][currNode[1]] = true;

		while (true) {
			row = currNode[0];
			col = currNode[1];
			if (canGo(row, col, NORTH) && !visited[row - 1][col]) {
				hash[row - 1][col] = new int[] {currNode[0], currNode[1], currDist + 1};
				visited[row - 1][col] = true;
				if (row - 1 == endRow && col == endCol) {
					currDist++;
					row = row - 1;
					while (hash[row][col][2] != 0) {
						maze.getRoom(row, col).onPath = true;
						int[] prev = hash[row][col];
						row = prev[0];
						col = prev[1];
					}
					return currDist;
				}
				next.add(new int[] {row - 1, col});
				nextIdx++;
			}
			if (canGo(row, col, EAST) && !visited[row][col + 1]) {
				hash[row][col + 1] = new int[] {currNode[0], currNode[1], currDist + 1};
				visited[row][col + 1] = true;
				if (row == endRow && col + 1 == endCol) {
					currDist++;
					col = col + 1;
					while (hash[row][col][2] != 0) {
						maze.getRoom(row, col).onPath = true;
						int[] prev = hash[row][col];
						row = prev[0];
						col = prev[1];
					}
					return currDist;
				}
				next.add(new int[] {row, col + 1});
				nextIdx++;
			}
			if (canGo(row, col, SOUTH) && !visited[row + 1][col]) {
				hash[row + 1][col] = new int[] {currNode[0], currNode[1], currDist + 1};
				visited[row + 1][col] = true;
				if (row + 1 == endRow && col == endCol) {
					currDist++;
					row = row + 1;
					while (hash[row][col][2] != 0) {
						maze.getRoom(row, col).onPath = true;
						int[] prev = hash[row][col];
						row = prev[0];
						col = prev[1];
					}
					return currDist;
				}
				next.add(new int[] {row + 1, col});
				nextIdx++;
			}
			if (canGo(row, col, WEST) && !visited[row][col - 1]) {
				hash[row][col - 1] = new int[] {currNode[0], currNode[1], currDist + 1};
				visited[row][col - 1] = true;
				if (row == endRow && col - 1 == endCol) {
					currDist++;
					col = col - 1;
					while (hash[row][col][2] != 0) {
						maze.getRoom(row, col).onPath = true;
						int[] prev = hash[row][col];
						row = prev[0];
						col = prev[1];
					}
					return currDist;
				}
				next.add(new int[] {row, col - 1});
				nextIdx++;
			}
			curr.remove(currIdx);
			currIdx--;

			if (curr.isEmpty()) {
				if (next.isEmpty()) {
					return null;
				} else {
					tmp = curr;
					curr = next;
					next = tmp;
					currIdx = nextIdx;
					nextIdx = -1;
					currDist++;
				}
			}
			currNode = curr.get(currIdx);
		}
	}

	@Override
	public Integer numReachable(int k) throws Exception {

		this.visited = new boolean[maze.getRows()][maze.getColumns()];
		ArrayList<int[]> curr = new ArrayList<>();
		int currIdx = 0;
		ArrayList<int[]> next = new ArrayList<>();
		int nextIdx = -1;
		ArrayList<int[]> tmp = null; // for swap *claps hands*

		int row = startRow;
		int col = startCol;
		int currDist = 0;
		int[] currNode = new int[] {row, col};
		curr.add(currNode);
		visited[currNode[0]][currNode[1]] = true;

		while (currDist < k) {
			row = currNode[0];
			col = currNode[1];
			if (canGo(row, col, NORTH) && !visited[row - 1][col]) {
				visited[row - 1][col] = true;
				next.add(new int[]{row - 1, col});
				nextIdx++;
			}
			if (canGo(row, col, EAST) && !visited[row][col + 1]) {
				visited[row][col + 1] = true;
				next.add(new int[]{row, col + 1});
				nextIdx++;
			}
			if (canGo(row, col, SOUTH) && !visited[row + 1][col]) {
				visited[row + 1][col] = true;
				next.add(new int[]{row + 1, col});
				nextIdx++;
			}
			if (canGo(row, col, WEST) && !visited[row][col - 1]) {
				visited[row][col - 1] = true;
				next.add(new int[]{row, col - 1});
				nextIdx++;
			}
			curr.remove(currIdx);
			currIdx--;

			if (curr.isEmpty()) {
				if (next.isEmpty()) {
					return 0;
				} else {
					tmp = curr;
					curr = next;
					next = tmp;
					currIdx = nextIdx;
					nextIdx = -1;
					currDist++;
				}
			}
			currNode = curr.get(currIdx);
		}
		return currIdx + 1;
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
