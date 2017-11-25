package com.Stranded.events;

import com.Stranded.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.*;

public class CobbleGenEvent implements Listener {

    private static final List<BlockFace> FACES = Arrays.asList(BlockFace.SELF, BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST);
    private Main p;

    public CobbleGenEvent(Main main) {
        p = main;
    }

    private void setRandomBlock(Block block) {

        double cobble = 50;
        double stone = cobble + 5;
        double ironOre = stone + 10;
        double coalOre = ironOre + 10;
        double goldOre = coalOre + 6;
        double lapisOre = goldOre + 3;
        double diamondOre = lapisOre + 1;
        double redstoneOre = diamondOre + 3;
        double emeraltOre = redstoneOre + 1;

        Random r = new Random();
        double randomDouble = r.nextInt((int) emeraltOre);
        double randomInt = r.nextInt((int) emeraltOre);

        randomDouble += randomInt / 100;

        if (randomDouble > emeraltOre)
            randomDouble = emeraltOre;

        if (randomDouble <= cobble) block.setType(Material.COBBLESTONE);
        else if (randomDouble <= stone) block.setType(Material.STONE);
        else if (randomDouble <= ironOre) block.setType(Material.IRON_ORE);
        else if (randomDouble <= coalOre) block.setType(Material.COAL_ORE);
        else if (randomDouble <= goldOre) block.setType(Material.GOLD_ORE);
        else if (randomDouble <= lapisOre) block.setType(Material.LAPIS_ORE);
        else if (randomDouble <= diamondOre) block.setType(Material.DIAMOND_ORE);
        else if (randomDouble <= redstoneOre) block.setType(Material.REDSTONE_ORE);
        else if (randomDouble <= emeraltOre) block.setType(Material.EMERALD_ORE);


    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    @SuppressWarnings("unused")
    public void onCleanStoneGen(BlockFromToEvent e) {

        final Block to = e.getToBlock();
        final Material prev = to.getType();

        p.getServer().getScheduler().runTask(p, () -> {

                    if (((prev.equals(Material.WATER)) || (prev.equals(Material.STATIONARY_WATER))) && (to.getType().equals(Material.STONE))) {
//                    to.setType(prev);

                        setRandomBlock(to);

                    }
                }
        );

    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    @SuppressWarnings("unused")
    public void onCobbleGen(BlockFromToEvent e) {

        Block b = e.getBlock();
        if ((b.getType().equals(Material.WATER)) || (b.getType().equals(Material.STATIONARY_WATER)) || (b.getType().equals(Material.LAVA)) || (b.getType().equals(Material.STATIONARY_LAVA))) {

            Block toBlock = e.getToBlock();
            if ((toBlock.getType().equals(Material.AIR)) && (generatesCobble(b, toBlock))) {

                final List<Block> prevBlock = new ArrayList<>();
                final List<Material> prevMat = new ArrayList<>();
                for (BlockFace face : FACES) {
                    Block r = toBlock.getRelative(face);
                    prevBlock.add(r);
                    prevMat.add(r.getType());
                }

                p.getServer().getScheduler().runTask(p, () -> {

                    Iterator<Block> blockIt = prevBlock.iterator();
                    Iterator<Material> matIt = prevMat.iterator();
                    while ((blockIt.hasNext()) && (matIt.hasNext())) {
                        Block block = blockIt.next();
                        Material material = matIt.next();
                        if ((block.getType().equals(Material.COBBLESTONE)) && (!block.getType().equals(material))) {
                            setRandomBlock(block);
                        }
                    }
                });
            }
        }
    }

    private boolean generatesCobble(Block block, Block toBlock) {
        Material mirrorID1 = (block.getType().equals(Material.WATER)) || (block.getType().equals(Material.STATIONARY_WATER)) ? Material.LAVA : Material.WATER;
        Material mirrorID2 = (block.getType().equals(Material.WATER)) || (block.getType().equals(Material.STATIONARY_WATER)) ? Material.STATIONARY_LAVA : Material.STATIONARY_WATER;
        for (BlockFace face : FACES) {
            Block r = toBlock.getRelative(face);
            if ((r.getType().equals(mirrorID1)) || (r.getType().equals(mirrorID2))) {
                return true;
            }
        }
        return false;
    }
}
