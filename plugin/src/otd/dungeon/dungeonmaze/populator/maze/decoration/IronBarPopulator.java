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
package otd.dungeon.dungeonmaze.populator.maze.decoration;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import otd.dungeon.dungeonmaze.populator.maze.MazeRoomBlockPopulator;
import otd.dungeon.dungeonmaze.populator.maze.MazeRoomBlockPopulatorArgs;

import java.util.Random;

public class IronBarPopulator extends MazeRoomBlockPopulator {

    /** General populator constants. */
	private static final int LAYER_MIN = 1;
	private static final int LAYER_MAX = 7;
    private static final int ROOM_ITERATIONS = 4;
	private static final float ROOM_ITERATIONS_CHANCE = .20f;

    /** Populator constants. */
	private static final float CHANCE_DOUBLE_HEIGHT = .66f;

	@Override
	public void populateRoom(MazeRoomBlockPopulatorArgs args) {
        final Chunk chunk = args.getSourceChunk();
        final Random rand = args.getRandom();
		final int x = args.getRoomChunkX();
		final int y = args.getChunkY();
		final int z = args.getRoomChunkZ();
		final int floorOffset = args.getFloorOffset();

        // Define the position variables
        int blockX, blockY, blockZ;

        // Determine the y position of the gap
        blockY = y + rand.nextInt(4 - floorOffset) + 1 + floorOffset;

        // Define the x and z position of the broken wall
        if(rand.nextBoolean()) {
            blockX = x + (rand.nextBoolean() ? 0 : 7);
            blockZ = z + rand.nextInt(6) + 1;

        } else {
            blockX = z + rand.nextInt(6) + 1;
            blockZ = x + (rand.nextBoolean() ? 0 : 7);
        }

        // Specify the bars base block
        Block barsBase = chunk.getBlock(blockX, blockY, blockZ);
        if(barsBase.getType() == Material.COBBLESTONE || barsBase.getType() == Material.MOSSY_COBBLESTONE || barsBase.getType() == Material.STONE_BRICKS) {
            // Set the block type to the iron bars
            barsBase.setType(Material.IRON_BARS);

            // Check whether bars of two blocks height should be spawned
            if(rand.nextFloat() < CHANCE_DOUBLE_HEIGHT) {
                Block block2 = chunk.getBlock(blockX, blockY + 1, blockZ);
                block2.setType(Material.IRON_BARS);
            }
        }
	}

    @Override
    public int getRoomIterations() {
        return ROOM_ITERATIONS;
    }

    @Override
    public float getRoomIterationsChance() {
        return ROOM_ITERATIONS_CHANCE;
    }

	/**
	 * Get the minimum layer
	 * @return Minimum layer
	 */
	@Override
	public int getMinimumLayer() {
		return LAYER_MIN;
	}
	
	/**
	 * Get the maximum layer
	 * @return Maximum layer
	 */
	@Override
	public int getMaximumLayer() {
		return LAYER_MAX;
	}
}
