package com.Stranded.worldGeneration;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class WorldGenerate {

    public static void generate() {

        Generator g = new Generator();

        //Islands
        WorldCreator WC = new WorldCreator("Islands");
        WC.generator(g);
        Bukkit.getServer().createWorld(WC);

        //War
        WorldCreator WG2 = new WorldCreator("War");
        WG2.generator(g);
        Bukkit.getServer().createWorld(WG2);
    }
}
