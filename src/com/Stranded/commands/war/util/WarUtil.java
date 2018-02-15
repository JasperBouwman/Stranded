package com.Stranded.commands.war.util;

import com.Stranded.Files;
import com.Stranded.GettingFiles;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static com.Stranded.GettingFiles.getFiles;

public class WarUtil {

    public static boolean testIfIsInWarWithComments(Player player) {

        String uuid = player.getUniqueId().toString();

        Files warData = getFiles("warData.yml");
        Files config = getFiles("config.yml");
        String ownIslandName = config.getConfig().getString("island." + uuid);

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

    public static int testIfIsInWar(Player player) {
        String ownIslandName = getFiles("config.yml").getConfig().getString("island." + player.getUniqueId().toString());
        return testIfIsInWar(ownIslandName);
    }

    public static int testIfIsInWar(String island) {

        Files warData = getFiles("warData.yml");

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

    public static boolean testIfPlayerIsInWarFast(Player player) {
        Files config = getFiles("config.yml");
        return config.getConfig().getStringList("playersInWar").contains(player.getUniqueId().toString());
    }

    public static int testIfPlayerIsInWar(Player player) {

        String uuid = player.getUniqueId().toString();

        Files warData = getFiles("warData.yml");
        Files config = getFiles("config.yml");

        ArrayList<String> list = (ArrayList<String>) config.getConfig().getStringList("playersInWar");
        if (list.contains(uuid)) {
            for (String warID : warData.getConfig().getConfigurationSection("war.war").getKeys(false)) {
                if (warData.getConfig().getStringList("war.war." + warID + ".blue.players").contains(uuid)) {
                    return 1;
                }
                if (warData.getConfig().getStringList("war.war." + warID + ".red.players").contains(uuid)) {
                    return 1;
                }
            }
        }
        return 0;
    }

}
