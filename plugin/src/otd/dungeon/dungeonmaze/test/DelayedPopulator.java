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
package otd.dungeon.dungeonmaze.test;

import org.bukkit.Chunk;
import org.bukkit.World;
import otd.dungeon.dungeonmaze.populator.ChunkBlockPopulator;

import java.util.Random;

public class DelayedPopulator {

    private ChunkBlockPopulator populator;
    private World world;
    private Random rand;
    private Chunk chunk;

    public DelayedPopulator(ChunkBlockPopulator populator, World world, Random rand, Chunk chunk) {
        this.populator = populator;
        this.world = world;
        this.rand = rand;
        this.chunk = chunk;
    }

    public ChunkBlockPopulator getPopulator() {
        return populator;
    }

    public World getWorld() {
        return world;
    }

    public Random getRand() {
        return rand;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void populate() {
        this.populator.populate(this.world, this.rand, this.chunk, true);
    }
}
