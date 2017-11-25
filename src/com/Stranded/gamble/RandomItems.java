package com.Stranded.gamble;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class RandomItems {

    public static ItemStack getRandomItem() {
        Random random = new Random();

        int randomItem = random.nextInt(7);

        switch (randomItem) {
            case 0:
                return new ItemStack(Material.STONE);
            case 1:
                return new ItemStack(Material.DIAMOND);
            case 2:
                return new ItemStack(Material.COBBLESTONE);
            case 3:
                return new ItemStack(Material.EXP_BOTTLE);
            case 4:
                return new ItemStack(Material.WOOD);
            case 5:
                return new ItemStack(Material.LOG);
            case 6:
                return new ItemStack(Material.GLOWSTONE);
            default:
                return new ItemStack(Material.CONCRETE);
        }
    }
}
