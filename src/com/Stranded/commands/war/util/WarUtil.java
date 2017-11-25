package com.Stranded.commands.war.util;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class WarUtil {

    public static boolean testIfIsInWarWithComments(Main p, Player player) {

        String uuid = player.getUniqueId().toString();

        Files warData = new Files(p, "warData.yml");
        String ownIslandName = p.getConfig().getString("island." + uuid);

        if (warData.getConfig().contains("war.pending.island1." + ownIslandName)) {
            player.sendMessage("your island is already trying to start a war");
            return true;
        }
        if (warData.getConfig().contains("war.pending.island2." + ownIslandName)) {
            player.sendMessage("your island is already trying to start a war");
            return true;
        }

        for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {

            if (warData.getConfig().getString("war.war." + warID + ".blue.islandName").equals(ownIslandName)) {
                player.sendMessage("your island is already in a war");
                return true;
            }
            if (warData.getConfig().getString("war.war." + warID + ".red.islandName").equals(ownIslandName)) {
                player.sendMessage("your island is already in a war");
                return true;
            }
        }
        return false;
    }

    public static int testIfIsInWar(Main p, Player player) {
        String ownIslandName = p.getConfig().getString("island." + player.getUniqueId().toString());
        return testIfIsInWar(p, ownIslandName);
    }

    public static int testIfIsInWar(Main p, String island) {

        Files warData = new Files(p, "warData.yml");

        if (warData.getConfig().contains("war.pending.island1." + island)) {
            return 1;
        }
        if (warData.getConfig().contains("war.pending.island2." + island)) {
            return 1;
        }

        for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {

            if (warData.getConfig().getString("war.war." + warID + ".blue.islandName").equals(island)) {
                return 2;
            }
            if (warData.getConfig().getString("war.war." + warID + ".red.islandName").equals(island)) {
                return 2;
            }
        }
        return 0;
    }

    public static int testIfPlayerIsInWar(Main p, Player player) {

        String uuid = player.getUniqueId().toString();

        Files warData = new Files(p, "warData.yml");

        ArrayList<String> list = (ArrayList<String>) p.getConfig().getStringList("playersInWar");
        if (list.contains(uuid)) {
            for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
                if (warData.getConfig().getStringList("war.war." + warID + ".blue.players").contains(uuid)) {
                   return  1;
                }
                if (warData.getConfig().getStringList("war.war." + warID + ".red.players").contains(uuid)) {
                   return 1;
                }
            }
        }
        return 0;
    }


}
