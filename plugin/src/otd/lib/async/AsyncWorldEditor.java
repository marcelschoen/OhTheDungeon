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
package otd.lib.async;

import forge_sandbox.BlockPos;
import forge_sandbox.greymerk.roguelike.treasure.ITreasureChest;
import forge_sandbox.greymerk.roguelike.treasure.TreasureManager;
import forge_sandbox.greymerk.roguelike.worldgen.*;
import forge_sandbox.greymerk.roguelike.worldgen.blocks.BlockType;
import forge_sandbox.greymerk.roguelike.worldgen.shapes.RectSolid;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import otd.lib.ZoneWorld;
import otd.lib.async.later.roguelike.Later;

import java.util.*;

/**
 *
 * @author
 */
public class AsyncWorldEditor implements IWorldEditor {
    
    private final World world;
//    public List<Later> later_task = new ArrayList<>();
    private TreasureManager chests;
    private Map<Material, Integer> stats;
    
    private static List<Material> invalid;
    static {
        // TODO
        invalid = new ArrayList<>();
        invalid.add(Material.OAK_PLANKS);
        invalid.add(Material.WATER);
        invalid.add(Material.CACTUS);
        invalid.add(Material.SNOW);
        invalid.add(Material.GRASS);
        invalid.add(Material.STONE);
        invalid.add(Material.OAK_LEAVES);
        invalid.add(Material.POPPY);
        invalid.add(Material.DANDELION);
    };
    
    public void setDefaultState(Material data) {
        this.zone_world.default_state = data;
    }
    
    public AsyncWorldEditor(World world) {
        this.world = world;
        stats = new HashMap<>();
	this.chests = new TreasureManager();
    }

    @Override
    public String getWorldName() {
        return world.getName();
    }

