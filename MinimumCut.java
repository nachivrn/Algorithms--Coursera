import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MinimumCut {
    private static int minimumCut = Integer.MAX_VALUE;
    private static final int MAX_THREAD_POOL_SIZE = 10;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);

    static Graph readInput(File input) throws Exception {
        Graph graph = new Graph();
        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);
        String str;
        while ((str = br.readLine()) != null) {
            String[] strArr = str.trim().split("\t");
            graph.addVertexNeighbors(strArr);
        }
        br.close();
        graph.vertexes.remove(0);
        return graph;
    }

    static Graph copyGraph(Graph g) {
        Graph newGraph = new Graph();
        newGraph.vertexes.remove(0);
        for (int i = 1; i <= g.vertexes.size(); i++) {
            newGraph.vertexes.get(i).addNeighbors(g.vertexes.get(i).getNeighbors());
        }
        return newGraph;
    }

    static void updateMergedNodes(LinkedHashMap<Integer, Integer> mergedNodes) {
        Integer[] keys = new Integer[mergedNodes.size()];
        mergedNodes.keySet().toArray(keys);
        for (int i = keys.length - 1; i >= 0; i--) {
            int value = mergedNodes.get(keys[i]);
            if (mergedNodes.containsKey(value)) {
                int newValue = mergedNodes.get(value);
                mergedNodes.put(keys[i], newValue);
            }
        }

    }

    static int randomContraction(Graph graph) {
        LinkedHashMap<Integer, Integer> merged = new LinkedHashMap<Integer, Integer>();
        Random random = new Random();
        while (true) {
            if (graph.vertexes.size() <= 2) {
                break;
            }
            List<Integer> keys = new ArrayList<Integer>(graph.vertexes.keySet());
            int nodeAIndex = keys.get(random.nextInt(keys.size()));
            Vertex nodeA = graph.vertexes.get(nodeAIndex);
            List<Integer> nodeANeighbors = nodeA.getNeighbors();
            ListIterator<Integer> nodeAIter = nodeANeighbors.listIterator();
            while (nodeAIter.hasNext()) {
                int name = nodeAIter.next();
                if (merged.containsKey(name)) {
                    nodeAIter.remove();
                    nodeAIter.add(merged.get(name));
                }
            }
            int rand = random.nextInt(nodeANeighbors.size());
            int nodeBIndex = nodeANeighbors.get(rand);
            Vertex nodeB = graph.vertexes.get(nodeBIndex);
            List<Integer> nodeBNeighbors = nodeB.getNeighbors();
            while (nodeANeighbors.remove(new Integer(nodeBIndex))) ;
            merged.put(nodeBIndex, nodeAIndex);
            updateMergedNodes(merged);
            for (int adj : nodeBNeighbors) {
                if (adj != nodeAIndex) {
                    if (merged.get(adj) == null) {
                        nodeANeighbors.add(adj);
                    } else if (merged.get(adj) != nodeAIndex) {
                        nodeANeighbors.add(merged.get(adj));
                    }
                }
            }
            graph.vertexes.remove(new Integer(nodeBIndex));
        }
        return graph.vertexes.get(new ArrayList<Integer>(graph.vertexes.keySet()).get(0)).
                getNeighbors().size();
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage : java MinimumCut <input.txt>");
            System.exit(-1);
        }
        Graph graph = readInput(new File(args[0]));
        int numberOfTimes = (int) (Math.pow(Graph.MAX_NODES, 2) * Math.log(Graph.MAX_NODES));
        for (int i = 0; i < numberOfTimes; i++) {
            executorService.execute(new Worker(copyGraph(graph)));
        }
        executorService.shutdown();
    }

    static class Worker implements Runnable {
        private Graph graph;

        public Worker(Graph graph) {
            this.graph = graph;
        }

        public void run() {
            int min = randomContraction(graph);
            synchronized (MinimumCut.class) {
                if (min < minimumCut) {
                    System.out.println(min);
                    minimumCut = min;
                }
            }
        }
    }

    static class Graph {
        private static final int MAX_NODES = 200;
        private HashMap<Integer, Vertex> vertexes = new HashMap<Integer, Vertex>();

        public Graph() {
            for (int i = 0; i <= MAX_NODES; i++) {
                vertexes.put(i, new Vertex());
            }
        }

        public void addVertexNeighbors(String[] nodes) {
            if (nodes.length < 0) {
                throw new IllegalArgumentException();
            }
            int vertex = Integer.parseInt(nodes[0]);
            for (int i = 1; i < nodes.length; i++) {
                addEdge(vertex, Integer.parseInt(nodes[i]));
            }
        }

        public void addEdge(int u, int v) {
            if (u < 0 || u > vertexes.size() || v < 0 || v > vertexes.size()) {
                throw new IndexOutOfBoundsException();
            }
            vertexes.get(u).addNeighbors(v);
        }
    }

    static class Vertex {
        private LinkedList<Integer> neighbors = new LinkedList<Integer>();

        public void addNeighbors(int v) {
            if (!neighbors.contains(v))
                neighbors.add(v);
        }

        public void addNeighbors(List<Integer> neighborList) {
            neighbors.addAll(neighborList);
        }

        public List<Integer> getNeighbors() {
            return neighbors;
        }
    }

}
