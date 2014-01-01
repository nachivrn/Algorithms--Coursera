import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class DijkstraShortestDistance {
    private static final int NUM_OF_NODES = 200;
    private static Node[] graph = new Node[NUM_OF_NODES];
    private static PriorityQueue<Node> heap = new PriorityQueue<Node>(NUM_OF_NODES, new NodeComparator());

    static {
        for (int i = 0; i < NUM_OF_NODES; i++) {
            graph[i] = new Node(i + 1);
        }
    }

    static Edge[] createEdges(String[] strArr) {
        Edge[] edges = new Edge[strArr.length - 1];
        for (int i = 1, j = 0; i < strArr.length; i++, j++) {
            String[] subStrArr = strArr[i].split(",");
            edges[j] = new Edge(graph[Integer.parseInt(subStrArr[0]) - 1], Integer.parseInt(subStrArr[1]));
        }
        return edges;
    }

    static void readInput(File input) throws Exception {
        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);
        String str;
        try {
            while ((str = br.readLine()) != null) {
                String[] strArr = str.split("\t");
                Edge[] edges = createEdges(strArr);
                graph[Integer.parseInt(strArr[0]) - 1].setEdges(edges);
            }
        } finally {
            br.close();
        }
    }

    static int [] findShortestPath() {
        int[] shortestDist = new int[NUM_OF_NODES];
        // Insert start node with key value of zero to the heap
        graph[0].setKey(0);
        heap.add(graph[0]);

        Set<Node> exploredNodes = new HashSet<Node>();
        for (int i = 1; i < NUM_OF_NODES; i++) {
            heap.add(graph[i]);
        }
        for (int j = 0; j < NUM_OF_NODES; j++) {
            Node n = heap.poll();
            exploredNodes.add(n);
            shortestDist[n.getVertex() - 1] = n.getKey();
            for (Edge edge : n.getEdges()) {
                Node neighbor = edge.getNode();
                if (exploredNodes.contains(neighbor)) {
                    continue;
                }
                // update the neighbors of the node extracted with new key value to the heap
                heap.remove(neighbor);
                int newKey =  Math.min(neighbor.getKey(), n.getKey() + edge.getDistance());
                neighbor.setKey(newKey);
                heap.add(neighbor);
            }
        }
        return shortestDist;
    }

    public static void main(String[] args) throws Exception{
        int[] desiredNodes = new int[] {7,37,59,82,99,115,133,165,188,197} ;
        if (args.length < 1) {
                   System.out.println("Usage : java DijkstraShortestDistance <input.txt>");
                   System.exit(-1);
               }
        readInput(new File(args[0]));
        int[] shortestDistance = findShortestPath();
        for (int i = 0; i < desiredNodes.length; i++) {
            System.out.print(shortestDistance[desiredNodes[i] - 1]);
            if (i != desiredNodes.length - 1)
                System.out.print(",");
        }
    }


    static class Node {
        private int vertex;
        private Edge[] edges;
        private int key = 1000000;

        public Node(int vertex) {
            this.vertex = vertex;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public int getKey() {
            return this.key;
        }

        public void setEdges(Edge[] edges) {
            this.edges = edges;
        }

        public Edge[] getEdges() {
            return this.edges;
        }

        public int getVertex() {
            return vertex;
        }

    }

    static class Edge {
        Node n;
        int distance;

        public Edge(Node n, int distance) {
            this.n = n;
            this.distance = distance;
        }

        int getDistance() {
            return distance;
        }

        Node getNode() {
            return n;
        }

    }

    static class NodeComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            Node n1 = (Node) o1;
            Node n2 = (Node) o2;
            Integer n1Key = new Integer(n1.key);
            Integer n2Key = new Integer(n2.key);
            return n1Key.compareTo(n2Key);
        }
    }
}
