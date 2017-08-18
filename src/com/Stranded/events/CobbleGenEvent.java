package com.Stranded.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.Random;

public class CobbleGenEvent implements Listener {

    @EventHandler
    @SuppressWarnings({"unused", "deprecation"})
    public void onFromTo(BlockFromToEvent event) {
        int id = event.getBlock().getTypeId();
        if (id >= 8 && id <= 11) {
            Block b = event.getToBlock();
            int toID = b.getTypeId();
            if (toID == 0) {
                if (generatesCobble(id, b)) {
                    Random pick = new Random();
                    int chance = 0;
                    for (int counter = 1; counter <= 1; counter++) {
                        chance = 1 + pick.nextInt(100);
                    }

                    double cobble = 50; // 50
                    double stone = cobble + 5; // 55
                    double coal = stone + 20; // 75
                    double iron = coal + 12; // 87
                    double gold = iron + 5; // 92
                    double redstone = gold + 3; // 95
                    double emerald = redstone + 1; // 96
                    double diamond = emerald + 1; // 97
                    double lapis = diamond + 3; //100

                    if (chance > 0 && chance <= cobble) b.setType(Material.COBBLESTONE);
                    if (chance > cobble && chance <= stone) b.setType(Material.STONE);
                    if (chance > stone && chance <= coal) b.setType(Material.COAL_ORE);
                    if (chance > coal && chance <= iron) b.setType(Material.IRON_ORE);
                    if (chance > iron && chance <= gold) b.setType(Material.GOLD_ORE);
                    if (chance > gold && chance <= redstone) b.setType(Material.REDSTONE_ORE);
                    if (chance > redstone && chance <= emerald) b.setType(Material.DIAMOND_ORE);
                    if (chance > emerald && chance <= diamond) b.setType(Material.EMERALD_ORE);
                    if (chance > diamond && chance <= lapis) b.setType(Material.LAPIS_ORE);

                    }
                }
            }
        }
    private final BlockFace[] faces = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };

    @SuppressWarnings("deprecation")
    private boolean generatesCobble(int id, Block b) {
        int mirrorID1 = (id == 8 || id == 9 ? 10 : 8);
        int mirrorID2 = (id == 8 || id == 9 ? 11 : 9);
        for (BlockFace face : faces) {
            Block r = b.getRelative(face, 1);
            if (r.getTypeId() == mirrorID1 || r.getTypeId() == mirrorID2) {
                return true;
            }
        }
        return false;
    }
}
