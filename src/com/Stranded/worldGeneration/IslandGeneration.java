package com.Stranded.worldGeneration;

import com.Stranded.Files;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Villager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import static com.Stranded.GettingFiles.getFiles;

public class IslandGeneration {

    public static Location nexusLocation;
    public static UUID UUID;

    public static void generate(Location l, String type) {

        HashMap<Integer, Block> blocks = new HashMap<>();

        Files f = getFiles("islands.yml");
        for (String s : f.getConfig().getConfigurationSection("islandData.islandTypes").getKeys(false)) {

            if (type.equalsIgnoreCase(f.getConfig().getString("islandData.islandTypes." + s + ".name"))) {

                Location l1 = (Location) f.getConfig().get("islandData.islandTypes." + s + ".defaultLocation");

                Location L1 = new Location(l1.getWorld(), l1.getX() + 12, 74, l1.getZ() - 12);
                Location L2 = new Location(l1.getWorld(), l1.getX() - 12, 55, l1.getZ() + 12);

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

                            Block block = new Location(l.getWorld(), xx, yy, zz).getBlock();

                            blocks.put(BlockCount, block);

                            BlockCount++;
                        }
                    }
                }

                break;
            }
        }

        Location L1 = new Location(l.getWorld(), l.getX() + 12, l.getY() - 9, l.getZ() - 12);
        Location L2 = new Location(l.getWorld(), L1.getX() - 24, L1.getY() + 19, L1.getZ() + 24);


        int minY = Math.min(L1.getBlockY(), L2.getBlockY());
        int minX = Math.min(L1.getBlockX(), L2.getBlockX());
        int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
        int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
        int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
        int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

        int BlockCount = 1;
        boolean nexus = true;

        for (int xx = minX; xx <= maxX; xx++) {
            for (int yy = minY; yy <= maxY; yy++) {
                for (int zz = minZ; zz <= maxZ; zz++) {

                    Block block = new Location(l.getWorld(), xx, yy, zz).getBlock();

                    setBlock(blocks.get(BlockCount), block);

                    if (nexus && block.getType().equals(Material.BEDROCK)) {
                        nexusLocation = spawnNexus(block.getLocation());
                        nexus = false;
                        block.setType(Material.AIR);
                    }

                    BlockCount++;
                }
            }
        }

        if (nexus) {
            nexusLocation = spawnNexus(new Location(l.getWorld(), maxX, maxY, maxZ));
        }

    }

    public static Location spawnNexus(Location l) {

        Files config = getFiles("config.yml");

        l.setX(l.getX() + 0.5);
        l.setZ(l.getZ() + 0.5);

        Villager v = l.getWorld().spawn(l, Villager.class);

        ArrayList<String> list = (ArrayList<String>) config.getConfig().getStringList("nexus.uuid");
        list.add(v.getUniqueId().toString());
        config.getConfig().set("nexus.uuid", list);
        config.saveConfig();

        v.setCustomName("§2Nexus");
        v.setCustomNameVisible(true);
        v.setProfession(Villager.Profession.NITWIT);
        v.setAI(false);
        v.setSilent(true);
        v.setAdult();
        v.setCanPickupItems(false);
        v.setCollidable(true);

        UUID = v.getUniqueId();

        return v.getLocation();
    }

    @SuppressWarnings("unused")
    private static void setBlock(Block blockFrom, Block blockTo, boolean setBiome) {
        setBlock(blockFrom, blockTo);
        if (setBiome) {
            blockTo.setBiome(blockFrom.getBiome());
        }
    }

    @SuppressWarnings("deprecation")
    private static void setBlock(Block blockFrom, Block blockTo) {

        blockTo.setType(blockFrom.getType());
        blockTo.setData(blockFrom.getData());

        switch (blockFrom.getType()) {
            case STANDING_BANNER:
            case WALL_BANNER:

                Banner toBanner = (Banner) blockTo.getState();
                Banner fromBanner = (Banner) blockFrom.getState();

                toBanner.setBaseColor(fromBanner.getBaseColor());
                toBanner.setPatterns(fromBanner.getPatterns());
                toBanner.update();

                break;
            case BEACON:

                Beacon toBeacon = (Beacon) blockTo.getState();
                Beacon fromBeacon = (Beacon) blockFrom.getState();

                toBeacon.setPrimaryEffect(fromBeacon.getPrimaryEffect().getType());
                toBeacon.setSecondaryEffect(fromBeacon.getSecondaryEffect().getType());
                toBeacon.setCustomName(fromBeacon.getCustomName());
                toBeacon.setLock(fromBeacon.getLock());
                toBeacon.getInventory().setContents(fromBeacon.getInventory().getContents());
                toBeacon.update();

                break;
            case BED_BLOCK:

                Bed toBed = (Bed) blockTo.getState();
                Bed fromBed = (Bed) blockFrom.getState();

                toBed.setColor(fromBed.getColor());
                toBed.update();

                break;
            case BREWING_STAND:

                BrewingStand toBrewingStand = (BrewingStand) blockTo.getState();
                BrewingStand fromBrewingStand = (BrewingStand) blockFrom.getState();

                toBrewingStand.setBrewingTime(fromBrewingStand.getBrewingTime());
                toBrewingStand.setFuelLevel(fromBrewingStand.getFuelLevel());
                toBrewingStand.setCustomName(fromBrewingStand.getCustomName());
                toBrewingStand.getInventory().setContents(fromBrewingStand.getInventory().getContents());
                toBrewingStand.setLock(fromBrewingStand.getLock());
                toBrewingStand.update();

                break;
            case CHEST:
            case TRAPPED_CHEST:

                Chest toChest = (Chest) blockTo.getState();
                Chest fromChest = (Chest) blockFrom.getState();

                toChest.setCustomName(fromChest.getCustomName());
                try {
                    toChest.getInventory().setContents(fromChest.getInventory().getContents());
                } catch (IllegalArgumentException e) {
                    //
                }
                toChest.setLock(fromChest.getLock());
                toChest.update();

                break;
            case COMMAND_CHAIN:
            case COMMAND_REPEATING:
            case COMMAND:
                CommandBlock toCommandBlock = (CommandBlock) blockTo.getState();
                CommandBlock fromCommandBlock = (CommandBlock) blockFrom.getState();

                toCommandBlock.setCommand(fromCommandBlock.getCommand());
                toCommandBlock.setName(fromCommandBlock.getName());
                toCommandBlock.update();

                break;
            case MOB_SPAWNER:

                CreatureSpawner toCreatureSpawner = (CreatureSpawner) blockTo.getState();
                CreatureSpawner fromCreatureSpawner = (CreatureSpawner) blockFrom.getState();

                toCreatureSpawner.setDelay(fromCreatureSpawner.getDelay());
                toCreatureSpawner.setSpawnedType(fromCreatureSpawner.getSpawnedType());
                toCreatureSpawner.update();

                break;
            case DISPENSER:

                Dispenser toDispenser = (Dispenser) blockTo.getState();
                Dispenser fromDispenser = (Dispenser) blockFrom.getState();

                toDispenser.setCustomName(fromDispenser.getCustomName());
                toDispenser.setLock(fromDispenser.getLock());
                toDispenser.getInventory().setContents(fromDispenser.getInventory().getContents());
                toDispenser.update();

                break;
            case DROPPER:

                Dropper toDropper = (Dropper) blockTo.getState();
                Dropper fromDropper = (Dropper) blockFrom.getState();

                toDropper.setCustomName(fromDropper.getCustomName());
                toDropper.setLock(fromDropper.getLock());
                toDropper.getInventory().setContents(fromDropper.getInventory().getContents());
                toDropper.update();

                break;
            case ENCHANTMENT_TABLE:

                EnchantingTable toEnchantmentTable = (EnchantingTable) blockTo.getState();
                EnchantingTable fromEnchantmentTable = (EnchantingTable) blockFrom.getState();

                toEnchantmentTable.setCustomName(fromEnchantmentTable.getCustomName());
                toEnchantmentTable.update();

                break;
            case END_GATEWAY:

                EndGateway toEndGateway = (EndGateway) blockTo.getState();
                EndGateway fromEndGateway = (EndGateway) blockFrom.getState();

                toEndGateway.setExactTeleport(fromEndGateway.isExactTeleport());
                toEndGateway.setExitLocation(fromEndGateway.getExitLocation());
                toEndGateway.update();


                break;
            case FLOWER_POT:

                FlowerPot toFlower = (FlowerPot) blockTo.getState();
                FlowerPot fromFlower = (FlowerPot) blockFrom.getState();

                toFlower.setContents(fromFlower.getContents());
                toFlower.update();

                break;
            case FURNACE:

                Furnace toFurnace = (Furnace) blockTo.getState();
                Furnace fromFurnace = (Furnace) blockFrom.getState();

                toFurnace.setBurnTime(fromFurnace.getBurnTime());
                toFurnace.setCookTime(fromFurnace.getCookTime());
                toFurnace.setCustomName(fromFurnace.getCustomName());
                toFurnace.setLock(fromFurnace.getLock());
                toFurnace.getInventory().setContents(fromFurnace.getInventory().getContents());
                toFurnace.update();

                break;
            case HOPPER:

                Hopper toHopper = (Hopper) blockTo.getState();
                Hopper fromHopper = (Hopper) blockFrom.getState();

                toHopper.setCustomName(fromHopper.getCustomName());
                toHopper.setLock(fromHopper.getLock());
                toHopper.getInventory().setContents(fromHopper.getInventory().getContents());
                toHopper.update();

                break;
            case JUKEBOX:

                Jukebox toJukebox = (Jukebox) blockTo.getState();
                Jukebox fromJukebox = (Jukebox) blockFrom.getState();

                toJukebox.setPlaying(fromJukebox.getPlaying());
                toJukebox.update();

                break;
            case NOTE_BLOCK:

                NoteBlock toNoteBlock = (NoteBlock) blockTo.getState();
                NoteBlock fromNoteBlock = (NoteBlock) blockFrom.getState();

                toNoteBlock.setNote(fromNoteBlock.getNote());
                toNoteBlock.update();

                break;
            case SIGN_POST:
            case WALL_SIGN:

                Sign toSign = (Sign) blockTo.getState();
                Sign fromSign = (Sign) blockFrom.getState();

                for (int i = 0; i < 4; i++) {
                    toSign.setLine(i, fromSign.getLine(i));
                }
                toSign.update();

                break;
            case SKULL:

                Skull toSkull = (Skull) blockTo.getState();
                Skull fromSkull = (Skull) blockFrom.getState();

                toSkull.setSkullType(fromSkull.getSkullType());
                toSkull.setRotation(fromSkull.getRotation());
                toSkull.setOwner(fromSkull.getOwner());
                toSkull.update();

                break;
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

                ShulkerBox toShulkerBox = (ShulkerBox) blockTo.getState();
                ShulkerBox fromShulkerBox = (ShulkerBox) blockFrom.getState();

                toShulkerBox.setCustomName(fromShulkerBox.getCustomName());
                toShulkerBox.setLock(fromShulkerBox.getLock());
                toShulkerBox.getInventory().setContents(fromShulkerBox.getInventory().getContents());
                toShulkerBox.update();
                break;
        }
    }
}
