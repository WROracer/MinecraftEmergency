package de.wroracer.minecraftemergency.npc.path;

import org.bukkit.Location;

import java.util.*;

public class Pathfinding {
    private final List<Node> nodes;

    public Pathfinding() {
        this.nodes = new ArrayList<>();

    }

    private static double heuristic(Node a, Node b) {
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2) + Math.pow(a.getZ() - b.getZ(), 2));
    }

    private static void reconstructPath(Map<Node, Node> cameFrom, Node current, Queue<Node> path) {
        if (cameFrom.containsKey(current)) {
            reconstructPath(cameFrom, cameFrom.get(current), path);
        }

        path.add(current);
        System.out.println(current);
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Queue<Node> findPath(Node start, Node goal) {
        Set<Node> closed = new HashSet<Node>();
        Map<Node, Node> cameFrom = new HashMap<Node, Node>();
        Map<Node, Double> gScore = new HashMap<Node, Double>();
        Map<Node, Double> fScore = new HashMap<Node, Double>();

        PriorityQueue<Node> open = new PriorityQueue<Node>(new NodeComparator(fScore));

        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, goal));

        open.add(start);

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (current == goal) {
                Queue<Node> path = new ArrayDeque<>();
                reconstructPath(cameFrom, current, path);
                return path;
            }

            closed.add(current);

            for (Node neighbor : current.getConnections()) {
                if (closed.contains(neighbor)) {
                    continue;
                }

                double tentativeGScore = gScore.get(current) + current.getDistance(neighbor);

                if (!open.contains(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic(neighbor, goal));

                    if (!open.contains(neighbor)) {
                        open.add(neighbor);
                    }
                }
            }
        }

        System.out.println("No path found.");
        return null;
    }

    public Node getNearbyNodes(Location pos, int dis) {
        for (Node node : nodes) {
            Location nodePos = node.getLocation();
            if (nodePos.distance(pos) <= dis) {
                return node;
            }
        }
        return null;
    }

    private static class NodeComparator implements Comparator<Node> {

        private final Map<Node, Double> fScore;

        public NodeComparator(Map<Node, Double> fScore) {
            this.fScore = fScore;
        }

        @Override
        public int compare(Node o1, Node o2) {
            return Double.compare(fScore.get(o1), fScore.get(o2));
        }
    }
}
