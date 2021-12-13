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
package otd.dungeon.dungeonmaze.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

@SuppressWarnings("UnusedDeclaration")
public class MazeUtils {

	/**
	 * Get the level of Dungeon Maze at a specific location
	 * @param l Location to get the level of
	 * @return Dungeon Maze level, 0 if there's no level at the param location
	 */
	public static int getDMLevel(Location l) {
		return getDMLevel(l.getWorld().getName(), l.getBlockY());
	}

	/**
	 * Get the level of Dungeon Maze at a specific location
	 * @param b Block to get the level of
	 * @return Dungeon Maze level, 0 if there's no level at the param location
	 */
	public static int getDMLevel(Block b) {
		return getDMLevel(b.getWorld().getName(), b.getY());
	}
	
	/**
	 * Get the level of Dungeon Maze at a specific location in a world
	 * @param w Name of the world to check in
 	 * @param y Y coordinate to get the level of
	 * @return Dungeon Maze level, 0 if there's no level at the param location
	 */
	public static int getDMLevel(String w, int y) {
		// TODO: Make sure to update this if any modifications are going to be made (possible using config files, optionally)
		
		// Make sure the world name isn't an empty string
		if(w.equals(""))
			return 0;
		
		// Is the block bellow the Dungeon Maze?
		if(y < 30)
			return 0;
		
		// Check if the block is inside the Dungeon Maze, if so return it's level
		int curLevel = 1;
		for (int dml=30; dml < 30+(7*6); dml+=6) {
			if(dml >= y && dml + 6 < y)
				return curLevel;
			curLevel++;
		}
		
		// The block was above the Dungeon Maze, return zero
		return 0;
	}
}
