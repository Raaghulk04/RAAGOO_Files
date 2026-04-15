import java.util.ArrayDeque;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	private Maze maze;
	private int rows;
	private int cols;
	private int[][][][] dist;
	private boolean[][][] visited;
	private ArrayDeque<int[]> queue;
	private int currDist;
	private int numOfcurrDist;
	private int numOfnextDist;
	private boolean[][] flatVisited;
	private int reachable[];

//	public MazeSolverWithPower() {
//		// TODO: Initialize variables.
//	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.maze = maze;
		this.rows = maze.getRows();
		this.cols = maze.getColumns();
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find shortest path.
		return pathSearch(startRow, startCol, endRow, endCol, 0);
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		// TODO: Find number of reachable rooms.
		if (k < 0) {
		    throw new Exception(String.format("Invalid numReachable: %d", k));
		}
		return k >= rows * cols ? 0 : reachable[k];
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow,
							  int endCol, int startingPower) throws Exception {
		// TODO: Find shortest path with powers allowed.
		for (int i = 0; i < maze.getRows(); i++) {
			for (int j = 0; j < maze.getColumns(); j++) {
				maze.getRoom(i, j).onPath = false;
			}
		}

		if (startRow < 0 || startRow >= rows || startCol < 0 || startCol >= cols) {
			throw new Exception(String.format("invalid start point: %d, %d", startRow, startCol));
		}

		this.dist = new int[rows][cols][startingPower + 1][4];
		this.visited = new boolean[rows][cols][startingPower + 1];
		this.reachable = new int[rows * cols];
		this.queue = new ArrayDeque<>();
		this.flatVisited = new boolean[rows][cols];
		this.queue.addLast(new int[] {startRow, startCol, startingPower});
		this.visited[startRow][startCol][startingPower] = true;
		this.flatVisited[startRow][startCol] = true;
		this.currDist = 0;
		this.dist[startRow][startCol][startingPower] = new int[] {startRow, startCol, startingPower, currDist};
		reachable[currDist]++;
		this.numOfcurrDist = 1;
		this.numOfnextDist = 0;
		currDist++;

		while (!queue.isEmpty()) {
			int[] curr = queue.removeFirst();
			numOfcurrDist--;

			for (int i = 0; i < DELTAS.length; i++) {
				int[] delta = DELTAS[i];
				int newRow = curr[0] + delta[0];
				int newCol = curr[1] + delta[1];
				if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols) {
					continue;
				}

				switch (i) {
					case NORTH:
						if (!maze.getRoom(curr[0], curr[1]).hasNorthWall()) {
							if (!visited[newRow][newCol][curr[2]]) {
								visited[newRow][newCol][curr[2]] = true;
								this.dist[newRow][newCol][curr[2]] = new int[] {curr[0], curr[1], curr[2], currDist};
								queue.addLast(new int[]{newRow, newCol, curr[2]});
								if (!flatVisited[newRow][newCol]) {
									flatVisited[newRow][newCol] = true;
									reachable[currDist]++;
								}
								numOfnextDist++;
							}
						} else {
							if (curr[2] != 0 && !visited[newRow][newCol][curr[2] - 1]) {
								visited[newRow][newCol][curr[2] - 1] = true;
								this.dist[newRow][newCol][curr[2] - 1] = new int[] {curr[0], curr[1], curr[2], currDist};
								queue.addLast(new int[]{newRow, newCol, curr[2] - 1});
								if (!flatVisited[newRow][newCol]) {
									flatVisited[newRow][newCol] = true;
									reachable[currDist]++;
								}
								numOfnextDist++;
							}
						}
						break;
					case SOUTH:
						if (!maze.getRoom(curr[0], curr[1]).hasSouthWall()) {
							if (!visited[newRow][newCol][curr[2]]) {
								visited[newRow][newCol][curr[2]] = true;
								this.dist[newRow][newCol][curr[2]] = new int[] {curr[0], curr[1], curr[2], currDist};
								queue.addLast(new int[]{newRow, newCol, curr[2]});
								if (!flatVisited[newRow][newCol]) {
									flatVisited[newRow][newCol] = true;
									reachable[currDist]++;
								}
								numOfnextDist++;
							}
						} else {
							if (curr[2] != 0 && !visited[newRow][newCol][curr[2] - 1]) {
								visited[newRow][newCol][curr[2] - 1] = true;
								this.dist[newRow][newCol][curr[2] - 1] = new int[] {curr[0], curr[1], curr[2], currDist};
								queue.addLast(new int[]{newRow, newCol, curr[2] - 1});
								if (!flatVisited[newRow][newCol]) {
									flatVisited[newRow][newCol] = true;
									reachable[currDist]++;
								}
								numOfnextDist++;
							}
						}
						break;
					case EAST:
						if (!maze.getRoom(curr[0], curr[1]).hasEastWall()) {
							if (!visited[newRow][newCol][curr[2]]) {
								visited[newRow][newCol][curr[2]] = true;
								this.dist[newRow][newCol][curr[2]] = new int[] {curr[0], curr[1], curr[2], currDist};
								queue.addLast(new int[]{newRow, newCol, curr[2]});
								if (!flatVisited[newRow][newCol]) {
									flatVisited[newRow][newCol] = true;
									reachable[currDist]++;
								}
								numOfnextDist++;
							}
						} else {
							if (curr[2] != 0 && !visited[newRow][newCol][curr[2] - 1]) {
								visited[newRow][newCol][curr[2] - 1] = true;
								this.dist[newRow][newCol][curr[2] - 1] = new int[] {curr[0], curr[1], curr[2], currDist};
								queue.addLast(new int[]{newRow, newCol, curr[2] - 1});
								if (!flatVisited[newRow][newCol]) {
									flatVisited[newRow][newCol] = true;
									reachable[currDist]++;
								}
								numOfnextDist++;
							}
						}
						break;
					case WEST:
						if (!maze.getRoom(curr[0], curr[1]).hasWestWall()) {
							if (!visited[newRow][newCol][curr[2]]) {
								visited[newRow][newCol][curr[2]] = true;
								this.dist[newRow][newCol][curr[2]] = new int[] {curr[0], curr[1], curr[2], currDist};
								queue.addLast(new int[]{newRow, newCol, curr[2]});
								if (!flatVisited[newRow][newCol]) {
									flatVisited[newRow][newCol] = true;
									reachable[currDist]++;
								}
								numOfnextDist++;
							}
						} else {
							if (curr[2] != 0 && !visited[newRow][newCol][curr[2] - 1]) {
								visited[newRow][newCol][curr[2] - 1] = true;
								this.dist[newRow][newCol][curr[2] - 1] = new int[] {curr[0], curr[1], curr[2], currDist};
								queue.addLast(new int[]{newRow, newCol, curr[2] - 1});
								if (!flatVisited[newRow][newCol]) {
									flatVisited[newRow][newCol] = true;
									reachable[currDist]++;
								}
								numOfnextDist++;
							}
						}
						break;
					default:
						throw new Exception(String.format("%d is not a valid direction!", i));
				}
			}

			if (numOfcurrDist == 0) {
				numOfcurrDist = numOfnextDist;
				numOfnextDist = 0;
				currDist++;
			}
		}

		maze.getRoom(startRow, startCol).onPath = true;
		if (startRow == endRow && startCol == endCol) {
			return 0;
		}

		int minDist = Integer.MAX_VALUE;
		int endLayer = -1;
		for (int i = 0; i <= startingPower; i++) {
			if (visited[endRow][endCol][i]) {
				if (minDist > dist[endRow][endCol][i][3]) {
					endLayer = i;
					minDist = dist[endRow][endCol][i][3];
				}
			}
		}

		if (endLayer == -1) {
			return null;
		}

		int currRow = endRow;
		int currCol = endCol;
		int currLayer = endLayer;
		while (dist[currRow][currCol][currLayer][3] != 0) {
			maze.getRoom(currRow, currCol).onPath = true;
			int temp1 = dist[currRow][currCol][currLayer][0];
			int temp2 = dist[currRow][currCol][currLayer][1];
			int temp3 = dist[currRow][currCol][currLayer][2];
			currRow = temp1;
			currCol = temp2;
			currLayer = temp3;
		}
		return minDist;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("C:/Users/start/OneDrive/Desktop/CS2040S/PS7_Part2/ps7_part2/maze-vertical.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 3, 0, 2));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 5; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
