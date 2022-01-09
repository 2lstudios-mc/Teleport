package dev._2lstudios.teleport.teleport;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class TeleportPlayers {
    private Map<UUID, TeleportPlayer> players;

    public TeleportPlayers() {
        players = new HashMap<>();
    }

    public TeleportPlayer create(UUID uuid) {
        TeleportPlayer teleportPlayer = new TeleportPlayer();
        
        players.put(uuid, teleportPlayer);

        return teleportPlayer;
    }

    public TeleportPlayer get(UUID uuid) {
        if (players.containsKey(uuid)) {
            return players.get(uuid);
        } else {
            return create(uuid);
        }
    }

    public TeleportPlayer get(Player player) {
        return get(player.getUniqueId());
    }

    public void delete(UUID uuid) {
        players.remove(uuid);
    }
}
