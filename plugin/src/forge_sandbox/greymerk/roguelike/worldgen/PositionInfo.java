package forge_sandbox.greymerk.roguelike.worldgen;

//import net.minecraft.world.World;

import forge_sandbox.BlockPos;
import org.bukkit.World;
import org.bukkit.block.Biome;

//import net.minecraft.world.biome.Biome;

public class PositionInfo implements IPositionInfo {

	private final World world;
	private final Coord pos;
	
	public PositionInfo(World world, Coord pos){
		this.world = world;
		this.pos = pos;
	}
	
	@Override
	public String getDimension() {
		return world.getName();
	}

	@Override
	public Biome getBiome() {
                BlockPos loc = pos.getBlockPos();
		return world.getBiome(loc.getX(), loc.getZ());
	}
}
