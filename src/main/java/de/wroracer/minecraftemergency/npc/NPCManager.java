package de.wroracer.minecraftemergency.npc;

import de.wroracer.minecraftemergency.MinecraftEmergency;
import de.wroracer.minecraftemergency.npc.path.Node;
import de.wroracer.minecraftemergency.npc.path.Pathfinding;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class NPCManager {

    private final MinecraftEmergency plugin;

    private final Pathfinding pathfinding;

    private final List<NPC> npcs = new ArrayList<>();

    public NPCManager(MinecraftEmergency plugin) {
        this.plugin = plugin;
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, () -> {
            for (NPC npc : npcs) {
                npc.move();
            }
        }, 0L, 1L);
        pathfinding = new Pathfinding();
    }

    public void addNPC(Location pos) {
       /* NPC npc = new NPC(pos, "Test "+npcs.size()+1);
        //calc next location add 5 to each
        Location nextLocation = pos.clone().add(5, 0, 5);
        npc.setNextLocation(nextLocation);
        npcs.add(npc);*/
        Node start = pathfinding.getNearbyNodes(pos, 10);
        Node end = pathfinding.getNearbyNodes(pos.clone().add(5, 0, 5), 10);
        Queue<Node> path = pathfinding.findPath(start, end);
        NPC npc = new NPC(pos, "Test " + npcs.size() + 1, path);
        npcs.add(npc);
    }

    public List<Node> pathTo(Location start, Location end) {
        //try adding a A* pathfinding algorithm

        return null;
    }

    public Pathfinding getPathfinding() {
        return pathfinding;
    }
}
