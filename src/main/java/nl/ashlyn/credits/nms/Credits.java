package nl.ashlyn.credits.nms;

import nl.ashlyn.core.utils.Logger;
import org.bukkit.entity.Player;

/**
 *  Created by Ashlyn on 28/11/2025
 */
public class Credits {

    private Credits() {
    }

    public static void init() {
    }

    /**
     * Shows the end credits screen to a player using the Paper API.
     */
    public static void showCredits(Player player) {
        player.showWinScreen();
        Logger.logCredits("Showed credits to " + player.getName());
    }
}
