package dev._2lstudios.teleportrequest.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev._2lstudios.teleportrequest.teleport.Teleports;

public class TPDeny implements CommandExecutor {
    private Server server;
    private Teleports teleports;

    public TPDeny(Server server, Teleports teleports) {
        this.server = server;
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
                    sender.sendMessage("Target is not online");
                }
            } else {
                sender.sendMessage("/tpdeny <target>");
            }
        } else {
            sender.sendMessage("Command not allowed from console");
        }

        return true;
    }
}
