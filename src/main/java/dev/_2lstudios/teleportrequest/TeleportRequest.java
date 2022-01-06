package dev._2lstudios.teleportrequest;

import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.teleportrequest.commands.TPACommand;
import dev._2lstudios.teleportrequest.commands.TPAccept;
import dev._2lstudios.teleportrequest.commands.TPDeny;
import dev._2lstudios.teleportrequest.listeners.EntityDamageListener;
import dev._2lstudios.teleportrequest.listeners.PlayerMoveListener;
import dev._2lstudios.teleportrequest.listeners.PlayerQuitListener;
import dev._2lstudios.teleportrequest.teleport.TeleportTask;
import dev._2lstudios.teleportrequest.teleport.Teleports;

public class TeleportRequest extends JavaPlugin {
    
    @Override
    public void onEnable () {
        saveDefaultConfig();

        TeleportRequest.instance = this;

        Server server = getServer();
        PluginManager pluginManager = server.getPluginManager();
        // TODO: Use config for messages
        Configuration config = getConfig();
        Teleports teleports = new Teleports();

        getCommand("tpa").setExecutor(new TPACommand(server, teleports));
        getCommand("tpaccept").setExecutor(new TPAccept(server, teleports));
        getCommand("tpdeny").setExecutor(new TPDeny(server, teleports));

        pluginManager.registerEvents(new EntityDamageListener(teleports), this);
        pluginManager.registerEvents(new PlayerMoveListener(teleports), this);
        pluginManager.registerEvents(new PlayerQuitListener(teleports), this);

        server.getScheduler().runTaskTimer(this, new TeleportTask(teleports), 20L, 20L);
    }

    private static TeleportRequest instance;

    public static TeleportRequest getInstance () {
        return TeleportRequest.instance;
    }
}