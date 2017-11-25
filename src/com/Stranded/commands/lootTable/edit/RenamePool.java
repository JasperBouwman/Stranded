package com.Stranded.commands.lootTable.edit;

import com.Stranded.commands.CmdManager;
import com.Stranded.lootTable.LootTable;
import org.bukkit.entity.Player;

public class RenamePool extends CmdManager {
    @Override
    public String getName() {
        return "renamePool".toLowerCase();
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        // lootTable edit <name> renamePool <poolName> <newName>

        if (args.length == 5) {
            LootTable lootTable = new LootTable(p, args[1]);
            boolean pool = lootTable.renamePool(args[3], args[4]);

            if (pool) {
                player.sendMessage("pool successfully renamed");
            } else {
                player.sendMessage(lootTable.error);
            }

        } else {
            player.sendMessage("wrong use");
        }

    }
}
