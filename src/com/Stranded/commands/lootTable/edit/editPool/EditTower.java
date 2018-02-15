package com.Stranded.commands.lootTable.edit.editPool;

import com.Stranded.commands.CmdManager;
import org.bukkit.entity.Player;

public class EditTower extends CmdManager {
    @Override
    public String getName() {
        return "editTower".toLowerCase();
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {
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

    }
}
