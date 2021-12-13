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

public class TorchPopulator extends MazeRoomBlockPopulator {

    // TODO: Torches on the walls!

    /** General populator constants. */
	private static final int LAYER_MIN = 2;
	private static final int LAYER_MAX = 7;
	private static final float ROOM_CHANCE = .1f;

    // TODO: Implement this!
	public static final double CHANCE_TORCH_ADDITION_EACH_LEVEL = 3.333; /* to 30 */

	@Override
	public void populateRoom(MazeRoomBlockPopulatorArgs args) {
        final Chunk chunk = args.getSourceChunk();
        final Random rand = args.getRandom();
		final int x = args.getRoomChunkX();
		final int z = args.getRoomChunkZ();
        final int torchX = x + rand.nextInt(6) + 1;
        final int torchY = args.getFloorY() + 1;
        final int torchZ = z + rand.nextInt(6) + 1;
								
        if(chunk.getBlock(torchX, torchY - 1, torchZ).getType() != Material.AIR) {
            Block torchBlock = chunk.getBlock(torchX, torchY, torchZ);
            if(torchBlock.getType() == Material.AIR) {
                torchBlock = chunk.getBlock(torchX, torchY, torchZ);
                torchBlock.setType(Material.TORCH);
            }
        }
	}

    @Override
    public float getRoomChance() {
        return ROOM_CHANCE;
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