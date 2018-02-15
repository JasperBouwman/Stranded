package com.Stranded.commands.warIsland.edit;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import static com.Stranded.GettingFiles.getFiles;

public class Teleport extends CmdManager {
    @Override
    public String getName() {
        return "teleport";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {
        //warIsland edit <theme> <war island ID> teleport
        //warIsland edit <theme> <war island ID> teleport <blue:red>

        Files warIslands = getFiles("warIslands.yml");

        if (args.length == 4) {

            String theme = args[1];
            String warIslandID = args[2];

            if (!warIslands.getConfig().contains("warIslands.island." + theme)) {
                player.sendMessage("this war theme doesn't exist");
                return;
            }

            if (!warIslands.getConfig().contains("warIslands.island." + theme + "." + warIslandID)) {
                player.sendMessage("this war island id doesn't exist");
                return;
            }

            player.teleport((Location) warIslands.getConfig().get("warIslands.island." + theme + "." + warIslandID + ".spawn.blue"));
            player.sendMessage("you have been teleported to the blue spawn from your chosen island");
        } else if (args.length == 5) {

            if (!args[4].equalsIgnoreCase("blue") && !args[4].equalsIgnoreCase("red")) {
                player.sendMessage("you must choose between the red or blue spawn");
                return;
            }

            String theme = args[1];
            String warIslandID = args[2];

            if (!warIslands.getConfig().contains("warIslands.island." + theme)) {
                player.sendMessage("this war theme doesn't exist");
                return;
            }

            if (!warIslands.getConfig().contains("warIslands.island." + theme + "." + warIslandID)) {
                player.sendMessage("this war island id doesn't exist");
                return;
            }

            player.teleport((Location) warIslands.getConfig().get("warIslands.island." + theme + "." + warIslandID + ".spawn." + args[4]));
            player.sendMessage("you have been teleported to the " + args[4] + " spawn from your chosen island");

        } else {
            player.sendMessage("wrong use");
        }
    }
}
