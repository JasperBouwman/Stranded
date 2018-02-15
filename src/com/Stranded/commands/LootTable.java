package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.commands.lootTable.Add;
import com.Stranded.commands.lootTable.Edit;
import com.Stranded.commands.lootTable.Remove;
import com.Stranded.commands.lootTable.Rename;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LootTable implements CommandExecutor {

    public List<CmdManager> actions = new ArrayList<>();
    private Main p;

    public LootTable(Main main) {
        p = main;
        actions.add(new Add());
        actions.add(new Edit());
        actions.add(new Remove());
        actions.add(new Rename());
    }

    // lootTable
    // lootTable add <name>
    // lootTable remove <name>
    // lootTable rename <name> <newName>

    // lootTable edit <name> addPool <poolName> [rolls=1,3,equal]
    // lootTable edit <name> removePool <poolName>
    // lootTable edit <name> renamePool <poolName> <newName>

    // lootTable edit <name> editPool <poolName> editRolls max <Integer>
    // lootTable edit <name> editPool <poolName> editRolls min <Integer>
    // lootTable edit <name> editPool <poolName> editRolls chance [equal, low, lower, high, higher]

    // lootTable edit <name> editPool <poolName> addTower <name> <tower> [chance=1,3,equal] [amount=1,3,equal] [level=1,3,equal]
    // lootTable edit <name> editPool <poolName> removeTower <name>
    // lootTable edit <name> editPool <poolName> editTower
    // lootTable edit <name> editPool <poolName> editTower <name> chance max <Integer>
    // lootTable edit <name> editPool <poolName> editTower <name> chance min <Integer]>
    // lootTable edit <name> editPool <poolName> editTower <name> chance chance [equal, low, lower, high, higher]
    // lootTable edit <name> editPool <poolName> editTower <name> amount max <Integer>
    // lootTable edit <name> editPool <poolName> editTower <name> amount min <Integer>
    // lootTable edit <name> editPool <poolName> editTower <name> amount chance [equal, low, lower, high, higher]
    // lootTable edit <name> editPool <poolName> editTower <name> level max <Integer>
    // lootTable edit <name> editPool <poolName> editTower <name> level min <Integer]>
    // lootTable edit <name> editPool <poolName> editTower <name> level chance [equal, low, lower, high, higher]
    // lootTable edit <name> editPool <poolName> editTower <name> type <towerType:random>

    // lootTable edit <name> editPool <poolName> addItem <name> <material> [chance=1,3,equal] [amount=1,3,equal] [damage=0]
    // lootTable edit <name> editPool <poolName> removeItem <name>
    // lootTable edit <name> editPool <poolName> editItem
    // lootTable edit <name> editPool <poolName> editItem <name> material <material>
    // lootTable edit <name> editPool <poolName> editItem <name> chance max <Integer>
    // lootTable edit <name> editPool <poolName> editItem <name> chance min <Integer>
    // lootTable edit <name> editPool <poolName> editItem <name> chance chance [equal, low, lower, high, higher]
    // lootTable edit <name> editPool <poolName> editItem <name> amount max <Integer>
    // lootTable edit <name> editPool <poolName> editItem <name> amount min <Integer>
    // lootTable edit <name> editPool <poolName> editItem <name> amount chance [equal, low, lower, high, higher]
    // lootTable edit <name> editPool <poolName> editItem <name> damage <Integer>

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {

        if (!(commandSender instanceof Player)) {
            return false;
        }
        Player player = (Player) commandSender;

        if (args.length == 0) {

            StringBuilder str = new StringBuilder();

            File[] listOfFiles = new File(p.getDataFolder() + "/lootTables").listFiles();

            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        String[] s = file.getName().split("\\.");
                        if (file.getName().endsWith(".yml")) {
                            str.append(Arrays.asList(s).subList(0, s.length - 1)).append(" ");
                        }
                    }
                }
                player.sendMessage("these are all the loot tables you can use: " + str.toString());
            } else {
                player.sendMessage("there are no loot tables");
            }
            return false;
        }

        for (CmdManager action : this.actions) {
            if (args[0].toLowerCase().equals(action.getName()) || args[0].toLowerCase().equals(action.getAlias())) {
                action.setMain(p);
                action.run(args, player);
                return false;
            }
        }
        player.sendMessage("this is not a sub-command");
        return false;
    }
}
