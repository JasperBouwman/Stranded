package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;

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
    public void run(String[] args) {
        if (p.getConfig().contains("island." + player.getName())) {
            Files f = new Files(p, "islands.yml");

            String island = p.getConfig().getString("island." + player.getName());
            int lvl = f.getConfig().getInt("island." + island + ".lvl");
            int spc = (19 + 6 * lvl) * (19 + 6 * lvl);

            player.sendMessage("Island Info:");
            player.sendMessage("island owner: " + f.getConfig().getString("island." + island + ".owner"));
            player.sendMessage("island level: " + lvl);
            player.sendMessage("island surface: " + spc + " (" + (19 + 6 * lvl) + "*" + (19 + 6 * lvl) + ")");

            ArrayList<String> list = (ArrayList<String>) f.getConfig().getStringList("island." + island + ".members");

            StringBuilder members = new StringBuilder();
            int swap = 0;
            for (String pl : list) {
                if (Bukkit.getPlayer(pl) != null) {
                    if (swap == 0) {
                        members.append("§2");
                        swap++;
                    } else {
                        members.append("§a");
                        swap = 0;
                    }
                } else {
                    if (swap == 0) {
                        members.append("§4");
                        swap++;
                    } else {
                        members.append("§c");
                        swap = 0;
                    }
                }
                members.append(pl);
                members.append("§r ");
            }
            player.sendMessage("members:\n" + members.toString());
        } else {
            player.sendMessage("your aren't in an island");
        }
    }

//    public static void info(Player player, Main p) {
//        if (p.getConfig().contains("island." + player.getName())) {
//            Files f = new Files(p, "islands.yml");
//
//            String island = p.getConfig().getString("island." + player.getName());
//            int lvl = f.getConfig().getInt("island." + island + ".lvl");
//            int spc = ((lvl - 1) * 6 + 25) ^ 2;
//
//            player.sendMessage("Island Info:");
//            player.sendMessage("island level: " + lvl);
//            player.sendMessage("island surface: " + spc);
//
//            ArrayList<String> list = (ArrayList<String>) f.getConfig().getStringList("island." + island + ".members");
//
//            String members = "";
//            int swap = 0;
//            for (String pl : list) {
//                if (Bukkit.getPlayer(pl) != null) {
//                    if (swap == 0) {
//                        members = members + "§2" + pl + "§r  ";
//                        swap++;
//                    } else {
//                        members = members + "§a" + pl + "§r  ";
//                        swap = 0;
//                    }
//                } else {
//                    if (swap == 0) {
//                        members = members + "§4" + pl + "§r  ";
//                        swap++;
//                    } else {
//                        members = members + "§c" + pl + "§r  ";
//                        swap = 0;
//                    }
//                }
//            }
//            player.sendMessage("member:\n" + members);
//        } else {
//            player.sendMessage("your arn't in an island");
//        }
//    }
}