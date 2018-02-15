package com.Stranded.commands.lootTable.edit.editPool;

import com.Stranded.commands.CmdManager;
import org.bukkit.entity.Player;

public class EditItem extends CmdManager {
    @Override
    public String getName() {
        return "editItem".toLowerCase();
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {
        // lootTable edit <name> editPool <poolName> editItem <name> material <material>
        // lootTable edit <name> editPool <poolName> editItem <name> chance max <Integer>
        // lootTable edit <name> editPool <poolName> editItem <name> chance min <Integer>
        // lootTable edit <name> editPool <poolName> editItem <name> chance chance [equal, low, lower, high, higher]
        // lootTable edit <name> editPool <poolName> editItem <name> amount max <Integer>
        // lootTable edit <name> editPool <poolName> editItem <name> amount min <Integer>
        // lootTable edit <name> editPool <poolName> editItem <name> amount chance [equal, low, lower, high, higher]
        // lootTable edit <name> editPool <poolName> editItem <name> damage <Integer>

    }
}
