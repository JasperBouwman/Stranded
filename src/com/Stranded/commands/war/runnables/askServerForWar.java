package com.Stranded.commands.war.runnables;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.Chat;
import com.Stranded.commands.stranded.Reload;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;

public class askServerForWar implements Runnable {

    private final String island;

    public askServerForWar(String island) {
        this.island = island;
    }

    @Override
    public void run() {
        Files warData = getFiles("warData.yml");

//        ArrayList<String> list = (ArrayList<String>) warData.getConfig().getStringList("war.pending.islandPendingList");
//
//        if (list.size() < 1) {
//            return;
//        }
//        String island = list.get(0);

        for (String players : warData.getConfig().getConfigurationSection("war.pending.island1." + island + ".players").getKeys(false)) {
            if (Bukkit.getPlayer(UUID.fromString(players)) != null) {
                Bukkit.getPlayer(UUID.fromString(players)).sendMessage("no other island reacted in time" + ChatColor.YELLOW + ChatColor.ITALIC);
            }

            if (warData.getConfig().contains("war.pending.acceptation." + players)) {
                warData.getConfig().set("war.pending.acceptation." + players, null);
            }

        }
        warData.getConfig().set("war.pending.island1." + island, null);

//        list.remove(island);
//        warData.getConfig().set("war.pending.islandPendingList", list);

        warData.saveConfig();


    }
}
