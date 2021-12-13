package forge_sandbox.greymerk.roguelike.treasure.loot.books;

import forge_sandbox.greymerk.roguelike.treasure.loot.BookBase;
import forge_sandbox.greymerk.roguelike.worldgen.IWorldEditor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.StringUtils;
import otd.util.FormatItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//import net.minecraft.block.Block;

public class BookStatistics extends BookBase{

	public BookStatistics(IWorldEditor editor){
		super("greymerk", "Statistics");
		
		for(String page : getPages(editor)){
			this.addPage(page);
		}		
	}
	
	private List<String> getPages(IWorldEditor editor){
		List<String> pages = new ArrayList<>();
		Map<Material, Integer> stats = editor.getStats();
		
		int counter = 0;
		String page = "";
		for(Material type : stats.keySet()){
			int count = stats.get(type);
			String name = StringUtils.abbreviate((new FormatItem(type)).getUnlocalizedName(), 16);
			String line = name + " : " + count + "\n";
			page += line;
			++counter;
			if(counter == 12){
				counter = 0;
				pages.add(page);
				page = "";
			}
		}
		pages.add(page);
		return pages;
	}
}
