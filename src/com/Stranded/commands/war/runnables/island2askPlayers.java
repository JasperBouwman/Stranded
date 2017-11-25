package com.Stranded.commands.war.runnables;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.commands.stranded.Reload;
import org.bukkit.Bukkit;

import java.util.UUID;

public class island2askPlayers implements Runnable {

    private Main p;
    private final String island;

    public island2askPlayers(Main main, String island) {
        p = main;
        this.island = island;
    }

    @Override
    public void run() {
        Files warData = new Files(p, "warData.yml");

//        ArrayList<String> list = (ArrayList<String>) warData.getConfig().getStringList("war.pending.memberPendingListNewIsland");
//
//        if (list.size() < 1) {
//            return;
//        }
//        String island = list.get(0);

        for (String players : warData.getConfig().getConfigurationSection("war.pending.island2." + island + ".players").getKeys(false)) {
            if (Bukkit.getPlayer(UUID.fromString(players)) != null) {
                Bukkit.getPlayer(UUID.fromString(players)).sendMessage("not everybody accepted this war invite");
            }
        }
        warData.getConfig().set("war.pending.island2." + island, null);

//        list.remove(island);
//        warData.getConfig().set("war.pending.memberPendingListNewIsland", list);

        warData.saveConfig();

    }
}
