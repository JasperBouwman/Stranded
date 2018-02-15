package com.Stranded.commands;

import com.Stranded.Files;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.events.ChatEvent.chatFilter;

public class Chat implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this");
            return false;
        }

        Player player = (Player) commandSender;
        Files config = getFiles("config.yml");

        if (!config.getConfig().contains("island." + player.getUniqueId().toString())) {
            player.sendMessage(ChatColor.RED + "You must be in a island");
            return false;
        }

        Files islands = getFiles("islands.yml");

        String island = config.getConfig().getString("island." + player.getUniqueId().toString());

        ArrayList<String> playerList = (ArrayList<String>) islands.getConfig().getStringList("island." + island + ".members");

        for (String member : playerList) {
            Player tempMember = Bukkit.getPlayer(UUID.fromString(member));
            if (tempMember != null) {
                tempMember.sendMessage("[" + island + "] <" + player.getName() + "> " + chatFilter(StringUtils.join(strings, " "), player));
            }
        }

        return false;
    }
}
