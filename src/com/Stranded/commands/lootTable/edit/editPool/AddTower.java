package com.Stranded.commands.lootTable.edit.editPool;

import com.Stranded.commands.CmdManager;
import com.Stranded.lootTable.LootTable;
import org.bukkit.entity.Player;

public class AddTower extends CmdManager {
    private int chanceMax = 2;
    private int chanceMin = 0;
    private String chance = "equal";
    private int amountMax = 2;
    private int amountMin = 0;
    private String amountChance = "equal";
    private int levelMax = 2;
    private int levelMin = 0;
    private String levelChance = "equal";

    @Override
    public String getName() {
        return "addTower".toLowerCase();
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        // lootTable edit <name> editPool <poolName> addTower <name> <tower> [chance=1,3,equal] [amount=1,3,equal] [level=1,3,equal]

        String lootTable = args[1];
        String poolName = args[3];

        if (!LootTable.containsLootTable(p, lootTable)) {
            player.sendMessage("this loot table doesn't exist");
            return;
        }
        if (!new LootTable(p, lootTable, poolName).containsPool()) {
            player.sendMessage("this pool doesn't exist");
            return;
        }

        switch (args.length) {
            case 10:
                if (test(args, 9)) {
                    return;
                }
            case 9:
                if (test(args, 8)) {
                    return;
                }
            case 8:
                if (test(args, 7)) {
                    return;
                }
            case 7:
                if (testTower(args[6])) {
                    player.sendMessage("this isn't a tower");
                    return;
                }



                boolean item = addTower(lootTable, poolName, args[5], args[6], chanceMax, chanceMin, chance, amountMax, amountMin, amountChance, levelMax, levelMin, levelChance);
                if (item) {
                    player.sendMessage("successfully added");
                } else {
                    player.sendMessage("item name already exist");
                }
                return;
            default:
                player.sendMessage("wrong use");
        }
    }

    private boolean addTower(String lootTable, String poolName, String itemName, String towerType,
                             int chanceMax, int chanceMin, String chance,
                             int amountMax, int amountMin, String amountChance,
                             int levelMax, int levelMin, String levelChance) {

        LootTable lootTableUtil = new LootTable(p, lootTable, poolName);
        lootTableUtil.setItemName(itemName);
        return lootTableUtil.addTower(towerType.toLowerCase(), chanceMax, chanceMin, chance, amountMax, amountMin, amountChance, levelMax, levelMin, levelChance);
    }

    private boolean testTower(String tower) {
        //true=failure
        switch (tower.toLowerCase()) {
            case "tnt":
            case "slowness":
            case "hunger":
            case "wither":
            case "arrow":
            case "haste":
            case "regeneration":
            case "speed":
                return false;
            default:
                return true;
        }
    }

    @SuppressWarnings("All")
    private boolean test(String[] args, int locate) {
        if (args[locate].toLowerCase().startsWith("chance")) {
            return testChance(args, locate);
        } else if (args[locate].toLowerCase().startsWith("amount")) {
            return testAmount(args, locate);
        } else if (args[locate].toLowerCase().startsWith("level")) {
            return testLevel(args, locate);
        } else {
            return true;
        }
    }

    private boolean testLevel(String[] args, int locate) {
        //true=failure
        String[] s = args[locate].split(",");

        switch (s.length) {
            case 3:
                switch (s[2].toLowerCase()) {
                    case "equal":
                    case "high":
                    case "low":
                        levelChance = s[2].toLowerCase();
                        break;
                    default:
                        return true;
                }
            case 2:
                try {
                    levelMax = Integer.parseInt(s[0].toLowerCase().replace("level=", ""));
                } catch (NumberFormatException e) {
                    return true;
                }
                try {
                    levelMin = Integer.parseInt(s[1]);
                } catch (NumberFormatException e) {
                    return true;
                }
                return levelMin > levelMax || levelMin < 0;
            default:
                return true;
        }
    }

    private boolean testAmount(String[] args, int locate) {
        //true=failure
        String[] s = args[locate].split(",");

        switch (s.length) {
            case 3:
                switch (s[2].toLowerCase()) {
                    case "equal":
                    case "high":
                    case "low":
                        amountChance = s[2].toLowerCase();
                        break;
                    default:
                        return true;
                }
            case 2:
                try {
                    amountMax = Integer.parseInt(s[0].toLowerCase().replace("amount=", ""));
                } catch (NumberFormatException e) {
                    return true;
                }
                try {
                    amountMin = Integer.parseInt(s[1]);
                } catch (NumberFormatException e) {
                    return true;
                }
                return amountMin > amountMax;
            default:
                return true;
        }
    }

    private boolean testChance(String[] args, int locate) {
        //true=failure
        String[] s = args[locate].split(",");

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
                return chanceMin > chanceMax;
            default:
                return true;
        }
    }

}