    @Override
    public Block getBlock(Coord pos) {
        return world.getBlockAt(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public long getSeed() {
        return this.world.getSeed();
    }

    @Override
    public void fillDown(Random rand, Coord origin, IBlockFactory blocks) {
        Coord cursor = new Coord(origin);
        
        while(!getMaterial(cursor).isSolid() && cursor.getY() > 1){
            blocks.set(this, rand, cursor);
            cursor.add(Cardinal.DOWN);
        }
    }

    @Override
    public boolean canPlace(MetaBlock block, Coord pos, Cardinal dir) {
        return this.isAirBlock(pos);
    }

    @Override
    public boolean validGroundBlock(Coord pos) {
        if(isAirBlock(pos)) return false;
        Material material = getMaterial(pos);
        return !invalid.contains(material);
    }

    @Override
    public void spiralStairStep(Random rand, Coord origin, IStair stair, IBlockFactory fill){
        MetaBlock air = BlockType.get(BlockType.AIR);
        Coord cursor;
        Coord start;
        Coord end;

        start = new Coord(origin);
        start.add(new Coord(-1, 0, -1));
        end = new Coord(origin);
        end.add(new Coord(1, 0, 1));

        RectSolid.fill(this, rand, start, end, air);
        fill.set(this, rand, origin);

        Cardinal dir = Cardinal.directions[origin.getY() % 4];
        cursor = new Coord(origin);
        cursor.add(dir);
        stair.setOrientation(Cardinal.left(dir), false).set(this, cursor);
        cursor.add(Cardinal.right(dir));
        stair.setOrientation(Cardinal.right(dir), true).set(this, cursor);
        cursor.add(Cardinal.reverse(dir));
        stair.setOrientation(Cardinal.reverse(dir), true).set(this, cursor);
    }
    
    public void setBlockState(BlockPos pos, BlockData data, int flag) {
        zone_world.setBlockData(pos.getX(), pos.getY(), pos.getZ(), data, false);
    }
    public void setBlockState(BlockPos pos, Material data, int flag) {
        zone_world.setType(pos.getX(), pos.getY(), pos.getZ(), data, false);
    }
    public void setBlockState(int x, int y, int z, Material data) {
        zone_world.setType(x, y, z, data, false);
    }
    
    public Material getBlockState(BlockPos pos) {
        return zone_world.getType(pos.getX(), pos.getY(), pos.getZ());
    }
    
    public BlockData getBlockStateBD(BlockPos pos) {
        return zone_world.getData(pos.getX(), pos.getY(), pos.getZ());
    }
    
    public Material getBlockState(int x, int y, int z) {
        return zone_world.getType(x, y, z);
    }

    @Override
    public int getStat(Block block) {
        return 0;
    }

    @Override
    public Map<Material, Integer> getStats() {
        return this.stats;
    }

    @Override
    public TreasureManager getTreasure() {
        return this.chests;
    }

    @Override
    public void addChest(ITreasureChest toAdd) {
        this.chests.add(toAdd);
    }

    @Override
    public IPositionInfo getInfo(Coord pos) {
        return new PositionInfo(this.world, pos);
    }

    @Override
    public Biome getBiome(Coord pos) {
        return this.world.getBiome(pos.getX(), pos.getZ());
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public boolean isFakeWorld() {
        return true;
    }

    @Override
    public void addLater(Later later) {
        this.zone_world.addLater(later);
//        later_task.add(later);
    }
    
    public final ZoneWorld zone_world = new ZoneWorld();
    
    public ZoneWorld getAsyncWorld() {
        return this.zone_world;
    }
    
    @Override
    public boolean commit(int count) {
        return true;
    }
    
    @Override
    public MetaBlock getMetaBlock(Coord pos) {
        return null;
    }

    @Override
    public Material getMaterial(Coord pos) {
        return zone_world.getType(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean isAirBlock(Coord pos) {
        return zone_world.getType(pos.getX(), pos.getY(), pos.getZ()) == Material.AIR;
    }

    @Override
    public boolean setBlock(Coord pos, MetaBlock block, boolean fillAir, boolean replaceSolid) {
        return this.setBlock(pos, block, block.getFlag(), fillAir, replaceSolid);
    }
    
    public void setBlockData(int x, int y, int z, BlockData bd) {
        Material material = bd.getMaterial();
        boolean patch = false;
        if( material == Material.IRON_BARS || material == Material.REDSTONE_WIRE
                || material == Material.WATER || material == Material.LAVA
                || material == Material.OAK_FENCE || material == Material.SPRUCE_FENCE
                || material == Material.JUNGLE_FENCE || material == Material.BIRCH_FENCE
                || material == Material.DARK_OAK_FENCE || material == Material.ACACIA_FENCE
                || material == Material.NETHER_BRICK_FENCE
                ) {
            patch = true;
        }
        zone_world.setBlockData(x, y, z, bd, patch);
    }
    public void setBlockType(int x, int y, int z, Material material) {
        boolean patch = false;
        if( material == Material.IRON_BARS || material == Material.REDSTONE_WIRE
                || material == Material.WATER || material == Material.LAVA
                || material == Material.OAK_FENCE || material == Material.SPRUCE_FENCE
                || material == Material.JUNGLE_FENCE || material == Material.BIRCH_FENCE
                || material == Material.DARK_OAK_FENCE || material == Material.ACACIA_FENCE
                || material == Material.NETHER_BRICK_FENCE
                ) {
            patch = true;
        }
        zone_world.setType(x, y, z, material, patch);
    }
    
    private boolean setBlock(Coord pos, MetaBlock block, int flags, boolean fillAir, boolean replaceSolid) {
        Material material = block.getBlock();
        Material mat = getMaterial(pos);

        if(mat == Material.CHEST) return false;
        if(mat == Material.TRAPPED_CHEST) return false;
        if(mat == Material.SPAWNER) return false;
                
        boolean isAir = mat == Material.AIR;
        
        if(!fillAir && isAir) return false;
        if(!replaceSolid && !isAir)    return false;
                
        boolean patch = false;
        
        if( material == Material.IRON_BARS || material == Material.REDSTONE_WIRE
                || material == Material.WATER || material == Material.LAVA
                || material == Material.OAK_FENCE || material == Material.SPRUCE_FENCE
                || material == Material.JUNGLE_FENCE || material == Material.BIRCH_FENCE
                || material == Material.DARK_OAK_FENCE || material == Material.ACACIA_FENCE
                || material == Material.NETHER_BRICK_FENCE
                ) {
            patch = true;
        }
        
                
        try{
            if(!patch) {
                zone_world.setBlockData(pos.getX(), pos.getY(), pos.getZ(), block.getState(), flags == 1);
            } else {
                zone_world.setType(pos.getX(), pos.getY(), pos.getZ(), block.getBlock(), true);
            }
                        
        } catch(NullPointerException npe){
            //ignore it.
        }
        
        Material type = material;
        Integer count = stats.get(type);
        if(count == null){
            stats.put(type, 1);    
        } else {
            stats.put(type, count + 1);
        }
        
        return true;
    }

}
