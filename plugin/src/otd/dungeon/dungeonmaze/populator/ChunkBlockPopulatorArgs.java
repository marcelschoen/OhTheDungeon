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
package otd.dungeon.dungeonmaze.populator;

import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Random;
import java.util.Set;

public class ChunkBlockPopulatorArgs {

    /** The world of the chunk. */
	private World w;
    /** The random object used for random generation with the proper seed. */
	private Random rand;
    /** The source chunk. */
	private Chunk chunkSrc;
        public Set<String> custom;
    /** The dungeon chunk data. */

	/**
	 * Constructor.
     *
	 * @param world World
	 * @param rand Random instance
	 * @param chunk Source chunk
     * @param dungeonChunk Dungeon chunk instance
	 */
	public ChunkBlockPopulatorArgs(World world, Random rand, Chunk chunk, Set<String> custom) {
		this.w = world;
		this.rand = rand;
		this.chunkSrc = chunk;
                this.custom = custom;
	}
	
	/**
	 * Get the world
	 * @return World
	 */
	public World getWorld() {
		return this.w;
	}
	
	/**
	 * Set the world
	 * @param w World
	 */
	public void setWorld(World w) {
		this.w = w;
	}
	
	/**
	 * Get the Random instance
	 * @return Random instance
	 */
	public Random getRandom() {
		return this.rand;
	}
	
	/**
	 * Set the Random instance
	 * @param rand Random instance
	 */
	public void setRandom(Random rand) {
		this.rand = rand;
	}
	
	/**
	 * Get the source chunk
	 * @return Source chunk
	 */
	public Chunk getSourceChunk() {
		return this.chunkSrc;
	}
	
	/**
	 * Set the source chunk
	 * @param chunkSrc Source chunk
	 */
	public void setSourceChunk(Chunk chunkSrc) {
		this.chunkSrc = chunkSrc;
	}
}
