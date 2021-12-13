package forge_sandbox.greymerk.roguelike.worldgen.filter;

import forge_sandbox.greymerk.roguelike.theme.ITheme;
import forge_sandbox.greymerk.roguelike.worldgen.*;
import forge_sandbox.greymerk.roguelike.worldgen.blocks.BlockType;
import forge_sandbox.greymerk.roguelike.worldgen.shapes.IShape;
import forge_sandbox.greymerk.roguelike.worldgen.shapes.RectWireframe;

import java.util.Random;

public class WireframeFilter implements IFilter{

	@Override
	public void apply(IWorldEditor editor, Random rand, ITheme theme, IBounded box) {
		Coord start = box.getStart();
		Coord end = box.getEnd();
		
		start.add(Cardinal.UP, 100);
		end.add(Cardinal.UP, 100);
		
		IShape shape = new RectWireframe(start, end);
		IBlockFactory block = BlockType.get(BlockType.SEA_LANTERN);
		
		shape.fill(editor, rand, block);
	}
}
