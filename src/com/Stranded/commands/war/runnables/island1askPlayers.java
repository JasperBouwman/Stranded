package com.Stranded.commands.war.runnables;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.stranded.Reload;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;
import static com.Stranded.GettingFiles.getFiles;

public class island1askPlayers implements Runnable {

    private final String island;

    public island1askPlayers(String island) {
        this.island = island;
    }

    @Override
    public void run() {
        Files warData = getFiles("warData.yml");

//        ArrayList<String> list = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingList");
//
//        if (list.size() < 1) {
//            return;
//        }
//        String island = list.get(0);

        for (String players : warData.getConfig().getConfigurationSection("war.pending.island1." + island + ".players").getKeys(false)) {
            if (Bukkit.getPlayer(UUID.fromString(players)) != null) {
                Bukkit.getPlayer(UUID.fromString(players)).sendMessage("not everybody accepted this war invite in time" + ChatColor.ITALIC + ChatColor.YELLOW);
            }
        }
        warData.getConfig().set("war.pending.island1." + island, null);

//        list.remove(island);
//        warData.getConfig().set("war.pending.memberPendingList", list);

        warData.saveConfig();


    }
}
