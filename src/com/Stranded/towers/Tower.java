package com.Stranded.towers;

import com.Stranded.Files;
import com.Stranded.Main;
import com.Stranded.playerUUID.PlayerUUID;
import com.Stranded.towers.models.*;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

import static com.Stranded.GettingFiles.getFiles;

public class Tower {

    private String towerType;
    private int lvl;
    private UUID owner;
    private Files towers;

    public static int MAX_UPGRADE = 20;

    public Tower(String playerName) {
        this.lvl = 0;
        String uuid = PlayerUUID.getPlayerUUID(playerName);
        this.owner = UUID.fromString( uuid == null ? "" : uuid);
        this.towers = getFiles("towers.yml");
        this.towerType = "";
    }

    public Tower(String playerName, String towerType) {
        this.towerType = towerType;
        this.lvl = 0;
        String uuid = PlayerUUID.getPlayerUUID(playerName);
        this.owner = UUID.fromString( uuid == null ? "" : uuid);
        this.towers = getFiles("towers.yml");
    }

    public Tower(UUID uuid, String towerType, int lvl) {
        this.towerType = towerType;
        this.lvl = lvl;
        this.owner = uuid;
        this.towers = getFiles("towers.yml");
    }

    public Tower(String playerName, String towerType, int lvl) {
        this.towerType = towerType;
        this.lvl = lvl;
        String uuid = PlayerUUID.getPlayerUUID(playerName);
        this.owner = UUID.fromString( uuid == null ? "" : uuid);
        this.towers = getFiles("towers.yml");
    }

    public ArrayList<ItemStack> filterTowers(String filter) {
        ArrayList<ItemStack> list = getTowers();
        ArrayList<ItemStack> filtered = new ArrayList<>();

        for (ItemStack is : list) {
            if (is.hasItemMeta()) {
                if (is.getItemMeta().hasLore()) {
                    if (is.getItemMeta().getLore().get(0).equals(filter)) {
                        filtered.add(is);
                    }
                }
            }
        }

        return filtered;
    }

    public boolean removeTower(ItemStack tower) {

        for (String id : towers.getConfig().getConfigurationSection("towers." + owner.toString()).getKeys(false)) {
            if (tower.equals(towers.getConfig().get("towers." + owner.toString() + "." + id))) {
                towers.getConfig().set("towers." + owner.toString() + "." + id, null);
                towers.saveConfig();
                return true;
            }
        }
        return false;
    }

    public ArrayList<ItemStack> getTowers() {
        ArrayList<ItemStack> list = new ArrayList<>();
        if (towers.getConfig().contains("towers." + owner.toString())) {
            for (String id : towers.getConfig().getConfigurationSection("towers." + owner.toString()).getKeys(false)) {
                ItemStack tower = (ItemStack) towers.getConfig().get("towers." + owner.toString() + "." + id);
                list.add(tower);
            }
        }
        return list;

    }

    public void saveTower(ItemStack tower) {
        int id = 0;
        while (true) {
            if (!towers.getConfig().contains("towers." + owner.toString() + "." + id)) {
                towers.getConfig().set("towers." + owner.toString() + "." + id, tower);
                towers.saveConfig();
                return;
            }
            id++;
        }
    }

    void saveTower() {
        int id = 0;
        while (true) {
            if (!towers.getConfig().contains("towers." + owner.toString() + "." + id)) {
                ItemStack is = toItemStack();
                towers.getConfig().set("towers." + owner.toString() + "." + id, is);
                towers.saveConfig();
                return;
            }
            id++;
        }
    }

    @SuppressWarnings("deprecation")
    private ItemStack toItemStack() {

        towerType = towerType.replace("Regen", "Regeneration").replace("Tp", "Teleport");
        int data = 0;
        ArrayList<String> lore = new ArrayList<>();
        String name = "";
        //towersData
        switch (towerType) {
            case "TNT":
                data = (14);
                name = "§4Enemy Tower";
                break;
            case "Slowness":
                data = (10);
                name = "§4Enemy Tower";
                break;
            case "Hunger":
                data = (13);
                name = "§4Enemy Tower";
                break;
            case "Wither":
                data = (15);
                name = "§4Enemy Tower";
                break;
            case "Arrow":
                data = (8);
                name = "§4Enemy Tower";
                break;
            case "Haste":
                data = (4);
                name = "§3Friendly Tower";
                break;
            case "Regeneration":
                data = (1);
                name = "§3Friendly Tower";
                break;
            case "Speed":
                data = (0);
                name = "§3Friendly Tower";
                break;
            case "Teleport":
                data = (3);
                name = "§3Friendly Tower";
                break;
        }
        lore.add(towerType);
        if (lvl != 1) {
            if (lvl == 9) {
                lore.add("lvl: MAX");
            } else {
                lore.add("lvl: " + lvl);
            }
        }

        ItemStack is = new ItemStack(35, 1, (byte) data);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    public void setTower(Location location) {
        String level = lvl + "";

        if (lvl == Tower.MAX_UPGRADE) {
            level = "MAX";
        }

        //towersData
        switch (towerType) {
            case "TNT":
                TntTower.Tower(location, level);
                break;
            case "Slowness":
                SlowTower.Tower(location, level);
                break;
            case "Hunger":
                HungerTower.Tower(location, level);
                break;
            case "Wither":
                WitherTower.Tower(location, level);
                break;
            case "Arrow":
                ArrowTower.Tower(location, level);
                break;
            case "Haste":
                HasteTower.Tower(location, level);
                break;
            case "Regeneration":
                RegenerationTower.Tower(location, level);
                break;
            case "Speed":
                SpeedTower.Tower(location, level);
                break;
            case "Teleport":
                TeleportTower.Tower(location, level);
                break;
        }
    }

    public void saveTowers(ArrayList<ItemStack> list) {

        towers.getConfig().set("towers." + owner.toString(), null);
        int id = 0;
        for (ItemStack is : list) {
            towers.getConfig().set("towers." + owner.toString() + "." + id, is);
            id++;
        }
        towers.saveConfig();

    }
}
