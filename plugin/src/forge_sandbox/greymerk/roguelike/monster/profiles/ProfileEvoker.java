package forge_sandbox.greymerk.roguelike.monster.profiles;

import forge_sandbox.greymerk.roguelike.monster.IEntity;
import forge_sandbox.greymerk.roguelike.monster.IMonsterProfile;
import forge_sandbox.greymerk.roguelike.monster.MobType;
import org.bukkit.World;

import java.util.Random;

public class ProfileEvoker implements IMonsterProfile {

	@Override
	public void addEquipment(World world, Random rand, int level, IEntity mob) {
		mob.setMobClass(MobType.EVOKER, true);
	}

}
