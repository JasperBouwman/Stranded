package com.Stranded.commands;

import com.Stranded.Main;
import com.Stranded.commands.lootTable.*;
import com.Stranded.commands.lootTable.Edit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LootTable implements CommandExecutor {
    
    private Main p;
    public List<CmdManager> actions = new ArrayList<>();
    
    public LootTable(Main main) {
        p = main;
        actions.add(new Add());
        actions.add(new Edit());
        actions.add(new Remove());
        actions.add(new Rename());
    }

    // lootTable /*get all lootTables*/
    // lootTable add <name>
    // lootTable remove <name>
    // lootTable rename <name> <newName>

    // lootTable edit <name> addPool <poolName> [rollChance /*default 100*/]
    // lootTable edit <name> removePool <poolName>
    // lootTable edit <name> renamePool <poolName> <newName>

    // lootTable edit <name> editPool <poolName> editChance max <Integer [<101]>
    // lootTable edit <name> editPool <poolName> editChance min <Integer [>-1]>
    // lootTable edit <name> editPool <poolName> editChance chance [equal, low, lower, high, higher]

    // lootTable edit <name> editPool <poolName> addTower <name> <tower> /*chance:[max=100,min=0],amount:[max=64,min=0],damage:0*/
    // lootTable edit <name> editPool <poolName> removeTower <name>
    // lootTable edit <name> editPool <poolName> editTower <name> chance max <Integer [<101]>
    // lootTable edit <name> editPool <poolName> editTower <name> chance min <Integer [>-1]>
    // lootTable edit <name> editPool <poolName> editTower <name> chance chance [equal, low, lower, high, higher]
    // lootTable edit <name> editPool <poolName> editTower <name> amount max <Integer [< maxStack]>
    // lootTable edit <name> editPool <poolName> editTower <name> amount min <Integer [>-1]>
    // lootTable edit <name> editPool <poolName> editTower <name> amount chance [equal, low, lower, high, higher]
    // lootTable edit <name> editPool <poolName> editTower <name> level max <Integer [< maxStack]>
    // lootTable edit <name> editPool <poolName> editTower <name> level min <Integer [>-1]>
    // lootTable edit <name> editPool <poolName> editTower <name> level chance [equal, low, lower, high, higher]
    // lootTable edit <name> editPool <poolName> editTower <name> type <towerType:random>

    // lootTable edit <name> editPool <poolName> addItem <name> <material> /*chance:[max=100,min=0],amount:[max=64,min=0],damage:0*/
    // lootTable edit <name> editPool <poolName> removeItem <name>
    // lootTable edit <name> editPool <poolName> editItem <name> material <material>
    // lootTable edit <name> editPool <poolName> editItem <name> chance max <Integer [<101]>
    // lootTable edit <name> editPool <poolName> editItem <name> chance min <Integer [<-1]>
    // lootTable edit <name> editPool <poolName> editItem <name> chance chance [equal, low, lower, high, higher]
    // lootTable edit <name> editPool <poolName> editItem <name> amount max <Integer [< maxStack]>
    // lootTable edit <name> editPool <poolName> editItem <name> amount min <Integer [>-1]>
    // lootTable edit <name> editPool <poolName> editItem <name> amount chance [equal, low, lower, high, higher]
    // lootTable edit <name> editPool <poolName> editItem <name> damage <Integer [<16]>
    
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
