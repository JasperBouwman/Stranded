package com.Stranded.worldGeneration.warIsland;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class BlockData {

    private String blockData; //this is saved @ this formation: x\y\z\Data\[true]

    public BlockData(Location L1, Location L2, Block block) {
        int minX = Math.min(L1.getBlockX(), L2.getBlockX());
        int minY = Math.min(L1.getBlockY(), L2.getBlockY());
        int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
        int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
        int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
        int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

        int x = maxX - minX;
        int y = maxY - minY;
        int z = maxZ - minZ;

        byte b = block.getData();

        blockData = "BlockData\\" + x + "\\" + y + "\\" + z + "\\" + b + "\\";

    }

    public BlockData(Location L1, Location L2, Block block, int extraDataID) {
        int minX = Math.min(L1.getBlockX(), L2.getBlockX());
        int minY = Math.min(L1.getBlockY(), L2.getBlockY());
        int minZ = Math.min(L1.getBlockZ(), L2.getBlockZ());
        int maxX = Math.max(L1.getBlockX(), L2.getBlockX());
        int maxY = Math.max(L1.getBlockY(), L2.getBlockY());
        int maxZ = Math.max(L1.getBlockZ(), L2.getBlockZ());

        int x = maxX - minX;
        int y = maxY - minY;
        int z = maxZ - minZ;

        byte b = block.getData();

        if (extraDataID >= 0) {
            blockData = "BlockData\\" + x + "\\" + y + "\\" + z + "\\"  + b + "\\" + extraDataID + "\\";
        } else {
            blockData = "BlockData\\" + x + "\\" + y + "\\" + z + "\\" + b + "\\";
        }
    }


    public BlockData(String blockData) {
        this.blockData = blockData;
    }

    public Location toLocation(Location main) {

        int x = getX();
        int y = getY();
        int z = getZ();

        int lx = main.getBlockX();
        int ly = main.getBlockY();
        int lz = main.getBlockZ();

        return new Location(main.getWorld(), lx - x, ly - y, lz - z);
    }

    public ArrayList<BlockData> splitBlockData() {
        String[] blockData = this.blockData.split("\\\\\\\\");
        ArrayList<BlockData> list = new ArrayList<>();
        for (String s : blockData) {
            list.add(new BlockData(s));
        }

        return list;
    }

    public int getX() {
        return Integer.parseInt(blockData.split("\\\\")[1]);
    }

    public int getY() {
        return Integer.parseInt(blockData.split("\\\\")[2]);
    }

    public int getZ() {
        return Integer.parseInt(blockData.split("\\\\")[3]);
    }

    public byte getData() {
        return Byte.parseByte(blockData.split("\\\\")[4]);
    }

    public boolean hasExtraData() {
        return (blockData.split("\\\\").length) == 6;
    }

    public int getExtraDataID() {
        return Integer.parseInt(blockData.split("\\\\")[5]);
    }

    public String getBlockData() {
        return blockData;
    }

    public void setBlockData(String blockData) {
        this.blockData = blockData;
    }
}
