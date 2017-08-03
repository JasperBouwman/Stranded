package com.Stranded;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class Files {
    private final String fileName;
    private final JavaPlugin plugin;

    private File configFile;
    private FileConfiguration fileConfiguration;

    public Files(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.configFile = new File(plugin.getDataFolder(), fileName);
    }

    public void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        // Look for defaults in the jar
//        InputStream defConfigStream = plugin.getResource(fileName);
//        if (defConfigStream != null) {
//            @SuppressWarnings("deprecation")
//            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
//            fileConfiguration.setDefaults(defConfig);
//        }
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }
        return fileConfiguration;
    }

    public void saveConfig() {
        if (fileConfiguration == null || configFile == null) {
            return;
        } else {
            try {
                getConfig().save(configFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
            }
        }
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            this.plugin.saveResource(fileName, false);

            if (fileName.equals("islands.yml")) {
                placeDefaultIslands();
            }
        }
    }

    private void placeDefaultIslands() {

        Files defaultIslands = new Files(plugin , "defaultIslands.yml");

        Location L1 = new Location(Bukkit.getServer().getWorld("Islands"),-201013,54,-200013);
        Location L2 = new Location(Bukkit.getServer().getWorld("Islands"),-200987,75,-199726);

        int minX = Math.min(L1.getBlockX(), L2.getBlockX());
        int minY = Math.min(L1.getBlockY(), L2.getBlockY());
        int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
        int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
        int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
        int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

        int BlockCount = 1;

        for (int xx = minX; xx <= maxX; xx++) {
            for (int yy = minY; yy <= maxY; yy++) {
                for (int zz = minZ; zz <= maxZ; zz++) {

                    Block block = Bukkit.getServer().getWorld(L1.getWorld().getName()).getBlockAt(new Location(L1.getWorld(), xx, yy, zz));

                    block.setTypeIdAndData(defaultIslands.getConfig().getInt("island.block." + BlockCount + ".type"),
                            (byte) defaultIslands.getConfig().getInt("island.block." + BlockCount + ".data"), true);

                    BlockCount++;
                }
            }
        }
    }

    public void loadConfig() {
        fileConfiguration = getConfig();
        fileConfiguration.options().copyDefaults(true);

        if (new File(plugin.getDataFolder() + "/config.yml").exists()) {
            System.out.println("[" + plugin + "]" + "config.yml loaded.");
        } else {
            saveDefaultConfig();
            System.out.println("[" + plugin + "]" + "config.yml created and loaded.");
        }
    }
}
