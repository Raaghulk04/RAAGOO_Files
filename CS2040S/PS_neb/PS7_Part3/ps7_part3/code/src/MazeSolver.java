import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.function.Function;

public class MazeSolver implements IMazeSolver {
	static class Pair<T extends Comparable<T>, S> implements Comparable<Pair<T, S>> {
		public T key;
		public S value;

		public Pair(T t, S s) {
			key = t;
			value = s;
		}

		@Override
		public int compareTo(Pair<T, S> p) {
			return key.compareTo(p.key);
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Pair<?, ?>) {
				Pair<?, ?> p = (Pair<?, ?>) o;
				return this.key.equals(p.key) && this.value.equals(p.value);
			}
			return false;
		}
	}

	static class Heap<E extends Comparable<E>> {
		int lsIdx = 0;
		Comparator<E> comparator = E::compareTo;

		ArrayList<Pair<E, int[]>> pq = new ArrayList<>();
		Integer[][] StoIndex;
		ArrayList<int[]> indexToS = new ArrayList<>();

		Heap(int rows, int cols) {
			pq.add(null);
			indexToS.add(null);
			StoIndex = new Integer[rows][cols];
		}
		Heap(int rows, int cols, Comparator<E> c) {
			this(rows, cols);
			this.comparator = c;
		}

		void bubbleUp(int idx) {
			while (idx / 2 >= 1 && comparator.compare(pq.get(idx).key, (pq.get(idx / 2)).key) < 0) {
				Pair<E, int[]> temp = pq.get(idx);
				pq.set(idx, pq.get(idx / 2));
				pq.set(idx / 2, temp);

				indexToS.set(idx, pq.get(idx).value);
				indexToS.set(idx / 2, temp.value);
				StoIndex[pq.get(idx).value[0]][pq.get(idx).value[1]] = idx;
				StoIndex[temp.value[0]][temp.value[1]] = idx / 2;

				idx /= 2;
			}
		}

		void bubbleDown(int idx) {
			while (true) {
				// today on top 10 unreadable code
				int swapIdx = idx * 2 > lsIdx
						? -1
						: idx * 2 + 1 > lsIdx
						? comparator.compare(pq.get(idx).key, pq.get(idx * 2).key) < 0
							? -1 : idx * 2
						: comparator.compare(pq.get(idx).key, pq.get(idx * 2).key) < 0
						? comparator.compare(pq.get(idx).key, pq.get(idx * 2 + 1).key) < 0
							? -1 : idx * 2 + 1
						: comparator.compare(pq.get(idx).key, pq.get(idx * 2 + 1).key) < 0
							? idx * 2
							: comparator.compare(pq.get(idx * 2).key, pq.get(idx * 2 + 1).key) < 0
							? idx * 2 : idx * 2 + 1;

				if (swapIdx == -1) {
					break;
				}

				Pair<E, int[]> temp = pq.get(idx);
				pq.set(idx, pq.get(swapIdx));
				pq.set(swapIdx, temp);

				indexToS.set(idx, pq.get(idx).value);
				indexToS.set(swapIdx, temp.value);
				StoIndex[pq.get(idx).value[0]][pq.get(idx).value[1]] = idx;
				StoIndex[temp.value[0]][temp.value[1]] = swapIdx;

				idx = swapIdx;
			}
		}

		void insert(Pair<E, int[]> e) {
			lsIdx++;
			StoIndex[e.value[0]][e.value[1]] = lsIdx;
			indexToS.add(e.value);
			pq.add(e);
			bubbleUp(lsIdx);
		}

		Pair<E, int[]> pop() {
			Pair<E, int[]> top = pq.get(1);

			StoIndex[top.value[0]][top.value[1]] = null;
			indexToS.set(1, pq.get(lsIdx).value);
			StoIndex[pq.get(lsIdx).value[0]][pq.get(lsIdx).value[1]] = 1;
			indexToS.remove(lsIdx);
			pq.set(1, pq.get(lsIdx));
			pq.remove(lsIdx);
			lsIdx--;

			bubbleDown(1);

			return top;
		}

		void decrementKey(int[] val, E newE) {
			Pair<E, int[]> curr = pq.get(StoIndex[val[0]][val[1]]);
			E oldE = curr.key;

			if (comparator.compare(oldE, newE) > 0) {
				curr.key = newE;
				int idx = StoIndex[curr.value[0]][curr.value[1]];
				bubbleUp(idx);
			}
		}

		boolean contains(int[] val) {
			return StoIndex[val[0]][val[1]] != null;
		}

		boolean isEmpty() {
			return lsIdx == 0;
		}

		Pair<E, int[]> peek() {
			return isEmpty() ? null : pq.get(1);
		}
	}

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

	private Maze maze;
	private int rows;
	private int cols;

