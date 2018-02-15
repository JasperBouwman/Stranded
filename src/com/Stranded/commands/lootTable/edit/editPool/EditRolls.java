package com.Stranded.commands.lootTable.edit.editPool;

import com.Stranded.commands.CmdManager;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class EditRolls extends CmdManager {
    @Override
    public String getName() {
        return "editRolls".toLowerCase();
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {
        // lootTable edit <name> editPool <poolName> editRolls max <Integer>
        // lootTable edit <name> editPool <poolName> editRolls min <Integer>
        // lootTable edit <name> editPool <poolName> editRolls chance [equal, low, lower, high, higher]

    }

}
