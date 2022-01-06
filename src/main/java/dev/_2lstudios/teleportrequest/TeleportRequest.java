package dev._2lstudios.teleportrequest;

import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.teleportrequest.commands.TPACommand;
import dev._2lstudios.teleportrequest.commands.TPAccept;
import dev._2lstudios.teleportrequest.commands.TPDeny;
import dev._2lstudios.teleportrequest.config.MessagesConfig;
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
        Configuration config = getConfig();
        MessagesConfig messagesConfig = new MessagesConfig(config);
        Teleports teleports = new Teleports(messagesConfig);

        getCommand("tpa").setExecutor(new TPACommand(server, messagesConfig, teleports));
        getCommand("tpaccept").setExecutor(new TPAccept(server, messagesConfig, teleports));
        getCommand("tpdeny").setExecutor(new TPDeny(server, messagesConfig, teleports));

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