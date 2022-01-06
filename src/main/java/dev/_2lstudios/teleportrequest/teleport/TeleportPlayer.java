package dev._2lstudios.teleportrequest.teleport;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class TeleportPlayer {
    private PendingTeleport pendingTeleport;
    private Collection<UUID> receivedRequests;

    public TeleportPlayer() {
        this.receivedRequests = new HashSet<>();
    }

    public boolean receivedRequest(UUID uuid) {
        return receivedRequests.contains(uuid);
    }

    public void receiveRequest(UUID uuid) {
        receivedRequests.add(uuid);
    }

    public void unreceiveRequest(UUID uuid) {
        receivedRequests.remove(uuid);
    }

    public void setPendingTeleport(PendingTeleport pendingTeleport) {
        this.pendingTeleport = pendingTeleport;
    }

    public PendingTeleport getPendingTeleport() {
        return pendingTeleport;
    }
}
