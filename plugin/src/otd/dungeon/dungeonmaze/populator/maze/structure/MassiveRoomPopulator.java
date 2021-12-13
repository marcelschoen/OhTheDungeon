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
import org.bukkit.World;
import otd.dungeon.dungeonmaze.populator.maze.MazeRoomBlockPopulator;
import otd.dungeon.dungeonmaze.populator.maze.MazeRoomBlockPopulatorArgs;

import java.util.Random;

public class MassiveRoomPopulator extends MazeRoomBlockPopulator {

    /** General populator constants. */
	private static final int LAYER_MIN = 1;
	private static final int LAYER_MAX = 7;
	private static final float ROOM_CHANCE = .005f;
        
        public boolean const_room = true;
        @Override
        public boolean getConstRoom() {
            return const_room;
        }

	@Override
	public void populateRoom(MazeRoomBlockPopulatorArgs args) {
		final World world = args.getWorld();
		final Chunk chunk = args.getSourceChunk();
		final Random rand = args.getRandom();
		final int x = args.getRoomChunkX();
		final int y = args.getChunkY();
		final int yFloor = args.getFloorY();
		final int yCeiling = args.getCeilingY();
		final int z = args.getRoomChunkZ();

//        // Register the current room as constant room
//        ////DungeonMaze.instance.registerConstantRoom(world.getName(), chunk, x, y, z);

        // Walls
        for(int x2 = x; x2 <= x + 7; x2 += 1) {
            for(int y2 = yFloor + 1; y2 <= yCeiling - 1; y2 += 1) {
                chunk.getBlock(x2, y2, z).setType(Material.STONE_BRICKS);
                chunk.getBlock(x2, y2, z + 7).setType(Material.STONE_BRICKS);
            }
        }
        for(int z2 = z; z2 <= z + 7; z2 += 1) {
            for(int y2 = yFloor + 1; y2 <= yCeiling - 1; y2 += 1) {
                chunk.getBlock(x, y2, z2).setType(Material.STONE_BRICKS);
                chunk.getBlock(x + 7, y2, z2).setType(Material.STONE_BRICKS);
            }
        }

        // Make the room massive with stone
        for(int x2 = x + 1; x2 <= x + 6; x2 += 1)
            for(int y2 = yFloor + 1; y2 <= yCeiling - 1; y2 += 1)
                for(int z2 = z + 1; z2 <= z + 6; z2 += 1)
                    chunk.getBlock(x2, y2, z2).setType(Material.STONE);

        // Fill the massive room with some ores!
        for(int x2 = x + 1; x2 <= x + 6; x2 += 1) {
            for(int y2 = yFloor + 1; y2 <= yCeiling - 1; y2 += 1) {
                for(int z2 = z + 1; z2 <= z + 6; z2 += 1) {
                    if(rand.nextInt(100) < 2) {
                        switch(rand.nextInt(8)) {
                        case 0:
                            chunk.getBlock(x2, y2, z2).setType(Material.GOLD_ORE);
                            break;
                        case 1:
                            chunk.getBlock(x2, y2, z2).setType(Material.IRON_ORE);
                            break;
                        case 2:
                            chunk.getBlock(x2, y2, z2).setType(Material.COAL_ORE);
                            break;
                        case 3:
                            chunk.getBlock(x2, y2, z2).setType(Material.LAPIS_ORE);
                            break;
                        case 4:
                            chunk.getBlock(x2, y2, z2).setType(Material.DIAMOND_ORE);
                            break;
                        case 5:
                            chunk.getBlock(x2, y2, z2).setType(Material.REDSTONE_ORE);
                            break;
                        case 6:
                            chunk.getBlock(x2, y2, z2).setType(Material.EMERALD_ORE);
                            break;
                        case 7:
                            chunk.getBlock(x2, y2, z2).setType(Material.CLAY);
                            break;
                        case 8:
                            chunk.getBlock(x2, y2, z2).setType(Material.COAL_ORE);
                            break;
                        default:
                            chunk.getBlock(x2, y2, z2).setType(Material.COAL_ORE);
                        }
                    }
                }
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