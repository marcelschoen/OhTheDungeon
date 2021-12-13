package forge_sandbox.greymerk.roguelike.dungeon.tasks;

import forge_sandbox.greymerk.roguelike.dungeon.IDungeon;
import forge_sandbox.greymerk.roguelike.dungeon.settings.ISettings;
import forge_sandbox.greymerk.roguelike.worldgen.IWorldEditor;

import java.util.Random;

public interface IDungeonTask {

	public boolean execute(IWorldEditor editor, Random rand, IDungeon dungeon, ISettings settings, int index);
	
}
