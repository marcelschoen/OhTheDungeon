package forge_sandbox.greymerk.roguelike.dungeon.tasks;

import forge_sandbox.greymerk.roguelike.dungeon.DungeonStage;

import java.util.List;

public interface IDungeonTaskRegistry {

	public void addTask(IDungeonTask task, DungeonStage stage);
	
	public List<IDungeonTask> getTasks(DungeonStage stage);
	
}
