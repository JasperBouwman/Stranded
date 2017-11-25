package com.Stranded.commands.warIsland;

import com.Stranded.Files;
import com.Stranded.commands.CmdManager;
import com.Stranded.commands.warIsland.edit.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.Stranded.towers.inventory.InventoryEvent.toItemStack;

public class Edit extends CmdManager {

    public List<CmdManager> actions = new ArrayList<>();

    public Edit() {
        actions.add(new BlueSpawn());
        actions.add(new RedSpawn());
        actions.add(new MaxPlayers());
        actions.add(new MinPlayers());
        actions.add(new Teleport());
    }

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        if (!player.hasPermission("Stranded.editWarIsland")) {
            player.sendMessage("you don't have permission");
            return;
        }

        Files warIslands = new Files(p, "warIslands.yml");

        //warIsland edit <theme> (this return all the IDs for this theme)
        //warIsland edit <theme> <war island id> teleport
        //warIsland edit <theme> <war island id> teleport <blue:red>
        //warIsland edit <theme> <war island id> maxPlayers
        //warIsland edit <theme> <war island id> minPlayers
        //warIsland edit <theme> <war island id> blueSpawn
        //warIsland edit <theme> <war island id> redSpawn

        if (args.length == 1) {
            player.sendMessage("wrong use");
            return;
        }

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

        if (args.length == 3) {

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

            String[] lore = new String[]{"Theme: " + theme, "WarIslandID: " + warIslandID};

            ItemStack is = toItemStack(Material.STICK, 0, "WarIsland Boundary Shower", lore);

            player.getInventory().addItem(is);

            player.sendMessage("you just got a warIsland pos shower, when you have this item in your main hand you will see the boundries of that island");
        }

        if (args.length > 3) {
            if (warIslands.getConfig().contains("warIslands.island." + args[1])) {
                if (warIslands.getConfig().contains("warIslands.island." + args[1] + "." + args[2])) {
                    for (CmdManager action : this.actions) {
                        if (args[3].toLowerCase().equals(action.getName()) || args[3].toLowerCase().equals(action.getAlias())) {
                            action.setMain(p);
                            action.run(args, player);
                            return;
                        }
                    }
                } else {
                    player.sendMessage("this war island ID doesn't exist");
                }
            } else {
                player.sendMessage("this theme doesn't exist");
            }
        }
    }
}
