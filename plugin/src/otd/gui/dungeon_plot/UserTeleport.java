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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import otd.Main;
import otd.config.WorldConfig;
import otd.gui.Content;
import otd.util.I18n;
import otd.world.ChunkList;
import otd.world.DungeonTask;
import otd.world.DungeonWorld;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shadow
 */
public class UserTeleport extends Content {
    public final static UserTeleport instance = new UserTeleport();
    private final static int SLOT = 54;
    public UserTeleport() {
        super(I18n.instance.Dungeon_Plot_User_Teleport, SLOT);
    }
    
    public static void teleportBed(Player p) {
        Location loc = p.getBedSpawnLocation();
        if(loc == null) {
            loc = Bukkit.getWorlds().get(0).getSpawnLocation();
        }
        p.teleport(loc);
    }
    
    private static void teleport(Player p, int[] pos) {
        if(
                DungeonWorld.world == null ||
                WorldConfig.wc.dungeon_world.finished == false ||
                DungeonTask.isGenerating()
                ) {
            p.sendMessage(I18n.instance.Dungeon_Plot_Not_Ready);
            return;
        }
        int x = pos[0] * 16;
        int z = pos[1] * 16 - 16;
        
        int y = DungeonWorld.world.getHighestBlockYAt(x, z);
        if(y > 80) y = 66;
        y++;
        p.teleport(new Location(DungeonWorld.world, x, y, z));
        
        Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
            p.sendMessage(ChatColor.GREEN + I18n.instance.User_TP_Suggest2);
        }, 1L);
    }
    
    @Override
    public void init() {
        int count = WorldConfig.wc.dungeon_world.dungeon_count_finish;
        
        for(int i = 0; i < count; i++) {
            ItemStack is = new ItemStack(Material.COMPASS);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Dungeon_Plot + " " + i);
            
            List<String> lores = new ArrayList<>();
            lores.add(I18n.instance.Click_To_Teleport);
            
            im.setLore(lores);
            
            is.setItemMeta(im);
            
            addItem(i, is);
        }
        
        {
            ItemStack is = new ItemStack(Material.RED_BED);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(I18n.instance.Back_To_Normal_World);
            
            List<String> lores = new ArrayList<>();
            lores.add(I18n.instance.Click_To_Teleport);
            
            im.setLore(lores);
            
            is.setItemMeta(im);
            
            addItem(53, is);
        }
    }
    
    @EventHandler
    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof UserTeleport)) {
            return;
        }
        
        if (e.getClick().equals(ClickType.NUMBER_KEY)){
            kcancel(e);
            return;
        }

        kcancel(e);
        int slot = e.getRawSlot();
        Player p = (Player) e.getWhoClicked();
        UserTeleport holder = (UserTeleport) e.getInventory().getHolder();
        if(holder == null) return;
        
        if(slot == 53) {
            p.closeInventory();
            teleportBed(p);
            return;
        }
        
        int count = WorldConfig.wc.dungeon_world.dungeon_count_finish;
        if(slot < count) {
            ItemStack is = e.getInventory().getItem(slot);
            if(is != null && is.getType() == Material.COMPASS) {
                int[] pos = ChunkList.chunk_table.get(slot);
                p.closeInventory();
                teleport(p, pos);
            }
        }
    }
}
