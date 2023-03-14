package de.wroracer.minecraftemergency.npc.path;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final Location location;
    private final List<Node> connections = new ArrayList<>();

    public Node(Location location, Node... connections) {
        this.location = location;
        this.connections.addAll(List.of(connections));
    }

    public List<Node> getConnections() {
        return connections;
    }

    public Location getLocation() {
        return location;
    }

    public Double getDistance(Node neighbor) {
        return Math.sqrt(Math.pow(getX() - neighbor.getX(), 2) + Math.pow(getY() - neighbor.getY(), 2) + Math.pow(getZ() - neighbor.getZ(), 2));
    }

    public double getX() {
        return location.getX();
    }

    public double getY() {
        return location.getY();
    }

    public double getZ() {
        return location.getZ();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            Node node = (Node) obj;
            return node.getX() == getX() && node.getY() == getY() && node.getZ() == getZ();
        }
        return false;
    }
}
