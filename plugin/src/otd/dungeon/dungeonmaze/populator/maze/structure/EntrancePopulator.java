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

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import otd.dungeon.dungeonmaze.populator.maze.MazeRoomBlockPopulator;
import otd.dungeon.dungeonmaze.populator.maze.MazeRoomBlockPopulatorArgs;

import java.util.Random;

public class EntrancePopulator extends MazeRoomBlockPopulator {

    /** General populator constants. */
	private static final int LAYER_MIN = 7;
	private static final int LAYER_MAX = 7;
	private static final int CHANCE_ENTRANCE = 1000;//5; // Promile
        
        private static final BlockData LADDER2 =
                Bukkit.createBlockData("minecraft:ladder[facing=north]");
        private static final BlockData LADDER5 =
                Bukkit.createBlockData("minecraft:ladder[facing=east]");
        private static final BlockData VINE4 =
                Bukkit.createBlockData("minecraft:vine[east=false,south=false,north=true,west=false,up=false]");
        private static final BlockData STEP5 =
                Bukkit.createBlockData("minecraft:stone_brick_slab[type=bottom]");

	@Override
	public void populateRoom(MazeRoomBlockPopulatorArgs args) {
		Chunk chunk = args.getSourceChunk();
		Random rand = args.getRandom();
		int x = args.getRoomChunkX();
		int y = args.getChunkY();
		int z = args.getRoomChunkZ();
		
		// Apply chances
		if (rand.nextInt(1000) < CHANCE_ENTRANCE) {
							
			int yGround;
			
			// Choose a rand hole look
			switch(rand.nextInt(4)) {
			case 0:
				// Get ground worldHeight
                //noinspection StatementWithEmptyBody
                for(yGround = 100; chunk.getBlock(x, yGround, z + 3).getType() == Material.AIR; yGround--) {}
				
				// Generate the hole
				for(int xx = 0; xx < 8; xx++) {
					for(int zz = 0; zz < 8; zz++) {
						for(int yy = y; yy < yGround + 1; yy++) {
							if(xx == 0 || xx == 7 || zz == 0 || zz == 7)
								chunk.getBlock(x + xx, yy, z + zz).setType(Material.STONE_BRICKS);
							else
								chunk.getBlock(x + xx, yy, z + zz).setType(Material.AIR);
						}
					}
				}
				
				// Generate ladders in the hole with some randomness for ladders which looks like broken and old ladders
				for(int yy = y; yy < yGround + 1; yy++) {
					if(rand.nextInt(100) < 80) {
						chunk.getBlock(x + 1, yy, z + 3).setBlockData(LADDER5);
					}
					if(rand.nextInt(100) < 80) {
						chunk.getBlock(x + 1, yy, z + 4).setBlockData(LADDER5);
					}
				}
				
				// Remove all the dirt above the hole
				for(int xx = 0; xx < 8; xx++) {
					for(int yy = yGround + 1; yy < yGround + 4; yy++) {
						for(int zz = 0; zz < 8; zz++) {
							chunk.getBlock(x + xx, yy, z + zz).setType(Material.AIR);
						}
					}
				}
				
				// Get the floor location of the room
				int yFloor = y - 6; /* -6 to start 1 floor lower */
				Block roomBottomBlock = chunk.getBlock(x + 2, y, z + 2);
				Material type = roomBottomBlock.getType();

				if(!(type==Material.COBBLESTONE || type==Material.MOSSY_COBBLESTONE || type==Material.NETHERRACK || type==Material.SOUL_SAND))
					yFloor++;

				// Generate corner poles inside the hole
				if(chunk.getBlock(x + 1, yFloor, z + 1).getType() == Material.AIR) {
					chunk.getBlock(x + 1, yFloor, z + 1).setType(Material.OAK_PLANKS);
					chunk.getBlock(x + 1, yFloor, z + 6).setType(Material.OAK_PLANKS);
					chunk.getBlock(x + 6, yFloor, z + 1).setType(Material.OAK_PLANKS);
					chunk.getBlock(x + 6, yFloor, z + 6).setType(Material.OAK_PLANKS);
				}
				for(int yy = yFloor + 1; yy < yGround + 4; yy++) {
					chunk.getBlock(x + 1, yy, z + 1).setType(Material.OAK_PLANKS);
					chunk.getBlock(x + 1, yy, z + 6).setType(Material.OAK_PLANKS);
					chunk.getBlock(x + 6, yy, z + 1).setType(Material.OAK_PLANKS);
					chunk.getBlock(x + 6, yy, z + 6).setType(Material.OAK_PLANKS);
				}


				// Generate the house on the hole
				//   corners
				for(int yy = yGround + 1; yy < yGround + 4; yy++) {
					chunk.getBlock(x, yy, z).setType(Material.STONE_BRICKS);
					chunk.getBlock(x, yy, z + 7).setType(Material.STONE_BRICKS);
					chunk.getBlock(x + 7, yy, z).setType(Material.STONE_BRICKS);
					chunk.getBlock(x + 7, yy, z + 7).setType(Material.STONE_BRICKS);
				}

				//   walls
				for(int xx = 1; xx < 7; xx++) {
					for(int yy = yGround + 1; yy < yGround + 4; yy++) {
						chunk.getBlock(x + xx, yy, z).setType(Material.COBBLESTONE);
						chunk.getBlock(x + xx, yy, z + 7).setType(Material.COBBLESTONE);
						chunk.getBlock(x, yy, z + xx).setType(Material.COBBLESTONE);
						chunk.getBlock(x + 7, yy, z + xx).setType(Material.COBBLESTONE);
					}
				}

				//   ceiling
				for(int xx = 0; xx < 8; xx++) {
					for(int zz = 0; zz < 8; zz++) {
						int yy = yGround + 4;
						if(rand.nextInt(100) < 90 || (xx == 0 || xx == 7 || zz == 0 || zz == 7)) {
							chunk.getBlock(x + xx, yy, z + zz).setType(Material.STONE_BRICKS);
						}
					}
				}

				//   struct bars
				for(int zz = 1; zz < 7; zz++) {
					int yy = yGround + 3;
					chunk.getBlock(x + 2, yy, z + zz).setType(Material.OAK_PLANKS);
					chunk.getBlock(x + 5, yy, z + zz).setType(Material.OAK_PLANKS);
				}

				//   gate
				chunk.getBlock(x, yGround + 1, z + 2).setType(Material.OAK_FENCE);
				chunk.getBlock(x, yGround + 1, z + 5).setType(Material.OAK_FENCE);
				chunk.getBlock(x, yGround + 2, z + 2).setType(Material.OAK_FENCE);
				chunk.getBlock(x, yGround + 2, z + 5).setType(Material.OAK_FENCE);
				chunk.getBlock(x, yGround + 1, z + 3).setType(Material.AIR);
				chunk.getBlock(x, yGround + 1, z + 4).setType(Material.AIR);
				chunk.getBlock(x, yGround + 2, z + 3).setType(Material.AIR);
				chunk.getBlock(x, yGround + 2, z + 4).setType(Material.AIR);
				for(int zz = 2; zz < 6; zz++)
					chunk.getBlock(x, yGround + 3, z + zz).setType(Material.OAK_PLANKS);
//				chunk.getBlock(x - 1, yGround + 2, z + 1).setType(Material.TORCH);
//				chunk.getBlock(x - 1, yGround + 2, z + 6).setType(Material.TORCH);

				break;

			case 1:

				// Get ground worldHeight
                //noinspection StatementWithEmptyBody
                for(yGround = 100; chunk.getBlock(x + 3, yGround, z + 7).getType() == Material.AIR; yGround--) {}

				// Generate the hole
				for(int xx = 0; xx < 8; xx++) {
					for(int zz = 0; zz < 8; zz++) {
						for(int yy = y; yy < yGround + 1; yy++) {
							if(xx == 0 || xx == 7 || zz == 0 || zz == 7)
								chunk.getBlock(x + xx, yy, z + zz).setType(Material.STONE_BRICKS);
							else
								chunk.getBlock(x + xx, yy, z + zz).setType(Material.AIR);
						}
					}
				}

				// Generate ladders in the hole with some noise for ladders which looks like broken & old ladders
				for(int yy = y; yy < yGround + 1; yy++) {
					if(rand.nextInt(100) < 80) {
						chunk.getBlock(x + 3, yy, z + 6).setBlockData(LADDER2);
					}
					if(rand.nextInt(100) < 80) {
						chunk.getBlock(x + 4, yy, z + 6).setBlockData(LADDER2);
					}
				}

				// Remove all the dirt above the hole
				for(int xx = 0; xx < 8; xx++)
					for(int yy = yGround + 1; yy < yGround + 4; yy++)
						for(int zz = 0; zz < 8; zz++)
							chunk.getBlock(x + xx, yy, z + zz).setType(Material.AIR);

				// Generate the house on the hole
				// Corners
				for(int yy = yGround + 1; yy < yGround + 4; yy++) {
					chunk.getBlock(x, yy, z).setType(Material.STONE_BRICKS);
					chunk.getBlock(x, yy, z + 7).setType(Material.STONE_BRICKS);
					chunk.getBlock(x + 7, yy, z).setType(Material.STONE_BRICKS);
					chunk.getBlock(x + 7, yy, z + 7).setType(Material.STONE_BRICKS);
				}

				// Walls
				for(int xx = 1; xx < 7; xx++) {
					for(int yy = yGround + 1; yy < yGround + 4; yy++) {
						chunk.getBlock(x + xx, yy, z).setType(Material.COBBLESTONE);
						chunk.getBlock(x + xx, yy, z + 7).setType(Material.COBBLESTONE);
						chunk.getBlock(x, yy, z + xx).setType(Material.COBBLESTONE);
						chunk.getBlock(x + 7, yy, z + xx).setType(Material.COBBLESTONE);
					}
				}

				//   ceiling
				for(int xx = 0; xx < 8; xx++) {
					for(int zz = 0; zz < 8; zz++) {
						int yy = yGround + 4;
						if(rand.nextInt(100) < 90 || (xx == 0 || xx == 7 || zz == 0 || zz == 7)) {
							chunk.getBlock(x + xx, yy, z + zz).setType(Material.STONE_BRICKS);
						}
					}
				}

				// Struct bars
				for(int xx = 1; xx < 7; xx++) {
					int yy = yGround + 3;
					chunk.getBlock(x + xx, yy, z + 2).setType(Material.OAK_PLANKS);
					chunk.getBlock(x + xx, yy, z + 5).setType(Material.OAK_PLANKS);
				}

				// Doors
				chunk.getBlock(x + 3, yGround + 1, z + 7).setType(Material.AIR);
				chunk.getBlock(x + 4, yGround + 1, z + 7).setType(Material.AIR);
				chunk.getBlock(x + 3, yGround + 2, z + 7).setType(Material.AIR);
				chunk.getBlock(x + 4, yGround + 2, z + 7).setType(Material.AIR);
//				chunk.getBlock(x + 2, yGround + 2, z + 8).setType(Material.WALL_TORCH);
//				chunk.getBlock(x + 5, yGround + 2, z + 8).setType(Material.WALL_TORCH);

				break;

			case 2:

				// Get ground worldHeight
                //noinspection StatementWithEmptyBody
                for(yGround = 100; chunk.getBlock(x + 3, yGround, z + 3).getType() == Material.AIR; yGround--) {}

				// Generate the hole
				for(int xx = 0; xx < 8; xx++) {
					for(int zz = 0; zz < 8; zz++) {
						for(int yy = y; yy < yGround + 1; yy++) {
							if(xx == 0 || xx == 7 || zz == 0 || zz == 7)
								chunk.getBlock(x + xx, yy, z + zz).setType(Material.STONE_BRICKS);
							else
								chunk.getBlock(x + xx, yy, z + zz).setType(Material.AIR);
						}
					}
				}

				// Generate ladders in the hole with some noise for ladders which looks like broken & old ladders
				for(int yy = y; yy < yGround + 1; yy++) {
					if(rand.nextInt(100) < 80) {
						chunk.getBlock(x + 1, yy, z + 3).setBlockData(LADDER5);
					}
					if(rand.nextInt(100) < 80) {
						chunk.getBlock(x + 1, yy, z + 4).setBlockData(LADDER5);
					}
				}

				// Remove all the dirt above the hole
				for(int xx = 0; xx < 8; xx++)
					for(int yy = yGround + 1; yy < yGround + 4; yy++)
						for(int zz = 0; zz < 8; zz++)
							chunk.getBlock(x + xx, yy, z + zz).setType(Material.AIR);


				// Generate the house on the hole
				//   corners
				for(int yy = yGround + 1; yy < yGround + 4; yy++) {
					chunk.getBlock(x, yy, z).setType(Material.STONE_BRICKS);
					chunk.getBlock(x, yy, z + 7).setType(Material.STONE_BRICKS);
					chunk.getBlock(x + 7, yy, z).setType(Material.STONE_BRICKS);
					chunk.getBlock(x + 7, yy, z + 7).setType(Material.STONE_BRICKS);
				}

				//   ceiling
				for(int xx = 0; xx < 8; xx++) {
					for(int zz = 0; zz < 8; zz++) {
						int yy = yGround + 4;
						if(xx == 0 || xx == 7 || zz == 0 || zz == 7)
							chunk.getBlock(x + xx, yy, z + zz).setType(Material.NETHER_BRICKS);

                        else if(rand.nextInt(100) < 95) {
							chunk.getBlock(x + xx, yy + 1, z + zz).setBlockData(STEP5);
						}
					}
				}

				//   struct bars
				for(int xx = 1; xx < 7; xx++) {
					int yy = yGround + 4;
					chunk.getBlock(x + xx, yy, z + 2).setType(Material.NETHER_BRICKS);
					chunk.getBlock(x + xx, yy, z + 5).setType(Material.NETHER_BRICKS);
				}

				break;

			case 3:
				// Get ground worldHeight
                //noinspection StatementWithEmptyBody
                for(yGround = 100; chunk.getBlock(x + 3, yGround, z + 3).getType() == Material.AIR; yGround--) {}

				// Generate the hole
				for(int xx = 0; xx < 8; xx++) {
					for(int zz = 0; zz < 8; zz++) {
						for(int yy = y; yy < yGround + 1; yy++) {
							if(xx == 0 || xx == 7 || zz == 0 || zz == 7)
								chunk.getBlock(x + xx, yy, z + zz).setType(Material.STONE_BRICKS);
							else
								chunk.getBlock(x + xx, yy, z + zz).setType(Material.AIR);
						}
					}
				}

				// Generate ladders/VINES! in the hole with some noise for ladders which looks like broken & old ladders
				if(rand.nextInt(2) == 0) {
					for(int yy = y; yy < yGround + 1; yy++) {
						if(rand.nextInt(100) < 80) {
							chunk.getBlock(x + 3, yy, z + 6).setBlockData(LADDER2);
						}
						if(rand.nextInt(100) < 80) {
							chunk.getBlock(x + 4, yy, z + 6).setBlockData(LADDER2);
						}
					}
				} else {
					for(int yy = y; yy < yGround + 1; yy++) {
						if(rand.nextInt(100) < 60) {
							chunk.getBlock(x + 3, yy, z + 1).setBlockData(VINE4);
						}
						if(rand.nextInt(100) < 60) {
							chunk.getBlock(x + 4, yy, z + 1).setBlockData(VINE4);
						}
					}
				}


				// Remove all the dirt above the hole
				for(int xx = 0; xx < 8; xx++)
					for(int yy = yGround + 1; yy < yGround + 4; yy++)
						for(int zz = 0; zz < 8; zz++)
							chunk.getBlock(x + xx, yy, z + zz).setType(Material.AIR);

				// Generate torches on the corners
				//   corners
				chunk.getBlock(x, yGround + 1, z).setType(Material.TORCH);
				chunk.getBlock(x, yGround + 1, z + 7).setType(Material.TORCH);
				chunk.getBlock(x + 7, yGround + 1, z).setType(Material.TORCH);
				chunk.getBlock(x + 7, yGround + 1, z + 7).setType(Material.TORCH);
				break;

			default:
				// Get ground worldHeight
                //noinspection StatementWithEmptyBody
                for(yGround = 100; chunk.getBlock(x + 3, yGround, z + 3).getType() == Material.AIR; yGround--) {}

				// Generate the hole
				for(int xx = 0; xx < 8; xx++) {
					for(int zz = 0; zz < 8; zz++) {
						for(int yy = y; yy < yGround + 1; yy++) {
							if(xx == 0 || xx == 7 || zz == 0 || zz == 7)
								chunk.getBlock(x + xx, yy, z + zz).setType(Material.STONE_BRICKS);
							else
								chunk.getBlock(x + xx, yy, z + zz).setType(Material.AIR);
						}
					}
				}

				// Generate ladders in the hole with some noise for ladders which looks like broken & old ladders
				for(int yy = y; yy < yGround + 1; yy++) {
					if(rand.nextInt(100) < 80) {
						chunk.getBlock(x + 3, yy, z + 6).setBlockData(LADDER2);
					}
					if(rand.nextInt(100) < 80) {
						chunk.getBlock(x + 4, yy, z + 6).setBlockData(LADDER2);
					}
				}

				// Remove all the dirt above the hole
				for(int xx = 0; xx < 8; xx++)
					for(int yy = yGround + 1; yy < yGround + 4; yy++)
						for(int zz = 0; zz < 8; zz++)
							chunk.getBlock(x + xx, yy, z + zz).setType(Material.AIR);


				// Generate torches on the corners
				chunk.getBlock(x, yGround + 1, z).setType(Material.TORCH);
				chunk.getBlock(x, yGround + 1, z + 7).setType(Material.TORCH);
				chunk.getBlock(x + 7, yGround + 1, z).setType(Material.TORCH);
				chunk.getBlock(x + 7, yGround + 1, z + 7).setType(Material.TORCH);
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