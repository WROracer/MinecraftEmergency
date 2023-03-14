package de.wroracer.minecraftemergency.wand;

import de.wroracer.minecraftemergency.MinecraftEmergency;
import de.wroracer.minecraftemergency.npc.path.Node;
import org.apache.commons.lang3.Validate;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.util.Vector;

public class PathWand implements Listener {

    private final MinecraftEmergency plugin;
    private boolean holdWand = false;
    private Node selectedNode = null;

    public PathWand(MinecraftEmergency plugin) {
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskTimerAsynchronously(MinecraftEmergency.getInstance(), () -> {
            if (holdWand) {
                showPaths();
            }
        }, 0, 1);
    }

    private void showPaths() {
        plugin.getNpcManager().getPathfinding().getNodes().forEach(node -> {
            //Show a particle at the node
            //node.getLocation().getWorld().playEffect(node.getLocation(), Effect.ENDER_SIGNAL, 1);
            Color color = Color.GREEN;
            if (selectedNode.equals(node)) {
                color = Color.BLUE;
            }

            Particle.REDSTONE.builder().extra(0).color(color).location(node.getLocation()).count(1).spawn();
            drawBlock(node.getLocation(), color);

            //Show a particle line to the next nodes
            node.getConnections().forEach(nextNode -> {
                drawLine(node.getLocation().clone().add(0.5, -0.5, 0.5), nextNode.getLocation().clone().add(0.5, -0.5, 0.5), 0.5);
            });
        });
    }

    private void drawBlock(Location loc, Color color) {

        drawLine(loc, loc.clone().add(1, 0, 0), 0.49, color);
        drawLine(loc, loc.clone().add(0, 0, 1), 0.49, color);
        drawLine(loc, loc.clone().add(0, -1, 0), 0.49, color);

        drawLine(loc.clone().add(0, -1, 0), loc.clone().add(1, -1, 0), 0.49, color);
        drawLine(loc.clone().add(0, -1, 0), loc.clone().add(0, -1, 1), 0.49, color);

        drawLine(loc.clone().add(1, 0, 0), loc.clone().add(1, -1, 0), 0.49, color);
        drawLine(loc.clone().add(1, 0, 0), loc.clone().add(1, 0, 1), 0.49, color);

        drawLine(loc.clone().add(0, 0, 1), loc.clone().add(1, 0, 1), 0.49, color);
        drawLine(loc.clone().add(0, 0, 1), loc.clone().add(0, -1, 1), 0.49, color);

        drawLine(loc.clone().add(1, 0, 1), loc.clone().add(1, -1, 1), 0.49, color);

    }

    public void drawLine(
            /* Would be your orange wool */Location point1,
            /* Your white wool */ Location point2,
            /*Space between each particle*/double space
    ) {
        drawLine(point1, point2, space, Color.RED);
    }

    private void drawLine(Location point1, Location point2, double space, Color color) {
        World world = point1.getWorld();

        /*Throw an error if the points are in different worlds*/
        Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");

        /*Distance between the two particles*/
        double distance = point1.distance(point2);

        /* The points as vectors */
        Vector p1 = point1.toVector();
        Vector p2 = point2.toVector();

        /* Subtract gives you a vector between the points, we multiply by the space*/
        Vector vector = p2.clone().subtract(p1).normalize().multiply(space);

        /*The distance covered*/
        double covered = 0;

        /* We run this code while we haven't covered the distance, we increase the point by the space every time*/
        for (; covered < distance; p1.add(vector)) {
            /*Spawn the particle at the point*/
            Particle.REDSTONE.builder().extra(0).color(color).location(world, p1.getX(), p1.getY(), p1.getZ()).spawn();

            /* We add the space covered */
            covered += space;
        }
    }

    @EventHandler
    private void onPlayerClick(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.STICK) {
            this.holdWand = true;
            event.setCancelled(true);
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                //Add a node
                Node node = new Node(event.getClickedBlock().getLocation().add(0, 1, 0));
                if (selectedNode != null) {
                    selectedNode.getConnections().add(node);
                    node.getConnections().add(selectedNode);
                }
                selectedNode = node;
                plugin.getNpcManager().getPathfinding().addNode(node);
            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                //Select a node
                selectedNode = plugin.getNpcManager().getPathfinding().getNodes().stream().filter(node -> node.getLocation().distance(event.getClickedBlock().getLocation()) < 1).findFirst().orElse(null);
            }
        }
    }

    @EventHandler
    public void onPlayerItemHeldChange(PlayerItemHeldEvent event) {
        //check if player is holding a stick
        holdWand = event.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK;
    }

}
