package com.Stranded.commands;

import com.Stranded.Files;
import com.Stranded.Main;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

import static com.Stranded.events.ChatEvent.chatFilter;

public class Chat implements CommandExecutor {

    private Main p;

    public Chat(Main main) {
        p = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("you must be a player to use this");
            return false;
        }

        Player player = (Player) commandSender;

        if (!p.getConfig().contains("island." + player.getUniqueId().toString())) {
            player.sendMessage("you must be in a island");
            return false;
        }

        Files islands = new Files(p, "islands.yml");

        String island = p.getConfig().getString("island." + player.getUniqueId().toString());

        ArrayList<String> playerList = (ArrayList<String>) islands.getConfig().getStringList("island." + island + ".members");

        for (String member : playerList) {
            Player tempMember = Bukkit.getPlayer(UUID.fromString(member));
            if (tempMember != null) {
                tempMember.sendMessage("[" + island + "] <" + player.getName() + "> " + chatFilter(StringUtils.join(strings, " "), p));
            }
        }

        return false;
    }
}
