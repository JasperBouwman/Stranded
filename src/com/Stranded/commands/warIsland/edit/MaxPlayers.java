package com.Stranded.commands.warIsland.edit;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.entity.Player;

public class MaxPlayers extends CmdManager {
    @Override
    public String getName() {
        return "maxplayers";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {
        //warIsland edit <theme> <war island ID> maxPlayers <integer>

        Files warIslands = new Files(p, "warIslands.yml");

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

            int max = warIslands.getConfig().getInt("warIslands.island." + theme + "." + warIslandID + ".maxPlayers");

            player.sendMessage("the maxPlayers value is now " + max + " players");

        } else if (args.length == 5) {
            int max;
            try {
                max = Integer.parseInt(args[4]);
            } catch (NumberFormatException e) {
                player.sendMessage("please use a number");
                return;
            }

            String theme = args[1];
            String warIslandID = args[2];

            int min = warIslands.getConfig().getInt("warIslands.island." + theme + "." + warIslandID + ".minPlayers");

            if (min > max) {
                player.sendMessage("you cant go lower than " + min + " players");
                return;
            }

            warIslands.getConfig().set("warIslands.island." + theme + "." + warIslandID + ".maxPlayers", max);
            warIslands.saveConfig();

            player.sendMessage("the maxPlayers value has been edited");
        } else {
            player.sendMessage("wrong use");
        }
    }
}
