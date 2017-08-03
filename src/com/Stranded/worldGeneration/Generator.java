package com.Stranded.worldGeneration;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Generator extends ChunkGenerator {

    /**
     *
     * @param x
     * X co-ordinate of the block to be set in the array
     * @param y
     * Y co-ordinate of the block to be set in the array
     * @param z
     * Z co-ordinate of the block to be set in the array
     * @param chunk
     * An array containing the Block id's of all the blocks in the chunk. The first offset
     * is the block section number. There are 16 block sections, stacked vertically, each of which
     * 16 by 16 by 16 blocks.
     * @param material
     * The material to set the block to.
     */
    void setBlock(int x, int y, int z, byte[][] chunk, Material material) {
        if (y < 256 && y >= 0 && x <= 16 && x >= 0 && z <= 16 && z >= 0) {
            if (chunk[y >> 4] == null)
                chunk[y >> 4] = new byte[16 * 16 * 16];
            chunk[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = (byte) material.getId();
        }
    }

    byte getBlock(int x, int y, int z, byte[][] chunk) {
        //if the Block section the block is in hasn't been used yet, allocate it
        if (chunk[y >> 4] == null)
            return 0; //block is air as it hasn't been allocated
        if (!(y <= 256 && y >= 0 && x <= 16 && x >= 0 && z <= 16 && z >= 0))
            return 0;
        try {
            return chunk[y >> 4][((y & 0xF) << 8) | (z << 4) | x];
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    @Override
/**
 *
 * @param world
 * The world the chunk belongs to
 * @param rand
 * Don't use this, make a new random object using the world seed (world.getSeed())
 * @param biome
 * Use this to set/get the current biome
 * @param ChunkX and ChunkZ
 * The x and z co-ordinates of the current chunk.
 */
    public byte[][] generateBlockSections(World world, Random rand, int ChunkX, int ChunkZ, BiomeGrid biomeGrid) {

        byte[][] chunk = new byte[world.getMaxHeight() / 16][];

//        for (int x=0; x<16; x++) { //loop through all of the blocks in the chunk that are lower than maxHeight
//            for (int z=0; z<16; z++) {
//                int maxHeight = 1; //how thick we want out flat terrain to be
//                for (int y=0;y<maxHeight;y++) {
//                    setBlock(x,y,z,chunk,Material.BEDROCK); //set the current block to stone
//                }
//            }
//        }

//        biomeGrid.setBiome( x, y, Biome.DEEP_OCEAN);


        SimplexOctaveGenerator gen1 = new SimplexOctaveGenerator(world, 8);
        gen1.setScale(1 / 64.0);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {

                biomeGrid.setBiome(x, z, Biome.DEEP_OCEAN); //set the biome

                int realX = x + ChunkX * 16; //used so that the noise function gives us
                int realZ = z + ChunkZ * 16; //different values each chunk
                double frequency = 5.0; // the reciprocal of the distance between points
                double amplitude = 0.1; // The distance between largest min and max values
                int multitude = 10; //how much we multiply the value between -1 and 1. It will determine how "steep" the hills will be.
                int height = 30;

                double maxHeight = gen1.noise(realX, realZ, frequency, amplitude) * multitude + height; //create the noise
                for (int y=0;y<maxHeight;y++) {
                    setBlock(x,y,z,chunk,Material.STONE); //set the current block to stone
                }
                setBlock(x, 0, z, chunk, Material.BEDROCK); //set lowest layer to bedrock

                for (int y =(int) maxHeight + 1; y <= 64; y++) {
                    setBlock(x, y, z, chunk, Material.WATER); //set the water
                }
            }
        }

//        SimplexOctaveGenerator gen2 = new SimplexOctaveGenerator(world, 8);
//        gen2.setScale(1 / 32.0);
//        for (int x = 0; x < 16; x++) {
//            for (int z = 0; z < 16; z++) {
//                int realX = x + ChunkX * 16; //used so that the noise function gives us
//                int realZ = z + ChunkZ * 16; //different values each chunk
//                double frequency = 1.0; // the reciprocal of the distance between points
//                double amplitude = 3.0; // The distance between largest min and max values
//                int multitude = 2; //how much we multiply the value between -1 and 1. It will determine how "steep" the hills will be.
//                double lowe = gen1.noise(realX, realZ, frequency, amplitude) * multitude / 4 + 100; //create the noise
//
//                for (int i = 0; i <= lowe; i++) {
//                        setBlock(x,i,z,chunk,Material.STONE); //set the current block to stone
//
//
//                }
//            }
//        }

//        SimplexOctaveGenerator gen2 = new SimplexOctaveGenerator(world,8);
//        gen2.setScale(1/32.0);
//
//        for (int x=0; x<16; x++) {
//            for (int z=0; z<16; z++) {
//
//                int realX = x + ChunkX * 16; //used so that the noise function gives us
//                int realZ = z + ChunkZ * 16; //different values each chunk
//                double frequency = 0.5; // the reciprocal of the distance between points
//                double amplitude = 0.5; // The distance between largest min and max values
//                int multitude = 64; //how much we multiply the value between -1 and 1. It will determine how "steep" the hills will be.
//                int sea_level = 64;
//
//                double maxHeight = gen1.noise(realX, realZ, frequency, amplitude) * multitude + sea_level;
//                for (int y=0;y<maxHeight;y++) {
//                    setBlock(x,y,z,chunk,Material.GRASS); //set the current block to stone
//                }
//            }
//        }
//
//        Math.max(gen1.getYScale(), gen2.getYScale());

//        SimplexOctaveGenerator overhangs = new SimplexOctaveGenerator(world,8);
//        SimplexOctaveGenerator bottoms = new SimplexOctaveGenerator(world,8);
//
//        overhangs.setScale(1/64.0); //little note: the .0 is VERY important
//        bottoms.setScale(1/128.0);
//
//        int overhangsMagnitude = 16; //used when we generate the noise for the tops of the overhangs
//        int bottomsMagnitude = 32;
//
//        for (int x=0; x<16; x++) {
//            for (int z=0; z<16; z++) {
//                int realX = x + ChunkX * 16;
//                int realZ = z + ChunkZ * 16;
//
//                int bottomHeight = (int) (bottoms.noise(realX, realZ, 0.8, 0.5) * bottomsMagnitude + 64);
//                int maxHeight = (int) overhangs.noise(realX, realZ, 0.5, 0.5) * overhangsMagnitude + bottomHeight + 16;
//                double threshold = 0.3;
//
//                //make the terrain
//                for (int y=0; y<maxHeight; y++) {
//                    if (y > bottomHeight) { //part where we do the overhangs
//                        double density = overhangs.noise(realX, y, realZ, 0.5, 0.5);
//
//                        if (density > threshold) setBlock(x,y,z,chunk,Material.STONE);
//
//                    } else {
//                        setBlock(x,y,z,chunk,Material.STONE);
////                        biomeGrid.setBiome(x, y, Biome.MESA);
//                    }
//                }
//
//                //turn the tops into grass
//                setBlock(x,bottomHeight,z,chunk,Material.GRASS); //the top of the base hills
//                setBlock(x,bottomHeight - 1,z,chunk,Material.DIRT);
//                setBlock(x,bottomHeight - 2,z,chunk,Material.DIRT);
//
//                for (int y=bottomHeight + 1; y>bottomHeight && y < maxHeight; y++ ) { //the overhang
//                    int thisblock = getBlock(x, y, z, chunk);
//                    int blockabove = getBlock(x, y+1, z, chunk);
//
//                    if(thisblock != Material.AIR.getId() && blockabove == Material.AIR.getId()) {
//                        setBlock(x, y, z, chunk, Material.GRASS);
//                        if(getBlock(x, y-1, z, chunk) != Material.AIR.getId())
//                            setBlock(x, y-1, z, chunk, Material.DIRT);
//                        if(getBlock(x, y-2, z, chunk) != Material.AIR.getId())
//                            setBlock(x, y-2, z, chunk, Material.DIRT);
//                    }
//                }
//
//                setBlock(x, 1, z, chunk, Material.BEDROCK);
//                biomeGrid.setBiome(x, z, Biome.DEEP_OCEAN);
//
//            }
//        }

        return chunk;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
      return Arrays.asList((BlockPopulator)
//              new MoonCraterPopulator(),
              new FlagPopulator(),
              new IslandPopulator());
//        return new ArrayList<BlockPopulator>();
    }

//    @Override
//    public List<BlockPopulator> getDefaultPopulators(World world) {
//        return Arrays.asList((BlockPopulator)new MoonCraterPopulator(), new FlagPopulator());
//    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
//        int x = random.nextInt(200) - 100;
//        int z = random.nextInt(200) - 100;
//        int y = world.getHighestBlockYAt(x, z);
//        return new Location(world, x, y, z);
        return new Location(world, 0, 64, 0);
    }
}