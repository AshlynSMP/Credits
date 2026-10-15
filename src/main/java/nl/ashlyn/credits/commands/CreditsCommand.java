package nl.ashlyn.credits.commands;

import nl.ashlyn.credits.nms.Credits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *  Created by Ashlyn on 28/11/2025
 *  Originally by LoneDev on SpigotMC but updated to work with newer versions
 *  https://www.spigotmc.org/resources/end-credits.90026/
 */
public class CreditsCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public CreditsCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "Permission denied.");
            return true;
        }

        if (args.length > 0) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                sender.sendMessage(ChatColor.YELLOW + "Player not found!");
                return true;
            }
            Credits.showCredits(player);
        } else {
            // TODO: Implement luckperms api
            plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp group default permission unset voicechat.speak");
            for (Player player : Bukkit.getOnlinePlayers()) {
                Credits.showCredits(player);
            }
        }

        return true;
    }
}