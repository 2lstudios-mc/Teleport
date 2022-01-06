package dev._2lstudios.teleportrequest.teleport;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Player;

import dev._2lstudios.teleportrequest.utils.BukkitUtils;

public class Teleports {
    private Collection<PendingTeleport> pendingTeleports;
    private TeleportPlayers teleportPlayers;

    public Teleports() {
        this.pendingTeleports = new HashSet<>();
        this.teleportPlayers = new TeleportPlayers();
    }

    public Collection<PendingTeleport> getPendingTeleports() {
        return pendingTeleports;
    }

    public void acceptRequest(Player sender, Player target) {
        if (sender == target) {
            sender.sendMessage("Can't accept own request");
            return;
        }

        UUID senderUUID = sender.getUniqueId();
        UUID targetUUID = target.getUniqueId();
        TeleportPlayer senderPlayer = teleportPlayers.get(senderUUID);
        TeleportPlayer targetPlayer = teleportPlayers.get(targetUUID);

        if (!targetPlayer.receivedRequest(senderUUID)) {
            target.sendMessage(sender.getName() + " hasn't sent you a teleport request");
            return;
        }

        int seconds = BukkitUtils.isNearPlayer(sender, 32) ? 10 : 3;
        PendingTeleport pendingTeleport = new PendingTeleport(sender, target, seconds);

        targetPlayer.unreceiveRequest(senderUUID);
        senderPlayer.setPendingTeleport(pendingTeleport);
        pendingTeleports.add(pendingTeleport);

        sender.sendMessage(target.getName() + " has accepted your teleport request\nTeleporting to " + target.getName() + " in " + seconds + " seconds");
        target.sendMessage("You accepted " + sender.getName() + " teleport request\nTeleporting " + sender.getName() + " to you in " + seconds + " seconds");
    }

    public void sendRequest(Player sender, Player target) {
        if (sender == target) {
            sender.sendMessage("Can't send request to self");
            return;
        }

        UUID senderUUID = sender.getUniqueId();
        TeleportPlayer targetPlayer = teleportPlayers.get(target);

        if (targetPlayer.receivedRequest(senderUUID)) {
            sender.sendMessage("Already sent request to " + target.getName());
            return;
        }

        targetPlayer.receiveRequest(senderUUID);

        sender.sendMessage("You sent a teleport request to " + target.getName());
        target.sendMessage(sender.getName() + " sent you a teleport request");
    }

    public void denyRequest(Player sender, Player target) {
        if (sender == target) {
            sender.sendMessage("Can't deny own request");
            return;
        }

        UUID senderUUID = sender.getUniqueId();
        TeleportPlayer targetPlayer = teleportPlayers.get(target);

        targetPlayer.unreceiveRequest(senderUUID);

        sender.sendMessage(target.getName() + " has denied your teleport request");
        target.sendMessage("You denied " + sender.getName() + " teleport request");
    }

    public void clear(Player player) {
        teleportPlayers.delete(player.getUniqueId());
    }

    public void cancelPending(Player player) {
        TeleportPlayer teleportPlayer = teleportPlayers.get(player);
        PendingTeleport pendingTeleport = teleportPlayer.getPendingTeleport();

        if (pendingTeleport != null) {
            Player target = pendingTeleport.getTarget();

            teleportPlayer.setPendingTeleport(null);
            pendingTeleports.remove(pendingTeleport);
            player.sendMessage("Teleport to " + target.getName() + " cancelled because of movement");
        }
    }

    public void teleport(PendingTeleport teleportRequest) {
        Player sender = teleportRequest.getSender();
        Player target = teleportRequest.getTarget();

        if (!sender.isOnline() || !sender.isValid()) {
            return;
        }

        if (!target.isOnline()) {
            sender.sendMessage("Target player is not online");
        }

        if (!target.isValid()) {
            sender.sendMessage("Target player is dead");
        }

        sender.sendMessage("Teleporting to " + target.getName());
        target.sendMessage("A player has teleported to you");
        sender.teleport(target);
        teleportPlayers.get(sender).setPendingTeleport(null);
    }
}
