package dev._2lstudios.teleportrequest.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import dev._2lstudios.teleportrequest.teleport.Teleports;

public class EntityDamageListener implements Listener {
    private Teleports teleports;

    public EntityDamageListener(Teleports teleports) {
        this.teleports = teleports;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;

            teleports.cancelPending(player);
        }
    }
}
