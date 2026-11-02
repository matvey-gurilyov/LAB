import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Main {

    static final double INF = Double.POSITIVE_INFINITY;

    record Edge<V>(V from, V to, double w) {}

    record BfResult<V>(Map<V, Double> dist, Map<V, V> prev, List<Map<V, Double>> history) {}

    record EarlyResult<V>(Map<V, Double> dist, Map<V, V> prev, int rounds) {}

    record FwResult(double[][] dist, int[][] nxt) {}

    static int score = 0;
    static int maxScore = 0;

    static void check(String name, boolean ok, int points) {
        maxScore += points;
        if (ok) {
            score += points;
            System.out.println("  [OK]   " + name + " (+" + points + ")");
        } else {
            System.out.println("  [FAIL] " + name + " (0/" + points + ")");
        }
    }

    static String fmt(double x) {
        return Double.isInfinite(x) ? "inf" : String.valueOf((long) x);
    }

    static <V> String row(Map<V, Double> m) {
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (double x : m.values()) {
            if (!first) sb.append(", ");
            sb.append(fmt(x));
            first = false;
        }
        return sb.append("]").toString();
    }

    static String row(double[] r) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < r.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(fmt(r[i]));
        }
        return sb.append("]").toString();
    }

    static <V> BfResult<V> bellmanFord(List<V> vertices, List<Edge<V>> edges, V source) {
        Map<V, Double> dist = new LinkedHashMap<>();
        Map<V, V> prev = new LinkedHashMap<>();
        for (V v : vertices) {
            dist.put(v, INF);
            prev.put(v, null);
        }
        dist.put(source, 0.0);
        List<Map<V, Double>> history = new ArrayList<>();
        history.add(new LinkedHashMap<>(dist));
        for (int k = 1; k < vertices.size(); k++) {
            Map<V, Double> next = new LinkedHashMap<>(dist);
            for (Edge<V> e : edges) {
                if (dist.get(e.from()) + e.w() < next.get(e.to())) {
                    next.put(e.to(), dist.get(e.from()) + e.w());
                    prev.put(e.to(), e.from());
                }
            }
            dist = next;
            history.add(new LinkedHashMap<>(dist));
        }
        for (Edge<V> e : edges) {
            if (dist.get(e.from()) + e.w() < dist.get(e.to())) {
                throw new IllegalStateException("negative cycle reachable from source");
            }
        }
        return new BfResult<>(dist, prev, history);
    }

    static <V> List<V> buildPath(Map<V, V> prev, V target) {
        List<V> path = new ArrayList<>();
        V cur = target;
        while (cur != null) {
            path.add(cur);
            cur = prev.get(cur);
        }
        Collections.reverse(path);
        return path;
    }

    static <V> EarlyResult<V> bellmanFordEarly(List<V> vertices, List<Edge<V>> edges, V source) {
        Map<V, Double> dist = new LinkedHashMap<>();
        Map<V, V> prev = new LinkedHashMap<>();
        for (V v : vertices) {
            dist.put(v, INF);
            prev.put(v, null);
        }
        dist.put(source, 0.0);
        int rounds = 0;
        for (int k = 1; k < vertices.size(); k++) {
            rounds++;
            boolean changed = false;
            Map<V, Double> next = new LinkedHashMap<>(dist);
            for (Edge<V> e : edges) {
                if (dist.get(e.from()) + e.w() < next.get(e.to())) {
                    next.put(e.to(), dist.get(e.from()) + e.w());
                    prev.put(e.to(), e.from());
                    changed = true;
                }
            }
            dist = next;
            if (!changed) {
                return new EarlyResult<>(dist, prev, rounds);
            }
        }
        for (Edge<V> e : edges) {
            if (dist.get(e.from()) + e.w() < dist.get(e.to())) {
                throw new IllegalStateException("negative cycle reachable from source");
            }
        }
        return new EarlyResult<>(dist, prev, rounds);
    }

    static FwResult floydWarshall(int n, List<Edge<Integer>> edges) {
        double[][] d = new double[n][n];
        int[][] nxt = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(d[i], INF);
            Arrays.fill(nxt[i], -1);
            d[i][i] = 0;
        }
        for (Edge<Integer> e : edges) {
            d[e.from()][e.to()] = e.w();
            nxt[e.from()][e.to()] = e.to();
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (d[i][k] + d[k][j] < d[i][j]) {
                        d[i][j] = d[i][k] + d[k][j];
                        nxt[i][j] = nxt[i][k];
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (d[i][i] < 0) {
                throw new IllegalStateException("negative cycle detected");
            }
        }
        return new FwResult(d, nxt);
    }

    static List<Integer> fwPath(int[][] nxt, int i, int j) {
        List<Integer> path = new ArrayList<>();
        if (nxt[i][j] == -1 && i != j) {
            return path;
        }
        int cur = i;
        path.add(cur);
        while (cur != j) {
            cur = nxt[cur][j];
            path.add(cur);
        }
        return path;
    }

    static double[][] allPairsBf(int n, List<Edge<Integer>> edges) {
        List<Integer> vertices = new ArrayList<>();
        for (int i = 0; i < n; i++) vertices.add(i);
        double[][] result = new double[n][n];
        for (int s = 0; s < n; s++) {
            BfResult<Integer> r = bellmanFord(vertices, edges, s);
            for (int v = 0; v < n; v++) {
                result[s][v] = r.dist().get(v);
            }
        }
        return result;
    }

    static List<Edge<Integer>> randomGraph(int n, int m, long seed) {
        Random rnd = new Random(seed);
        int[] h = new int[n];
        for (int i = 0; i < n; i++) h[i] = rnd.nextInt(20);
        Set<Long> used = new HashSet<>();
        List<Edge<Integer>> edges = new ArrayList<>();
        while (edges.size() < m) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);
            if (u == v) continue;
            long key = (long) u * n + v;
            if (!used.add(key)) continue;
            int w = rnd.nextInt(10) + h[u] - h[v];
            edges.add(new Edge<>(u, v, w));
        }
        return edges;
    }

    static boolean hasNegativeEdge(List<Edge<Integer>> edges) {
        for (Edge<Integer> e : edges) {
            if (e.w() < 0) return true;
        }
        return false;
    }

    static void task1() {
        System.out.println("=== Task 1: Bellman-Ford ===");
        List<String> vs = List.of("S", "A", "B", "C", "D");
        List<Edge<String>> es = List.of(
                new Edge<>("S", "A", 6),
                new Edge<>("S", "B", 3),
                new Edge<>("B", "A", -2),
                new Edge<>("A", "C", 3),
                new Edge<>("B", "D", 7),
                new Edge<>("C", "D", -1));

        Map<Integer, double[]> myTrace = new LinkedHashMap<>();
        myTrace.put(1, new double[]{0, 6, 3, INF, INF});
        myTrace.put(2, new double[]{0, 1, 3, 9, 10});
        myTrace.put(3, new double[]{0, 1, 3, 4, 8});
        myTrace.put(4, new double[]{0, 1, 3, 4, 3});

        BfResult<String> r = bellmanFord(vs, es, "S");

        System.out.println("Part 1A trace:");
        System.out.println("  d(0): " + row(r.history().get(0)));
        for (int k = 1; k <= 4; k++) {
            System.out.println("  d(" + k + "): " + row(myTrace.get(k)));
        }

        boolean traceOk = true;
        for (int k = 1; k <= 4; k++) {
            double[] expected = myTrace.get(k);
            int idx = 0;
            for (double x : r.history().get(k).values()) {
                if (Double.compare(x, expected[idx++]) != 0) traceOk = false;
            }
        }
        check("1A hand trace matches algorithm", traceOk, 6);

        Map<String, Double> expectedDist = new LinkedHashMap<>();
        expectedDist.put("S", 0.0);
        expectedDist.put("A", 1.0);
        expectedDist.put("B", 3.0);
        expectedDist.put("C", 4.0);
        expectedDist.put("D", 3.0);
        check("1B final distances", r.dist().equals(expectedDist), 9);

        boolean histOk = r.history().size() == 5
                && row(r.history().get(2)).equals("[0, 1, 3, 9, 10]")
                && row(r.history().get(3)).equals("[0, 1, 3, 4, 8]")
                && row(r.history().get(4)).equals("[0, 1, 3, 4, 3]")
                && row(r.history().get(0)).equals("[0, inf, inf, inf, inf]");
        check("1B history rows", histOk, 7);

        List<String> path = buildPath(r.prev(), "D");
        System.out.println("  path S->D: " + path);
        check("1C build_path", path.equals(List.of("S", "B", "A", "C", "D")), 8);

        List<Edge<String>> cyc = List.of(
                new Edge<>("S", "A", 1),
                new Edge<>("A", "B", -2),
                new Edge<>("B", "A", 1));
        boolean thrown = false;
        try {
            bellmanFord(List.of("S", "A", "B"), cyc, "S");
        } catch (IllegalStateException ex) {
            thrown = true;
        }
        check("1B negative cycle detected", thrown, 6);

        List<String> six = List.of("S", "A", "B", "X", "Y", "Z");
        List<Edge<String>> chain = List.of(new Edge<>("S", "A", 1), new Edge<>("A", "B", 1));
        EarlyResult<String> er = bellmanFordEarly(six, chain, "S");
        System.out.println("  early exit rounds: " + er.rounds());
        EarlyResult<String> er2 = bellmanFordEarly(vs, es, "S");
        check("1D early exit after 3 rounds and same distances",
                er.rounds() == 3 && er.dist().get("B") == 2.0 && er2.dist().equals(expectedDist), 9);
    }

    static void task2() {
        System.out.println("=== Task 2: Floyd-Warshall ===");
        int n = 4;
        List<Edge<Integer>> es = List.of(
                new Edge<>(0, 1, 3),
                new Edge<>(0, 2, 5),
                new Edge<>(1, 2, -2),
                new Edge<>(2, 3, 2),
                new Edge<>(3, 0, 4));

        FwResult fw = floydWarshall(n, es);
        System.out.println("Distance matrix:");
        for (double[] r : fw.dist()) System.out.println("  " + row(r));

        double[][] expected = {
                {0, 3, 1, 3},
                {4, 0, -2, 0},
                {6, 9, 0, 2},
                {4, 7, 5, 0}};
        check("2A distance matrix", Arrays.deepEquals(fw.dist(), expected), 15);

        List<Integer> p = fwPath(fw.nxt(), 3, 2);
        System.out.println("  path 3->2: " + p);
        check("2B fw_path 3->2", p.equals(List.of(3, 0, 1, 2)), 7);

        List<Edge<Integer>> disc = List.of(new Edge<>(0, 1, 1), new Edge<>(2, 3, 1));
        FwResult fd = floydWarshall(4, disc);
        check("2B no path gives INF and empty path",
                Double.isInfinite(fd.dist()[0][3]) && fwPath(fd.nxt(), 0, 3).isEmpty(), 6);

        List<Edge<Integer>> neg = List.of(new Edge<>(0, 1, 1), new Edge<>(1, 2, -3), new Edge<>(2, 0, 1));
        boolean thrown = false;
        try {
            floydWarshall(3, neg);
        } catch (IllegalStateException ex) {
            thrown = true;
        }
        check("2A negative cycle detected", thrown, 6);

        boolean cross = true;
        for (int t = 0; t < 10; t++) {
            int size = 6 + t;
            List<Edge<Integer>> g = randomGraph(size, size * 3, 100 + t);
            if (!Arrays.deepEquals(floydWarshall(size, g).dist(), allPairsBf(size, g))) cross = false;
        }
        check("2C cross-check with Bellman-Ford on random graphs", cross, 11);

        int big = 80;
        List<Edge<Integer>> dense = randomGraph(big, 2000, 7);
        System.out.println("  dense graph has negative edges: " + hasNegativeEdge(dense));
        long t0 = System.nanoTime();
        double[][] viaBf = allPairsBf(big, dense);
        long t1 = System.nanoTime();
        double[][] viaFw = floydWarshall(big, dense).dist();
        long t2 = System.nanoTime();
        System.out.printf("  2D timing n=%d m=%d: Bellman-Ford all pairs %.2f ms, Floyd-Warshall %.2f ms, equal=%b%n",
                big, dense.size(), (t1 - t0) / 1e6, (t2 - t1) / 1e6, Arrays.deepEquals(viaBf, viaFw));
    }

    static void reflection() {
        System.out.println("=== Reflection ===");
        System.out.println("1. A simple shortest path has at most n-1 edges, so n-1 rounds are enough when no negative cycle exists. "
                + "An edge that still improves in round n means a negative cycle is reachable from the source.");
        System.out.println("2. If round k wrote into the same array it read, an edge relaxed earlier in the round could be reused immediately, "
                + "so d(k) would no longer mean best path with at most k edges and the history rows would be wrong.");
        System.out.println("3. Floyd-Warshall has three nested loops of length n, so it is O(n^3). "
                + "It is preferable on dense graphs, with negative weights, or when a simple all-pairs routine is wanted, "
                + "since repeating Bellman-Ford costs about n^2*m and Dijkstra cannot handle negative edges.");
        System.out.println("4. In the timing above Floyd-Warshall was faster than running Bellman-Ford from every vertex, "
                + "which matches the theory of n^3 versus n^2*m on a dense graph.");
    }

    public static void main(String[] args) {
        task1();
        task2();
        reflection();
        System.out.println("=== Automatic score: " + score + " / " + maxScore + " ===");
    }
}
