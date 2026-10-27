public class Task1_BFS {

    static final int NONE = -1;

    static final String[] NAMES = {"A", "B", "C", "D", "E", "F", "G"};
    static final boolean[][] G = new boolean[7][7];

    static int[] dist = new int[7];
    static int[] parent = new int[7];

    static void edge(char a, char b) {
        G[a - 'A'][b - 'A'] = true;
        G[b - 'A'][a - 'A'] = true;
    }

    static void buildGraph() {
        edge('A', 'B'); edge('A', 'C'); edge('B', 'D'); edge('B', 'E');
        edge('C', 'F'); edge('E', 'F'); edge('F', 'G');
    }

    static String bfs(int start) {
        int n = NAMES.length;
        boolean[] discovered = new boolean[n];
        int[] queue = new int[n];
        int front = 0, back = 0;
        StringBuilder order = new StringBuilder();

        for (int i = 0; i < n; i++) {
            dist[i] = NONE;
            parent[i] = NONE;
        }

        discovered[start] = true;
        dist[start] = 0;
        queue[back++] = start;

        while (front < back) {
            int u = queue[front++];
            order.append(order.length() == 0 ? "" : " ").append(NAMES[u]);

            for (int v = 0; v < n; v++) {
                if (G[u][v] && !discovered[v]) {
                    discovered[v] = true;
                    dist[v] = dist[u] + 1;
                    parent[v] = u;
                    queue[back++] = v;
                }
            }
        }
        return order.toString();
    }

    static void printShortest(int from, int to) {
        bfs(from);
        if (dist[to] == NONE) {
            System.out.println("Shortest " + NAMES[from] + " -> " + NAMES[to] + ": no path");
            return;
        }

        int[] rev = new int[NAMES.length];
        int len = 0;
        for (int x = to; x != NONE; x = parent[x]) {
            rev[len++] = x;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = len - 1; i >= 0; i--) {
            sb.append(NAMES[rev[i]]);
            if (i > 0) sb.append(" -> ");
        }
        System.out.println("Shortest " + NAMES[from] + " -> " + NAMES[to]
                + ": distance " + dist[to] + ", path " + sb);
    }

    public static void main(String[] args) {
        buildGraph();

        System.out.println("BFS order from A: " + bfs(0));
        printShortest(0, 6);
        printShortest(3, 6);
    }
}
