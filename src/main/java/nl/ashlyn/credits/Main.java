package nl.ashlyn.credits;

import nl.ashlyn.core.utils.Logger;
import nl.ashlyn.credits.commands.CreditsCommand;
import nl.ashlyn.credits.nms.Credits;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Credits.init();
        registerCommands();

        Logger.logCredits("+=========================================+");
        Logger.logCredits("Credits Initiated!" + " (Build: v" + getDescription().getVersion() + ")");
        Logger.logCredits("+=========================================+");
    }

    @Override
    public void onDisable() {
        Logger.logCredits("+=========================================+");
        Logger.logCredits("Credits Disabled!" + " (Build: v" + getDescription().getVersion() + ")");
        Logger.logCredits("+=========================================+");
    }

    private void registerCommands()
    {
        registerCommand("credits", new CreditsCommand(this));
    }

    private void registerCommand(String name, CommandExecutor executor)
    {
        this.getCommand(name).setExecutor(executor);
        Logger.logCredits("Registered command: " + ChatColor.GREEN + name);
    }
}
