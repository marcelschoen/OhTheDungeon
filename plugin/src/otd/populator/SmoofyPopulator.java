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
package otd.populator;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import otd.Main;
import otd.config.SimpleWorldConfig;
import otd.config.WorldConfig;
import otd.dungeon.dungeonmaze.SmoofyDungeonPopulator;
import otd.util.AsyncLog;

import java.util.Random;
import java.util.Set;

/**
 *
 * @author
 */
public class SmoofyPopulator implements IPopulator {
    @Override
    public Set<String> getBiomeExclusions(World world) {
        SimpleWorldConfig swc = WorldConfig.wc.dict.get(world.getName());
        return swc.smoofydungeon.biomeExclusions;
    }
    @Override
    public boolean generateDungeon(World world, Random random, Chunk chunk) {
        halfAsyncGenerate(world, chunk, random);
        AsyncLog.logMessage("[Smoofy Dungeon @ " + world.getName() + "] x=" + chunk.getX() * 16 + ", z=" + chunk.getZ() * 16);
        return true;
    }
    
    public static void halfAsyncGenerate(World w, Chunk c, Random rand) {
        SmoofyDungeonPopulator.SmoofyDungeonInstance instance = new SmoofyDungeonPopulator.SmoofyDungeonInstance();
        BukkitRunnable r = new BukkitRunnable() {
            int step = 0;
            @Override
            public void run() {
                boolean b = instance.placeDungeon(w, rand, c.getX(), c.getZ(), step, 3, 3, 1, 1);
                step++;
                if(b) this.cancel();
            }
        };
        r.runTaskTimer(Main.instance, 1, 1);
    }
}
