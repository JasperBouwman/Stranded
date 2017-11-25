package com.Stranded.commands.lootTable.edit;

import com.Stranded.commands.CmdManager;
import com.Stranded.lootTable.LootTable;
import org.bukkit.entity.Player;

public class AddPool extends CmdManager {
    private int chanceMax = 2;
    private int chanceMin = 0;
    private String chance = "equal";

    @Override
    public String getName() {
        return "addPool".toLowerCase();
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        // lootTable edit <name> addPool <poolName> [chance=1,3,equal]
        String lootTable = args[1];

        switch (args.length) {
            case 5:

                if (args[4].toLowerCase().startsWith("chance")) {
                    if (testChance(args)) {
                        player.sendMessage("invalid chance argument");
                        return;
                    }
                } else {
                    player.sendMessage("wrong chance argument");
                    return;
                }

            case 4:
                String poolName = args[3];

                boolean pool = addPool(lootTable, poolName, chanceMin, chanceMax, chance);
                if (pool) {
                    player.sendMessage("successfully added");
                } else {
                    player.sendMessage("this pool already exist");
                }

                return;
            default:
                player.sendMessage("wrong use");
        }
    }

    private boolean testChance(String[] args) {
        //true=failure
        String[] s = args[4].split(",");

        switch (s.length) {
            case 3:
                switch (s[2].toLowerCase()) {
                    case "equal":
                    case "high":
                    case "low":
                        chance = s[2].toLowerCase();
                        break;
                    default:
                        return true;
                }
            case 2:
                try {
                    chanceMax = Integer.parseInt(s[0].toLowerCase().replace("chance=", ""));
                } catch (NumberFormatException e) {
                    return true;
                }
                try {
                    chanceMin = Integer.parseInt(s[1]);
                } catch (NumberFormatException e) {
                    return true;
                }
                return false;
            default:
                return true;
        }
    }

    private boolean addPool(String lootTable, String poolName, int chanceMin, int chanceMax, String chance) {
        LootTable lootTableUtil = new LootTable(p, lootTable, poolName);
        return lootTableUtil.addPool(chanceMin, chanceMax, chance);
    }

}
