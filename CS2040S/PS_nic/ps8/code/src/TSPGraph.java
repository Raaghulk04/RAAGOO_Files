import java.util.ArrayList;
import java.util.Stack;

public class TSPGraph implements IApproximateTSP {
    private ArrayList<ArrayList<Integer>> adjList;
    private Stack<Integer> leafList;
    private class Edge {
        int u, v;
        double weight;

        // Constructor
        public Edge(int u, int v, TSPMap map) {
            this.u = u;
            this.v = v;
            this.weight = map.pointDistance(u, v);
        }
    }

    @Override
    public void MST(TSPMap map) {
        int mapSize = map.getCount();

        // Edge Case: Empty map
        if (mapSize == 0) {
            return;
        }

        // Initialize Union find
        UnionFind uf = new UnionFind(mapSize);

        // Initialize Edge List
        ArrayList<Edge> edgeList = new ArrayList<>();

        // Initialize Adj List
        this.adjList = new ArrayList<>();

        // Initialize leaf List
        this.leafList = new Stack<>();

        // Create Edge and adj list
        for (int i = 0; i < mapSize; i++) {
            // For all neighbour of currPoint
            for (int j = i + 1; j < mapSize; j++) {
                // add Edge
                edgeList.add(new Edge(i, j, map));
            }

            // create adj list for each point
            this.adjList.add(new ArrayList<>());
        }

        // Sort Edge List
        edgeList.sort((a,b) -> Double.compare(a.weight, b.weight));

        // run Kruskal
        int edgeIndex = 0;
        int edgeCount = 0;
        while (edgeCount < mapSize - 1) {
            // Get Edge
            Edge currEdge = edgeList.get(edgeIndex++);
            int u = currEdge.u;
            int v = currEdge.v;

            // Case 1: Both point same set, dont connect edge
            if (uf.isSameSet(u, v)) {
                continue;
            }

            // Case 2: Both point different set, so union 
            uf.union(u, v);

            // update
            this.adjList.get(u).add(v);
            this.adjList.get(v).add(u);
            edgeCount++;
        }

        // Run DFS to set links
        linksDFS(map);

        return;
    }

    @Override
    public void TSP(TSPMap map) {
        MST(map);

        // Edge Case: empty map == empty adjacency list
        if (this.adjList.isEmpty()) {return;}

        // Initialize visited
        boolean[] visited = new boolean[adjList.size()];

        // Run One pass DFS from a leaf to source
        int leafPoint = leafList.pop();
        visited[leafPoint] = true;
        int sourcePoint = onePassDFS(leafPoint, visited, map);

        // Iterate through all leaf nodes
        while (!leafList.isEmpty()) {
            int nextLeaf = leafList.peek();
            map.setLink(leafToEnd(nextLeaf, visited, map), leafPoint); // set this leafToEnd point to the most recent leafPoint
            leafPoint = leafList.pop(); // update leafPoint
        }

        // Connect Source to most recent leafPoint
        map.setLink(sourcePoint, leafPoint);

        return;
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        int mapSize = map.getCount();

        // Edge Case: Empty map / 1 point
        if (mapSize <= 1) {
            return true; // vacuous truth
        }

        // select source point
        int sourcePoint = 0;
        int nextPoint = map.getLink(sourcePoint);

        // Initialize edge count to tackle potential loop inside TSP
        int edgeCount = 0;

        // traverse till it hit back the sourcePoint 
        while ((edgeCount < mapSize - 1) && nextPoint != sourcePoint) {
            edgeCount++;
            nextPoint = map.getLink(nextPoint);
        }

        return (edgeCount + 1 == mapSize) && (nextPoint == sourcePoint);
    }

