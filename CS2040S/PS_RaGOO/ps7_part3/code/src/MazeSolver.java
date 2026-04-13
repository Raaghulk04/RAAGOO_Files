import java.util.*;
import java.util.function.BiFunction;
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
	private int[][] distance;
	private int rows;
	private int columns;
	private PriorityQueue<Node> q = new PriorityQueue<>();

	private Node endNode;

	private class Node implements Comparable<Node> {
		public int row;
		public int col;
		public int dist;

		public Node(int row, int col, int dist) {
			this.row = row;
			this.col = col;
			this.dist = dist;
		}

		public Node(int row, int col) {
			this.row = row;
			this.col = col;
			this.dist = 0;
		}

		@Override
		public int compareTo(Node o) {
			if (this.dist != o.dist) return Integer.compare(this.dist, o.dist);
			if (this.row != o.row) return Integer.compare(this.row, o.row);
			return Integer.compare(this.col, o.col);
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof Node && ((Node) o).row == this.row && ((Node) o).col == this.col;
		}
	}

	private Maze maze;

	public MazeSolver() {
		// TODO: Initialize variables.
	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.maze = maze;
		this.rows = maze.getRows();
		this.columns = maze.getColumns();


		// setting initial distance for each node to be infinity
		this.distance = new int[this.rows][this.columns];
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				this.distance[i][j] = Integer.MAX_VALUE;
			}
		}
	}

	private int go(Node n, int dir) {
		return WALL_FUNCTIONS.get(dir).apply(maze.getRoom(n.row, n.col));
	}

	private boolean validate(int srow, int scol) {
		return srow >= 0 && srow < this.rows &&
				scol >= 0 && scol < this.columns;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level.
		this.endNode = new Node(endRow, endCol);
		return validate(startRow, startCol) && validate(endRow, endCol)
				? solve(startRow, startCol, Integer::sum, 0)
				: null;
	}

	private Integer solve(int startRow, int startCol, BiFunction<Integer, Integer, Integer> func, int init) {
		// Wanted to try treeSet + HashMap method, but gave up and just did lazy dijkstra :)
		// which is why im still using a treeSet (kinda lazy to change it back to PriorityQueue)
		// never mind, ill just stick with the priority queue :)

		// clear all data structures
		q.clear();
		for (int j = 0; j < this.rows; j++) {
			for (int k = 0; k < this.columns; k++) {
				distance[j][k] = Integer.MAX_VALUE;
			}
		}
		// set the distance to the source node to be 0
		distance[startRow][startCol] = init;
		// push the source node into the tree set
		Node start = new Node(startRow, startCol, init);
		this.q.offer(start);

		while (!this.q.isEmpty()) {
			// take the node with the lowest distance (priority) first
			Node curr = this.q.poll();
			// this node will have the correct distance. this is the invariant of dijkstra
            if (curr.equals(endNode)) {
				return curr.dist;
			}

			// the current distance is larger than already computed shortest, we don't need to care about this
			if (distance[curr.row][curr.col] < curr.dist) continue;

			for (int i = 0; i < 4; i++) {
				int cost = go(curr, i);
				int newRow = curr.row + DELTAS[i][0];
				int newCol = curr.col + DELTAS[i][1];
				// if empty space, the edge weight will be 1
				if (cost == EMPTY_SPACE) {
					if (validate(newRow, newCol)) {
						Node newNode = new Node(newRow, newCol, curr.dist + 1);
						if (curr.dist + 1 < distance[newRow][newCol]) {
							maintainQueue(newNode);
						}
					}
				}
				// else if not wall, edge weight will be whatever the cost is.
				if (cost != TRUE_WALL && cost != EMPTY_SPACE) {
					if (validate(newRow, newCol)) {
						Node newNode = new Node(newRow, newCol, func.apply(curr.dist, cost));
						if (func.apply(curr.dist, cost) < distance[newRow][newCol]) {
							maintainQueue(newNode);
						}
					}
				}
			}
		}
		return null;

	}

	private void maintainQueue(Node curr) {
		q.add(curr);
		distance[curr.row][curr.col] = curr.dist;
	}


	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level given new rules.
		// similar to normal dijkstra, except we take max instead of sum
		this.endNode = new Node(endRow, endCol);
		return validate(startRow, startCol) && validate(endRow, endCol)
				? solve(startRow, startCol, Math::max, 0)
				: null;
	}

	private Integer helper(int startRow, int startCol, int endRow, int endCol, int init) {
		this.endNode = new Node(endRow, endCol);
		return validate(startRow, startCol) && validate(endRow, endCol)
				? solve(startRow, startCol, Math::max, init)
				: null;
	}


	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// run a dijkstra from start point to end point, let the result be u
		// run another dijkstra from special to end, let the result be v2
		// if u < v2, then just take u otherwise v2. this is the case which we traverse start to end to special to end.

		Integer u = helper(startRow, startCol, endRow, endCol, 0);
		Integer v2 = helper(sRow, sCol, endRow, endCol, -1);
		if (u == null) return null;
		return (v2 == null) || u < v2
				? u : v2;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("haunted-maze-sample.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.bonusSearch(0, 1, 0, 5));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
