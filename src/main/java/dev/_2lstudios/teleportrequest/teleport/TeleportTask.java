package dev._2lstudios.teleportrequest.teleport;

import java.util.Collection;
import java.util.Iterator;

public class TeleportTask implements Runnable {
    private Teleports teleports;

    public TeleportTask(Teleports teleports) {
        this.teleports = teleports;
    }

    @Override
    public void run() {
        Collection<PendingTeleport> teleportRequests = teleports.getPendingTeleports();
        Iterator<PendingTeleport> iterator = teleportRequests.iterator();

        while (iterator.hasNext()) {
            PendingTeleport teleportRequest = iterator.next();

            if (teleportRequest.tick() <= 0) {
                iterator.remove();
                teleports.teleport(teleportRequest);
            }
        }
    }
}