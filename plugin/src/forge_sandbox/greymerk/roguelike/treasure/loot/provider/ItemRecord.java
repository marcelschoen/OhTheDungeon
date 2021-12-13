package forge_sandbox.greymerk.roguelike.treasure.loot.provider;

import forge_sandbox.greymerk.roguelike.treasure.loot.Record;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

//import net.minecraft.item.ItemStack;

public class ItemRecord extends ItemBase{

	public ItemRecord(int weight, int level) {
		super(weight, level);
	}

	@Override
	public ItemStack getLootItem(Random rand, int level) {
		return Record.getRandomRecord(rand);
	}
	
	

}
