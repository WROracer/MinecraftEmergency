package de.wroracer.minecraftemergency;


import de.wroracer.minecraftemergency.command.TestNPCCommand;
import de.wroracer.minecraftemergency.npc.NPCManager;
import de.wroracer.minecraftemergency.wand.PathWand;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinecraftEmergency extends JavaPlugin {

    private static MinecraftEmergency INSTANCE;
    private NPCManager npcManager;

    public static MinecraftEmergency getInstance() {
        return INSTANCE;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        // Plugin startup logic
        npcManager = new NPCManager(this);

        getCommand("testnpc").setExecutor(new TestNPCCommand(this));

        //Register Listeners
        getServer().getPluginManager().registerEvents(new PathWand(this), this);
    }

    public NPCManager getNpcManager() {
        return npcManager;
    }
}
