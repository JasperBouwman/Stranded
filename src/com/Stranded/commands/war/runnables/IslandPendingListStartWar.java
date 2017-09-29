package com.Stranded.commands.war.runnables;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;

import java.util.ArrayList;

public class IslandPendingListStartWar implements Runnable {

    private Main p;

    public IslandPendingListStartWar(Main main) {
        p = main;
    }

    @Override
    public void run() {
        Files warData = new Files(p, "warData.yml");

        ArrayList<String> list = (ArrayList<String>) warData.getConfig().getStringList("war.pending.islandPendingListStartWar");

        if (list.size() < 1) {
            return;
        }
        String island = list.get(0);

        for (String players : warData.getConfig().getConfigurationSection("war.pending.island2." + island + ".players").getKeys(false)) {
            if (Bukkit.getPlayerExact(players) != null) {
                Bukkit.getPlayerExact(players).sendMessage("there was no island that wanted war");
            }
        }
        warData.getConfig().set("war.pending.island2." + island, null);

        list.remove(island);
        warData.getConfig().set("war.pending.islandPendingListStartWar", list);

        warData.saveConfig();

    }
}
