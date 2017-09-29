package com.Stranded.commands.war.util;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.towers.TowerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class EndWar {
    private String warWinMsg;
    private String warWinMsgOffline;
    private String warLoseMsg;
    private String warLoseMsgOffline;

    private ArrayList<String> islandWin;
    private ArrayList<String> islandLose;
    private String islandWinName;
    private String islandLoseName;
    private Files warData;

    public static void testPlayers(Main p, String warID, String side) {

        Files warData = new Files(p, "warData.yml");
        Files warIslands = new Files(p, "warIslands.yml");

        String theme = warData.getConfig().getString("war.war." + warID + ".warIsland");
        int warIslandID = warData.getConfig().getInt("war.war." + warID + ".warIsland." + theme);

        int min = warIslands.getConfig().getInt("warIslands.island" + theme + "." + warIslandID + ".minPlayers");
        int playersCount = 0;

        for (String players : warData.getConfig().getStringList("war.war." + warID + "." + side + ".players")) {
            if (Bukkit.getPlayerExact(players) != null) {
                playersCount++;
            }
        }
        if (playersCount < min) {
            new EndWar().endWar(p, warID, 1, side.equals("blue") ? "blue" : "red");
        }
        if (playersCount == min) {
            for (String players : warData.getConfig().getStringList("war.war." + warID + "." + side + ".players")) {
                if (Bukkit.getPlayerExact(players) != null) {
                    Bukkit.getPlayerExact(players).sendMessage("nobody can leave anymore, your island has reached the minimum amount of players for this island");
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

        if (reason == 1) {
            initReason1();
            if (islandWin.equals("blue")) {
                this.islandWin = (ArrayList<String>) warData.getConfig().getStringList("war.war." + warID + ".blue.players");
                this.islandLose = (ArrayList<String>) warData.getConfig().getStringList("war.war." + warID + ".red.players");
                this.islandWinName = warData.getConfig().getString("war.war." + warID + ".blue.islandName");
                this.islandLoseName = warData.getConfig().getString("war.war." + warID + ".red.islandName");
            } else if (islandWin.equals("red")) {
                this.islandWin = (ArrayList<String>) warData.getConfig().getStringList("war.war." + warID + ".red.players");
                this.islandLose = (ArrayList<String>) warData.getConfig().getStringList("war.war." + warID + ".blue.players");
                this.islandWinName = warData.getConfig().getString("war.war." + warID + ".red.islandName");
                this.islandLoseName = warData.getConfig().getString("war.war." + warID + ".blue.islandName");
            }
        } else if (reason == 2) {
            initReason2();
            if (islandWin.equals("blue")) {
                this.islandWin = (ArrayList<String>) warData.getConfig().getStringList("war.war." + warID + ".blue.players");
                this.islandLose = (ArrayList<String>) warData.getConfig().getStringList("war.war." + warID + ".red.players");
                this.islandWinName = warData.getConfig().getString("war.war." + warID + ".blue.islandName");
                this.islandLoseName = warData.getConfig().getString("war.war." + warID + ".red.islandName");
            } else if (islandWin.equals("red")) {
                this.islandWin = (ArrayList<String>) warData.getConfig().getStringList("war.war." + warID + ".red.players");
                this.islandLose = (ArrayList<String>) warData.getConfig().getStringList("war.war." + warID + ".blue.players");
                this.islandWinName = warData.getConfig().getString("war.war." + warID + ".red.islandName");
                this.islandLoseName = warData.getConfig().getString("war.war." + warID + ".blue.islandName");
            }
        }


        this.warData = new Files(p, "warData.yml");
        Files islands = new Files(p, "islands.yml");
        Files warIslands = new Files(p, "warIslands.yml");

        String islandWinName = p.getConfig().getString("island." + this.islandWinName);
        String islandLoseName = p.getConfig().getString("island." + this.islandLoseName);

        Location islandWinLocation = (Location) islands.getConfig().get("island." + islandWinName + ".home");
        Location islandLoseLocation = (Location) islands.getConfig().get("island." + islandLoseName + ".home");

        for (String s : this.islandWin) {

            if (Bukkit.getPlayerExact(s) != null) {
                Player player = Bukkit.getPlayer(s);

                player.sendMessage(warWinMsg.replace("%islandLoseName%", islandLoseName));
                player.setLevel(player.getLevel() + 30);
                player.teleport(islandWinLocation);
            } else {

                p.getConfig().set("online.warWin." + s, warWinMsgOffline.replace("%islandLoseName%", islandLoseName));
                p.saveConfig();

            }
        }

        for (String s : islandLose) {

            if (Bukkit.getPlayerExact(s) != null) {
                Player player = Bukkit.getPlayer(s);

                player.sendMessage(warLoseMsg.replace("%islandWinName%", islandWinName));
                player.teleport(islandLoseLocation);
            } else {
                p.getConfig().set("online.warLose." + s, warLoseMsgOffline.replace("%islandWinName%", islandWinName));
                p.saveConfig();
            }
        }

        for (String towerID : warData.getConfig().getConfigurationSection("war.war." + warID + ".towers.blue").getKeys(false)) {
            TowerManager.removeWarTower(warData, warID, towerID, "blue");
        }
        for (String towerID : warData.getConfig().getConfigurationSection("war.war." + warID + ".towers.red").getKeys(false)) {
            TowerManager.removeWarTower(warData, warID, towerID, "red");
        }

        String theme = warData.getConfig().getString("war.war." + warID + ".warIsland");
        String warIslandID = warData.getConfig().getString("war.war." + warID + ".warIsland." + theme);
        warIslands.getConfig().set("warIslands.island." + theme + "." + warIslandID + ".inUse", false);

        warData.getConfig().set("war.war." + warID, null);

    }

    private void initReason1() {
        this.warWinMsg = "your island just won the war against %islandLoseName%\n" +
                "you win 30 xp and you can retrieve your towers at half of their lvl using /towers\n" +
                "the other island had to few players to play with";
        this.warWinMsgOffline = "your island had won the war against %islandLoseName%\n" +
                "you can retrieve your towers at half of their lvl using /towers\n" +
                "the other island had to few players to play with";

        this.warLoseMsg = "your island just lost the war against %islandWinName%\n" +
                "you didn't win any xp and you can retrieve your towers at lvl 0 using /towers\n" +
                "your island had to few players to play with";
        this.warLoseMsgOffline = "your island had lost the war against %islandWinName%\n" +
                "you can retrieve your towers at lvl 0 using /towers\n" +
                "your island had to few players to play with";
    }

    private void initReason2() {
        this.warWinMsg = "your island just won the war against %islandLoseName%\n" +
                "you win 30 xp and you can retrieve your towers at their lvl using /towers";
        this.warWinMsgOffline = "your island had won the war against %islandLoseName%\n" +
                "you can retrieve your towers at their lvl using /towers";

        this.warLoseMsg = "your island just lost the war against %islandWinName%\n" +
                "you didn't win any xp and you can retrieve your towers at lvl 0 using /towers";
        this.warLoseMsgOffline = "your island had lost the war against %islandWinName%\n" +
                "you can retrieve your towers at lvl 0 using /towers";
    }
}
