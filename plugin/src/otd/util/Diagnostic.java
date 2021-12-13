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
package otd.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.spigotmc.SpigotConfig;
import otd.lib.DungeonWorldManager;
import otd.lib.async.io.papermc.lib.PaperLib;
import otd.populator.DungeonPopulator;
import otd.world.WorldDefine;

import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author
 */
public class Diagnostic {
    
    private static boolean isWorldHookReady() {
        List<World> worlds = Bukkit.getWorlds();
        for(World world : worlds) {
            if(world.getName().equalsIgnoreCase(DungeonWorldManager.WORLD_NAME)) continue;
            if(world.getName().equalsIgnoreCase(WorldDefine.WORLD_NAME)) continue;
            List<BlockPopulator> populators = world.getPopulators();
            boolean flag = false;
            for(BlockPopulator populator : populators) {
                if(populator instanceof DungeonPopulator) {
                    flag = true;
                    break;
                }
            }
            if(!flag) return false;
        }
        return true;
    }
    
    public static void diagnostic() {
        if(!isWorldHookReady()) {
            Bukkit.getLogger().log(Level.INFO, "{0}[OTD] Looks like you use /reload to load this plugin.,.. You need to restart your server, otherwise it won''t work", ChatColor.RED);
        }
    }
    
    private static boolean isJre32() {
        return System.getProperty("sun.arch.data.model").equals("32");
    }
    
    public static void main(String[] args) {
        System.out.println(System.getProperty("sun.arch.data.model"));
    }
    
    private static boolean getBoolean(String path, boolean def, String worldName) {
        SpigotConfig.config.addDefault("world-settings.default." + path, def);
        return SpigotConfig.config.getBoolean("world-settings." + worldName + "." + path, SpigotConfig.config.getBoolean("world-settings.default." + path));
    }

    
    public static boolean isSpawnerNotReady() {
        boolean b = false;
        for(World world : Bukkit.getWorlds()) {
            b = b | getBoolean("nerf-spawner-mobs", false, world.getName());
        }
        return b;
    }
    
    public static void check(Player p) {
        int count = 0;
        if(isSpawnerNotReady()) {
            count++;
            p.sendMessage(ChatColor.BLUE + "https://github.com/OhTheDungeon/OhTheDungeon/blob/main/docs/Spawner_Not_Working.md");
        }
        if(isJre32()) {
            count++;
            p.sendMessage(ChatColor.BLUE + "https://github.com/OhTheDungeon/OhTheDungeon/blob/main/docs/Use_Jre64.md");
        }
        if(!isWorldHookReady()) {
            count++;
            p.sendMessage(ChatColor.BLUE + "https://github.com/OhTheDungeon/OhTheDungeon/blob/main/docs/Fail_to_Hook_Minecraft_World_Object.md");
        }
        if(!PaperLib.isPaper()) {
            count++;
            p.sendMessage(ChatColor.BLUE + "https://github.com/OhTheDungeon/OhTheDungeon/blob/main/docs/Using_Paper.md");
        }
        
        if(count > 0) {
            p.sendMessage("OTD founds " + count + " issue(s) on your server. Check the above link for help");
        } else {
            p.sendMessage("OTD founds 0 issue on your server.");
        }
    }
}
