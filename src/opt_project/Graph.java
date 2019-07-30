package opt_project;

import java.util.*;

public class Graph<T> {
    private HashMap<T, ArrayList<T>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addVertex(T vertex) {
        adjacencyList.put(vertex, new ArrayList<T>());
    }

    public void addEdge(T vertex, T neighbour) {
        adjacencyList.get(vertex).add(neighbour);
    }

    public ArrayList<T> get(T node) {
        return adjacencyList.get(node);
    }

    public List<List<T>> getPaths(T source, T destination) {
        List<List<T>> paths = new ArrayList<List<T>>();
        getAllPaths(source, destination, paths, new LinkedHashSet<T>());
        return paths;
    }

    private void getAllPaths(T current, T destination, List<List<T>> paths, LinkedHashSet<T> path) {
        path.add(current);
        if (current.equals(destination)) {
            paths.add(new ArrayList<T>(path));
            path.remove(current);
            return;
        }
        ArrayList<T> edges = get(current);
        for (T node : edges) {
            if (!path.contains(node)) {
                getAllPaths(node, destination, paths, path);
            }
        }
        path.remove(current);
    }

   /* public ArrayList<ArrayList<T>> getPaths(T source, T destination) {
        ArrayList<ArrayList<T>> paths = new ArrayList<>();
        Queue<ArrayList<T>> queue = new LinkedList<>();

        ArrayList<T> path = new ArrayList<T>();
        path.add(source);
        queue.add(path);

        while (!queue.isEmpty()) {
            path = queue.poll();
            T last = path.get(path.size() - 1);
            if (last.equals(destination)) {
                paths.add(path);
            }
            ArrayList<T> neighbours = adjacencyList.get(last);
            for (T neighbour : neighbours) {
                if (path.indexOf(neighbour) == -1) {
                    ArrayList<T> startPath = new ArrayList<T>(path);
                    startPath.add(neighbour);
                    queue.add(startPath);
                }
            }
        }
        return paths;
    }*/

    public String toString(T node) {
        String res = "";
        res += node.toString() + " : ";
        ArrayList<T> neighbours = adjacencyList.get(node);
        for (T edge : neighbours) {
            res += edge.toString() + " ";
        }
        return res;
    }

    public String toString() {
        String res = "";
        for (T node : adjacencyList.keySet()) {
            res += toString(node) + "\n";
        }
        return res;
    }
}