//	public MazeSolver() {
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
		// TODO: Find minimum fear level.
		// I am not in danger Skyler, I am the danger.
		if (startRow < 0 || startRow >= rows || startCol < 0 || startCol >= cols) {
			throw new IllegalArgumentException(String.format("Illegal pathSearch: %d %d %d %d", startRow, startCol, endRow, endCol));
		}

		int currDist = 0;
		Heap<Integer> heap = new Heap<>(rows, cols);
		boolean[][] visited = new boolean[rows][cols];

		int[] curr = new int[] {startRow, startCol};
		heap.insert(new Pair<>(0, curr));
		visited[startRow][startCol] = true;

		while (!heap.isEmpty()) {
			curr = heap.peek().value;
			currDist = heap.pop().key;

			if (curr[0] == endRow && curr[1] == endCol) {
				return currDist;
			}

			visited[curr[0]][curr[1]] = true;

			for (int i = 0; i < 4; i++) {
				// quit Arknights Endfield last week #feelsbad
				int[] next = new int[] {curr[0] + DELTAS[i][0], curr[1] + DELTAS[i][1]};
				if (next[0] < 0 || next[0] >= rows || next[1] < 0 || next[1] >= cols
					|| visited[next[0]][next[1]]) {
					continue;
				}
				Room r = maze.getRoom(curr[0], curr[1]);
				if (r == null) { continue; }
				int sanityCost = WALL_FUNCTIONS.get(i).apply(r);
				if (sanityCost != TRUE_WALL) {
					if (sanityCost == EMPTY_SPACE) {
						sanityCost = 1;
					}
					int nextDist = currDist + sanityCost;
					if (!heap.contains(next)) {
						heap.insert(new Pair<>(nextDist, next));
					} else {
						heap.decrementKey(next, nextDist);
					}
				}
			}
		}
		return null;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level given new rules.
        if (startRow < 0 || startRow >= rows || startCol < 0 || startCol >= cols) {
            throw new IllegalArgumentException(String.format("Illegal pathSearch: %d %d %d %d, Maze: %d x %d", startRow, startCol, endRow, endCol, rows, cols));
        }

        int currDist = 0;
        Heap<Integer> heap = new Heap<>(rows, cols);
        boolean[][] visited = new boolean[rows][cols];

        int[] curr = new int[] {startRow, startCol};
        heap.insert(new Pair<>(0, curr));
        visited[startRow][startCol] = true;

        while (!heap.isEmpty()) {
            curr = heap.peek().value;
            currDist = heap.pop().key;

            if (curr[0] == endRow && curr[1] == endCol) {
                return currDist;
            }

            visited[curr[0]][curr[1]] = true;

            for (int i = 0; i < 4; i++) {
                int[] next = new int[] {curr[0] + DELTAS[i][0], curr[1] + DELTAS[i][1]};
                if (next[0] < 0 || next[0] >= rows || next[1] < 0 || next[1] >= cols
                        || visited[next[0]][next[1]]) {
                    continue;
                }
                Room r = maze.getRoom(curr[0], curr[1]);
                if (r == null) { continue; }
                int sanityCost = WALL_FUNCTIONS.get(i).apply(r);
                if (sanityCost != TRUE_WALL) {
                    int nextDist = currDist;
                    if (sanityCost == EMPTY_SPACE) {
                        nextDist++;
                    } else if (sanityCost > currDist) {
                        nextDist = sanityCost;
                    }
                    if (!heap.contains(next)) {
                        heap.insert(new Pair<>(nextDist, next));
                    } else {
                        heap.decrementKey(next, nextDist);
                    }
                }
            }
        }
        return null;
	}

	public Integer pathFromS(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (startRow < 0 || startRow >= rows || startCol < 0 || startCol >= cols) {
			throw new IllegalArgumentException(String.format("Illegal pathSearch: %d %d %d %d, Maze: %d x %d", startRow, startCol, endRow, endCol, rows, cols));
		}

		int currDist = -1;
		Heap<Integer> heap = new Heap<>(rows, cols);
		boolean[][] visited = new boolean[rows][cols];

		int[] curr = new int[] {startRow, startCol};
		heap.insert(new Pair<>(-1, curr));
		visited[startRow][startCol] = true;

		while (!heap.isEmpty()) {
			curr = heap.peek().value;
			currDist = heap.pop().key;

			if (curr[0] == endRow && curr[1] == endCol) {
				return currDist;
			}

			visited[curr[0]][curr[1]] = true;

			for (int i = 0; i < 4; i++) {
				int[] next = new int[] {curr[0] + DELTAS[i][0], curr[1] + DELTAS[i][1]};
				if (next[0] < 0 || next[0] >= rows || next[1] < 0 || next[1] >= cols
						|| visited[next[0]][next[1]]) {
					continue;
				}
				Room r = maze.getRoom(curr[0], curr[1]);
				if (r == null) { continue; }
				int sanityCost = WALL_FUNCTIONS.get(i).apply(r);
				if (sanityCost != TRUE_WALL) {
					int nextDist = currDist;
					if (sanityCost == EMPTY_SPACE) {
						nextDist++;
					} else if (sanityCost > currDist) {
						nextDist = sanityCost;
					}
					if (!heap.contains(next)) {
						heap.insert(new Pair<>(nextDist, next));
					} else {
						heap.decrementKey(next, nextDist);
					}
				}
			}
		}
		return null;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// TODO: Find minimum fear level given new rules and special room.
		Integer startToEnd = bonusSearch(startRow, startCol, endRow, endCol);
		Integer SToEnd = pathFromS(sRow, sCol, endRow, endCol);
		if (startToEnd == null) {
			return null;
		}
		System.out.println(SToEnd + " " + startToEnd);
		return SToEnd == null || startToEnd < SToEnd
				? startToEnd : SToEnd;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("C:/Users/start/OneDrive/Desktop/CS2040S/PS7_Part3/ps7_part3/code/haunted-maze-simple.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.bonusSearch(0, 5, 0, 4, 0, 1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
