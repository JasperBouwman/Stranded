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

    }
}
