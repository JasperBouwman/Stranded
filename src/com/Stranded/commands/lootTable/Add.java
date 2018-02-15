package com.Stranded.commands.lootTable;

import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.lootTable.LootTable;
import org.bukkit.entity.Player;

public class Add extends CmdManager {
    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        //lootTable add <name>

        if (args.length == 2) {
            if (Main.containsSpecialCharacter(args[1])) {
                player.sendMessage("can not contains any special characters");
                return;
            }
            if (LootTable.addLootTable(p, args[1]) != null) {
                player.sendMessage("table added");
            } else {
                player.sendMessage("this lootTable already exist");
            }
        } else {
            player.sendMessage("wrong use");
        }
    }
}
