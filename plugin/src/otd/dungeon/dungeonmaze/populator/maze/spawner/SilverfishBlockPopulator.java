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
package otd.dungeon.dungeonmaze.populator.maze.spawner;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import otd.dungeon.dungeonmaze.populator.maze.MazeRoomBlockPopulator;
import otd.dungeon.dungeonmaze.populator.maze.MazeRoomBlockPopulatorArgs;

import java.util.Random;

public class SilverfishBlockPopulator extends MazeRoomBlockPopulator {

    /** General populator constants. */
	private static final int LAYER_MIN = 3;
	private static final int LAYER_MAX = 7;
	private static final int ROOM_ITERATIONS = 8;
	private static final float ROOM_ITERATIONS_CHANCE = .75f;

    // TODO: Implement this feature!
	public static final double CHANCE_ADDITION_EACH_LEVEL = -4.167; /* to 75 */

	@Override
	public void populateRoom(MazeRoomBlockPopulatorArgs args) {
		Chunk chunk = args.getSourceChunk();
		Random rand = args.getRandom();
		final int x = args.getRoomChunkX();
		final int y = args.getChunkY();
		final int floorOffset = args.getFloorOffset();
		final int z = args.getRoomChunkZ();
        int blockX = x + rand.nextInt(8);
        int blockY = y + rand.nextInt(4 - floorOffset) + 1 + floorOffset;
        int blockZ = z + rand.nextInt(8);

        Block lanternBlock = chunk.getBlock(blockX, blockY, blockZ);
        if(lanternBlock.getType() == Material.STONE) {
            lanternBlock.setType(Material.INFESTED_STONE);
        } else if(lanternBlock.getType() == Material.COBBLESTONE) {
            lanternBlock.setType(Material.INFESTED_COBBLESTONE);
        } else if(lanternBlock.getType() == Material.MOSSY_COBBLESTONE) {
            lanternBlock.setType(Material.INFESTED_COBBLESTONE);
        } else if(lanternBlock.getType() == Material.STONE_BRICKS) {
            lanternBlock.setType(Material.INFESTED_STONE_BRICKS);
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
