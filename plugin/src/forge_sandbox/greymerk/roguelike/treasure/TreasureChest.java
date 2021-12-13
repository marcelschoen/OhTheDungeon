package forge_sandbox.greymerk.roguelike.treasure;

import forge_sandbox.greymerk.roguelike.worldgen.Coord;
import forge_sandbox.greymerk.roguelike.worldgen.IWorldEditor;
import forge_sandbox.greymerk.roguelike.worldgen.MetaBlock;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import otd.lib.ZoneWorld;
import otd.lib.async.later.roguelike.Chest_Generate_Later;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//import shadow_lib.async.later.Chest_SetRandomSlot_Later;
//import shadow_lib.async.later.Chest_SetSlot_Later;

public class TreasureChest implements ITreasureChest{

    protected Inventory inventory;
    protected Treasure type;
    protected Random rand;
    private InventoryHolder chest;
    private Block block;
    private int level;
    private IWorldEditor editor;
    private Coord pos;
    
    @Override
    public Coord getPos() {
        return this.pos;
    }
        
    private static class Slot {
        int slot;
        ItemStack item;
        public Slot(int slot, ItemStack item) {
            this.slot = slot;
            this.item = item;
        }
    }
    List<Slot> slots = new ArrayList<>();
    
    static {
        ZoneWorld.registerSpecialBlock(Material.TRAPPED_CHEST);
        ZoneWorld.registerSpecialBlock(Material.CHEST);
    }

    public TreasureChest(Treasure type){
        this.type = type;
        this.level = 0;
    }
        
    public void generate_later_chunk(Chunk chunk, Coord pos) {
        int x = pos.getX() % 16;
        int y = pos.getY();
        int z = pos.getZ() % 16;
        if(x < 0) x = x + 16;
        if(z < 0) z = z + 16;            

        BlockState bs = chunk.getBlock(x, y, z).getState();
        if(bs instanceof InventoryHolder) this.chest = (InventoryHolder) bs;
        this.block = editor.getBlock(pos);
        this.inventory = new Inventory(rand, chest);
        editor.addChest(this);
    }
    
    public void generate_later(IWorldEditor editor, Coord pos) {
        BlockState bs = editor.getBlock(pos).getState();
        if(bs instanceof InventoryHolder) this.chest = (InventoryHolder) bs;
        this.block = editor.getBlock(pos);
        this.inventory = new Inventory(rand, chest);    
        editor.addChest(this);
    }
    
    @Override
    public void addBufferedItems(IWorldEditor editor) {
        BlockState bs = editor.getBlock(pos).getState();
        if(bs instanceof InventoryHolder) this.chest = (InventoryHolder) bs;
        this.inventory = new Inventory(rand, chest);
        
        for(Slot slot : slots) {
            if(slot.slot == -1) {
                this.inventory.setRandomEmptySlot(slot.item);
            } else {
                this.inventory.setInventorySlot(slot.slot, slot.item);
            }
        }
    }
    
    @Override
    public ITreasureChest generate(IWorldEditor editor, Random rand, Coord pos, int level, boolean trapped) throws ChestPlacementException {
        this.rand = rand;
        this.level = level;
        this.editor = editor;
        
        MetaBlock chestType = new MetaBlock(trapped ? Material.TRAPPED_CHEST : Material.CHEST);
        
        boolean success = chestType.set(editor, pos);
        
        if(!success){
            throw new ChestPlacementException("Failed to place chest in world");
        }
        
        if(!editor.isFakeWorld()) this.generate_later(editor, pos);
        else {
            editor.addLater(new Chest_Generate_Later(this, editor, pos));
            this.inventory = null;
        }
        
        this.pos = pos;
                
        return this;
    }
    
    public void setSlot_later(int slot, ItemStack item) {
        this.inventory.setInventorySlot(slot, item);
    }
        
    @Override
    public boolean setSlot(int slot, ItemStack item){
        if(this.editor.isFakeWorld()) {
            slots.add(new Slot(slot, item));
        } else {
            this.setSlot_later(slot, item);
        }
        return true;
    }
    
    public void setRandomEmptySlot_later(ItemStack item) {
        this.inventory.setRandomEmptySlot(item);
    }

    @Override
    public boolean setRandomEmptySlot(ItemStack item){
            if(this.editor.isFakeWorld()) {
                slots.add(new Slot(-1, item));
            } else {
                this.setRandomEmptySlot_later(item);
            }
            return true;
    }

    @Override
    public Treasure getType(){
        return this.type;
    }
    
    @Override
    public int getSize(){
        return 27; //for async
    }

    @Override
    public int getLevel() {
        if(level < 0) return 0;
        if(level > 4) return 4;
        return this.level;
    }

    @Override
    public void setLootTable(LootTable table) {
    }
}
