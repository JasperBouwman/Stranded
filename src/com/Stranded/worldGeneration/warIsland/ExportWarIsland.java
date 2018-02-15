package com.Stranded.worldGeneration.warIsland;

import com.Stranded.Files;
import org.bukkit.Location;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.spigotmc.AsyncCatcher;

public class ExportWarIsland {

    private String save = "";
    private int duplicates = -1;
    private byte oldData = -1;
    private int oldMaterial = -1;
    private int oldExtraDataID = -1;
    private boolean oldExtraData = false;

    public void exportWarIsland(Files island, String theme, int minPlayers, int maxPlayers, Location blueSpawn, Location redSpawn, Location L1, Location L2, Player player) {

        island.getConfig().set("warIsland.theme", theme);
        island.getConfig().set("warIsland.maxPlayers", maxPlayers);
        island.getConfig().set("warIsland.minPlayers", minPlayers);
        island.getConfig().set("warIsland.blueSpawn", blueSpawn);
        island.getConfig().set("warIsland.redSpawn", redSpawn);
        island.getConfig().set("warIsland.islandSize.first", L1);
        island.getConfig().set("warIsland.islandSize.second", L2);

        int minX = Math.min(L1.getBlockX(), L2.getBlockX());
        int minY = Math.min(L1.getBlockY(), L2.getBlockY());
        int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
        int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
        int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
        int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

        island.getConfig().set("warIsland.y", maxY);

        boolean tmp = AsyncCatcher.enabled;
        AsyncCatcher.enabled = false;

        island.getConfig().set("warIsland.width", maxX - minX);
        island.getConfig().set("warIsland.height", maxY - minY);
        island.getConfig().set("warIsland.length", maxZ - minZ);

        new Thread(() -> {

            int extraDataID = 0;

            int totalBlockSaves = 0;

            int lastX = 0;
            int lastY = 0;
            int lastZ = 0;

            for (int xx = minX; xx <= maxX; xx++) {
                for (int yy = minY; yy <= maxY; yy++) {
                    for (int zz = minZ; zz <= maxZ; zz++) {

                        Block block = new Location(L1.getWorld(), xx, yy, zz).getBlock();

                        extraDataID = saveBlock(island, block, extraDataID);

                        lastX = xx;
                        lastY = yy;
                        lastZ = zz;

                        if (save.length() > 10000) {
                            island.getConfig().set("warIsland.region.blocks." + totalBlockSaves, save);
                            save = "";
                            totalBlockSaves++;
                        }

                    }
                }
            }

            saveBlock(island, new Location(L1.getWorld(), lastX, lastY, lastZ + 1).getBlock(), extraDataID);

            island.getConfig().set("warIsland.region.blocks." + totalBlockSaves, save);
            island.saveConfig();

            player.sendMessage("done exporting");


            AsyncCatcher.enabled = tmp;
        }).start();

    }

    @SuppressWarnings("deprecation")
    private int saveBlock(Files island, Block block, int extraDataID) {

        boolean extraData = false;

        switch (block.getType()) {
            case STANDING_BANNER:
            case WALL_BANNER:

                Banner banner = (Banner) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".banner.pattern", banner.getPatterns());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".banner.baseColor", banner.getBaseColor().toString());
                island.saveConfig();
                extraData = true;

                break;
            case BEACON:

                Beacon beacon = (Beacon) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".beacon.primaryEffect", beacon.getPrimaryEffect());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".beacon.secondaryEffect", beacon.getSecondaryEffect());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".beacon.customName", beacon.getCustomName());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".beacon.lock", beacon.getLock());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".beacon.inventory", beacon.getInventory().getContents());
                island.saveConfig();
                extraData = true;

                break;
            case BED_BLOCK:

                Bed bed = (Bed) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".bed.color", bed.getColor().toString());
                island.saveConfig();
                extraData = true;

                break;
            case BREWING_STAND:

                BrewingStand brewingStand = (BrewingStand) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".brewingStand.brewingTime", brewingStand.getBrewingTime());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".brewingStand.fuelLevel", brewingStand.getFuelLevel());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".brewingStand.customName", brewingStand.getCustomName());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".brewingStand.inventory", brewingStand.getInventory().getContents());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".brewingStand.lock", brewingStand.getLock());
                island.saveConfig();
                extraData = true;

                break;
            case CHEST:
            case TRAPPED_CHEST:

                Chest chest = (Chest) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".chest.customName", chest.getCustomName());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".chest.inventory", chest.getInventory().getContents());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".chest.lock", chest.getLock());
                island.saveConfig();
                extraData = true;

                break;
            case COMMAND_CHAIN:
            case COMMAND_REPEATING:
            case COMMAND:
                CommandBlock commandBlock = (CommandBlock) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".commandBlock.command", commandBlock.getCommand());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".commandBlock.name", commandBlock.getName());
                island.saveConfig();

                break;
            case MOB_SPAWNER:

                CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".spawner.delay", creatureSpawner.getDelay());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".spawner.spawnedType", creatureSpawner.getSpawnedType().toString());
                island.saveConfig();
                extraData = true;

                break;
            case DISPENSER:

                Dispenser dispenser = (Dispenser) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".dispenser.customName", dispenser.getCustomName());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".dispenser.lock", dispenser.getLock());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".dispenser.inventory", dispenser.getInventory().getContents());
                island.saveConfig();
                extraData = true;

                break;
            case DROPPER:

                Dropper dropper = (Dropper) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".dropper.customName", dropper.getCustomName());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".dropper.lock", dropper.getLock());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".dropper.inventory", dropper.getInventory().getContents());
                island.saveConfig();
                extraData = true;

                break;
            case ENCHANTMENT_TABLE:

                EnchantingTable enchantmentTable = (EnchantingTable) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".enchantmentTable.customName", enchantmentTable.getCustomName());
                island.saveConfig();
                extraData = true;

                break;
            case FLOWER_POT:

