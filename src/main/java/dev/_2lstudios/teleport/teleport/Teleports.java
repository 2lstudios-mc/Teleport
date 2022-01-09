package dev._2lstudios.teleport.teleport;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Player;

import dev._2lstudios.teleport.config.MessagesConfig;
import dev._2lstudios.teleport.config.Placeholder;
import dev._2lstudios.teleport.utils.BukkitUtils;

public class Teleports {
    private Collection<PendingTeleport> pendingTeleports;
    private TeleportPlayers teleportPlayers;
    private MessagesConfig messagesConfig;

    public Teleports(MessagesConfig messagesConfig) {
        this.pendingTeleports = new HashSet<>();
        this.teleportPlayers = new TeleportPlayers();
        this.messagesConfig = messagesConfig;
    }

    public Collection<PendingTeleport> getPendingTeleports() {
        return pendingTeleports;
    }

    public void acceptRequest(Player sender, Player target) {
        if (sender == target) {
            sender.sendMessage(messagesConfig.getMessage("accept.self"));
            return;
        }

        UUID senderUUID = sender.getUniqueId();
        UUID targetUUID = target.getUniqueId();
        TeleportPlayer senderPlayer = teleportPlayers.get(senderUUID);
        TeleportPlayer targetPlayer = teleportPlayers.get(targetUUID);
        Placeholder targetPlaceholder = new Placeholder("%target%", target.getName());
        Placeholder senderPlaceholder = new Placeholder("%sender%", sender.getName());

        if (!targetPlayer.receivedRequest(senderUUID)) {
            Placeholder[] placeholders = { targetPlaceholder, senderPlaceholder};

            target.sendMessage(messagesConfig.getMessage("accept.unsent", placeholders));
            return;
        }

        int seconds = BukkitUtils.isNearPlayer(sender, 32) ? 10 : 3;
        PendingTeleport pendingTeleport = new PendingTeleport(sender, target, seconds);

        targetPlayer.unreceiveRequest(senderUUID);
        senderPlayer.setPendingTeleport(pendingTeleport);
        pendingTeleports.add(pendingTeleport);

        Placeholder secondsPlaceholder = new Placeholder("%seconds%", String.valueOf(seconds));
        Placeholder[] placeholders = { targetPlaceholder, senderPlaceholder, secondsPlaceholder};

        sender.sendMessage(messagesConfig.getMessage("accept.received", placeholders));
        target.sendMessage(messagesConfig.getMessage("accept.sent", placeholders));
    }

    public void sendRequest(Player sender, Player target) {
        if (sender == target) {
            sender.sendMessage(messagesConfig.getMessage("send.self"));
            return;
        }

        UUID senderUUID = sender.getUniqueId();
        TeleportPlayer targetPlayer = teleportPlayers.get(target);
        Placeholder targetPlaceholder = new Placeholder("%target%", target.getName());
        Placeholder senderPlaceholder = new Placeholder("%sender%", sender.getName());
        Placeholder[] placeholders = { targetPlaceholder, senderPlaceholder };

        if (targetPlayer.receivedRequest(senderUUID)) {
            sender.sendMessage(messagesConfig.getMessage("send.already", placeholders));
            return;
        }

        targetPlayer.receiveRequest(senderUUID);

        sender.sendMessage(messagesConfig.getMessage("send.sent", placeholders));
        target.sendMessage(messagesConfig.getMessage("send.received", placeholders));
    }

    public void denyRequest(Player sender, Player target) {
        if (sender == target) {
            sender.sendMessage(messagesConfig.getMessage("deny.self"));
            return;
        }

        UUID senderUUID = sender.getUniqueId();
        TeleportPlayer targetPlayer = teleportPlayers.get(target);

        if (!targetPlayer.receivedRequest(senderUUID)) {
            target.sendMessage(messagesConfig.getMessage("deny.unsent"));
            return;
        }

        targetPlayer.unreceiveRequest(senderUUID);

        Placeholder targetPlaceholder = new Placeholder("%target%", target.getName());
        Placeholder senderPlaceholder = new Placeholder("%sender%", sender.getName());
        Placeholder[] placeholders = { targetPlaceholder, senderPlaceholder };

        sender.sendMessage(messagesConfig.getMessage("deny.received", placeholders));
        target.sendMessage(messagesConfig.getMessage("deny.sent", placeholders));
    }

    public void clear(Player player) {
        teleportPlayers.delete(player.getUniqueId());
    }

    public void cancelPending(Player player) {
        TeleportPlayer teleportPlayer = teleportPlayers.get(player);
        PendingTeleport pendingTeleport = teleportPlayer.getPendingTeleport();

        if (pendingTeleport != null) {
            Player target = pendingTeleport.getTarget();
            Player sender = pendingTeleport.getSender();

            teleportPlayer.setPendingTeleport(null);
            pendingTeleports.remove(pendingTeleport);

            Placeholder targetPlaceholder = new Placeholder("%target%", target.getName());
            Placeholder senderPlaceholder = new Placeholder("%sender%", sender.getName());
            Placeholder[] placeholders = { targetPlaceholder, senderPlaceholder };

            player.sendMessage(messagesConfig.getMessage("movement", placeholders));
        }
    }

    public void teleport(PendingTeleport teleportRequest) {
        Player sender = teleportRequest.getSender();
        Player target = teleportRequest.getTarget();

        if (!sender.isOnline() || !sender.isValid()) {
            return;
        }

        Placeholder targetPlaceholder = new Placeholder("%target%", target.getName());
        Placeholder senderPlaceholder = new Placeholder("%sender%", sender.getName());
        Placeholder[] placeholders = { targetPlaceholder, senderPlaceholder };

        if (!target.isOnline()) {
            sender.sendMessage(messagesConfig.getMessage("offline", placeholders));
        }

        if (!target.isValid()) {
            sender.sendMessage(messagesConfig.getMessage("dead", placeholders));
        }

        sender.teleport(target);
        teleportPlayers.get(sender).setPendingTeleport(null);

        sender.sendMessage(messagesConfig.getMessage("teleport.sender", placeholders));
        target.sendMessage(messagesConfig.getMessage("teleport.target", placeholders));
    }
}
