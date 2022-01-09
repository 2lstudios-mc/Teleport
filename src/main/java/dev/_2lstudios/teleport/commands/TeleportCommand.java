package dev._2lstudios.teleport.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev._2lstudios.teleport.config.MessagesConfig;
import dev._2lstudios.teleport.config.Placeholder;

public class TeleportCommand implements CommandExecutor {
    private Server server;
    private MessagesConfig messagesConfig;

    public TeleportCommand(Server server, MessagesConfig messagesConfig) {
        this.server = server;
        this.messagesConfig = messagesConfig;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("telepot.teleport")) {
            if (sender instanceof Player) {
                if (args.length > 0) {
                    String playerName = args[0];
                    Player targetPlayer = server.getPlayer(playerName);
                    Placeholder targetPlaceholder = new Placeholder("%target%", playerName);
                    Placeholder senderPlaceholder = new Placeholder("%sender%", sender.getName());
                    Placeholder[] placeholders = { targetPlaceholder, senderPlaceholder };

                    if (targetPlayer != null) {
                        Player senderPlayer = (Player) sender;

                        senderPlayer.teleport(targetPlayer);
                        sender.sendMessage(messagesConfig.getMessage("teleport.sender", placeholders));
                    } else {
                        sender.sendMessage(messagesConfig.getMessage("teleport.offline", placeholders));
                    }
                } else {
                    sender.sendMessage(messagesConfig.getMessage("teleport.usage"));
                }
            } else {
                sender.sendMessage(messagesConfig.getMessage("console"));
            }
        } else {
            sender.sendMessage(messagesConfig.getMessage("permission"));
        }

        return true;
    }
}