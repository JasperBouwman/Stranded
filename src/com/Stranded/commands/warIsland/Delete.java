package com.Stranded.commands.warIsland;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;
import static com.Stranded.GettingFiles.getFiles;

public class Delete extends CmdManager {
    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //warIsland delete <theme> (this return all the IDs for this theme)
        //warIsland delete <theme> <war island ID>

        Files warIslands = getFiles("warIslands.yml");
        Files config = getFiles("config.yml");
        if (args.length == 2) {
            if (warIslands.getConfig().contains("warIslands.island." + args[1])) {
                StringBuilder str = new StringBuilder();

                for (String s : warIslands.getConfig().getConfigurationSection("warIslands.island." + args[1]).getKeys(false)) {
                    str.append(s);
                    str.append(" ");
                }

                player.sendMessage("there are the IDs for this war island theme: " + str.toString());

            } else {
                player.sendMessage("this theme doesn't exist");
            }
        }
        else if (args.length == 3) {

            String theme = args[1];
            String warIslandID = args[2];
            if (!warIslands.getConfig().contains("warIslands.island." + theme)) {
                player.sendMessage("this theme doesn't exist");
                return;
            }
            if (!warIslands.getConfig().contains("warIslands.island." + theme + "." + warIslandID)) {
                player.sendMessage("this war island id doesn't exist");
                return;
            }
            if (warIslands.getConfig().getBoolean("warIslands.island." + theme + "." + warIslandID + ".inUse")) {
                player.sendMessage("this island is now in use, you can remove this when its not in use");
            } else {

                String blueUUID = warIslands.getConfig().getString("warIslands.island." + theme + "." + warIslandID + ".nexus.blue");
                String redUUID = warIslands.getConfig().getString("warIslands.island." + theme + "." + warIslandID + ".nexus.red");
                String blueASUUID = warIslands.getConfig().getString("warIslands.island." + theme + "." + warIslandID + ".armorStand.blue");
                String redASUUID = warIslands.getConfig().getString("warIslands.island." + theme + "." + warIslandID + ".armorStand.red");

                ArrayList<String> list = (ArrayList<String>) config.getConfig().getStringList("nexus.uuid");
                if (list.contains(blueUUID)) {
                    list.remove(blueUUID);
                }
                if (list.contains(redUUID)) {
                    list.remove(redUUID);
                }
                config.getConfig().set("nexus.uuid", list);
                config.saveConfig();

                Entity blue = Bukkit.getEntity(UUID.fromString(blueUUID));
                Entity red = Bukkit.getEntity(UUID.fromString(redUUID));
                Entity blueAS = Bukkit.getEntity(UUID.fromString(blueASUUID));
                Entity redAS = Bukkit.getEntity(UUID.fromString(redASUUID));

                blue.remove();
                red.remove();
                blueAS.remove();
                redAS.remove();

                warIslands.getConfig().set("warIslands.island." + theme + "." + warIslandID, null);

                if (warIslands.getConfig().getConfigurationSection("warIslands.island." + theme).getKeys(false).size() == 0) {
                    warIslands.getConfig().set("warIslands.island." + theme, null);
                }

                warIslands.saveConfig();
                player.sendMessage("this war island is now removed, this means not that the island is removed from the world. you have to do that manually if you wish");
            }
        } else {
            player.sendMessage("wrong use");
        }
    }
}
