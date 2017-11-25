package com.Stranded.worldGeneration.warIsland;

import com.Stranded.Files;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportWarIsland {

    public static List<String> getBlocks(String blocks) {
        return Arrays.asList(blocks.split("/"));
    }

    @SuppressWarnings("deprecation")
    public static Material getMaterial(String blockData) {
        try {
            return Material.getMaterial(Integer.parseInt(blockData.split("\\\\")[0]));
        } catch (NumberFormatException e) {
            return Material.AIR;
        }
    }

    public static boolean hasExtraData(String blockData) {
        String[] block = blockData.split("\\\\");
        if (block.length > 1) {
            if (block[block.length - 1].startsWith("D")) {
                return true;
            }
        }
        return false;
    }

    public static int getExtraDataID(String blockData) {
        String[] block = blockData.split("\\\\");
        if (block.length > 1) {
            if (block[block.length - 1].startsWith("D")) {
                return Integer.parseInt(block[block.length - 1].replace("D", ""));
            }
        }
        return -1;
    }

    public static boolean hasRepeat(String blockData) {
        for (String s : blockData.split("\\\\")) {
            if (s.startsWith("X")) {
                return true;
            }
        }
        return false;
    }

    public static int getRepeat(String blockData) {
        for (String s : blockData.split("\\\\")) {
            if (s.startsWith("X")) {
                return Integer.parseInt(s.replace("X", ""));
            }
        }
        return 0;
    }

    public static byte getData(String blockData) {
        String[] block = blockData.split("\\\\");
        if (block.length > 1) {
            if (!block[1].startsWith("D") && !block[1].startsWith("X")) {
                return Byte.parseByte(block[1]);
            }
        }
        return 0;
    }

    public static Location getLocation(Location location, int width, int height, int length) {

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return new Location(location.getWorld(), x - width, y - height, z - length);
    }

    @SuppressWarnings("All")
    public static void importWarIsland(Files island, Player player) {

        int y = island.getConfig().getInt("warIsland.y");

        int totalWidth = island.getConfig().getInt("warIsland.width");
        int totalHeight = island.getConfig().getInt("warIsland.height");
        int totalLength = island.getConfig().getInt("warIsland.length");
        int width = totalWidth;
        int height = totalHeight;
        int length = totalLength;

        Location temp = player.getLocation().clone();
        temp.setY(y);

        for (String blocksID : island.getConfig().getConfigurationSection("warIsland.region.blocks").getKeys(false)) {

            String BlockSet = island.getConfig().getString("warIsland.region.blocks." + blocksID);

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
                                case STANDING_BANNER:
                                case WALL_BANNER:
                                    Banner banner = (Banner) l.getBlock().getState();

                                    List<Pattern> patterns = (List<Pattern>) island.getConfig().get("warIsland.region.extraData." + extraDataID + ".banner.pattern");
                                    DyeColor color = DyeColor.valueOf(island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".banner.baseColor"));

                                    banner.setPatterns(patterns);
                                    banner.setBaseColor(color);

                                    break;
                                case BEACON:

                                    Beacon beacon = (Beacon) l.getBlock().getState();

                                    PotionEffect primary = (PotionEffect) island.getConfig().get("warIsland.region.extraData." + extraDataID + ".beacon.primaryEffect");
                                    PotionEffect secondary = (PotionEffect) island.getConfig().get("warIsland.region.extraData." + extraDataID + ".beacon.secondaryEffect");
                                    String customName = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".beacon.customName");
                                    String lock = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".beacon.lock");
                                    ArrayList<ItemStack> content = (ArrayList<ItemStack>) island.getConfig().get("warIsland.region.extraData." + extraDataID + ".beacon.inventory");

                                    beacon.setPrimaryEffect(primary.getType());
