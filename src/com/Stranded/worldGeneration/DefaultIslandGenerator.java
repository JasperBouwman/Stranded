package com.Stranded.worldGeneration;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static com.Stranded.GettingFiles.getFiles;
import static com.Stranded.worldGeneration.regions.GetRegion.*;

public class DefaultIslandGenerator {

    public DefaultIslandGenerator() {

        Files islands = getFiles("islands.yml");
        islands.getConfig().set("islandData.islandTypesCopied", true);
        islands.saveConfig();

        Location L1 = new Location(Bukkit.getWorld("Islands"), -200987, 54, -200013);
        Location L2 = new Location(Bukkit.getWorld("Islands"), -201013, 75, -199726);

        int minY = Math.min(L1.getBlockY(), L2.getBlockY());
        int minX = Math.min(L1.getBlockX(), L2.getBlockX());
        int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
        int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
        int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
        int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

        int BlockCount = 1;

        Files defaultIslands = getFiles("defaultIslands.yml");

        if (!defaultIslands.getConfig().contains("models")) {
            Bukkit.broadcastMessage("The File 'defaultIslands.yml' is not correct, please download the right file and put it in the folder '/plugins/Stranded'");
            return;
        }

        for (int xx = minX; xx <= maxX; xx++) {
            for (int yy = minY; yy <= maxY; yy++) {
                for (int zz = minZ; zz <= maxZ; zz++) {

                    Block block = new Location(L1.getWorld(), xx, yy, zz).getBlock();

                    int type = defaultIslands.getConfig().getInt("models.default." + BlockCount + ".type");
                    byte bits = (byte) defaultIslands.getConfig().getInt("models.default." + BlockCount + ".byte");

                    block.setTypeIdAndData(type, bits, true);


                    if (block.getType().equals(Material.LONG_GRASS)) {
                        block.setData((byte) 1);
                    }

                    if (defaultIslands.getConfig().contains("models.default." + BlockCount + ".chest")) {

                        ArrayList<?> inv = (ArrayList<?>) defaultIslands.getConfig().get("models.default." + BlockCount + ".chest.inv");
                        Chest ch = (Chest) block.getState();

                        byte bit = (byte) defaultIslands.getConfig().getInt("models.default." + BlockCount + ".chest.materialData");
                        ch.getData().setData(bit);

                        int loop = 0;
                        for (Object t : inv) {
                            ItemStack is = (ItemStack) t;
                            if (is != null) {
                                ch.getInventory().setItem(loop, is);
                            }
                            loop++;
                        }
                    }

                    BlockCount++;
                }
            }
        }
        Bukkit.broadcastMessage("done with copying default islands");
    }

    public DefaultIslandGenerator(Main p) {

        Files islands = getFiles("islands.yml");
        islands.getConfig().set("islandData.islandTypesCopied", true);
        islands.saveConfig();

        Files defaultIslands = new Files(p, "defaultIslands1.yml");

        int y = defaultIslands.getConfig().getInt("island.y");

        int totalWidth = defaultIslands.getConfig().getInt("island.width");
        int totalHeight = defaultIslands.getConfig().getInt("island.height");
        int totalLength = defaultIslands.getConfig().getInt("island.length");
        int width = totalWidth;
        int height = totalHeight;
        int length = totalLength;

        Location temp = new Location(Bukkit.getWorld("Islands"), -200987, 54, -199726);
        temp.setY(y);

        for (String blocksID : defaultIslands.getConfig().getConfigurationSection("island.region.blocks").getKeys(false)) {

            String BlockSet = defaultIslands.getConfig().getString("island.region.blocks." + blocksID);

            for (String blockData : getBlocks(BlockSet)) {

                int repeat = 0;

                if (hasRepeat(blockData)) {
                    repeat = getRepeat(blockData);
                }

                Material m = getMaterial(blockData);

                for (int loop = 0; loop <= repeat; loop++) {

                    if (m.equals(Material.STRUCTURE_VOID)) {
                        length--;
                        if (length == -1) {
                            height--;
                            length = totalLength;
                        }
                        if (height == -1) {
                            width--;
                            height = totalHeight;
                        }
                        continue;
                    }

                    Location l = getLocation(temp, width, height, length);

                    if (!l.getBlock().getType().equals(m)) {
                        l.getBlock().setType(m);
                    }
                    l.getBlock().setData(getData(blockData));

                    if (hasExtraData(blockData)) {
                        int extraDataID = getExtraDataID(blockData);

                        if (extraDataID >= 0) {

                            switch (m) {
                                case BREWING_STAND:

                                    BrewingStand brewingStand = (BrewingStand) l.getBlock().getState();

                                    int time = defaultIslands.getConfig().getInt("island.region.extraData." + extraDataID + ".brewingStand.brewingTime");
                                    int level = defaultIslands.getConfig().getInt("island.region.extraData." + extraDataID + ".brewingStand.fuelLevel");
                                    String customName = defaultIslands.getConfig().getString("island.region.extraData." + extraDataID + ".brewingStand.customName");
                                    ArrayList<ItemStack> content = (ArrayList<ItemStack>) defaultIslands.getConfig().get("island.region.extraData." + extraDataID + ".brewingStand.inventory");
                                    String lock = defaultIslands.getConfig().getString("island.region.extraData." + extraDataID + ".brewingStand.lock");

                                    brewingStand.setBrewingTime(time);
                                    brewingStand.setFuelLevel(level);
                                    brewingStand.setCustomName(customName);
                                    brewingStand.setLock(lock);
                                    int i = 0;
                                    for (ItemStack is : content) {
                                        brewingStand.getInventory().setItem(i, is);
                                        i++;
                                    }
                                    break;
                                case CHEST:
                                case TRAPPED_CHEST:

                                    Chest chest = (Chest) l.getBlock().getState();

                                    customName = defaultIslands.getConfig().getString("island.region.extraData." + extraDataID + ".chest.customName");
                                    content = ((ArrayList<ItemStack>) defaultIslands.getConfig().get("island.region.extraData." + extraDataID + ".chest.inventory"));
                                    lock = defaultIslands.getConfig().getString("island.region.extraData." + extraDataID + ".chest.lock");
                                    chest.setCustomName(customName);
                                    chest.setLock(lock);

                                    if (chest.getInventory().getSize() >= content.size()) {
                                        i = 0;
                                        for (ItemStack is : content) {
                                            chest.getInventory().setItem(i, is);
                                            i++;
                                        }
                                    }
                                    break;
                            }
                        }
                    }

                    length--;
                    if (length == -1) {
                        height--;
                        length = totalLength;
                    }
                    if (height == -1) {
                        width--;
                        height = totalHeight;
                    }

                }
            }
        }
    }
}
