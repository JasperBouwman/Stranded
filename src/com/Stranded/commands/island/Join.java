package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class Join extends CmdManager {

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args) {
        if (p.getConfig().contains("invited." + player.getName() + ".island")) {
            Files f = new Files(p, "islands.yml");

            String islandNew = p.getConfig().getString("invited." + player.getName() + ".island");
            ArrayList<String> nw = (ArrayList<String>) f.getConfig().getStringList("island." + islandNew + ".members");
            player.sendMessage("you now joined " + islandNew);

            for (String s : nw) {
                if (Bukkit.getPlayerExact(s) != null) {
                    Bukkit.getPlayerExact(s).sendMessage(player.getName() + " joined your island");
                }
            }

            if (p.getConfig().contains("island." + player.getName())) {
                String islandOld = p.getConfig().getString("island." + player.getName());
                ArrayList<String> old = (ArrayList<String>) f.getConfig().getStringList("island." + islandOld + ".members");
                if (old.contains(player.getName())) {
                    old.remove(player.getName());
                    f.getConfig().set("island." + islandOld + ".members", old);

                    for (String s : old) {
                        if (Bukkit.getPlayerExact(s) != null) {
                            Bukkit.getPlayerExact(s).sendMessage(player.getName() + " left your island");
                        }
                    }
                    if (old.size() == 0) {
                        f.getConfig().set("island." + islandOld, null);
                    }
                }
                p.getConfig().set("island." + player.getName(), islandNew);
            }

            p.getConfig().set("invited." + player.getName(), null);
            p.getConfig().set("island." + player.getName(), islandNew);
            nw.add(player.getName());
            f.getConfig().set("island." + islandNew + ".members", nw);
            f.saveConfig();
            p.saveConfig();

        }
        player.sendMessage("you aren't invited for anything");
    }

//    public static void join(Player player, Main p) {
//        if (p.getConfig().contains("invited." + player.getName())) {
//            Files f = new Files(p, "islands.yml");
//
//            String islandNew = p.getConfig().getString("invited." + player.getName());
//            ArrayList<String> nw = (ArrayList<String>) f.getConfig().getStringList("island." + islandNew + ".members");
//            player.sendMessage(islandNew);
//            if (nw.contains(player.getName())) {
//                return;
//            }
//            if (!nw.contains(player.getName())) {
//
//                for (String s : nw) {
//                    if (Bukkit.getPlayerExact(s) != null) {
//                        Bukkit.getPlayerExact(s).sendMessage(player.getName() + " joined your island");
//                    }
//                }
//
//                p.getConfig().set("invited." + player.getName(), null);
//                p.saveConfig();
//                nw.add(player.getName());
//                f.getConfig().set("island." + islandNew + ".members", nw);
//            }
//            if (p.getConfig().contains("island." + player.getName())) {
//                String islandOld = p.getConfig().getString("island." + player.getName());
//                ArrayList<String> old = (ArrayList<String>) f.getConfig().getStringList("island." + islandOld + ".members");
//                if (old.contains(player.getName())) {
//                    old.remove(player.getName());
//                    f.getConfig().set("island." + islandOld + ".members", old);
//
//                    for (String s : old) {
//                        if (Bukkit.getPlayerExact(s) != null) {
//                            Bukkit.getPlayerExact(s).sendMessage(player.getName() + " left your island");
//                        }
//                    }
//                    if (old.size() == 0) {
//                        f.getConfig().set("island." + islandOld, null);
//                        f.saveConfig();
//                    }
//                }
//                p.getConfig().set("island." + player.getName(), islandNew);
//                p.saveConfig();
//            }
//            f.saveConfig();
//
//        }
//    }
}
