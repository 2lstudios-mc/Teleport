package dev._2lstudios.teleport.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev._2lstudios.teleport.config.MessagesConfig;
import dev._2lstudios.teleport.config.Placeholder;
import dev._2lstudios.teleport.teleport.Teleports;

public class TPDeny implements CommandExecutor {
    private Server server;
    private MessagesConfig messagesConfig;
    private Teleports teleports;

    public TPDeny(Server server, MessagesConfig messagesConfig, Teleports teleports) {
        this.server = server;
        this.messagesConfig = messagesConfig;
        this.teleports = teleports;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                String targetName = args[0];
                Player senderPlayer = (Player) sender;
                Player targetPlayer = server.getPlayer(targetName);

                if (targetPlayer != null && targetPlayer.isOnline()) {
                    teleports.denyRequest(targetPlayer, senderPlayer);
                } else {
                    Placeholder targetPlaceholder = new Placeholder("%target%", targetName);
                    Placeholder senderPlaceholder = new Placeholder("%sender%", senderPlayer.getName());
                    Placeholder[] placeholders = { targetPlaceholder, senderPlaceholder };

                    sender.sendMessage(messagesConfig.getMessage("teleport.offline", placeholders));
                }
            } else {
                sender.sendMessage(messagesConfig.getMessage("deny.usage"));
            }
        } else {
            sender.sendMessage(messagesConfig.getMessage("console"));
        }

        return true;
    }
}
