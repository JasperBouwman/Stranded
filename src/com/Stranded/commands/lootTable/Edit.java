package com.Stranded.commands.lootTable;

import com.Stranded.commands.CmdManager;
import com.Stranded.commands.lootTable.edit.AddPool;
import com.Stranded.commands.lootTable.edit.EditPool;
import com.Stranded.commands.lootTable.edit.RemovePool;
import com.Stranded.commands.lootTable.edit.RenamePool;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Edit extends CmdManager {

    public List<CmdManager> actions = new ArrayList<>();

    public Edit() {
        actions.add(new AddPool());
        actions.add(new EditPool());
        actions.add(new RemovePool());
        actions.add(new RenamePool());
    }

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String getAlias() {
        return null;
    }

    // lootTable edit <name> addPool <poolName> [chance:[max=100,min=0]
    // lootTable edit <name> removePool <poolName>
    // lootTable edit <name> renamePool <poolName> <newName>

    // lootTable edit <name> editPool <poolName> editChance max <Integer [<101]>
    // lootTable edit <name> editPool <poolName> editChance min <Integer [>-1]>
    // lootTable edit <name> editPool <poolName> editChance chance [equal, low, lower, high, higher]

    // lootTable edit <name> editPool <poolName> addTower <name> <tower> chance:[max=100,min=0],amount:[max=64,min=0],damage:0
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

    // lootTable edit <name> editPool <poolName> addItem <name> <material> chance:[max=100,min=0],amount:[max=64,min=0],damage:0
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
    public void run(String[] args, Player player) {

        if (args.length > 2) {
            for (CmdManager action : actions) {
                if (action.getName().equalsIgnoreCase(args[3]) || action.getAlias().equalsIgnoreCase(args[3])) {
                    action.setMain(p);
                    action.run(args, player);
                    return;
                }
            }
            player.sendMessage("this sub-command doesn't exist");
        } else {
            player.sendMessage("wrong use");
        }

    }
}

















