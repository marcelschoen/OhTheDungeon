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
package otd.dungeon.dungeonmaze.populator.maze.structure;

import org.bukkit.Chunk;
import org.bukkit.Material;
import otd.dungeon.dungeonmaze.populator.maze.MazeRoomBlockPopulator;
import otd.dungeon.dungeonmaze.populator.maze.MazeRoomBlockPopulatorArgs;
import otd.dungeon.dungeonmaze.util.NumberUtils;

import java.util.Random;

public class StrutPopulator extends MazeRoomBlockPopulator {

    /** General populator constants. */
	private static final int LAYER_MIN = 2;
	private static final int LAYER_MAX = 7;
	private static final int ROOM_CHANCE = 2;

    /** Populator constants. */
	private static final float CHANCE_STRUT_NEAR_SPAWN = .5f;
	private static final int STRUT_DISTANCE_NEAR_SPAWN_MAX = 4; // Distance in chunks

	@Override
	public void populateRoom(MazeRoomBlockPopulatorArgs args) {
		final Chunk chunk = args.getSourceChunk();
		final Random rand = args.getRandom();
        final int x = args.getRoomChunkX();
        final int yFloor = args.getFloorY();
        final int yCeiling = args.getCeilingY();
        final int z = args.getRoomChunkZ();
		
		// Make sure the distance between the spawn and the current chunk is allowed
		if(NumberUtils.distanceFromZero(chunk.getX(), chunk.getZ()) < STRUT_DISTANCE_NEAR_SPAWN_MAX) {
			// Strut near spawn
			if (rand.nextFloat() < CHANCE_STRUT_NEAR_SPAWN) {
				final int yStrutBar = yCeiling - 1;
				
				if(chunk.getBlock(x + 2, yStrutBar, z).getType() == Material.AIR) {
					// Generate strut bar
					for(int xx = 1; xx < 7; xx++)
						chunk.getBlock(x + xx, yStrutBar, z).setType(Material.OAK_PLANKS);
						
					// Generate strut poles
					for(int yy = yFloor + 1; yy < yStrutBar; yy++) {
						chunk.getBlock(x + 1, yy, z).setType(Material.OAK_FENCE);
						chunk.getBlock(x + 6, yy, z).setType(Material.OAK_FENCE);
					}
				}	
			}
			if (rand.nextInt(100) < CHANCE_STRUT_NEAR_SPAWN) {
				final int yStrutBar = yCeiling - 1;

				if(chunk.getBlock(x, yStrutBar, z + 2).getType() == Material.AIR) {
					// Generate strut bar
					for(int zz = 1; zz < 7; zz++)
						chunk.getBlock(x, yStrutBar, z + zz).setType(Material.OAK_PLANKS);

					// Generate strut poles
					for(int yy = yFloor + 1; yy < yStrutBar; yy++) {
						chunk.getBlock(x, yy, z+1).setType(Material.OAK_FENCE);
						chunk.getBlock(x, yy, z+6).setType(Material.OAK_FENCE);
					}
				}
				
			}
		} else {
			// Normal strut
			if(rand.nextInt(100) < ROOM_CHANCE) {
				final int yStrutBar = yCeiling - 1;
				
				if(chunk.getBlock(x + 2, yStrutBar, z).getType() == Material.AIR) {
					// Generate strut bar
					for(int xx = 1; xx < 7; xx++)
						chunk.getBlock(x + xx, yStrutBar, z).setType(Material.OAK_PLANKS);
						
					// Generate strut poles
					for(int yy = yFloor + 1; yy < yStrutBar; yy++) {
						chunk.getBlock(x+1, yy, z).setType(Material.OAK_FENCE);
						chunk.getBlock(x+6, yy, z).setType(Material.OAK_FENCE);
					}
				}
					
			}
			if (rand.nextInt(100) < ROOM_CHANCE) {
				final int yStrutBar = yCeiling - 1;

				if(chunk.getBlock(x, yStrutBar, z + 2).getType() == Material.AIR) {
					// Generate strut bar
					for(int zz = 1; zz < 7; zz++)
						chunk.getBlock(x, yStrutBar, z + zz).setType(Material.OAK_PLANKS);
						
					// Generate strut poles
					for(int yy = yFloor + 1; yy < yStrutBar; yy++) {
						chunk.getBlock(x, yy, z+1).setType(Material.OAK_FENCE);
						chunk.getBlock(x, yy, z+6).setType(Material.OAK_FENCE);
					}
				}
					
			}
		}
	}

    @Override
    public float getRoomChance() {
        // TODO: Improve this!
        return 1.0f;
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