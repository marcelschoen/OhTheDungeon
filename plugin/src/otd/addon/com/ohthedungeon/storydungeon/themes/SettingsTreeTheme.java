package otd.addon.com.ohthedungeon.storydungeon.themes;

import forge_sandbox.greymerk.roguelike.dungeon.settings.*;
import forge_sandbox.greymerk.roguelike.dungeon.settings.base.SettingsBase;
import forge_sandbox.greymerk.roguelike.dungeon.towers.Tower;
import forge_sandbox.greymerk.roguelike.theme.Theme;
import otd.lib.BiomeDictionary;

import java.util.ArrayList;
import java.util.List;
//import net.minecraftforge.common.BiomeDictionary;

public class SettingsTreeTheme extends DungeonSettings{
	
	public static final SettingIdentifier ID = new SettingIdentifier(SettingsContainer.BUILTIN_NAMESPACE, "ice");
	
	public SettingsTreeTheme(){
		
		this.id = ID;
		this.inherit.add(SettingsBase.ID);
		
		this.criteria = new SpawnCriteria();
		List<BiomeDictionary.Type> biomes = new ArrayList<>();
		biomes.add(BiomeDictionary.Type.SNOWY);
		this.criteria.setBiomeTypes(biomes);
		
		this.towerSettings = new TowerSettings(Tower.TREE, Theme.getTheme(Theme.OAK));
	}
}
