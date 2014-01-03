import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class StronglyConnectedComponents {

    private static final int NUM_OF_VERTEX = 875714;
    private static int[] results = new int[NUM_OF_VERTEX + 1];

    static Node[] createGraph() {
        Node[] graph = new Node[NUM_OF_VERTEX + 1];
        for (int i = 1; i <= NUM_OF_VERTEX; i++) {
            graph[i] = new Node(i);
        }
        return graph;
    }

    static Node[] initGraph(File input) throws Exception {
        Node[] directedGraph = createGraph();
        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);
        String str;
        try {
            while ((str = br.readLine()) != null) {
                String[] strArr = str.split(" ");
                directedGraph[Integer.parseInt(strArr[0].trim())].getNeighbors().add(Integer.parseInt(strArr[1].trim()));
            }
        } finally {
            br.close();
        }
        return directedGraph;
    }

    static Node[] transposeGraph(Node[] graph) {
        Node[] transposeGraph = createGraph();
        for (int i = 1; i < graph.length; i++) {
            for (int j : graph[i].getNeighbors()) {
                transposeGraph[j].getNeighbors().add(i);
            }
        }
        return transposeGraph;
    }

    static void kosarajuSCC(Node[] graph) {
        Stack<Node> s = new Stack<Node>();
        boolean[] visited = new boolean[graph.length + 1];
        for (int i = graph.length - 1; i >= 1; i--) {
            if (!visited[i]) {
                dfs_pass1(graph, graph[i], visited, s);
            }
        }
        Node[] transposeGraph = transposeGraph(graph);
        System.out.println("Finished Transpose");
        visited = new boolean[graph.length + 1];
        while (!s.isEmpty()) {
            Node v = s.pop();
            if (!visited[v.getName()]) {
                results[v.getName()]= dfs_pass2(transposeGraph, transposeGraph[v.getName()], visited);
            }
        }
        Arrays.sort(results);
        for (int j = results.length - 1; j > results.length - 6; j--) {
            System.out.print(results[j]);
            if (j != results.length - 6)
                System.out.print(",");
        }

    }

    static void dfs_pass1(Node[] graph, Node vertex, boolean[] visited, Stack<Node> s) {
        Stack<Integer> tempStack = new Stack<Integer>();
        tempStack.push(vertex.getName());
        while (!tempStack.isEmpty()) {
            Node v = graph[tempStack.peek()];
            if (!visited[v.getName()]) {
                visited[v.getName()] = true;
                for (int n : v.getNeighbors()) {
                    if (!visited[n])
                        tempStack.push(n);
                }
            } else {
                int n = tempStack.pop();
                if (!s.contains(graph[n])) {
                    s.push(graph[n]);
                }
            }
        }
    }

    static int dfs_pass2(Node[] graph, Node vertex, boolean[] visited) {
        Stack<Integer> tempStack = new Stack<Integer>();
        int count = 0;
        tempStack.push(vertex.getName());
        while (!tempStack.isEmpty()) {
            Node v = graph[tempStack.pop()];
            if (!visited[v.getName()]) {
                visited[v.getName()] = true;
                count++;
                for (int n : v.getNeighbors()) {
                    tempStack.push(n);
                }
            }
        }
        return count;
    }

    static class Node {
        int name;
        List<Integer> neighbors = new LinkedList<Integer>();

        public Node(int name) {
            this.name = name;
        }

        public List<Integer> getNeighbors() {
            return neighbors;
        }

        int getName() {
            return name;
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage : java StronglyConnectedComponents <input.txt>");
            System.exit(-1);
        }

        Node[] graph = initGraph(new File(args[0]));
        System.out.println("Finished Initializing");
        kosarajuSCC(graph);
    }

}
