package dev._2lstudios.teleport.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import dev._2lstudios.teleport.teleport.Teleports;

public class PlayerMoveListener implements Listener {
    private Teleports teleports;

    public PlayerMoveListener(Teleports teleports) {
        this.teleports = teleports;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().distance(event.getTo()) > 0) {
            teleports.cancelPending(event.getPlayer());
        }
    }
}
