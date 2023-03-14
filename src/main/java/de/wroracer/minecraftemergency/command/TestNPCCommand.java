package de.wroracer.minecraftemergency.command;

import de.wroracer.minecraftemergency.MinecraftEmergency;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestNPCCommand implements CommandExecutor {

    private final MinecraftEmergency plugin;

    public TestNPCCommand(MinecraftEmergency plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
       if (commandSender instanceof Player){
              Player player = (Player) commandSender;
              plugin.getNpcManager().addNPC(player.getLocation());
       }
        return false;
    }
}
