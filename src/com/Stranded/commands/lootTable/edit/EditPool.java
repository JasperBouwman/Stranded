package com.Stranded.commands.lootTable.edit;

import com.Stranded.commands.CmdManager;
import com.Stranded.commands.lootTable.Rename;
import com.Stranded.commands.lootTable.edit.editPool.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EditPool extends CmdManager {

    public List<CmdManager> actions = new ArrayList<>();

    public EditPool() {
        actions.add(new AddItem());
        actions.add(new AddTower());
        actions.add(new EditChance());
        actions.add(new EditItem());
        actions.add(new EditTower());
        actions.add(new RemoveItem());
        actions.add(new RemoveTower());
    }

    @Override
    public String getName() {
        return "editPool".toLowerCase();
    }

    @Override
    public String getAlias() {
        return null;
    }

    // lootTable edit <name> editPool <poolName> editChance max <Integer [<101]>
    // lootTable edit <name> editPool <poolName> editChance min <Integer [<-1]>
    // lootTable edit <name> editPool <poolName> editChance chance [equal, low, lower, high, higher]

    // lootTable edit <name> editPool <poolName> addTower <name> <tower> /*chance:[max=100,min=0],amount:[max=64,min=0],damage:0*/
    // lootTable edit <name> editPool <poolName> removeTower <name>
    // lootTable edit <name> editPool <poolName> editTower <name> chance max <Integer [<101]>
    // lootTable edit <name> editPool <poolName> editTower <name> chance min <Integer [<-1]>
    // lootTable edit <name> editPool <poolName> editTower <name> chance chance [equal, low, lower, high, higher]
    // lootTable edit <name> editPool <poolName> editTower <name> amount max <Integer [< maxStack]>
    // lootTable edit <name> editPool <poolName> editTower <name> amount min <Integer [>-1]>
    // lootTable edit <name> editPool <poolName> editTower <name> amount chance [equal, low, lower, high, higher]
    // lootTable edit <name> editPool <poolName> editTower <name> level max <Integer [< maxStack]>
    // lootTable edit <name> editPool <poolName> editTower <name> level min <Integer [>-1]>
    // lootTable edit <name> editPool <poolName> editTower <name> level chance [equal, low, lower, high, higher]

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
    public void run(String[] args, Player player) {

        if (args.length > 5) {
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
