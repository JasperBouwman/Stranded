package com.Stranded.commands.lootTable.edit;

import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.lootTable.LootTable;
import org.bukkit.entity.Player;

public class AddPool extends CmdManager {
    private int rollsMax = 2;
    private int rollsMin = 0;
    private String rolls = "equal";

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

        // lootTable edit <name> addPool <poolName> [rolls=1,3,equal]
        String lootTable = args[1];

        switch (args.length) {
            case 5:

                if (args[4].toLowerCase().startsWith("rolls")) {
                    if (testRolls(args)) {
                        player.sendMessage("invalid rolls argument");
                        return;
                    }
                } else {
                    player.sendMessage("wrong rolls argument");
                    return;
                }

            case 4:
                String poolName = args[3];
                if (Main.containsSpecialCharacter(args[3])) {
                    player.sendMessage("can not contains any special characters");
                    return;
                }

                boolean pool = addPool(lootTable, poolName, rollsMin, rollsMax, rolls);
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

    private boolean testRolls(String[] args) {
        //true=failure
        String[] s = args[4].split(",");

        switch (s.length) {
            case 3:
                switch (s[2].toLowerCase()) {
                    case "equal":
                    case "high":
                    case "higher":
                    case "low":
                    case "lower":
                        rolls = s[2].toLowerCase();
                        break;
                    default:
                        return true;
                }
            case 2:
                try {
                    rollsMax = Integer.parseInt(s[0].toLowerCase().replace("rolls=", ""));
                } catch (NumberFormatException e) {
                    return true;
                }
                try {
                    rollsMin = Integer.parseInt(s[1]);
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
