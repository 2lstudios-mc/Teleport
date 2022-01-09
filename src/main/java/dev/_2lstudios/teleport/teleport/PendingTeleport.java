package dev._2lstudios.teleport.teleport;

import org.bukkit.entity.Player;

public class PendingTeleport {
    private Player sender;
    private Player target;
    private int left;

    public PendingTeleport(Player sender, Player target, int left) {
        this.sender = sender;
        this.target = target;
        this.left = left;
    }

    /* Ticks and then returns if should teleport */
    public int tick() {
        return --left;
    }

    public Player getSender() {
        return sender;
    }

    public Player getTarget() {
        return target;
    }
}
