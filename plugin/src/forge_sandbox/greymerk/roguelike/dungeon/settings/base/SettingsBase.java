package forge_sandbox.greymerk.roguelike.dungeon.settings.base;

import forge_sandbox.greymerk.roguelike.dungeon.settings.DungeonSettings;
import forge_sandbox.greymerk.roguelike.dungeon.settings.SettingIdentifier;
import forge_sandbox.greymerk.roguelike.dungeon.settings.SettingsContainer;

import java.util.ArrayList;

public class SettingsBase extends DungeonSettings{
	
	public static final SettingIdentifier ID = new SettingIdentifier(SettingsContainer.BUILTIN_NAMESPACE, "base");
	
	public SettingsBase(){
		
		this.id = ID;
		this.inherit = new ArrayList<SettingIdentifier>();
		this.inherit.add(SettingsRooms.ID);
		this.inherit.add(SettingsSecrets.ID);
		this.inherit.add(SettingsSegments.ID);
		this.inherit.add(SettingsLayout.ID);
		this.inherit.add(SettingsTheme.ID);
		this.inherit.add(SettingsLootRules.ID);
	}
}
