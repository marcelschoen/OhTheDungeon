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

import org.bukkit.Chunk;
import org.bukkit.Material;
import otd.dungeon.dungeonmaze.populator.surface.SurfaceBlockPopulator;
import otd.dungeon.dungeonmaze.populator.surface.SurfaceBlockPopulatorArgs;

import java.util.Random;

public class FlowerPopulator extends SurfaceBlockPopulator {
    
    private static final Material FLOWERS[] = {
        Material.POPPY,
        Material.BLUE_ORCHID,
        Material.ALLIUM,
        Material.AZURE_BLUET,
        Material.RED_TULIP,
        Material.ORANGE_TULIP,
        Material.WHITE_TULIP,
        Material.PINK_TULIP,
        Material.OXEYE_DAISY,
        Material.LILY_OF_THE_VALLEY,
        Material.CORNFLOWER,
        Material.SWEET_BERRY_BUSH,
    };

    /** General populator constants. */
    private static final int CHUNK_ITERATIONS = 10;
    private static final float CHUNK_ITERATIONS_CHANCE = .15f;

	@Override
	public void populateSurface(SurfaceBlockPopulatorArgs args) {
		final Chunk chunk = args.getSourceChunk();
		final Random rand = args.getRandom();
        final int xFlower = rand.nextInt(16);
        final int zFlower = rand.nextInt(16);

        // Get the surface level at the location of the flower
        final int ySurface = args.getSurfaceLevel(xFlower, zFlower);

        // Make sure the surface block is grass
        if(chunk.getBlock(xFlower, ySurface, zFlower).getType() == Material.GRASS_BLOCK) {
            final int flowerY = ySurface + 1;

            // Spawn the flower
            if (rand.nextInt(2) == 0)
                chunk.getBlock(xFlower, flowerY, zFlower).setType(Material.DANDELION);

            else {
                chunk.getBlock(xFlower, flowerY, zFlower).setType(FLOWERS[rand.nextInt(FLOWERS.length)]);
            }
        }
	}
	
	/**
	 * Get a random flower type
	 * @param rand Random instance
	 * @return Random flower type ID
	 */
	public byte getRandomFlowerType(Random rand) {
		return (byte) (rand.nextInt(9));
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