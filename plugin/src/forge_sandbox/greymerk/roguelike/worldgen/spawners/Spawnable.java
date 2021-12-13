package forge_sandbox.greymerk.roguelike.worldgen.spawners;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import forge_sandbox.greymerk.roguelike.worldgen.Coord;
import forge_sandbox.greymerk.roguelike.worldgen.IWorldEditor;
import forge_sandbox.greymerk.roguelike.worldgen.MetaBlock;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import otd.Main;
import otd.MultiVersion;
import otd.lib.ZoneWorld;
import otd.lib.api.SpawnerApi;
import otd.lib.api.SpawnerDecryAPI;
import otd.lib.async.later.roguelike.Spawnable_Later;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Spawnable {

	public Spawner type;
	private final List<SpawnPotential> potentials;
        
        public static SpawnerApi.SpawnerHandler handler = null;
        
        static {
            ZoneWorld.registerSpecialBlock(Material.SPAWNER);
        }
		
	public Spawnable(Spawner type){
		this.potentials = new ArrayList<>();
		this.type = type;
	}
	
	public Spawnable(JsonElement data) throws Exception{
		this.potentials = new ArrayList<>();
		
		JsonArray arr = data.getAsJsonArray();
		for(JsonElement e : arr){
			SpawnPotential potential = new SpawnPotential(e.getAsJsonObject());
			this.potentials.add(potential);
		}
	}
        
        public void generate_later_chunk(Chunk chunk, Coord pos, IWorldEditor editor, Random rand, Coord cursor, int level) {
            if(handler != null) {
                handler.generate(this, pos, editor, rand, cursor, level);
            } else {
                this.generate_later_orign_chunk(chunk, pos, editor, rand, cursor, level);
            }
        }
        
        public void generate_later(Coord pos, IWorldEditor editor, Random rand, Coord cursor, int level) {
            if(handler != null) {
                handler.generate(this, pos, editor, rand, cursor, level);
            } else {
                this.generate_later_orign(pos, editor, rand, cursor, level);
            }
        }

        private static class GenerateLaterOrigin117R1 {
            public void generate_chunk(Chunk chunk, Coord pos, IWorldEditor editor, Random rand, Coord cursor, int level, Spawnable s) {
                
//                Bukkit.getLogger().log(Level.SEVERE, pos.getX() + "," + pos.getY() + "," + pos.getZ());
                
                int x = pos.getX() % 16;
                int y = pos.getY();
                int z = pos.getZ() % 16;
                if(x < 0) x = x + 16;
                if(z < 0) z = z + 16;  
            
            	Block tileentity = chunk.getBlock(x, y, z);
                BlockState blockState = tileentity.getState();
                if (!(blockState instanceof CreatureSpawner)) return;
                org.bukkit.craftbukkit.v1_17_R1.CraftWorld ws = (org.bukkit.craftbukkit.v1_17_R1.CraftWorld) 
                        tileentity.getWorld();
                net.minecraft.world.level.block.entity.TileEntity te = 
                        ws.getHandle().getTileEntity(
                                new net.minecraft.core.BlockPosition(pos.getX(), pos.getY(), pos.getZ()));
                if(te == null) return;

                net.minecraft.nbt.NBTTagCompound nbt = new net.minecraft.nbt.NBTTagCompound();
                nbt.setInt("x", pos.getX());
                nbt.setInt("y", pos.getY());
                nbt.setInt("z", pos.getZ());

                nbt.set("SpawnPotentials", (net.minecraft.nbt.NBTBase) s.getSpawnPotentials(rand, level));

                te.load(nbt);
                
                SpawnerDecryAPI.setSpawnerDecry(tileentity, Main.instance);
            }
            
            public void generate(Coord pos, IWorldEditor editor, Random rand, Coord cursor, int level, Spawnable s) {
                Block tileentity = editor.getBlock(pos);
                BlockState blockState = tileentity.getState();
                if (!(blockState instanceof CreatureSpawner)) return;
                org.bukkit.craftbukkit.v1_17_R1.CraftWorld ws = (org.bukkit.craftbukkit.v1_17_R1.CraftWorld) 
                        tileentity.getWorld();
                net.minecraft.world.level.block.entity.TileEntity te = 
                        ws.getHandle().getTileEntity(
                                new net.minecraft.core.BlockPosition(pos.getX(), pos.getY(), pos.getZ()));
                if(te == null) return;

                net.minecraft.nbt.NBTTagCompound nbt = new net.minecraft.nbt.NBTTagCompound();
                nbt.setInt("x", pos.getX());
                nbt.setInt("y", pos.getY());
                nbt.setInt("z", pos.getZ());

                nbt.set("SpawnPotentials", (net.minecraft.nbt.NBTBase) s.getSpawnPotentials(rand, level));

                te.load(nbt);
                
                SpawnerDecryAPI.setSpawnerDecry(tileentity, Main.instance);
            }
        }

        private static class GenerateLaterOriginUnknown {

            private static class NMS {
                public void generate_chunk(Chunk chunk, Coord pos, IWorldEditor editor, Random rand, Coord cursor, int level, Spawnable s) {

                    int x = pos.getX() % 16;
                    int y = pos.getY();
                    int z = pos.getZ() % 16;
                    if(x < 0) x = x + 16;
                    if(z < 0) z = z + 16;

                    Block tileentity = chunk.getBlock(x, y, z);
                    BlockState blockState = tileentity.getState();
                    if (!(blockState instanceof CreatureSpawner)) return;

                    try {
                        Object ws = MultiVersion.SpawnableTest.getHandle.invoke(tileentity.getWorld());
                        Object bp = MultiVersion.SpawnableTest.NewBlockPosition.newInstance(pos.getX(), pos.getY(), pos.getZ());
                        Object te = MultiVersion.SpawnableTest.getTileEntity.invoke(ws, bp);

                        if(te == null) return;

                        net.minecraft.nbt.NBTTagCompound nbt = new net.minecraft.nbt.NBTTagCompound();
                        nbt.setInt("x", pos.getX());
                        nbt.setInt("y", pos.getY());
                        nbt.setInt("z", pos.getZ());

                        nbt.set("SpawnPotentials", (net.minecraft.nbt.NBTBase) s.getSpawnPotentials(rand, level));

                        MultiVersion.SpawnableTest.load.invoke(te, nbt);

                        SpawnerDecryAPI.setSpawnerDecry(tileentity, Main.instance);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
                    }
                }

                public void generate(Coord pos, IWorldEditor editor, Random rand, Coord cursor, int level, Spawnable s) {

                    Block tileentity = editor.getBlock(pos);
                    BlockState blockState = tileentity.getState();
                    if (!(blockState instanceof CreatureSpawner)) return;

                    try {
                        Object ws = MultiVersion.SpawnableTest.getHandle.invoke(tileentity.getWorld());
                        Object bp = MultiVersion.SpawnableTest.NewBlockPosition.newInstance(pos.getX(), pos.getY(), pos.getZ());
                        Object te = MultiVersion.SpawnableTest.getTileEntity.invoke(ws, bp);

                        if(te == null) return;

                        net.minecraft.nbt.NBTTagCompound nbt = new net.minecraft.nbt.NBTTagCompound();
                        nbt.setInt("x", pos.getX());
                        nbt.setInt("y", pos.getY());
                        nbt.setInt("z", pos.getZ());

                        nbt.set("SpawnPotentials", (net.minecraft.nbt.NBTBase) s.getSpawnPotentials(rand, level));

                        MultiVersion.SpawnableTest.load.invoke(te, nbt);

                        SpawnerDecryAPI.setSpawnerDecry(tileentity, Main.instance);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
                    }
                }
            }

            public void generate_chunk(Chunk chunk, Coord pos, IWorldEditor editor, Random rand, Coord cursor, int level, Spawnable s) {

                boolean vanilla = false;
                if(!MultiVersion.SpawnableTest.result || !MultiVersion.SpawnPotentialTest.result) {
                    //vanilla mode spawner
                    vanilla = true;
                }

                if(vanilla) {
                    int x = pos.getX() % 16;
                    int y = pos.getY();
                    int z = pos.getZ() % 16;
                    if(x < 0) x = x + 16;
                    if(z < 0) z = z + 16;

                    Block tileentity = chunk.getBlock(x, y, z);
                    BlockState blockState = tileentity.getState();
                    if (!(blockState instanceof CreatureSpawner)) return;

                    CreatureSpawner tileentitymobspawner = (CreatureSpawner)blockState;
                    tileentitymobspawner.setSpawnedType(Spawner.toEntityType(s.type));
                    tileentitymobspawner.update();
                    SpawnerDecryAPI.setSpawnerDecry(tileentity, Main.instance);
                } else {
                    new NMS().generate_chunk(chunk, pos, editor, rand, cursor, level, s);
                }
            }

            public void generate(Coord pos, IWorldEditor editor, Random rand, Coord cursor, int level, Spawnable s) {

                boolean vanilla = false;
                if(!MultiVersion.SpawnableTest.result || !MultiVersion.SpawnPotentialTest.result) {
                    //vanilla mode spawner
                    vanilla = true;
                }

                if(vanilla) {
                    Block tileentity = editor.getBlock(pos);
                    BlockState blockState = tileentity.getState();
                    if (!(blockState instanceof CreatureSpawner)) return;
                    CreatureSpawner tileentitymobspawner = (CreatureSpawner)blockState;
                    tileentitymobspawner.setSpawnedType(Spawner.toEntityType(s.type));
                    tileentitymobspawner.update();
                    SpawnerDecryAPI.setSpawnerDecry(tileentity, Main.instance);
                } else {
                    new NMS().generate(pos, editor, rand, cursor, level, s);
                }
            }
        }


        public void generate_later_orign(Coord pos, IWorldEditor editor, Random rand, Coord cursor, int level) {
            (new GenerateLaterOrigin117R1()).generate(pos, editor, rand, cursor, level, this);
        }
        
        public void generate_later_orign_chunk(Chunk chunk, Coord pos, IWorldEditor editor, Random rand, Coord cursor, int level) {
            (new GenerateLaterOrigin117R1()).generate_chunk(chunk, pos, editor, rand, cursor, level, this);
        }
        
		
	public void generate(IWorldEditor editor, Random rand, Coord cursor, int level){
		Coord pos = new Coord(cursor);
		editor.setBlock(pos, new MetaBlock(Material.SPAWNER), true, true);
		
                
                if(!editor.isFakeWorld()) generate_later(pos, editor, rand, cursor, level);
                else editor.addLater(new Spawnable_Later(this, pos, editor, rand, cursor, level));
	}

        private static class GetSpawnPotentials117R1 {
            public Object get(Random rand, int level, Spawnable s) {
                if(s.type != null){
			SpawnPotential potential = new SpawnPotential(Spawner.getName(s.type));
			return potential.getNBTTagList(rand, level);
		}
		
		net.minecraft.nbt.NBTTagList potentials = 
                        new net.minecraft.nbt.NBTTagList();
		
		for(SpawnPotential potential : s.potentials){
			net.minecraft.nbt.NBTTagCompound nbt = 
                                (net.minecraft.nbt.NBTTagCompound) potential.getNBTTagCompound(level);
			potentials.add(nbt);
		}
		
		return potentials;
            }
        }
        
        private static class GetSpawnPotentialsUnknown {
            
            private static class NBT {
                public Object getWithNBT(Random rand, int level, Spawnable s) {

                    if(s.type != null){
                            SpawnPotential potential = new SpawnPotential(Spawner.getName(s.type));
                            return potential.getNBTTagList(rand, level);
                    }

                    net.minecraft.nbt.NBTTagList potentials = 
                            new net.minecraft.nbt.NBTTagList();

                    for(SpawnPotential potential : s.potentials){
                            net.minecraft.nbt.NBTTagCompound nbt = 
                                    (net.minecraft.nbt.NBTTagCompound) potential.getNBTTagCompound(level);
                            potentials.add(nbt);
                    }

                    return potentials;
                }
            }
            
            public Object get(Random rand, int level, Spawnable s) {
                
                if(!MultiVersion.SpawnableTest.result || !MultiVersion.SpawnPotentialTest.result) return null;
                
                return (new NBT()).getWithNBT(rand, level, s);
            }
        }
        
	private Object getSpawnPotentials(Random rand, int level){
		return (new GetSpawnPotentials117R1()).get(rand, level, this);
	}
}
