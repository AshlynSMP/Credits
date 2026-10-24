package nl.ashlyn.credits.commands;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.messaging.MessagingService;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.matcher.NodeMatcher;
import nl.ashlyn.credits.nms.Credits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

/**
 *  Created by Ashlyn on 28/11/2025
 *  Originally by LoneDev on SpigotMC but updated to work with newer versions
 *  https://www.spigotmc.org/resources/end-credits.90026/
 */
public class CreditsCommand implements CommandExecutor {

    private static final String SPEAK_PERMISSION = "voicechat.speak";

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

        if (args.length > 0 && args[0].equalsIgnoreCase("unmute")) {
            unmuteDefaultGroup();
            sender.sendMessage(ChatColor.GREEN + "Voice chat speak enabled for the default group.");
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
            muteDefaultGroup();
            for (Player player : Bukkit.getOnlinePlayers()) {
                Credits.showCredits(player);
            }
        }

        return true;
    }

    /**
     * Removes the voicechat.speak node from the LuckPerms default group.
     */
    private void muteDefaultGroup() {
        updateDefaultGroup(group -> group.data().clear(NodeMatcher.key(SPEAK_PERMISSION)));
    }

    /**
     * Sets voicechat.speak back to true on the LuckPerms default group.
     */
    private void unmuteDefaultGroup() {
        updateDefaultGroup(group -> {
            group.data().clear(NodeMatcher.key(SPEAK_PERMISSION));
            group.data().add(Node.builder(SPEAK_PERMISSION).value(true).build());
        });
    }

    /**
     * Applies a change to the LuckPerms default group, saves it and pushes
     * the change to online players and other servers.
     */
    private void updateDefaultGroup(Consumer<Group> action) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        luckPerms.getGroupManager()
                .modifyGroup("default", action)
                .thenCompose(ignored -> luckPerms.runUpdateTask())
                .thenRun(() -> luckPerms.getMessagingService().ifPresent(MessagingService::pushUpdate))
                .exceptionally(throwable -> {
                    plugin.getLogger().warning("Could not update " + SPEAK_PERMISSION + " on default group: " + throwable.getMessage());
                    return null;
                });
    }
}