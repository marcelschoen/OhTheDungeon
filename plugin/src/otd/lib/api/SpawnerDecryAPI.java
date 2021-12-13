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
package otd.lib.api;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import otd.config.SimpleWorldConfig;
import otd.config.WorldConfig;

import java.util.SplittableRandom;

/**
 *
 * @author
 */
public class SpawnerDecryAPI {
    
    public static SplittableRandom random = new SplittableRandom();
    
    public static void setSpawnerDecry(Block block, JavaPlugin plugin) {
        
        World world = block.getWorld();
        String name = world.getName();
        
        double rate = 0;
        if(WorldConfig.wc.dict.containsKey(name)) {
            SimpleWorldConfig swc = WorldConfig.wc.dict.get(name);
            rate = swc.spawner_rejection_rate;
        }
        
        if(random.nextDouble() < rate) {
            block.setType(Material.AIR);
            return;
        }
        
        if(!(block.getState() instanceof TileState)) return;
        TileState ts = (TileState) block.getState();
        NamespacedKey key = new NamespacedKey(plugin, "decry");
        ts.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte)15);
        
        ts.update(true, false);
    }
}