    @Override
    public double tourDistance(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        int mapSize = map.getCount();

        // Edge Case: not a valid tour
        if (!isValidTour(map)) {
            return -1;
        }

        // Edge Case 2: empty map or one point
        if (mapSize <= 1) {
            return 0;
        }

        // traverse agn from source back to source and get distance
        int sourcePoint = 0;
        int currPoint = sourcePoint;
        int nextPoint = map.getLink(currPoint);
        double distance = 0;
        while (nextPoint != sourcePoint) {
            // add in edge from currPoint to nextPoint
            distance += map.pointDistance(currPoint, nextPoint);

            // update currPoint and nextPoint
            currPoint = nextPoint;
            nextPoint = map.getLink(nextPoint);
        }
        return distance + map.pointDistance(currPoint, nextPoint);
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "/home/kopiosiewdai/NUS/CS2040S/ps/ps8/code/fiftypoints.txt");
        TSPGraph graph = new TSPGraph();

        // graph.MST(map);
        graph.TSP(map);
        System.out.println(graph.isValidTour(map));
        System.out.println(graph.tourDistance(map));
    }

    private void linksDFS(TSPMap map) {
        // number of Points
        int n = this.adjList.size();
        
        // Initialize visited
        boolean[] visited = new boolean[n];
        
        // run DFS on first point
        visited[0] = true;
        DFS(0, visited, map);

        return ;
    }

    private int DFS (int currPoint, boolean[] visited, TSPMap map) {
        ArrayList<Integer> neighPoints = this.adjList.get(currPoint);
        int neighSize = neighPoints.size();
        boolean isLeaf = true;
        
        // Traverse down each possible neighbours
        for (int i = 0; i < neighSize; i++) {
            int neighPoint = neighPoints.get(i);

            // Edge Case: visited before
            if (visited[neighPoint]) {continue;}

            // update visited
            visited[neighPoint] = true;

            // bro is definitely not a leaf
            isLeaf = false;

            // traverse
            map.setLink(DFS(neighPoint, visited, map), currPoint);
        }

        if (isLeaf) {
            leafList.push(currPoint);
        }

        return currPoint;
    }

    private int onePassDFS(int currPoint, boolean[] visited, TSPMap map) {
        // Goal: traverse from currPoint all the way to source
        int nextPoint = map.getLink(currPoint);

        // Edge Case: sourcePoint reached
        if (nextPoint == -1) {
            return currPoint;
        }

        // update visited
        visited[nextPoint] = true;

        // go nextPoint
        return onePassDFS(nextPoint, visited, map);
    }

    private int leafToEnd(int currPoint, boolean[] visited, TSPMap map) {
        // Goal: traverse from currPoint all the way to node that will connect with the "main" onePassDFS. return this node
        int nextPoint = map.getLink(currPoint);

        // Hit something that was already visited before
        if (visited[nextPoint]) {
            return currPoint;
        }

        // update visited
        visited[nextPoint] = true;

        // go nextPoint
        return leafToEnd(nextPoint, visited, map);
    }

    class UnionFind {
        private int[] parent;
        private int[] rank;

        // Constructor
        public UnionFind(int n) {
            this.parent = new int[n];
            this.rank = new int[n];

            for (int i = 0; i < n; i++) {
                this.parent[i] = i; // set everyone to be own parent
                this.rank[i] = 0;
            }
        }

        public boolean isSameSet(int a, int b) {
            return find(a) == find(b);
        }

        public void union(int a, int b) {
            // Get root + path compression
            int rootA = find(a);
            int rootB = find(b);

            // is Same set?
            if (rootA == rootB) {
                return ;
            }

            // Union by rank
            if (rank[rootA] < rank[rootB]) {
                parent[rootA] = rootB;
            } else if (rank[rootB] < rank[rootA]) {
                parent[rootB] = rootA;
            } else {
                parent[rootA] = rootB;
                rank[rootB]++;
            }
        }

        private int find(int x) {
            int root = parent[x];

            // Edge Case: I am my own parent
            if (root == x) {
                return x;
            }

            // lost child, need to find parent
            parent[x] = find(root);

            return parent[x];
        }
    }
}
