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
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.inventory.ItemStack;
import otd.dungeon.dungeonmaze.populator.maze.MazeRoomBlockPopulator;
import otd.dungeon.dungeonmaze.populator.maze.MazeRoomBlockPopulatorArgs;
import otd.dungeon.dungeonmaze.util.ChestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpawnChamberPopulator extends MazeRoomBlockPopulator {

    /** General populator constants. */
    private static final int LAYER_MIN = 7;
    private static final int LAYER_MAX = 7;
    private static final float ROOM_CHANCE = 1.0f;
    
    public boolean const_room = true;
    @Override
    public boolean getConstRoom() {
        return const_room;
    }
    
    private static final BlockData CHEST2 =
            Bukkit.createBlockData("minecraft:chest[facing=north,type=single]");
    private static final BlockData CHEST3 =
            Bukkit.createBlockData("minecraft:chest[facing=south,type=single]");

    @Override
    public void populateRoom(MazeRoomBlockPopulatorArgs args) {
        final World world = args.getWorld();
        final Chunk chunk = args.getSourceChunk();
        final Random rand = args.getRandom();
        final int roomX = args.getRoomChunkX();
        final int roomY = args.getChunkY();
        final int roomZ = args.getRoomChunkZ();

        // Make sure this is the chunk at (0, 0)
        if(chunk.getX() != 0 || chunk.getZ() != 0 || roomX != 0 || roomZ != 0)
            return;

//        // Register the current room as constant room
//        //DungeonMaze.instance.registerConstantRoom(world.getName(), chunk.getX(), chunk.getZ(), roomX, roomY, roomZ);

        // Break out the original walls, but not the corners
        for(int x = 0; x < 8; x++)
            for(int y = roomY + 2; y < 30 + (7 * 6); y++)
                for(int z = 0; z < 8; z++)
                    // Make sure this isn't a corner
                    if((x == 0 || x == 7) && (z == 0 || z == 7))
                        chunk.getBlock(roomX + x, y, roomZ + z).setType(Material.AIR);

        // Floor of a layer stone bricks with cobble stone below it
        for(int x = roomX; x < roomX + 8; x++) {
            for(int z = roomZ; z < roomZ + 8; z++) {
                chunk.getBlock(x, roomY + 1, z).setType(Material.STONE_BRICKS);
                chunk.getBlock(x, roomY, z).setType(Material.COBBLESTONE);
            }
        }

        // Ceiling
        for(int x = roomX; x < roomX + 8; x++)
            for(int z = roomZ; z < roomZ + 8; z++)
                chunk.getBlock(x, roomY + 6, z).setType(Material.STONE_BRICKS);

        // Generate 4 circular blocks in the middle of the floor
        for(int x = roomX + 3; x <= roomX + 4; x++) {
            for(int z = roomZ + 3; z <= roomZ + 4; z++) {
                chunk.getBlock(x, roomY + 1, z).setType(Material.CHISELED_STONE_BRICKS);
            }
        }

        // Create iron fence walls
        for(int i = 1; i < 7; i++) {
            for(int y = roomY + 2; y < roomY + 6; y++) {
                chunk.getBlock(roomX + i, y, roomZ).setType(Material.IRON_BARS);
                chunk.getBlock(roomX + i, y, roomZ + 7).setType(Material.IRON_BARS);

                chunk.getBlock(roomX, y, roomZ + i).setType(Material.IRON_BARS);
                chunk.getBlock(roomX + 7, y, roomZ + i).setType(Material.IRON_BARS);
            }
        }

        // Create gates
        for(int x = roomX + 2; x < roomX + 6; x++) {
            for(int y = roomY + 2; y < roomY + 5; y++) {
                chunk.getBlock(x, y, roomZ).setType(Material.STONE_BRICKS);
                chunk.getBlock(x, y, roomZ + 7).setType(Material.STONE_BRICKS);
            }
        }
        for(int z = roomZ + 2; z < roomZ + 6; z++) {
            for(int y = roomY + 2; y < roomY + 5; y++) {
                chunk.getBlock(roomX, y, z).setType(Material.STONE_BRICKS);
                chunk.getBlock(roomX + 7, y, z).setType(Material.STONE_BRICKS);
            }
        }
        for(int x = roomX + 3; x < roomX + 5; x++) {
            for(int y = roomY + 2; y < roomY + 4; y++) {
                chunk.getBlock(x, y, roomZ).setType(Material.AIR);
                chunk.getBlock(x, y, roomZ + 7).setType(Material.AIR);
            }
        }
        for(int z = roomZ + 3; z < roomZ + 5; z++) {
            for(int y = roomY + 2; y < roomY + 4; y++) {
                chunk.getBlock(roomX, y, z).setType(Material.AIR);
                chunk.getBlock(roomX + 7, y, z).setType(Material.AIR);
            }
        }

        // Empty ItemStack list for events
        List<ItemStack> emptyList = new ArrayList<>();

        // Create chests
        chunk.getBlock(roomX + 1, roomY + 2, roomZ + 1).setBlockData(CHEST3);

        chunk.getBlock(roomX + 1, roomY + 2, roomZ + 6).setBlockData(CHEST2);

        chunk.getBlock(roomX + 6, roomY + 2, roomZ + 1).setBlockData(CHEST3);

        chunk.getBlock(roomX + 6, roomY + 2, roomZ + 6).setBlockData(CHEST2);

        ChestUtils.addItemsToChest(chunk.getBlock(roomX + 1, roomY + 2, roomZ + 1), emptyList, true, rand, world);
        ChestUtils.addItemsToChest(chunk.getBlock(roomX + 1, roomY + 2, roomZ + 6), emptyList, true, rand, world);
        ChestUtils.addItemsToChest(chunk.getBlock(roomX + 6, roomY + 2, roomZ + 1), emptyList, true, rand, world);
        ChestUtils.addItemsToChest(chunk.getBlock(roomX + 6, roomY + 2, roomZ + 6), emptyList, true, rand, world);

        // Define the relative positions of the torches in the spawn chamber
        int relativeTorchCoords[][] = {
                {1, 2},
                {1, 5},
                {6, 2},
                {6, 5},
                {2, 1},
                {2, 6},
                {5, 1},
                {5, 6},
        };

        // Place the torches
        for(int[] torchCoords : relativeTorchCoords) {
            // Get the relative coordinates of the torch block
            int xTorch = torchCoords[0];
            int zTorch = torchCoords[1];

            // Get the block to convert to a torch
            Block b = chunk.getBlock(roomX + xTorch, roomY + 3, roomZ + zTorch);

            // Set the material of the block
            b.setType(Material.WALL_TORCH);

            // Get the torch data instance
            Directional torch = (Directional) b.getBlockData();

            // Determine and set the facing of the torch
            BlockFace torchFace = BlockFace.NORTH;
            if(xTorch == 6)
                torchFace = BlockFace.WEST;
            else if(xTorch == 1)
                torchFace = BlockFace.EAST;
            else if(zTorch == 1)
                torchFace = BlockFace.SOUTH;
            torch.setFacing(torchFace);

            // Set the data value based on the torch facing, and update the block
            b.setBlockData(torch);
        }
    }

    @Override
    public float getRoomChance() {
        return ROOM_CHANCE;
    }

    /**
     * Get the minimum layer
     *
     * @return Minimum layer
     */
    @Override
    public int getMinimumLayer() {
        return LAYER_MIN;
    }

    /**
     * Get the maximum layer
     *
     * @return Maximum layer
     */
    @Override
    public int getMaximumLayer() {
        return LAYER_MAX;
    }
}