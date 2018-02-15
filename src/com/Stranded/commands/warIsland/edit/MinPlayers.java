package com.Stranded.commands.warIsland.edit;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.entity.Player;
import static com.Stranded.GettingFiles.getFiles;

public class MinPlayers extends CmdManager {
    @Override
    public String getName() {
        return "minplayers";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {
        //warIsland edit <theme> <war island ID> minPlayers <integer>

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

            int max = warIslands.getConfig().getInt("warIslands.island." + theme + "." + warIslandID + ".minPlayers");

            player.sendMessage("the minPlayers value is now " + max + " players");

        } else if (args.length == 5) {
            int min;
            try {
                min = Integer.parseInt(args[4]);
            } catch (NumberFormatException e) {
                player.sendMessage("please use a number");
                return;
            }

            String theme = args[1];
            String warIslandID = args[2];

            int max = warIslands.getConfig().getInt("warIslands.island." + theme + "." + warIslandID + ".maxPlayers");

            if (max < min) {
                player.sendMessage("you cant go higher than " + max + " players");
                return;
            }

            warIslands.getConfig().set("warIslands.island." + theme + "." + warIslandID + ".minPlayers", min);
            warIslands.saveConfig();

            player.sendMessage("the minPlayers value has been edited");
        } else {
            player.sendMessage("wrong use");
        }
    }
}
