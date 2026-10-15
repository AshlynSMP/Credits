package nl.ashlyn.credits.nms.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import nl.ashlyn.core.utils.Logger;
import nl.ashlyn.credits.Main;
import nl.ashlyn.credits.nms.Credits;
import nl.ashlyn.credits.nms.ICredits;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *  Created by Ashlyn on 28/11/2025
 *  Originally by LoneDev on SpigotMC but updated to work with newer versions
 *  https://www.spigotmc.org/resources/end-credits.90026/
 */
public class CreditsNMS implements ICredits {
    private static Field field_spectatorMode = null;

    @Override
    public void showCredits(Player player) {
        if (field_spectatorMode == null) {
            Logger.logCredits("Failed to show credits screen to player. This server version is not compatible.");
            return;
        }

        PacketContainer packet = new PacketContainer(PacketType.Play.Server.GAME_STATE_CHANGE);
        try {
            Object spectatorMode = field_spectatorMode.get(null);
            packet.getModifier().write(0, spectatorMode);
            packet.getFloat().write(0, 1.0F);
            Credits.sendPacket(player, packet);

            Logger.logCredits("Sent credits packet to " + player.getName());
        } catch (IllegalAccessException e) {
            Logger.logCredits("Failed to show credits screen to player:");
            e.printStackTrace();
        }
    }

    static {
        try {
            Field eField = PacketType.Play.Server.GAME_STATE_CHANGE.getPacketClass().getDeclaredField("e");
            eField.setAccessible(true);
            Class<?> valueType = eField.getType();

            try {
                Field win = valueType.getDeclaredField("WIN_GAME");
                if (Modifier.isStatic(win.getModifiers()) && win.getType().equals(valueType)) {
                    win.setAccessible(true);
                    field_spectatorMode = win;
                }
            } catch (NoSuchFieldException e) {
                Class<?> declaring = valueType.getDeclaringClass();
                if (declaring != null) {
                    try {
                        Field win = declaring.getDeclaredField("WIN_GAME");
                        if (Modifier.isStatic(win.getModifiers()) && win.getType().equals(valueType)) {
                            win.setAccessible(true);
                            field_spectatorMode = win;
                        }
                    } catch (NoSuchFieldException err) {
                        Logger.logCredits("Failed to show credits screen to player:");
                        err.printStackTrace();
                    }
                }
            }
        } catch (NoSuchFieldException e) {
            Logger.logCredits("Failed to initialize the plugin. This server version is not compatible.");
            e.printStackTrace();
        }
    }
}
