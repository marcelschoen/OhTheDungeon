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

//import zhehe.com.timvisee.dungeonmaze.Config;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import otd.dungeon.dungeonmaze.populator.maze.MazeRoomBlockPopulator;
import otd.dungeon.dungeonmaze.populator.maze.MazeRoomBlockPopulatorArgs;
import otd.dungeon.dungeonmaze.util.ChestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class AbandonedDefenceCastleRoomPopulator extends MazeRoomBlockPopulator {

	// TODO: Use material enums instead of ID's due to ID deprecation by Mojang

    /** General populator constants. */
	private static final int LAYER_MIN = 2;
	private static final int LAYER_MAX = 6;
	private static final float ROOM_CHANCE = .001f;
        
        private static final BlockData[] CAKES = {
//            Bukkit.createBlockData("minecraft:cake[bites=0]"),
            Bukkit.createBlockData("minecraft:cake[bites=1]"),
            Bukkit.createBlockData("minecraft:cake[bites=2]"),
            Bukkit.createBlockData("minecraft:cake[bites=3]"),
            Bukkit.createBlockData("minecraft:cake[bites=4]"),
            Bukkit.createBlockData("minecraft:cake[bites=5]"),
//            Bukkit.createBlockData("minecraft:cake[bites=6]"),
        };
        private static final BlockData CHEST4 = Bukkit.createBlockData("minecraft:chest[facing=west,type=right]");
        private static final BlockData CHEST5 = Bukkit.createBlockData("minecraft:chest[facing=west,type=left]");
        
        public boolean const_room = true;
        @Override
        public boolean getConstRoom() {
            return const_room;
        }

    /** Populator constants. */
	private static final float MOSS_CHANCE = .7f;
	private static final int MOSS_ITERATIONS = 80;
	private static final float CRACKED_CHANCE = .7f;
	private static final int CRACKED_ITERATIONS = 80;
        
        private static final BlockData FURNACE4 =
                Bukkit.createBlockData("minecraft:furnace[facing=west,lit=false]");
        private static final BlockData TORCH5 =
                Bukkit.createBlockData(Material.TORCH);
        private static final BlockData LADDER2 =
                Bukkit.createBlockData("minecraft:ladder[facing=north]");
        private static final BlockData CHEST2 =
                Bukkit.createBlockData("minecraft:chest[facing=north,type=single]");

	@Override
	public void populateRoom(MazeRoomBlockPopulatorArgs args) {
		final World world = args.getWorld();
		final Chunk chunk = args.getSourceChunk();
		final Random rand = args.getRandom();
		final int x = args.getRoomChunkX();
		final int y = args.getChunkY();
		final int floorOffset = args.getFloorOffset();
		final int yFloor = args.getFloorY();
		final int yCeiling = args.getCeilingY();
		final int z = args.getRoomChunkZ();

        // Register the room as constant room
        ////DungeonMaze.instance.registerConstantRoom(world.getName(), chunk, x, y, z);

        // Break out the original walls
        for(int xx = 1; xx < 7; xx++) {
            for(int yy = yFloor + 1; yy <= yCeiling - 1; yy++) {
                chunk.getBlock(x + xx, yy, z).setType(Material.AIR);
                chunk.getBlock(x + xx, yy, z + 7).setType(Material.AIR);
                chunk.getBlock(x, yy, z + xx).setType(Material.AIR);
                chunk.getBlock(x + 7, yy, z + xx).setType(Material.AIR);
            }
        }

        // Walls
        for(int xx = 1; xx < 7; xx++) {
            for(int yy = floorOffset + 1; yy <= floorOffset + 2; yy++) {
                chunk.getBlock(x + xx, y + yy, z + 1).setType(Material.STONE_BRICKS);
                chunk.getBlock(x + xx, y + yy, z + 6).setType(Material.STONE_BRICKS);
                chunk.getBlock(x + 1, y + yy, z + xx).setType(Material.STONE_BRICKS);
                chunk.getBlock(x + 6, y + yy, z + xx).setType(Material.STONE_BRICKS);
            }
        }

        // Generate merlons
        for(int xx = 0; xx < 7; xx++) {
            chunk.getBlock(x + xx, yFloor + 3, z).setType(Material.STONE_BRICKS);
            chunk.getBlock(x + xx, yFloor + 3, z + 7).setType(Material.STONE_BRICKS);
            chunk.getBlock(x, yFloor + 3, z + xx).setType(Material.STONE_BRICKS);
            chunk.getBlock(x + 7, yFloor + 3, z + xx).setType(Material.STONE_BRICKS);
        }

        chunk.getBlock(x, yFloor + 4, z + 1).setType(Material.STONE_BRICK_SLAB);
        chunk.getBlock(x, yFloor + 4, z + 3).setType(Material.STONE_BRICK_SLAB);
        chunk.getBlock(x, yFloor + 4, z + 5).setType(Material.STONE_BRICK_SLAB);
        chunk.getBlock(x + 7, yFloor + 4, z + 2).setType(Material.STONE_BRICK_SLAB);
        chunk.getBlock(x + 7, yFloor + 4, z + 4).setType(Material.STONE_BRICK_SLAB);
        chunk.getBlock(x + 7, yFloor + 4, z + 6).setType(Material.STONE_BRICK_SLAB);
        chunk.getBlock(x + 1, yFloor + 4, z).setType(Material.STONE_BRICK_SLAB);
        chunk.getBlock(x + 3, yFloor + 4, z).setType(Material.STONE_BRICK_SLAB);
        chunk.getBlock(x + 5, yFloor + 4, z).setType(Material.STONE_BRICK_SLAB);
        chunk.getBlock(x + 2, yFloor + 4, z + 7).setType(Material.STONE_BRICK_SLAB);
        chunk.getBlock(x + 4, yFloor + 4, z + 7).setType(Material.STONE_BRICK_SLAB);
        chunk.getBlock(x + 6, yFloor + 4, z + 7).setType(Material.STONE_BRICK_SLAB);

        // Place torches
        chunk.getBlock(x + 1, yFloor + 3, z + 1).setBlockData(TORCH5);
        chunk.getBlock(x + 1, yFloor + 3, z + 6).setBlockData(TORCH5);
        chunk.getBlock(x + 6, yFloor + 3, z + 1).setBlockData(TORCH5);
        chunk.getBlock(x + 6, yFloor + 3, z + 6).setBlockData(TORCH5);

        // Place ladders
        chunk.getBlock(x + 2, yFloor + 1, z + 5).setBlockData(LADDER2);
        chunk.getBlock(x + 2, yFloor + 2, z + 5).setBlockData(LADDER2);

        // Place crafting table, chests and furnaces
        chunk.getBlock(x + 2, yFloor + 1, z + 2).setType(Material.CRAFTING_TABLE);
        chunk.getBlock(x + 5, yFloor + 1, z + 2).setBlockData(CHEST4);
        ChestUtils.addItemsToChest(chunk.getBlock(x + 5, yFloor + 1, z + 2), genChestContent(rand), true, rand, world);

        chunk.getBlock(x + 5, yFloor + 1, z + 3).setBlockData(CHEST5);

        ChestUtils.addItemsToChest(chunk.getBlock(x + 5, yFloor + 1, z + 3), genChestContent(rand), true, rand, world);

        chunk.getBlock(x + 5, yFloor + 1, z + 4).setBlockData(FURNACE4);
        chunk.getBlock(x + 5, yFloor + 1, z + 5).setBlockData(FURNACE4);
		try {
			addItemsToFurnace(rand, (Furnace) chunk.getBlock(x + 5, yFloor + 1, z + 4).getState(), world);
			addItemsToFurnace(rand, (Furnace) chunk.getBlock(x + 5, yFloor + 1, z + 5).getState(), world);
		} catch(Exception ex) {
			// Show a proper error message
			Bukkit.getLogger().log(Level.SEVERE, "Failed to add items to furnace inventory");
		}

        // Place cake (with random pieces eaten)
        chunk.getBlock(x + 5, yFloor + 2, z + 5).setBlockData(CAKES[rand.nextInt(CAKES.length)]);
//        chunk.getBlock(x + 5, yFloor + 2, z + 5).setType(Material.CAKE);//TODO_CAKE
//        chunk.getBlock(x + 5, yFloor + 2, z + 5).setData((byte) rand.nextInt(4));

        // TODO: Place painting

        // Place some cobweb
        chunk.getBlock(x + 2, yFloor + 2, z + 2).setType(Material.COBWEB);
        chunk.getBlock(x + 3, yFloor + 1, z + 2).setType(Material.COBWEB);
        chunk.getBlock(x + 6, yFloor + 3, z + 6).setType(Material.COBWEB);
        chunk.getBlock(x + 6, yFloor + 4, z + 6).setType(Material.COBWEB);
        chunk.getBlock(x + 5, yFloor + 3, z + 6).setType(Material.COBWEB);
        chunk.getBlock(x + 6, yFloor + 3, z + 5).setType(Material.COBWEB);
        chunk.getBlock(x, yFloor + 4, z + 6).setType(Material.COBWEB);

        // Add some moss and cracked stone bricks
        for (int i = 0; i < MOSS_ITERATIONS; i++) {
            if (rand.nextInt(100) < MOSS_CHANCE) {

                Block block = chunk.getBlock(x + rand.nextInt(8), rand.nextInt((y + 6) - y + 1) + y, z + rand.nextInt(8));
                if (block.getType() == Material.COBBLESTONE)
                    block.setType(Material.MOSSY_COBBLESTONE);

                if (block.getType() == Material.STONE_BRICKS)
                    block.setType(Material.MOSSY_STONE_BRICKS);
            }
        }

        for (int i = 0; i < CRACKED_ITERATIONS; i++) {
            if (rand.nextInt(100) < CRACKED_CHANCE) {

                Block block = chunk.getBlock(x + rand.nextInt(8), rand.nextInt((y + 6) - y + 1) + y, z + rand.nextInt(8));
                if (block.getType() == Material.STONE_BRICKS)
                    block.setType(Material.CRACKED_STONE_BRICKS);
            }
        }
	}
	
	private void addItemsToFurnace(Random random, Furnace furnace, World world) {
            if(!ChestUtils.isBuiltInLootEnable(world)) return;
        // Create a list to put the items in
		List<ItemStack> items = new ArrayList<>();

        // Put the items in the list
		if(random.nextInt(100) < 5)
			items.add(new ItemStack(Material.GOLD_BLOCK, 1));
		if(random.nextInt(100) < 5)
			items.add(new ItemStack(Material.IRON_LEGGINGS, 1));
		if(random.nextInt(100) < 20)
			items.add(new ItemStack(Material.BRICK, 1));
		if(random.nextInt(100) < 80)
			items.add(new ItemStack(Material.COAL, 1));
		if(random.nextInt(100) < 80)
			items.add(new ItemStack(Material.COAL, 1, (short) 1));
		if(random.nextInt(100) < 80)
			items.add(new ItemStack(Material.IRON_INGOT, 2));
		if(random.nextInt(100) < 20)
			items.add(new ItemStack(Material.IRON_INGOT, 4));
		if(random.nextInt(100) < 80)
			items.add(new ItemStack(Material.GOLD_INGOT, 2));
		if(random.nextInt(100) < 20)
			items.add(new ItemStack(Material.GOLD_INGOT, 4));
		if(random.nextInt(100) < 40)
			items.add(new ItemStack(Material.BREAD, 1));
		if(random.nextInt(100) < 40)
			items.add(new ItemStack(Material.BUCKET, 1));
		if(random.nextInt(100) < 80)
			items.add(new ItemStack(Material.COOKED_CHICKEN, 2));
		if(random.nextInt(100) < 40)
			items.add(new ItemStack(Material.COOKED_CHICKEN, 4));
		if(random.nextInt(100) < 80)
			items.add(new ItemStack(Material.FLINT, 3));
		if(random.nextInt(100) < 40)
			items.add(new ItemStack(Material.FLINT, 5));
		if(random.nextInt(100) < 80)
			items.add(new ItemStack(Material.PORKCHOP, 1));
		if(random.nextInt(100) < 40)
			items.add(new ItemStack(Material.COOKED_COD, 1));
		if(random.nextInt(100) < 30)
			items.add(new ItemStack(Material.ENDER_PEARL, 1));
		if(random.nextInt(100) < 30)
			items.add(new ItemStack(Material.BLAZE_ROD, 1));
		if(random.nextInt(100) < 30)
			items.add(new ItemStack(Material.GHAST_TEAR, 1));
		if(random.nextInt(100) < 45)
			items.add(new ItemStack(Material.GOLD_NUGGET, 1));
		if(random.nextInt(100) < 30)
			items.add(new ItemStack(Material.NETHER_WART, 1));
		if(random.nextInt(100) < 30)
			items.add(new ItemStack(Material.SPIDER_EYE, 1));
		if(random.nextInt(100) < 30)
			items.add(new ItemStack(Material.BLAZE_POWDER, 1));
		if(random.nextInt(100) < 30)
			items.add(new ItemStack(Material.MAGMA_CREAM, 1));
		if(random.nextInt(100) < 30)
			items.add(new ItemStack(Material.ENDER_EYE, 1));
		if(random.nextInt(100) < 30)
			items.add(new ItemStack(Material.GLISTERING_MELON_SLICE, 1));
		
		// Add the selected items into the furnace
		if(random.nextInt(100) < 70)
			furnace.getInventory().setResult(items.get(random.nextInt(items.size())));

        // Update the furnace
//		furnace.update();
	}

	private List<ItemStack> genChestContent(Random random) {
        // Create a list to put the chest items in
		List<ItemStack> items = new ArrayList<>();

        // Put the items in the list
		if(random.nextInt(100) < 80)
			items.add(new ItemStack(Material.TORCH, 4));
		if(random.nextInt(100) < 40)
			items.add(new ItemStack(Material.TORCH, 8));
		if(random.nextInt(100) < 20)
			items.add(new ItemStack(Material.TORCH, 12));
		if(random.nextInt(100) < 40)
			items.add(new ItemStack(Material.APPLE, 1));
		if(random.nextInt(100) < 10)
			items.add(new ItemStack(Material.ARROW, 16));
		if(random.nextInt(100) < 5)
			items.add(new ItemStack(Material.ARROW, 24));
		if(random.nextInt(100) < 20)
			items.add(new ItemStack(Material.DIAMOND, 1));
		if(random.nextInt(100) < 50)
			items.add(new ItemStack(Material.IRON_INGOT, 1));
		if(random.nextInt(100) < 60)
			items.add(new ItemStack(Material.GOLD_INGOT, 1));
		if(random.nextInt(100) < 10)
			items.add(new ItemStack(Material.IRON_SWORD, 1));
		if(random.nextInt(100) < 40)
			items.add(new ItemStack(Material.WOODEN_SWORD, 1));
		if(random.nextInt(100) < 20)
			items.add(new ItemStack(Material.STONE_SWORD, 1));
		if(random.nextInt(100) < 80)
			items.add(new ItemStack(Material.WHEAT, 1));
		if(random.nextInt(100) < 10)
			items.add(new ItemStack(Material.WHEAT, 2));
		if(random.nextInt(100) < 5)
			items.add(new ItemStack(Material.WHEAT, 3));
		if(random.nextInt(100) < 20)
			items.add(new ItemStack(Material.BREAD, 1));
		if(random.nextInt(100) < 20)
			items.add(new ItemStack(Material.LEATHER_HELMET, 1));
		if(random.nextInt(100) < 20)
			items.add(new ItemStack(Material.LEATHER_CHESTPLATE, 1));
		if(random.nextInt(100) < 20)
			items.add(new ItemStack(Material.LEATHER_LEGGINGS, 1));
		if(random.nextInt(100) < 20)
			items.add(new ItemStack(Material.LEATHER_BOOTS, 1));
		if(random.nextInt(100) < 40)
			items.add(new ItemStack(Material.CHAINMAIL_HELMET, 1));
		if(random.nextInt(100) < 40)
			items.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
		if(random.nextInt(100) < 40)
			items.add(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
		if(random.nextInt(100) < 40)
			items.add(new ItemStack(Material.CHAINMAIL_BOOTS, 1));
		if(random.nextInt(100) < 10)
			items.add(new ItemStack(Material.IRON_HELMET, 1));
		if(random.nextInt(100) < 10)
			items.add(new ItemStack(Material.IRON_CHESTPLATE, 1));
		if(random.nextInt(100) < 10)
			items.add(new ItemStack(Material.IRON_LEGGINGS, 1));
		if(random.nextInt(100) < 10)
			items.add(new ItemStack(Material.IRON_BOOTS, 1));
		if(random.nextInt(100) < 30)
			items.add(new ItemStack(Material.FLINT, 3));
		if(random.nextInt(100) < 20)
			items.add(new ItemStack(Material.FLINT, 5));
		if(random.nextInt(100) < 10)
			items.add(new ItemStack(Material.FLINT, 7));
		if(random.nextInt(100) < 80)
			items.add(new ItemStack(Material.BEEF, 1));
		if(random.nextInt(100) < 10)
			items.add(new ItemStack(Material.PORKCHOP, 1));
		if(random.nextInt(100) < 15)
			items.add(new ItemStack(Material.REDSTONE, 5));
		if(random.nextInt(100) < 10)
			items.add(new ItemStack(Material.REDSTONE, 8));
		if(random.nextInt(100) < 5)
			items.add(new ItemStack(Material.REDSTONE, 13));
		if(random.nextInt(100) < 3)
			items.add(new ItemStack(Material.REDSTONE, 21));
		if(random.nextInt(100) < 10)
			items.add(new ItemStack(Material.COMPASS, 1));
		if(random.nextInt(100) < 80)
			items.add(new ItemStack(Material.COD, 1));
		if(random.nextInt(100) < 20)
			items.add(new ItemStack(Material.COOKED_COD, 1));
        if(random.nextInt(100) < 20)
            items.add(new ItemStack(Material.COOKED_COD, 1));
		if(random.nextInt(100) < 20) 			
			items.add(new ItemStack(Material.INK_SAC, 1));
		if(random.nextInt(100) < 5)
			items.add(new ItemStack(Material.CAKE, 1));
		if(random.nextInt(100) < 80)
			items.add(new ItemStack(Material.COOKIE, 3));
		if(random.nextInt(100) < 20)
			items.add(new ItemStack(Material.COOKIE, 5));

        // Determine how many items to put in the chest
		int itemCountInChest;
		switch (random.nextInt(8)) {
		case 0:
			itemCountInChest = 2;
			break;
		case 1:
			itemCountInChest = 2;
			break;
		case 2:
			itemCountInChest = 3;
			break;
		case 3:
			itemCountInChest = 3;
			break;
		case 4:
			itemCountInChest = 3;
			break;
		case 5:
			itemCountInChest = 4;
			break;
		case 6:
			itemCountInChest = 4;
			break;
		case 7:
			itemCountInChest = 5;
			break;
		default:
			itemCountInChest = 3;
			break;
		}

        // Create a list of results
		List<ItemStack> result = new ArrayList<>();
		
		// Add the selected items randomly
		for (int i = 0; i < itemCountInChest; i++)
			result.add(items.get(random.nextInt(items.size())));

        // Return the list of results
		return result;
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