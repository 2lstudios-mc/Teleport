package dev._2lstudios.teleport.utils;

import org.bukkit.entity.Player;

public class BukkitUtils {
    public static boolean isNearPlayer(Player player, int distance) {
        for (Player other : player.getWorld().getPlayers()) {
            if (player != other && player.getLocation().distance(other.getLocation()) <= distance) {
                return true;
            }
        }

        return false;
    }
}
