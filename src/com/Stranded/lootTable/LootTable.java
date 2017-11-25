package com.Stranded.lootTable;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Material;

import java.io.File;

@SuppressWarnings("unused")
public class LootTable {

    public String error = null;
    public static int max = 3;
    public static int min = 0;
    public static String chance = "equal";
    private Files lootTable;
    private String poolName;
    private String itemName = null;

    public LootTable(Main p, String lootTable, String poolName) {
        this.lootTable = new Files(p, "/lootTables", lootTable + ".yml");
        this.poolName = poolName;
    }

    public LootTable(Main p, String lootTable) {
        this.lootTable = new Files(p, "/lootTables", lootTable + ".yml");
    }

    public LootTable(Files lootTable) {
        this.lootTable = lootTable;
    }

    public static boolean containsLootTable(Main p, String lootTableName) {
        return new File(p.getDataFolder() + "/lootTables/" + lootTableName + ".yml").exists();
    }

    public static LootTable addLootTable(Main p, String lootTableName) {
        if (containsLootTable(p, lootTableName)) {
            return null;
        }
        Files lootTable = new Files(p, "/lootTables", lootTableName + ".yml");
        lootTable.getConfig().set("lootTable.pools", null);
        lootTable.saveConfig();
        return new LootTable(lootTable);
    }

    public static int removeLootTable(Main p, String lootTableName) {
        //0: removed; 1: doesn't exist; 2: could not been deleted
        if (!containsLootTable(p, lootTableName)) {
            return 1;
        }

        File lootTable = new File(p.getDataFolder() + "/lootTables", lootTableName + ".yml");

        return lootTable.delete() ? 0 : 2;
    }

    public static boolean isLootTable(Main p, String lootTableName) {
        Files file = new Files(p, "/lootTables", lootTableName + (lootTableName.toLowerCase().endsWith(".yml") ? "" : ".yml"));
        return file.getConfig().contains("lootTable");
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public boolean containsPool() {
        return lootTable.getConfig().contains("lootTable.pools." + poolName);
    }

    private boolean containsPool(String poolName) {
        return lootTable.getConfig().contains("lootTable.pools." + poolName);
    }

    public boolean addPool(int min, int max, String chance) {
        if (containsPool()) {
            return false;
        }
        lootTable.getConfig().set("lootTable.pools." + poolName + ".rolls.min", min);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".rolls.max", max);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".rolls.chance", chance);
        lootTable.saveConfig();
        return true;
    }

    public boolean addPool() {
        return addPool(0, 3, "equal");
    }

    public boolean removePool(String poolName) {
        if (!containsPool()) {
            return false;
        }
        lootTable.getConfig().set("lootTable.pools." + poolName, null);
        lootTable.saveConfig();
        return true;
    }

    public boolean renamePool(String poolName, String newName) {
        if (!containsPool()) {
            error = "pool doesn't exist";
            return false;
        }
        if (containsPool(newName)) {
            error = "pool already exist";
            return false;
        }

        lootTable.getConfig().set("lootTable.pools." + newName,
                lootTable.getConfig().getConfigurationSection("lootTable.pools." + poolName));

        lootTable.saveConfig();
        return true;
    }

    public boolean editMinRollsPool(int min) {
        if (!containsPool()) {
            return false;
        }
        lootTable.getConfig().set("lootTable.pools." + poolName + ".rolls.min", min);
        lootTable.saveConfig();
        return true;
    }

    public boolean editMaxRollsPool(int max) {
        if (!containsPool()) {
            return false;
        }
        lootTable.getConfig().set("lootTable.pools." + poolName + ".rolls.max", max);
        lootTable.saveConfig();
        return true;
    }

    public boolean editMinRollsPool(String chance) {
        if (!containsPool()) {
            return false;
        }
        lootTable.getConfig().set("lootTable.pools." + poolName + ".rolls.chance", chance);
        lootTable.saveConfig();
        return true;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    private boolean containsItem() {
        return itemName != null && !containsPool() && lootTable.getConfig().contains("lootTable.pools." + poolName + ".items." + itemName);
    }

    public boolean addItem(Material material, int damage,
                           int chanceMax, int chanceMin, String chance,
                           int amountMax, int amountMin, String amountChance) {
        if (containsItem()) {
            return false;
        }
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".type", "item");
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".material", material.toString());
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".damage", damage);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".chance.max", chanceMax);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".chance.min", chanceMin);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".chance.chance", chance);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".amount.max", amountMax);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".amount.min", amountMin);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".amount.amount", amountChance);
        lootTable.saveConfig();

        return true;
    }

    public boolean addTower(String towerType,
                            int chanceMax, int chanceMin, String chance,
                            int amountMax, int amountMin, String amountChance,
                            int levelMax, int levelMin, String levelChance) {
        if (containsItem()) {
            return false;
        }
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".type", "tower");
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".tower", towerType);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".chance.max", chanceMax);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".chance.min", chanceMin);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".chance.chance", chance);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".amount.max", amountMax);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".amount.min", amountMin);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".amount.amount", amountChance);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".level.max", levelMax);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".level.min", levelMin);
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName + ".level.amount", levelChance);
        lootTable.saveConfig();

        return true;
    }

    public boolean removeItem() {
        if (!containsItem()) {
            return false;
        }
        lootTable.getConfig().set("lootTable.pools." + poolName + ".items." + itemName, null);
        lootTable.saveConfig();
        return true;
    }
}
