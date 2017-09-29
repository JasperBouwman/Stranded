package com.Stranded.worldGeneration;

import com.Stranded.Files;
import com.Stranded.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class DefaultIslandGenerator {

    public DefaultIslandGenerator(Main p) {

        Location L1 = new Location(Bukkit.getWorld("Islands"), -200987, 54, -200013);
        Location L2 = new Location(Bukkit.getWorld("Islands"), -201013, 75, -199726);

        int minY = Math.min(L1.getBlockY(), L2.getBlockY());
        int minX = Math.min(L1.getBlockX(), L2.getBlockX());
        int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
        int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
        int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
        int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

        int BlockCount = 1;

        Files defaultIslands = new Files(p, "defaultIslands.yml");

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

        Files islands = new Files(p, "islands.yml");
        islands.getConfig().set("islandData.islandTypesCopied", true);
        Bukkit.broadcastMessage("done with copying default islands");
        islands.saveConfig();

    }

}