//                                    beacon.setSecondaryEffect(secondary.getType());
                                    beacon.setCustomName(customName);
                                    beacon.setLock(lock);
                                    int i = 0;
                                    for (ItemStack is : content) {
                                        beacon.getInventory().setItem(i, is);
                                        i++;
                                    }

                                    break;
                                case BED_BLOCK:

                                    Bed bed = (Bed) l.getBlock().getState();

                                    color = DyeColor.valueOf(island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".bed.color"));
                                    bed.setColor(color);
                                    bed.update();

                                    break;
                                case BREWING_STAND:

                                    BrewingStand brewingStand = (BrewingStand) l.getBlock().getState();

                                    int time = island.getConfig().getInt("warIsland.region.extraData." + extraDataID + ".brewingStand.brewingTime");
                                    int level = island.getConfig().getInt("warIsland.region.extraData." + extraDataID + ".brewingStand.fuelLevel");
                                    customName = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".brewingStand.customName");
                                    content = (ArrayList<ItemStack>) island.getConfig().get("warIsland.region.extraData." + extraDataID + ".brewingStand.inventory");
                                    lock = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".brewingStand.lock");

                                    brewingStand.setBrewingTime(time);
                                    brewingStand.setFuelLevel(level);
                                    brewingStand.setCustomName(customName);
                                    brewingStand.setLock(lock);
                                    i = 0;
                                    for (ItemStack is : content) {
                                        brewingStand.getInventory().setItem(i, is);
                                        i++;
                                    }
                                    break;
                                case CHEST:
                                case TRAPPED_CHEST:

                                    Chest chest = (Chest) l.getBlock().getState();

                                    customName = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".chest.customName");
                                    content = ((ArrayList<ItemStack>) island.getConfig().get("warIsland.region.extraData." + extraDataID + ".chest.inventory"));
                                    lock = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".chest.lock");
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
                                case COMMAND_CHAIN:
                                case COMMAND_REPEATING:
                                case COMMAND:

                                    CommandBlock commandBlock = (CommandBlock) l.getBlock().getState();

                                    String command = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".commandBlock.command");
                                    String name = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".commandBlock.name");

                                    commandBlock.setCommand(command);
                                    commandBlock.setName(name);

                                    break;
                                case MOB_SPAWNER:

                                    CreatureSpawner creatureSpawner = (CreatureSpawner) l.getBlock().getState();

                                    int delay = island.getConfig().getInt("warIsland.region.extraData." + extraDataID + ".spawner.delay");
                                    String entityType = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".spawner.spawnedType");

                                    creatureSpawner.setDelay(delay);
                                    creatureSpawner.setSpawnedType(EntityType.valueOf(entityType));

                                    break;
                                case DISPENSER:

                                    Dispenser dispenser = (Dispenser) l.getBlock().getState();

                                    customName = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".dispenser.customName");
                                    lock = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".dispenser.lock");
                                    content = (ArrayList<ItemStack>) island.getConfig().get("warIsland.region.extraData." + extraDataID + ".dispenser.inventory");

                                    dispenser.setCustomName(customName);
                                    dispenser.setLock(lock);
                                    i = 0;
                                    for (ItemStack is : content) {
                                        dispenser.getInventory().setItem(i, is);
                                        i++;
                                    }

                                    break;
                                case DROPPER:
                                    Dropper dropper = (Dropper) l.getBlock().getState();

                                    customName = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".dropper.customName");
                                    lock = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".dropper.lock");
                                    content = (ArrayList<ItemStack>) island.getConfig().get("warIsland.region.extraData." + extraDataID + ".dropper.inventory");

                                    dropper.setCustomName(customName);
                                    dropper.setLock(lock);
                                    i = 0;
                                    for (ItemStack is : content) {
                                        dropper.getInventory().setItem(i, is);
                                        i++;
                                    }

                                    break;
                                case ENCHANTMENT_TABLE:
                                    EnchantingTable enchantingTable = (EnchantingTable) l.getBlock().getState();

                                    customName = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".enchantmentTable.customName");

                                    enchantingTable.setCustomName(customName);

                                    break;
                                case FLOWER_POT:
