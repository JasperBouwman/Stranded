package com.Stranded.worldGeneration;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class IslandPopulator extends BlockPopulator {

    public void populate(World world, Random random, Chunk source) {

        int centerX = (source.getX() << 4);
        int centerZ = (source.getZ() << 4);
        int centerY = 64;

        for (int x = 0; x <= 16; x++) {
            for (int z = 0; z <= 16; z++) {

                int absX = Math.abs(x);
                int absZ = Math.abs(z);

                if ((absX + centerX) % 50 == 0 && (absZ + centerZ) % 50 == 0) {

                    world.getBlockAt(centerX + x, centerY, centerZ + z).setType(Material.STAINED_CLAY);
                    boolean loop = true;
                    while (loop) {
                        int r = random.nextInt(100);
                        int l = random.nextInt( 20);
                        if (r > 76) {
                            for (int i = 0; i <  l; i++) {
                                world.getBlockAt(centerX + x + i, centerY, centerZ + z).setType(Material.REDSTONE_BLOCK);

                            }
                            x = x +l;
                            continue;
                        }
                        if (r > 52) {
                            for (int i = 0; i <  l; i++) {
                                world.getBlockAt(centerX + x - i, centerY, centerZ + z).setType(Material.REDSTONE_BLOCK);

                            }
                            x = x -  l;
                            continue;
                        }
                        if (r > 27) {
                            for (int i = 0; i <  l; i++) {
                                world.getBlockAt(centerX + x, centerY, centerZ + z + i).setType(Material.REDSTONE_BLOCK);

                            }z = z +  l;
                            continue;
                        }
                        if (r > 4) {
                            for (int i = 0; i <  l; i++) {
                                world.getBlockAt(centerX + x, centerY, centerZ + z - i).setType(Material.REDSTONE_BLOCK);

                            }z = z -  l;
                            continue;
                        }

                        loop = false;

                    }
                }
            }
        }
    }
}
