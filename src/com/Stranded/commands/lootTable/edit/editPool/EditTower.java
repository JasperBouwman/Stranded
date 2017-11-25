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
        // lootTable edit <name> editPool <poolName> editTower <name> type <towerType:random>

    }
}
