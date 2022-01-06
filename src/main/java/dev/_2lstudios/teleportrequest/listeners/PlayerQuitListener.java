package dev._2lstudios.teleportrequest.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import dev._2lstudios.teleportrequest.teleport.Teleports;

public class PlayerQuitListener implements Listener {
    private Teleports teleports;

    public PlayerQuitListener(Teleports teleports) {
        this.teleports = teleports;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        teleports.clear(player);
    }
}
