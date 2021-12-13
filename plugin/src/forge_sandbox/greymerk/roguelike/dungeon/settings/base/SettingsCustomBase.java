package forge_sandbox.greymerk.roguelike.dungeon.settings.base;

import forge_sandbox.greymerk.roguelike.dungeon.LevelGenerator;
import forge_sandbox.greymerk.roguelike.dungeon.base.DungeonFactory;
import forge_sandbox.greymerk.roguelike.dungeon.base.DungeonRoom;
import forge_sandbox.greymerk.roguelike.dungeon.base.SecretFactory;
import forge_sandbox.greymerk.roguelike.dungeon.segment.Segment;
import forge_sandbox.greymerk.roguelike.dungeon.segment.SegmentGenerator;
import forge_sandbox.greymerk.roguelike.dungeon.settings.DungeonSettings;
import forge_sandbox.greymerk.roguelike.dungeon.settings.LevelSettings;
import forge_sandbox.greymerk.roguelike.dungeon.settings.SpawnCriteria;
import forge_sandbox.greymerk.roguelike.dungeon.settings.TowerSettings;
import forge_sandbox.greymerk.roguelike.dungeon.towers.Tower;
import forge_sandbox.greymerk.roguelike.theme.Theme;

public class SettingsCustomBase extends DungeonSettings{
	
	public SettingsCustomBase(){
		
		this.criteria = new SpawnCriteria();
		
		this.towerSettings = new TowerSettings(Tower.ROGUE, Theme.getTheme(Theme.QUARTZ));
		
		for(int i = 0; i < 5; ++i){
			LevelSettings level = new LevelSettings();
			level.setTheme(Theme.getTheme(Theme.QUARTZ));
			
			SegmentGenerator segments = new SegmentGenerator(Segment.ARCH);
			segments.add(Segment.DOOR, 10);
			level.setSegments(segments);

			DungeonFactory rooms = new DungeonFactory();
			rooms.addRandom(DungeonRoom.CORNER, 1);
			level.setRooms(rooms);
			
			level.setGenerator(LevelGenerator.CLASSIC);
			
			SecretFactory secrets = new SecretFactory();
			level.setSecrets(secrets);
			
			level.setDifficulty(i);
			
			level.setNumRooms(10);
			level.setRange(50);
			level.setScatter(15);
			
			levels.put(i, level);
		}
	}
}
