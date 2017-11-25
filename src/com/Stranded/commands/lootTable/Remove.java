package com.Stranded.commands.lootTable;

import com.Stranded.commands.CmdManager;
import com.Stranded.lootTable.LootTable;
import org.bukkit.entity.Player;

public class Remove extends CmdManager {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        // lootTable remove <name>

        if (args.length == 2) {
            int i = LootTable.removeLootTable(p, args[1]);
            if (i == 0) {
                player.sendMessage("table removed");
            } else if (i == 2) {
                player.sendMessage("this lootTable doesn't exist");
            } else {
                player.sendMessage("this lootTable could not be deleted");
            }
        } else {
            player.sendMessage("wrong use");
        }

    }
}
