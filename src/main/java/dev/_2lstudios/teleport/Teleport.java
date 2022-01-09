package dev._2lstudios.teleport;

import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.teleport.commands.TPACommand;
import dev._2lstudios.teleport.commands.TPAccept;
import dev._2lstudios.teleport.commands.TPDeny;
import dev._2lstudios.teleport.commands.TeleportCommand;
import dev._2lstudios.teleport.config.MessagesConfig;
import dev._2lstudios.teleport.listeners.EntityDamageListener;
import dev._2lstudios.teleport.listeners.PlayerMoveListener;
import dev._2lstudios.teleport.listeners.PlayerQuitListener;
import dev._2lstudios.teleport.teleport.TeleportTask;
import dev._2lstudios.teleport.teleport.Teleports;

public class Teleport extends JavaPlugin {
    @Override
    public void onEnable () {
        saveDefaultConfig();

        Teleport.instance = this;

        Server server = getServer();
        PluginManager pluginManager = server.getPluginManager();
        Configuration config = getConfig();
        MessagesConfig messagesConfig = new MessagesConfig(config);
        Teleports teleports = new Teleports(messagesConfig);

        getCommand("teleport").setExecutor(new TeleportCommand(server, messagesConfig));
        getCommand("tpa").setExecutor(new TPACommand(server, messagesConfig, teleports));
        getCommand("tpaccept").setExecutor(new TPAccept(server, messagesConfig, teleports));
        getCommand("tpdeny").setExecutor(new TPDeny(server, messagesConfig, teleports));

        pluginManager.registerEvents(new EntityDamageListener(teleports), this);
        pluginManager.registerEvents(new PlayerMoveListener(teleports), this);
        pluginManager.registerEvents(new PlayerQuitListener(teleports), this);

        server.getScheduler().runTaskTimer(this, new TeleportTask(teleports), 20L, 20L);
    }

    private static Teleport instance;

    public static Teleport getInstance () {
        return Teleport.instance;
    }
}