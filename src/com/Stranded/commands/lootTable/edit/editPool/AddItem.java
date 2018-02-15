package com.Stranded.commands.lootTable.edit.editPool;

import com.Stranded.Main;
import com.Stranded.commands.CmdManager;
import com.Stranded.lootTable.LootTable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AddItem extends CmdManager {
    private int chanceMax = 2;
    private int chanceMin = 0;
    private String chance = "equal";
    private int amountMax = 2;
    private int amountMin = 0;
    private String amountChance = "equal";
    private int damage = 0;
    private boolean setAmount = false;

    @Override
    public String getName() {
        return "addItem".toLowerCase();
    }

    @Override
    public String getAlias() {
        return null;
    }

    @Override
    public void run(String[] args, Player player) {

        // lootTable edit <name> editPool <poolName> addItem <name> <material> [chance=1,3,equal] [amount=1,3,equal] [damage=0]

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
                Material material = Bukkit.getUnsafe().getMaterialFromInternalName(args[6]);

                if (material.toString().equals("AIR") && !args[6].equalsIgnoreCase("air")) {
                    player.sendMessage(args[6] + " is not found as an material, this is now set to AIR");
                }

                if (Main.containsSpecialCharacter(args[5])) {
                    player.sendMessage("Your name can not contains any special characters");
                    return;
                }
                boolean item = addItem(lootTable, poolName, args[5], material, damage, chanceMax, chanceMin, chance, amountMax, amountMin, amountChance);
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

    @SuppressWarnings("All")
    private boolean test(String[] args, int locate) {
        if (args[locate].toLowerCase().startsWith("chance")) {
            return testChance(args, locate);
        } else if (args[locate].toLowerCase().startsWith("amount")) {
            return testAmount(args, locate);
        } else if (args[locate].toLowerCase().startsWith("damage")) {
            return testDamage(args, locate);
        } else {
            return true;
        }
    }

    private boolean testDamage(String[] args, int locate) {
        try {
            damage = Integer.parseInt(args[locate].toLowerCase().replace("damage=", ""));
            return false;
        } catch (NumberFormatException e) {
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
                    case "higher":
                    case "low":
                    case "lower":
                        amountChance = s[2].toLowerCase();
                        break;
                    default:
                        return true;
                }
            case 2:
                try {
                    amountMax = Integer.parseInt(s[0].toLowerCase().replace("amount=", ""));
                    setAmount = true;
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
                    case "higher":
                    case "low":
                    case "lower":
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

    private boolean addItem(String lootTable, String poolName, String itemName, Material material, int damage,
                            int chanceMax, int chanceMin, String chance,
                            int amountMax, int amountMin, String amountChance) {

        LootTable lootTableUtil = new LootTable(p, lootTable, poolName);
        lootTableUtil.setItemName(itemName);
        return lootTableUtil.addItem(material, damage, chanceMax, chanceMin, chance, amountMax, amountMin, amountChance);
    }
}
