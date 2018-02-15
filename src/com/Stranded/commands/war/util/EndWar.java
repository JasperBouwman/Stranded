package com.Stranded.commands.war.util;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.towers.RemoveTower.removeWarTower;

public class EndWar {
    private String warWinMsg;
    private String warWinMsgOffline;
    private String warLoseMsg;
    private String warLoseMsgOffline;

    public static void testPlayers(Main p, String warID, String side) {

        Files warData = getFiles("warData.yml");
        Files warIslands = getFiles("warIslands.yml");

        String theme = (String) warData.getConfig().getConfigurationSection("war.war." + warID + ".warIsland").getKeys(false).toArray()[0];
        int warIslandID = warData.getConfig().getInt("war.war." + warID + ".warIsland." + theme);

        int min = warIslands.getConfig().getInt("warIslands.island." + theme + "." + warIslandID + ".minPlayers");
        int playersCount = 0;

        for (String players : warData.getConfig().getStringList("war.war." + warID + "." + side + ".players")) {
            if (Bukkit.getPlayer(UUID.fromString(players)) != null) {
                playersCount++;
            }
        }

        if (playersCount <= min) {
            new EndWar().endWar(p, warID, 1, side.equals("blue") ? "red" : "blue");
            return;
        }
        if (playersCount == min + 1) {
            for (String players : warData.getConfig().getStringList("war.war." + warID + "." + side + ".players")) {
                if (Bukkit.getPlayer(UUID.fromString(players)) != null) {
                    Bukkit.getPlayer(UUID.fromString(players)).sendMessage("nobody can leave anymore, your island has reached the minimum amount of players for this island");
                }
            }
        }
    }

    public void endWar(Main p, String warID, int reason, String islandWin) {
        /*
         * reason
         * 1: to few players
         * 2: nexus killed
         */
        Files warData = getFiles("warData.yml");

        if (reason == 1) {
            initReason1();
        } else if (reason == 2) {
            initReason2();
        }

        String islandWinSide;
        String islandLoseSide;

        if (islandWin.equals("blue")) {
            islandWinSide = islandWin;
            islandLoseSide = "red";
        } else {
            islandWinSide = islandWin;
            islandLoseSide = "blue";
        }

        ArrayList<String> islandWinPlayerList = (ArrayList<String>) warData.getConfig().getStringList("war.war." + warID + "." + islandWinSide + ".players");
        ArrayList<String> islandLosePlayerList = (ArrayList<String>) warData.getConfig().getStringList("war.war." + warID + "." + islandLoseSide + ".players");
        String islandWinName = warData.getConfig().getString("war.war." + warID + "." + islandWinSide + ".islandName");
        String islandLoseName = warData.getConfig().getString("war.war." + warID + "." + islandLoseSide + ".islandName");

        Files islands = getFiles("islands.yml");
        Files warIslands = getFiles("warIslands.yml");
        Files config = getFiles("config.yml");

        Location islandWinLocation = (Location) islands.getConfig().get("island." + islandWinName + ".home");
        Location islandLoseLocation = (Location) islands.getConfig().get("island." + islandLoseName + ".home");

//        double wonXp = islands.getConfig().getDouble("island." + islandLoseName + ".nexusLvl") * 0.75 + 1;

        for (String s : islandWinPlayerList) {

            if (Bukkit.getPlayer(UUID.fromString(s)) != null) {
                Player player = Bukkit.getPlayer(UUID.fromString(s));

                double wonXp = islands.getConfig().getDouble("island." + islandLoseName + ".nexusLvl") * 0.5 + 1;

                int kills = warData.getConfig().getInt("war.war." + warID + "." + islandWinSide + ".kills." + s);
                int killStreak = warData.getConfig().getInt("war.war." + warID + "." + islandWinSide + ".killStreak." + s);

                wonXp += (kills + killStreak) / 2;

                player.sendMessage(warWinMsg.replace("%islandLoseName%", islandLoseName).replace("%wonXp%", wonXp + ""));
                player.setLevel((int) wonXp);
                player.teleport(islandWinLocation);
            } else {

                config.getConfig().set("online.warWin." + s, warWinMsgOffline.replace("%islandLoseName%", islandLoseName));
                config.saveConfig();

            }
        }

        for (String s : islandLosePlayerList) {

            if (Bukkit.getPlayer(UUID.fromString(s)) != null) {
                Player player = Bukkit.getPlayer(UUID.fromString(s));

                player.sendMessage(warLoseMsg.replace("%islandWinName%", islandWinName));
                player.teleport(islandLoseLocation);
            } else {
                config.getConfig().set("online.warLose." + s, warLoseMsgOffline.replace("%islandWinName%", islandWinName));
                config.saveConfig();
            }
        }
        for (String side : warData.getConfig().getConfigurationSection("war.war." + warID + ".towers").getKeys(false)) {
            for (String towerID : warData.getConfig().getConfigurationSection("war.war." + warID + ".towers." + side).getKeys(false)) {
                removeWarTower(warData, warID, towerID, side);
            }
        }

        ArrayList<String> list = (ArrayList<String>) config.getConfig().getStringList("playersInWar");
        list.removeAll(islandLosePlayerList);
        list.removeAll(islandWinPlayerList);
        config.getConfig().set("playersInWar", list);
        config.saveConfig();

        String theme = (String) warData.getConfig().getConfigurationSection("war.war." + warID + ".warIsland").getKeys(false).toArray()[0];
        String warIslandID = warData.getConfig().getString("war.war." + warID + ".warIsland." + theme);
        warIslands.getConfig().set("warIslands.island." + theme + "." + warIslandID + ".inUse", false);

        warData.getConfig().set("war.war." + warID, null);
        warData.saveConfig();
    }

    private void initReason1() {
        this.warWinMsg = "your island just won the war against %islandLoseName%\n" +
                "you win %wonXp% xp and you can retrieve your towers at half of their lvl using /tower\n" +
                "the other island had to few players to play with";
        this.warWinMsgOffline = "your island had won the war against %islandLoseName%\n" +
                "you can retrieve your towers at half of their lvl using /tower\n" +
                "the other island had to few players to play with";

        this.warLoseMsg = "your island just lost the war against %islandWinName%\n" +
                "you didn't win any xp and you can retrieve your towers at lvl 0 using /tower\n" +
                "your island had to few players to play with";
        this.warLoseMsgOffline = "your island had lost the war against %islandWinName%\n" +
                "you can retrieve your towers at lvl 0 using /tower\n" +
                "your island had to few players to play with";
    }

    private void initReason2() {
        this.warWinMsg = "your island just won the war against %islandLoseName%\n" +
                "you win %wonXp% xp and you can retrieve your towers at their lvl using /towers";
        this.warWinMsgOffline = "your island had won the war against %islandLoseName%\n" +
                "you can retrieve your towers at their lvl using /towers";

        this.warLoseMsg = "your island just lost the war against %islandWinName%\n" +
                "you didn't win any xp and you can retrieve your towers at lvl 0 using /towers";
        this.warLoseMsgOffline = "your island had lost the war against %islandWinName%\n" +
                "you can retrieve your towers at lvl 0 using /towers";
    }
}
