package forge_sandbox.greymerk.roguelike.treasure.loot.provider;

import forge_sandbox.greymerk.roguelike.treasure.loot.Book;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ItemBook extends ItemBase{

    Book type;
    
    public ItemBook(Book type){
        this(type, 1, 0);
    }
    
    public ItemBook(Book type, int weight, int level){
        super(weight, level);
        this.type = type;
    }

    @Override
    public ItemStack getLootItem(Random rand, int level) {
        return Book.get(type);
    }
    
}
