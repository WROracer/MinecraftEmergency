package de.wroracer.minecraftemergency.npc;

import de.wroracer.minecraftemergency.npc.path.Node;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.Queue;

public class NPC {
    private final ArmorStand armorStand;
    private final Queue<Node> path;
    private final double speed = 0.1;
    private Location nextLocation;

    public NPC(Location pos, String name, Queue<Node> path) {
        armorStand = pos.getWorld().spawn(pos, ArmorStand.class);
        armorStand.teleport(armorStand.getLocation().add(0, 0.1, 0));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
        armorStand.setSilent(true);
        armorStand.setCollidable(false);
        this.path = path;
        this.nextLocation = path.poll().getLocation();
    }

    public void move() {
        if (nextLocation != null) {
            Location currentLocation = armorStand.getLocation();
            double distance = currentLocation.distance(nextLocation);
            if (distance > speed) {
                double x = currentLocation.getX() + (nextLocation.getX() - currentLocation.getX()) / distance * speed;
                double y = currentLocation.getY() + (nextLocation.getY() - currentLocation.getY()) / distance * speed;
                double z = currentLocation.getZ() + (nextLocation.getZ() - currentLocation.getZ()) / distance * speed;
                armorStand.teleport(new Location(currentLocation.getWorld(), x, y, z));
            } else {
                armorStand.teleport(nextLocation);
                Node node = path.poll();
                if (node != null)
                    nextLocation = node.getLocation();
                else
                    nextLocation = null;
            }
        }
    }
}
