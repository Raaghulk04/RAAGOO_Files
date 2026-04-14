import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class TSPGraph implements IApproximateTSP {

    @Override
    public void MST(TSPMap map) {
        // TODO: implement this method
        int numPoints = map.getCount();
        if (numPoints == 0) return;

        // TreeMapPQ stores (Key: PointIndex, Priority: Distance)
        TreeMapPriorityQueue<Double, Integer> pq = new TreeMapPriorityQueue<>();

        // initialise all points
        // distance is infinity
        // parent is -1
        for (int i = 0; i < numPoints; i++) {
            map.eraseLink(i, false); // clear existing links
            if (i == 0) {
                pq.add(i, 0.0);
            } else {
                pq.add(i, Double.POSITIVE_INFINITY);
            }
        }

        while (!pq.isEmpty()) {
            int u = pq.extractMin();

            // for every other node v, check if we can decrease priority
            for (int v = 0; v < numPoints; v++) {
                if (u == v) continue;

                Double currentDist = pq.lookup(v);
                double weight = map.pointDistance(u, v);

                // if v is still in PQ and weight (u, v) is smaller than current weight
                if (currentDist != null && weight < currentDist) {
                    pq.decreasePriority(v, weight);
                    map.setLink(v, u, false); // store parent ptr in the link
                }
            }
        }

        map.redraw();
    }

    @Override
    public void TSP(TSPMap map) {
        MST(map);
        // TODO: implement the rest of this method.
        int numPoints = map.getCount();
        if (numPoints <= 1) return;

        // build adj list from mst parent links
        ArrayList<ArrayList<Integer>> adj = new ArrayList<>(numPoints);
        for (int i = 0; i < numPoints; i++) {
            adj.add(new ArrayList<>());
        }

        int root = 0;
        for (int i = 0; i < numPoints; i++) {
            int parent = map.getLink(i);
            if (parent != -1) {
                adj.get(parent).add(i);
                adj.get(i).add(parent);
            }
        }

        // dfs
        ArrayList<Integer> tour = new ArrayList<>();
        HashSet<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();

        stack.push(root);

        while (!stack.isEmpty()) {
            int curr = stack.pop();
            if (!visited.contains(curr)) {
                visited.add(curr);
                tour.add(curr);

                // add neighbours to stack
                for (int neighbour : adj.get(curr)) {
                    if (!visited.contains(neighbour)) {
                        stack.push(neighbour);
                    }
                }
            }
        }

        // set the links for the tsp tour cycle
        for (int i = 0; i < tour.size(); i++) {
            int from = tour.get(i);
            int to = tour.get((i + 1) % tour.size()); // back to start at the end
            map.setLink(from, to, false);
        }

        map.redraw();
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        int n = map.getCount();
        if (n <= 0) return false;

        HashSet<Integer> visited = new HashSet<>();
        int curr = 0; // start at city 1

        for (int i = 0; i < n; i++) {
            int next = map.getLink(curr);
            if (next < 0 || next >= n) {
                return false; // the link doesnt exist
            }

            // fail if we visit a node twice before the last step
            if (visited.contains(curr)) {
                return false;
            }

            visited.add(curr);
            curr = next;
        }

        // valid if we visited all cities and returned to start
        return curr == 0 && visited.size() == n;
    }

    @Override
    public double tourDistance(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        if (!isValidTour(map)) return -1;

        double totalDistance = 0;
        int n = map.getCount();
        int curr = 0;

        // follow the links sequentially to sum the distance
        for (int i = 0; i < n; i++) {
            int next = map.getLink(curr);
            totalDistance += map.pointDistance(curr, next);
            curr = next;
        }
        return totalDistance;
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "../hundredpoints.txt");
        TSPGraph graph = new TSPGraph();

        graph.MST(map);
        graph.TSP(map);
        System.out.println(graph.isValidTour(map));
        System.out.println(graph.tourDistance(map));
    }
}