//                FlowerPot flower = (FlowerPot) block.getState();
//
//                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".flowerPot.content", flower.getContents().toItemStack(1));
//                island.saveConfig();
//                extraData = true;
//
//                break;
            case FURNACE:

                Furnace furnace = (Furnace) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".furnace.burnTime", furnace.getBurnTime());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".furnace.cookTime", furnace.getCookTime());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".furnace.customName", furnace.getCustomName());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".furnace.lock", furnace.getLock());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".furnace.inventory", furnace.getInventory().getContents());
                island.saveConfig();
                extraData = true;

                break;
            case HOPPER:

                Hopper hopper = (Hopper) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".hopper.customName", hopper.getCustomName());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".hopper.lock", hopper.getLock());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".hopper.inventory", hopper.getInventory().getContents());
                island.saveConfig();
                extraData = true;

                break;
            case JUKEBOX:

                Jukebox jukebox = (Jukebox) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".jukebox.playing", jukebox.getPlaying().toString());
                island.saveConfig();
                extraData = true;

                break;
            case NOTE_BLOCK:

                NoteBlock noteBlock = (NoteBlock) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".noteBlock.note", noteBlock.getNote());
                island.saveConfig();
                extraData = true;

                break;
            case SIGN_POST:
            case WALL_SIGN:

                Sign sign = (Sign) block.getState();

                for (int i = 0; i < 4; i++) {
                    island.getConfig().set("warIsland.region.extraData." + extraDataID + ".sign." + i, sign.getLine(i));
                }
                island.saveConfig();
                extraData = true;

                break;
            case SKULL:

//                Skull skull = (Skull) block.getState();
//
//                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".skull.skullType", skull.getSkullType().toString());
//                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".skull.rotation", skull.getRotation().toString());
//                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".skull.owner", skull.getOwner());
//                island.saveConfig();
//                extraData = true;
//
//                break;
            case BLACK_SHULKER_BOX:
            case SILVER_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case WHITE_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:

                ShulkerBox shulkerBox = (ShulkerBox) block.getState();

                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".shulkerBox.customName", shulkerBox.getCustomName());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".shulkerBox.lock", shulkerBox.getLock());
                island.getConfig().set("warIsland.region.extraData." + extraDataID + ".shulkerBox.inventory", shulkerBox.getInventory().getContents());
                island.saveConfig();
                extraData = true;

                break;
        }

        int typeID = block.getTypeId();
        byte data = block.getData();

        if (oldMaterial == -1) {
            oldMaterial = block.getTypeId();
            oldData = block.getData();
            if (extraData) {
                oldExtraDataID = extraDataID;
                oldExtraData = true;
            }
        }

        boolean b = (!extraData && data == oldData && typeID == oldMaterial);

        if (!b) {
            save += oldMaterial +
                    (oldData != 0 ? "\\" + oldData : "") +
                    (oldExtraData ? "\\D" + oldExtraDataID : "") +
                    (duplicates > 0 ? "\\X" + duplicates : "") +
                    "/";
            duplicates = 0;
        }

        if (b) {
            duplicates++;
        } else {
            oldMaterial = block.getTypeId();
            oldData = block.getData();
            oldExtraDataID = extraDataID;
            oldExtraData = false;
        }

        if (extraData) {
            extraDataID++;
            oldExtraData = true;
        }
        return extraDataID;

    }
}
