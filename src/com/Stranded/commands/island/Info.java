package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import com.Stranded.playerUUID.PlayerUUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;

public class Info extends CmdManager {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getAlias() {
        return "i";
    }

    @Override
    public void run(String[] args, Player player) {

        //island info

        String uuid = player.getUniqueId().toString();
        Files config = getFiles("config.yml");

        if (config.getConfig().contains("island." + uuid)) {
            Files f = getFiles("islands.yml");

            String island = config.getConfig().getString("island." + uuid);
            int lvl = f.getConfig().getInt("island." + island + ".lvl");
            int spc = (19 + 6 * lvl) * (19 + 6 * lvl);

            player.sendMessage(ChatColor.DARK_BLUE + "Island Info:");
            player.sendMessage(ChatColor.BLUE + "Island name: " + ChatColor.DARK_AQUA + island);
            player.sendMessage(ChatColor.BLUE + "Island owner: " + ChatColor.DARK_AQUA + PlayerUUID.getPlayerName(f.getConfig().getString("island." + island + ".owner")));
            player.sendMessage(ChatColor.BLUE + "Island level: " + ChatColor.DARK_AQUA + lvl);
            player.sendMessage(ChatColor.BLUE + "Island surface: " + ChatColor.DARK_AQUA + spc + " (" + (19 + 6 * lvl) + "*" + (19 + 6 * lvl) + ")");
            player.sendMessage(ChatColor.BLUE + "Nexus level: " + ChatColor.DARK_AQUA + f.getConfig().getString("island." + island + ".nexusLvl"));

            ArrayList<String> list = (ArrayList<String>) f.getConfig().getStringList("island." + island + ".members");

            StringBuilder members = new StringBuilder();
            int swap = 0;
            for (String pl : list) {
                Player tmpPlayer = PlayerUUID.getPlayer(UUID.fromString(pl));
                if (tmpPlayer != null) {
                    if (swap == 0) {
                        members.append("§2");
                        swap++;
                    } else {
                        members.append("§a");
                        swap = 0;
                    }
                    members.append(tmpPlayer.getName());
                } else {
                    if (swap == 0) {
                        members.append("§4");
                        swap++;
                    } else {
                        members.append("§c");
                        swap = 0;
                    }
                    members.append(PlayerUUID.getPlayerName(pl));
                }
                members.append("§r ");
            }
            player.sendMessage(ChatColor.BLUE + "Members:\n" + members.toString());
        } else {
            player.sendMessage(ChatColor.RED + "You can't get your island info while you aren't in an island");
        }
    }
}