//                                    FlowerPot flowerPot = (FlowerPot) l.getBlock().getState();
//
//                                    ItemStack contentItem = island.getConfig().getItemStack("warIsland.region.extraData." + extraDataID + ".flowerPot.content");
//
//                                    flowerPot.setContents(new MaterialData(contentItem.getType(), contentItem.getData().getData()));

                                    break;
                                case FURNACE:
                                    Furnace furnace = (Furnace) l.getBlock().getState();

                                    short burnTime = Short.parseShort(island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".furnace.burnTime"));
                                    short cookTime = Short.parseShort(island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".furnace.cookTime"));
                                    customName = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".furnace.customName");
                                    lock = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".furnace.lock");
                                    content = (ArrayList<ItemStack>) island.getConfig().get("warIsland.region.extraData." + extraDataID + ".furnace.inventory");

                                    furnace.setBurnTime(burnTime);
                                    furnace.setCookTime(cookTime);
                                    furnace.setCustomName(customName);
                                    furnace.setLock(lock);
                                    i = 0;
                                    for (ItemStack is : content) {
                                        furnace.getInventory().setItem(i, is);
                                        i++;
                                    }

                                    break;
                                case HOPPER:
                                    Hopper hopper = (Hopper) l.getBlock().getState();

                                    customName = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".hopper.customName");
                                    lock = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".hopper.lock");
                                    content = (ArrayList<ItemStack>) island.getConfig().get("warIsland.region.extraData." + extraDataID + ".hopper.inventory");

                                    hopper.setCustomName(customName);
                                    hopper.setLock(lock);
                                    i = 0;
                                    for (ItemStack is : content) {
                                        hopper.getInventory().setItem(i, is);
                                        i++;
                                    }

                                    break;
                                case JUKEBOX:

                                    Jukebox jukebox = (Jukebox) l.getBlock().getState();
                                    Material disk = Material.getMaterial(island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".jukebox.playing"));
                                    jukebox.setPlaying(disk);
                                    break;
                                case NOTE_BLOCK:
                                    NoteBlock noteBlock = (NoteBlock) l.getBlock().getState();
                                    Note note = (Note) island.getConfig().get("warIsland.region.extraData." + extraDataID + ".noteBlock.note");
                                    noteBlock.setNote(note);
                                case SIGN_POST:
                                case WALL_SIGN:
                                    Sign sign = (Sign) l.getBlock().getState();
                                    for (i = 0; i < 4; i++) {
                                        String line = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".sign." + i);
                                        sign.setLine(i, line);
                                    }
                                    sign.update();

                                    break;
                                case SKULL:
//                                    Skull skull = (Skull) l.getBlock().getState();
//
//                                    String skullType = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".skull.skullType");
//                                    String rotation = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".skull.rotation");
////                                    String owner = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".skull.owner");
//
//                                    SkullType skullType1 = SkullType.valueOf(skullType);
//                                    BlockFace blockFace = BlockFace.valueOf(rotation);
//
//                                    skull.setSkullType(skullType1);
//                                    skull.setRotation(blockFace);
////                                    skull.setOwner(owner);

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

                                    ShulkerBox shulkerBox = (ShulkerBox) l.getBlock().getState();

                                    customName = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".shulkerBox.customName");
                                    lock = island.getConfig().getString("warIsland.region.extraData." + extraDataID + ".shulkerBox.lock");
                                    content = (ArrayList<ItemStack>) island.getConfig().get("warIsland.region.extraData." + extraDataID + ".shulkerBox.inventory");

                                    shulkerBox.setCustomName(customName);
                                    shulkerBox.setLock(lock);
                                    i = 0;
                                    for (ItemStack is : content) {
                                        shulkerBox.getInventory().setItem(i, is);
                                        i++;
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
