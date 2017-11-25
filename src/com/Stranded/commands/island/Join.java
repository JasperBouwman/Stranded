package com.Stranded.commands.island;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.stranded.Reload;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

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
    public void run(String[] args, Player player) {

        //island join

        String uuid = player.getUniqueId().toString();

        if (p.getConfig().contains("invited." + uuid)) {

            Files warData = new Files(p, "warData.yml");

            if (p.getConfig().getStringList("playersInWar").contains(uuid)) {
                player.sendMessage(ChatColor.RED + "You can't join an island while you are in a war");
                return;
            }
            if (warData.getConfig().contains("war.pending.island1." + p.getConfig().getString("island." + uuid))) {
                if (warData.getConfig().contains("war.pending.island1." + p.getConfig().getString("island." + uuid) + ".players." + uuid)) {
                    player.sendMessage(ChatColor.RED + "You can't leave your island while you are pending for a war");
                    return;
                }
            }
            if (warData.getConfig().contains("war.pending.island2." + p.getConfig().getString("island." + uuid))) {
                if (warData.getConfig().contains("war.pending.island2." + p.getConfig().getString("island." + uuid) + ".players." + uuid)) {
                    player.sendMessage(ChatColor.RED + "You can't leave your island while you are pending for a war");
                    return;
                }
            }

            Files islands = new Files(p, "islands.yml");

            String islandNew = p.getConfig().getString("invited." + uuid + ".newIsland");
            ArrayList<String> nw = (ArrayList<String>) islands.getConfig().getStringList("island." + islandNew + ".members");
            player.sendMessage(ChatColor.GREEN + "You joined " + islandNew);

            for (String s : nw) {
                if (Bukkit.getPlayer(UUID.fromString(s)) != null) {
                    Bukkit.getPlayer(UUID.fromString(s)).sendMessage(ChatColor.DARK_BLUE + player.getName() + ChatColor.BLUE + " Joined your island");
                }
            }

            Main.resetPlayerData(uuid, p);

            com.Stranded.Scoreboard.scores(p, player);

            com.Stranded.Scoreboard.updateIslandScoreboard(p, islandNew);

            if (p.getConfig().contains("island." + uuid)) {
                String islandOld = p.getConfig().getString("island." + uuid);
                ArrayList<String> old = (ArrayList<String>) islands.getConfig().getStringList("island." + islandOld + ".members");
                if (old.contains(uuid)) {
                    old.remove(uuid);
                    islands.getConfig().set("island." + islandOld + ".members", old);

                    for (String s : old) {
                        if (Bukkit.getPlayer(UUID.fromString(s)) != null) {
                            Bukkit.getPlayer(UUID.fromString(s)).sendMessage(ChatColor.DARK_RED + player.getName() + ChatColor.RED + " left your island");
                            com.Stranded.Scoreboard.scores(p, Bukkit.getPlayer(UUID.fromString(s)));
                        }
                    }
                    if (old.size() == 0) {
                        islands.getConfig().set("island." + islandOld, null);
                    }
                }
                p.getConfig().set("island." + uuid, islandNew);
            }

            p.saveConfig();
            Bukkit.getScheduler().cancelTask(islands.getConfig().getInt("invited." + uuid + ".pendingID"));
            Main.reloadHolds -= 1;
            if (Main.reloadPending && Main.reloadHolds == 0) {
                Reload.reload(p);
            }

            p.getConfig().set("invited." + uuid, null);
            p.getConfig().set("island." + uuid, islandNew);
            nw.add(uuid);
            islands.getConfig().set("island." + islandNew + ".members", nw);
            islands.saveConfig();
            p.saveConfig();
            return;

        }
        player.sendMessage(ChatColor.RED + "You aren't invited to join an island");
    }
}
