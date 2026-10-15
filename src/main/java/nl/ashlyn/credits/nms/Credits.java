package nl.ashlyn.credits.nms;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import nl.ashlyn.credits.nms.impl.CreditsNMS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *  Created by Ashlyn on 28/11/2025
 *  Originally by LoneDev on SpigotMC but updated to work with newer versions
 *  https://www.spigotmc.org/resources/end-credits.90026/
 */
public class Credits {
    private static Credits instance;
    private final ICredits nms;

    private Credits() {
        String version = getMcVersion();
        this.nms = new CreditsNMS();
    }

    public static void init() {
        getInstance();
    }

    private static Credits getInstance() {
        if (instance == null) {
            instance = new Credits();
        }

        return instance;
    }

    public static void showCredits(Player player) {
        getInstance().nms.showCredits(player);
    }

    public static String getMcVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf(46) + 1);
    }

    public static boolean sendPacket(Player player, PacketContainer packet) {
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        return true;
    }
}
