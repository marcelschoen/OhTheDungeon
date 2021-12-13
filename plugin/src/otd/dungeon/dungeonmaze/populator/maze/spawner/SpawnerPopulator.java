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

//package zhehe.com.timvisee.dungeonmaze.populator.maze.spawner;
//
////import zhehe.com.timvisee.dungeonmaze.Config;
//import java.util.Random;
//
//
//import zhehe.com.timvisee.dungeonmaze.util.NumberUtils;
//import org.bukkit.Chunk;
//import org.bukkit.Material;
//import org.bukkit.block.Block;
//import org.bukkit.entity.EntityType;
//
//import zhehe.com.timvisee.dungeonmaze.populator.maze.MazeRoomBlockPopulator;
//import zhehe.com.timvisee.dungeonmaze.populator.maze.MazeRoomBlockPopulatorArgs;
//import org.bukkit.block.BlockState;
//import org.bukkit.block.CreatureSpawner;
//import zhehe.com.timvisee.dungeonmaze.util.SpawnerUtils;
//
//public class SpawnerPopulator extends MazeRoomBlockPopulator {
//
//    /** General populator constants. */
//	private static final int LAYER_MIN = 1;
//	private static final int LAYER_MAX = 7;
//	private static final float ROOM_CHANCE = .06f;
//
//    /** Populator constants. */
//	private static final double SPAWN_DISTANCE_MIN = 2; // Chunks
//
//    // TODO: Implement this feature!
//	public static final double CHANCE_TORCH_ADDITION_EACH_LEVEL = -0.5; /* to 3 */
//
//	@Override
//	public void populateRoom(MazeRoomBlockPopulatorArgs args) {
//		Chunk chunk = args.getSourceChunk();
//		Random rand = args.getRandom();
//		final int x = args.getRoomChunkX();
//		final int z = args.getRoomChunkZ();
//		
//		// Make sure the distance between the spawn and the current chunk is allowed
//		if(NumberUtils.distanceFromZero(chunk.getX(), chunk.getZ()) < SPAWN_DISTANCE_MIN)
//			return;
//
//        int spawnerX = x + rand.nextInt(6) + 1;
//        int spawnerY = args.getFloorY() + 1;
//        int spawnerZ = z + rand.nextInt(6) + 1;
//
//        if(chunk.getBlock(spawnerX, spawnerY - 1, spawnerZ).getType() != Material.AIR) {
//            Block spawnerBlock = chunk.getBlock(spawnerX, spawnerY, spawnerZ);
//
//            if(spawnerBlock.getType() == Material.AIR) {
//                // Generate a random spawnedType for the spawner
//                EntityType spawnedType;
//                int i = rand.nextInt(25) + 1;
//                if(i >= 1 && i <= 10 && SpawnerUtils.isZombieAllowed(world))
//                    spawnedType = EntityType.ZOMBIE;
//
//                else if(i >= 11 && i <= 15 && Config.Skeleton)
//                    spawnedType = EntityType.SKELETON;
//
//                else if(i >= 16 && i <= 20 && Config.Spider)
//                    spawnedType = EntityType.SPIDER;
//
//                else if(i >= 21 && i <= 22 && Config.PigZombie)
//                    spawnedType = EntityType.PIG_ZOMBIE;
//
//                else if(i == 23 && Config.Enderman)
//                    spawnedType = EntityType.ENDERMAN;
//
//                else if(i == 24 && Config.MagmaCube)
//                    spawnedType = EntityType.MAGMA_CUBE;
//
//                else if(i == 25 && Config.Silverfish)
//                    spawnedType = EntityType.SILVERFISH;
//
//                else if(SpawnerUtils.isZombieAllowed(world))
//                    spawnedType = EntityType.ZOMBIE;
//
//                else // if no entity type is allowed and the random return none value, continue the for loop
//                    return;
//                
//                spawnerBlock.setType(Material.SPAWNER, true);
//                BlockState blockState = spawnerBlock.getState();
//                CreatureSpawner spawner = ((CreatureSpawner)blockState);
//                spawner.setSpawnedType(spawnedType);
//                blockState.update();
//            }
//        }
//	}
//	
//    @Override
//    public float getRoomChance() {
//        return ROOM_CHANCE;
//    }
//
//	/**
//	 * Get the minimum layer
//	 * @return Minimum layer
//	 */
//	@Override
//	public int getMinimumLayer() {
//		return LAYER_MIN;
//	}
//	
//	/**
//	 * Get the maximum layer
//	 * @return Maximum layer
//	 */
//	@Override
//	public int getMaximumLayer() {
//		return LAYER_MAX;
//	}
//}