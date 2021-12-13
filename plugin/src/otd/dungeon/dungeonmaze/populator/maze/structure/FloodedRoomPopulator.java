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

public class FloodedRoomPopulator extends MazeRoomBlockPopulator {

    /** General populator constants. */
	private static final int LAYER_MIN = 1;
	private static final int LAYER_MAX = 7;
	private static final float ROOM_CHANCE = .005f;
        
        public boolean const_room = true;
        @Override
        public boolean getConstRoom() {
            return const_room;
        }

    /** Populator constants. */
	private static final int CHANCE_WATER = 33; // If it's no water it will be lava

	@Override
	public void populateRoom(MazeRoomBlockPopulatorArgs args) {
		final World world = args.getWorld();
		final Chunk chunk = args.getSourceChunk();
		final Random rand = args.getRandom();
		final int x = args.getRoomChunkX();
		final int y = args.getChunkY();
		final int yFloor = args.getFloorY();
		final int z = args.getRoomChunkZ();

        // Register the current room as constant room
        //DungeonMaze.instance.registerConstantRoom(world.getName(), chunk.getX(), chunk.getZ(), x, y, z);

        // Walls
        for(int x2=x; x2 <= x + 7; x2+=1) {
            for(int y2= yFloor; y2 <= y + 6; y2+=1) {
                if(chunk.getBlock(x2, y2, z).getType() != Material.COBBLESTONE && chunk.getBlock(x2, y2, z).getType() != Material.MOSSY_COBBLESTONE)
                    chunk.getBlock(x2, y2, z).setType(Material.STONE_BRICKS);

                if(chunk.getBlock(x2, y2, z + 7).getType() != Material.COBBLESTONE && chunk.getBlock(x2, y2, z + 7).getType() != Material.MOSSY_COBBLESTONE)
                    chunk.getBlock(x2, y2, z + 7).setType(Material.STONE_BRICKS);

                if(chunk.getBlock(x, y2, x2).getType() != Material.COBBLESTONE && chunk.getBlock(x, y2, x2).getType() != Material.MOSSY_COBBLESTONE)
                    chunk.getBlock(x, y2, x2).setType(Material.STONE_BRICKS);

                if(chunk.getBlock(x + 7, y2, x2).getType() != Material.COBBLESTONE && chunk.getBlock(x + 7, y2, x2).getType() != Material.MOSSY_COBBLESTONE)
                    chunk.getBlock(x + 7, y2, x2).setType(Material.STONE_BRICKS);
            }
        }

        // Fill the room with lava or water
        Material type = Material.LAVA;
        if(rand.nextInt(100) < CHANCE_WATER)
            type = Material.WATER;

        for (int x2=x + 1; x2 <= x + 6; x2+=1)
            for (int y2 = yFloor + 1; y2 <= y + 5; y2+=1)
                for (int z2=z + 1; z2 <= z + 6; z2+=1)
                    chunk.getBlock(x2, y2, z2).setType(type);
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