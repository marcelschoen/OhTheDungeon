/* 
 * Copyright (C) 2021 shadow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package otd.dungeon.dungeonmaze.populator.surface.plants;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import otd.dungeon.dungeonmaze.populator.surface.SurfaceBlockPopulator;
import otd.dungeon.dungeonmaze.populator.surface.SurfaceBlockPopulatorArgs;

import java.util.Random;

public class TreePopulator extends SurfaceBlockPopulator {

    /** General populator constants. */
    private static final int CHUNK_ITERATIONS = 10;
    private static final float CHUNK_ITERATIONS_CHANCE = .1f;

	@Override
	public void populateSurface(SurfaceBlockPopulatorArgs args) {
		final World world = args.getWorld();
		final Chunk chunk = args.getSourceChunk();
		final Random rand = args.getRandom();

        // Determine the position of the tree in the chunk
        final int xTree = rand.nextInt(16);
        final int zTree = rand.nextInt(16);

        // Get the surface block at the trees position, and make sure it's valid
        Block surfaceBlock = world.getHighestBlockAt(chunk.getX() * 16 + xTree, chunk.getZ() * 16 + zTree);
        surfaceBlock = surfaceBlock.getRelative(BlockFace.DOWN);

        // Get the surface level
        final int ySurface = surfaceBlock.getY();
        final int yTree = ySurface + 1;

        // Get the biome
        final Biome biome = chunk.getWorld().getBiome((chunk.getX() * 16) + xTree, (chunk.getZ() * 16) + zTree);

        // Spawn the proper tree
        if(surfaceBlock.getType() == Material.GRASS_BLOCK) {
            if(biome.equals(Biome.FOREST))
                world.generateTree(new Location(world, (chunk.getX() * 16) + xTree, yTree, (chunk.getZ() * 16) + zTree), TreeType.BIRCH);

//            else if(biome.equals(Biome.JUNGLE) || biome.equals(Biome.JUNGLE_HILLS)) {
            else if(biome.equals(Biome.JUNGLE)) {
                switch(rand.nextInt(3)) {
                case 0:
                    world.generateTree(new Location(world, (chunk.getX() * 16) + xTree, yTree, (chunk.getZ() * 16) + zTree), TreeType.JUNGLE);
                    break;

                case 1:
                    world.generateTree(new Location(world, (chunk.getX() * 16) + xTree, yTree, (chunk.getZ() * 16) + zTree), TreeType.SMALL_JUNGLE);
                    break;

                case 2:
                    world.generateTree(new Location(world, (chunk.getX() * 16) + xTree, yTree, (chunk.getZ() * 16) + zTree), TreeType.JUNGLE_BUSH);
                    break;
                }

//            } else if(biome.equals(Biome.MUSHROOM_FIELDS) || biome.equals(Biome.MUSHROOM_FIELD_SHORE)) {
            } else if(biome.equals(Biome.MUSHROOM_FIELDS)) {
                switch(rand.nextInt(2)) {
                case 0:
                    world.generateTree(new Location(world, (chunk.getX() * 16) + xTree, yTree, (chunk.getZ() * 16) + zTree), TreeType.RED_MUSHROOM);
                    break;

                case 1:
                    world.generateTree(new Location(world, (chunk.getX() * 16) + xTree, yTree, (chunk.getZ() * 16) + zTree), TreeType.BROWN_MUSHROOM);
                    break;
                }

//            } else if(biome.equals(Biome.SWAMP) || biome.equals(Biome.SWAMP_HILLS))
            } else if(biome.equals(Biome.SWAMP))
                chunk.getWorld().generateTree(new Location(world, (chunk.getX() * 16) + xTree, yTree, (chunk.getZ() * 16) + zTree), TreeType.SWAMP);

//            else if(biome.equals(Biome.TAIGA) || biome.equals(Biome.TAIGA_HILLS)) {
            else if(biome.equals(Biome.TAIGA)) {
                switch(rand.nextInt(2)) {
                case 0:
                    world.generateTree(new Location(world, (chunk.getX() * 16) + xTree, yTree, (chunk.getZ() * 16) + zTree), TreeType.REDWOOD);
                    break;

                case 1:
                    world.generateTree(new Location(world, (chunk.getX() * 16) + xTree, yTree, (chunk.getZ() * 16) + zTree), TreeType.TALL_REDWOOD);
                    break;
                }

            } else
                world.generateTree(new Location(world, (chunk.getX() * 16) + xTree, yTree, (chunk.getZ() * 16) + zTree), TreeType.TREE);

        } else if(surfaceBlock.getType() == Material.SAND)
//            if(biome.equals(Biome.DESERT) || biome.equals(Biome.DESERT_HILLS)) {
            if(biome.equals(Biome.DESERT)) {
                // Determine the cactus height
                int cactusHeight = rand.nextInt(3);

                // Place the cactus
                for(int i = 0; i < cactusHeight; i++)
                    chunk.getBlock(xTree, yTree + i, zTree).setType(Material.CACTUS);
            }
	}

    @Override
    public int getChunkIterations() {
        return CHUNK_ITERATIONS;
    }

    @Override
    public float getChunkIterationsChance() {
        return CHUNK_ITERATIONS_CHANCE;
    }
}