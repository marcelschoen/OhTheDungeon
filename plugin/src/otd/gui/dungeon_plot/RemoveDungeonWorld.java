/* 
 * Copyright (C) 2021 shadow
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package otd.gui.dungeon_plot;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import otd.gui.Content;
import otd.util.I18n;
import otd.world.ChunkList;
import otd.world.DungeonWorld;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shadow
 */
public class RemoveDungeonWorld extends Content {
    
    public final static RemoveDungeonWorld instance = new RemoveDungeonWorld();
    
    public RemoveDungeonWorld() {
        super(I18n.instance.Menu_Remove_World, 9);
    }
    
    @Override
    public void init() {
        inv.clear();
        {
            ItemStack is = new ItemStack(Material.LAVA_BUCKET);
            ItemMeta im = is.getItemMeta();
            
            im.setDisplayName(I18n.instance.Menu_remove);
            List<String> lores = new ArrayList<>();
            lores.add(I18n.instance.Menu_remove_lore);
            im.setLore(lores);
            
            is.setItemMeta(im);
            
            addItem(4, is);
        }
    }
    
    @EventHandler
    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof RemoveDungeonWorld)) {
            return;
        }
        if (e.getClick().equals(ClickType.NUMBER_KEY)){
            kcancel(e);
            return;
        }

        kcancel(e);
        int slot = e.getRawSlot();
        Player p = (Player) e.getWhoClicked();
        RemoveDungeonWorld holder = (RemoveDungeonWorld) e.getInventory().getHolder();
        if(holder == null) return;
        
        if(slot == 4) {
            DungeonWorld.removeDungeonWorld();
            ChunkList.clear();
            p.closeInventory();
            p.sendMessage(ChatColor.RED + I18n.instance.Menu_remove_click);
        }
    }
}
