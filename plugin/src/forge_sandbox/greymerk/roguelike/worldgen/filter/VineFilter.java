package forge_sandbox.greymerk.roguelike.worldgen.filter;

import forge_sandbox.greymerk.roguelike.theme.ITheme;
import forge_sandbox.greymerk.roguelike.worldgen.Coord;
import forge_sandbox.greymerk.roguelike.worldgen.IBounded;
import forge_sandbox.greymerk.roguelike.worldgen.IWorldEditor;
import forge_sandbox.greymerk.roguelike.worldgen.blocks.Vine;
import forge_sandbox.greymerk.roguelike.worldgen.shapes.Shape;

import java.util.Random;

public class VineFilter implements IFilter{

	@Override
	public void apply(IWorldEditor editor, Random rand, ITheme theme, IBounded box) {
		for(Coord pos : box.getShape(Shape.RECTSOLID)){
			if(rand.nextInt(10) == 0) Vine.set(editor, pos);
		}
	}
}
