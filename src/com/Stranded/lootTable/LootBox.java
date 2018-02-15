package com.Stranded.lootTable;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.towers.Tower;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LootBox {

    private Files lootTableFile;
    private FileConfiguration lootTable;

    public LootBox(Main p, String lootTable) {
        this.lootTableFile = new Files(p, "/lootTables", lootTable + ".yml");
        this.lootTable = lootTableFile.getConfig();
    }

    public LootBox(Files lootTable) {
        this.lootTableFile = lootTable;
        this.lootTable = lootTableFile.getConfig();
    }

    public Inventory getLootBox(lootBoxSize size) {

        Inventory inv = Bukkit.createInventory(null, size.size, "LootBox");

        ArrayList<Integer> chestSlots = new ArrayList<>();
        for (int i = 0; i < size.size; i++) {
            chestSlots.add(i);
        }

        Set<String> pools = lootTable.getConfigurationSection("lootTable.pools").getKeys(false);

        for (String pool : pools) {

            int min = lootTable.getInt("lootTable.pools." + pool + ".rolls.min");
            int max = lootTable.getInt("lootTable.pools." + pool + ".rolls.max");
            String chance = lootTable.getString("lootTable.pools." + pool + ".rolls.chance");

            int rollChance = getInt(max, min, chance);

            for (int loop = 0; loop < rollChance; loop++) {
                for (String item : lootTable.getConfigurationSection("lootTable.pools." + pool + ".items").getKeys(false)) {

                    String itemPath = "lootTable.pools." + pool + ".items." + item;

                    int itemMin = lootTable.getInt(itemPath + ".chance.min");
                    int itemMax = lootTable.getInt(itemPath + ".chance.max");
                    String itemChance = lootTable.getString(itemPath + ".chance.chance");

                    int itemRoll = getInt(itemMax, itemMin, itemChance);

                    for (int itemLoop = 0; itemLoop < itemRoll; itemLoop++) {

                        String type = lootTable.getString(itemPath + ".type");

                        ItemStack is;

                        switch (type) {
                            case "item":

                                Material m = (Material.getMaterial(lootTable.getString(itemPath + ".material")));

                                int amountMax = lootTable.getInt(itemPath + ".amount.max");
                                int amountMin = lootTable.getInt(itemPath + ".amount.min");
                                String amountChance = lootTable.getString(itemPath + ".amount.chance");
                                int amount = getInt(amountMax, amountMin, amountChance);

                                byte damage = (byte) lootTable.getInt(itemPath + ".damage");

                                is = new ItemStack(m, amount, damage);

                                break;
                            case "tower":

                                m = Material.WOOL;
                                amountMax = lootTable.getInt(itemPath + ".amount.max");
                                amountMin = lootTable.getInt(itemPath + ".amount.min");
                                amountChance = lootTable.getString(itemPath + ".amount.chance");
                                amount = getInt(amountMax, amountMin, amountChance);

                                String towerType = lootTable.getString(itemPath + ".tower");
                                String name = "";
                                List<String> lore = new ArrayList<>();

                                damage = 0;

                                boolean whileLoop = true;
                                while (whileLoop) {
                                    switch (towerType.toLowerCase()) {
                                        case "tnt":
                                            damage = 14;
                                            name = "Enemy";
                                            lore.add("TNT");
                                            whileLoop = false;
                                            break;
                                        case "slowness":
                                            damage = 10;
                                            name = "Enemy";
                                            lore.add("Slowness");
                                            whileLoop = false;
                                            break;
                                        case "hunger":
                                            damage = 13;
                                            name = "Enemy";
                                            lore.add("Hunger");
                                            whileLoop = false;
                                            break;
                                        case "wither":
                                            damage = 15;
                                            name = "Enemy";
                                            lore.add("Wither");
                                            whileLoop = false;
                                            break;
                                        case "arrow":
                                            damage = 8;
                                            name = "Enemy";
                                            lore.add("Arrow");
                                            whileLoop = false;
                                            break;
                                        case "haste":
                                            damage = 4;
                                            name = "Friendly";
                                            lore.add("Haste");
                                            whileLoop = false;
                                            break;
                                        case "regeneration":
                                            damage = 1;
                                            name = "Friendly";
                                            lore.add("Regeneration");
                                            whileLoop = false;
                                            break;
                                        case "speed":
                                            damage = 0;
                                            name = "Friendly";
                                            lore.add("Speed");
                                            whileLoop = false;
                                            break;
                                        default:
                                            List<String> tmp = Arrays.asList("tnt", "slowness", "hunger", "wither", "arrow", "haste", "regeneration", "speed");
                                            towerType = tmp.get(new Random().nextInt(tmp.size()));
                                            whileLoop = true;
                                    }
                                }

                                int levelMax = lootTable.getInt(itemPath + ".level.max");
                                int levelMin = lootTable.getInt(itemPath + ".level.min");
                                String levelChance = lootTable.getString(itemPath + ".level.chance");
                                int level = getInt(levelMax, levelMin, levelChance);
                                if (level > 1) {
                                    if (level >= Tower.MAX_UPGRADE) {
                                        lore.add("lvl: MAX");
                                    } else {
                                        lore.add("lvl: " + level);
                                    }
                                }

                                is = new ItemStack(m, amount, damage);
                                ItemMeta im = is.getItemMeta();
                                im.setDisplayName(name);
                                im.setLore(lore);
                                is.setItemMeta(im);
                                break;
                            default:
                                continue;
                        }
                        int slot;

                        if (chestSlots.size() < 1) {
                            slot = new Random().nextInt(size.size);
                        } else {
                            slot = chestSlots.get(new Random().nextInt(chestSlots.size()));
                            //noinspection SuspiciousMethodCalls
                            chestSlots.remove((Object) slot);
                        }

                        inv.setItem(slot, is);

                    }

                }
            }
        }

        return inv;
    }

    private int getInt(int max, int min, String chance) {

        if (max < 0) {
            return 0;
        }
        if (max < min) {
            return min;
        }
        if (max == min) {
            return max;
        }

        ArrayList<Integer> list = new ArrayList<>();

        double chanceAmplifier = 0.5;

        switch (chance) {
            case "low":
                chanceAmplifier = 1;
            case "lower":
                int loop = 1;
                for (double i = max; i >= min; i -= chanceAmplifier) {
                    for (int ii = 0; ii < loop; ii++) {
                        list.add((int) i);
                    }
                    loop++;
                }
                break;
            case "high":
                chanceAmplifier = 1;
            case "higher":
                loop = 1;
                for (double i = min; i <= max; i += chanceAmplifier) {
                    for (int ii = 0; ii < loop; ii++) {
                        list.add((int) i);
                    }
                    loop++;
                }
                break;
            case "equal":
            default:
                return roundNumber((new Random().nextDouble() * (max - min) + min));
        }
        return list.get(new Random().nextInt(list.size()));
    }

    private int roundNumber(double d) {
        return d - ((int) d) < 0.5 ? (int) d : (int) (d + 1);
    }

    @SuppressWarnings("unused")
    public enum lootBoxSize {
        SMALL(27),
        LARGE(54);
        int size;
        lootBoxSize(int i) {
            this.size = i;
        }

    }

}
