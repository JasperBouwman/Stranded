package com.Stranded.commands.lootTable.edit;

import com.Stranded.commands.CmdManager;
import com.Stranded.lootTable.LootTable;
import org.bukkit.entity.Player;

public class RemovePool extends CmdManager {
    @Override
    public String getName() {
        return "removePool".toLowerCase();
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        // lootTable edit <name> removePool <poolName>

        if (args.length == 4) {

            boolean pool = new LootTable(p, args[1]).removePool(args[3]);

            if (pool) {
                player.sendMessage("pool successfully removed");
            } else {
                player.sendMessage("pool doesn't exist");
            }

        } else {
            player.sendMessage("wrong use");
        }

    }
}
