package forge_sandbox.greymerk.roguelike.worldgen.shapes;

import forge_sandbox.greymerk.roguelike.worldgen.Coord;
import forge_sandbox.greymerk.roguelike.worldgen.IBlockFactory;
import forge_sandbox.greymerk.roguelike.worldgen.IWorldEditor;

import java.util.List;
import java.util.Random;

public interface IShape extends Iterable<Coord>{

	public void fill(IWorldEditor editor, Random rand, IBlockFactory block);
	
	public void fill(IWorldEditor editor, Random rand, IBlockFactory block, boolean fillAir, boolean replaceSolid);
	
	public List<Coord> get();
	
}
