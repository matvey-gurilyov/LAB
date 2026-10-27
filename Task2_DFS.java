public class Task2_DFS {

    static final String[] NAMES = {"A", "B", "C", "D", "E", "F", "G"};
    static final boolean[][] G = new boolean[7][7];

    static void edge(char a, char b) {
        G[a - 'A'][b - 'A'] = true;
        G[b - 'A'][a - 'A'] = true;
    }

    static void buildGraph() {
        edge('A', 'B'); edge('A', 'C'); edge('B', 'D'); edge('B', 'E');
        edge('C', 'F'); edge('E', 'F'); edge('F', 'G');
    }

    static String dfs(int start) {
        int n = NAMES.length;
        boolean[] visited = new boolean[n];
        int[] stack = new int[n * n];
        int top = 0;
        StringBuilder order = new StringBuilder();

        stack[top++] = start;

        while (top > 0) {
            int u = stack[--top];
            if (visited[u]) continue;
            visited[u] = true;
            order.append(order.length() == 0 ? "" : " ").append(NAMES[u]);

            for (int v = n - 1; v >= 0; v--) {
                if (G[u][v] && !visited[v]) {
                    stack[top++] = v;
                }
            }
        }
        return order.toString();
    }

    public static void main(String[] args) {
        buildGraph();

        System.out.println("DFS order from A: " + dfs(0));
        System.out.println("DFS order from G: " + dfs(6));
    }
}
