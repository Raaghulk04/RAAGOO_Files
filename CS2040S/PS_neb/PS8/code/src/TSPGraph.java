import java.util.ArrayList;
import java.util.ArrayDeque;

public class TSPGraph implements IApproximateTSP {
    public static class UFDS {
        private int[][] parent;
        // num of pts
        public UFDS(int n, TSPMap map) {
            parent = new int[n][2]; // idx, rank
            for (int i = 0; i < n; i++) {
                parent[i] = new int[] {i, 0};
            }
        }
        private int[] findRoot(int idx) {
            if (parent[idx][0] == idx) {
                return parent[idx];
            }
            // NEEDS TO PROPAGATE PARENT RANK ALSO
            parent[idx] = findRoot(parent[idx][0]);
            return parent[idx];
        }
        public boolean isSameSet(int idx1, int idx2) {
            return findRoot(idx1)[0] == findRoot(idx2)[0];
        }
        public void union(int idx1, int idx2) {
            if (isSameSet(idx1, idx2)) {
                return;
            }
            int[] root1 = findRoot(idx1);
            int[] root2 = findRoot(idx2);
            if (root1[1] < root2[1]) {
                root1[0] = root2[0];
            } else if (root1[1] > root2[1]) {
                root2[0] = root1[0];
            } else {
                root1[0] = root2[0];
                root2[1]++;
            }
        }
    }

    public static class Edge {
        public int from;
        public int to;
        public double weight;
        public boolean inMST = false;

        public Edge(int from, int to, TSPMap map) {
            this.from = from;
            this.to = to;
            this.weight = map.pointDistance(from, to);
        }
    }

    private TSPMap.Point[] points;
    private ArrayList<Edge> edges;
    // MST is rooted at Point corresponding to 0
    private ArrayList<ArrayList<Edge>> MST;
    // visited for TSP
    boolean[] visited;

    @Override
    public void MST(TSPMap map) {
        if (map.getCount() <= 1) {
            return;
        }

        points = new TSPMap.Point[map.getCount()];
        for (int i = 0; i < map.getCount(); i++) {
            points[i] = map.getPoint(i);
        }

        edges = new ArrayList<>();
        for (int i = 0; i < map.getCount(); i++) {
            for (int j = i + 1; j < map.getCount(); j++) {
                edges.add(new Edge(i, j, map));
            }
        }
        edges.sort((a, b) -> Double.compare(a.weight, b.weight));

        ArrayList<ArrayList<Edge>> adjListUndir = new ArrayList<>();
        MST = new ArrayList<>();
        for (int i = 0; i < map.getCount(); i++) {
            adjListUndir.add(new ArrayList<>());
            MST.add(new ArrayList<>());
        }

        UFDS ufds = new UFDS(map.getCount(), map);
        for (Edge e : edges) {
            if (ufds.isSameSet(e.from, e.to)) {
                continue;
            }

            ufds.union(e.from, e.to);
            adjListUndir.get(e.from).add(e);
            adjListUndir.get(e.to).add(e);
        }

        ArrayDeque<Integer> queue = new ArrayDeque<>();

        queue.add(0);
        while (!queue.isEmpty()) {
            int curr = queue.pop();
            for (Edge e : adjListUndir.get(curr)) {
                if (e.inMST) {
                    continue;
                }
                if (e.from != curr) {
                    int tmp = e.from;
                    e.from = e.to;
                    e.to = tmp;
                }

                queue.add(e.to);
                e.inMST = true;
                map.setLink(e.to, e.from);
                MST.get(e.from).add(e);
            }
        }
    }

    @Override
    public void TSP(TSPMap map) {
        if (map.getCount() <= 1) {
            return;
        }

        MST(map);
        visited = new boolean[map.getCount()];
        // Return of the King: single element arrays
        // Let's finish this how we started ahh
        DFS(0, new int[1], map);
    }

    public void DFS(int idx, int[] prev, TSPMap map) {
        map.setLink(idx, prev[0]);

        prev[0] = idx;
        for (Edge e : MST.get(idx)) {
            if (visited[e.to]) {
                continue;
            }
            DFS(e.to, prev, map);
        }
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        // Note: this function should with *any* map, and not just results from TSP().
        boolean[] visitFrom = new boolean[map.getCount()];
        boolean[] visitTo = new boolean[map.getCount()];

        for (int i = 0; i < map.getCount(); i++) {
            // |E| = |V|
            if (map.getLink(i) == -1) {
                return false;
            } else {
                // check that every node has at most one ingoing/outgoing edge
                if (visitFrom[i] || visitTo[map.getLink(i)]) {
                    return false;
                } else {
                    visitFrom[i] = true;
                    visitTo[map.getLink(i)] = true;
                }
            }
        }

        int curr = 0;
        for (int i = 0; i < map.getCount(); i++) {
            if (map.getLink(curr) == 0 && i != map.getCount() - 1) {
                return false;
            }
            curr = map.getLink(i);
        }

        return true;
    }

    @Override
    public double tourDistance(TSPMap map) {
        // Note: this function should with *any* map, and not just results from TSP().
        if (!isValidTour(map)) {
            return -1;
        }

        double dist = 0.0;
        for (int i = 0; i < map.getCount(); i++) {
            int next = map.getLink(i);
            if (next == -1) {
                continue;
            }
            dist += map.pointDistance(i, next);
        }

        return dist;

        /*  Alternatively, *CS2030S jumpscare*
         *  return isValidTour(map)
         *      ? Stream.generate(0, x -> x + 1)
         *               .limit(map.getCount())
         *               .filter(i -> map.getLink(i) == -1)
         *               .map(i -> map.pointDistance(i, map.getLink(i)))
         *               .reduce(0.0, (x, y) -> x + y)
         *      : -1;
         */
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "C:/Users/start/OneDrive/Desktop/CS2040S/PS8/code/twentypoints.txt");
        TSPGraph graph = new TSPGraph();

        // graph.MST(map);
        // graph.TSP(map);
        System.out.println(graph.isValidTour(map));
        System.out.println(graph.tourDistance(map));
    }
}
