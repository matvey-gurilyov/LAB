public class Task3_Dijkstra {

    static final int INF = 1_000_000_000;
    static final int NONE = -1;

    static final String[] NAMES = {"A", "B", "C", "D", "E", "F"};
    static final int[][] G = new int[6][6];

    static void edge(char a, char b, int w) {
        G[a - 'A'][b - 'A'] = w;
        G[b - 'A'][a - 'A'] = w;
    }

    static void buildGraph() {
        edge('A', 'B', 4); edge('A', 'C', 2); edge('B', 'C', 1);
        edge('B', 'D', 5); edge('C', 'D', 8); edge('C', 'E', 10);
        edge('D', 'E', 2); edge('D', 'F', 6); edge('E', 'F', 3);
    }

    static String row(int[] dist) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dist.length; i++) {
            if (i > 0) sb.append(" ");
            sb.append(NAMES[i]).append("=").append(dist[i] == INF ? "INF" : String.valueOf(dist[i]));
        }
        return sb.toString();
    }

    static void dijkstra(int source) {
        int n = NAMES.length;
        int[] dist = new int[n];
        int[] prev = new int[n];
        boolean[] settled = new boolean[n];

        for (int i = 0; i < n; i++) {
            dist[i] = INF;
            prev[i] = NONE;
        }
        dist[source] = 0;
        System.out.println("init | " + row(dist));

        while (true) {
            int u = NONE;
            for (int i = 0; i < n; i++) {
                if (!settled[i] && dist[i] < INF && (u == NONE || dist[i] < dist[u])) {
                    u = i;
                }
            }
            if (u == NONE) break;

            settled[u] = true;

            for (int v = 0; v < n; v++) {
                int w = G[u][v];
                if (w > 0 && !settled[v]) {
                    if (dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                        prev[v] = u;
                    }
                }
            }
            System.out.println("settle " + NAMES[u] + " (" + dist[u] + ") | " + row(dist));
        }

        for (int t = 0; t < n; t++) {
            if (t == source) continue;
            if (dist[t] == INF) {
                System.out.println(NAMES[source] + " to " + NAMES[t] + ": no path");
                continue;
            }
            int[] rev = new int[n];
            int len = 0;
            for (int x = t; x != NONE; x = prev[x]) {
                rev[len++] = x;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = len - 1; i >= 0; i--) {
                sb.append(NAMES[rev[i]]);
                if (i > 0) sb.append(" -> ");
            }
            System.out.println(NAMES[source] + " to " + NAMES[t] + ": cost " + dist[t] + ": " + sb);
        }
    }

    public static void main(String[] args) {
        buildGraph();
        dijkstra(0);
    }
}
