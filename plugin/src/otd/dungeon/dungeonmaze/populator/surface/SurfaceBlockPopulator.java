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
package otd.dungeon.dungeonmaze.populator.surface;

import org.bukkit.Chunk;
import org.bukkit.World;
import otd.dungeon.dungeonmaze.populator.ChunkBlockPopulator;
import otd.dungeon.dungeonmaze.populator.ChunkBlockPopulatorArgs;

import java.util.Random;

public abstract class SurfaceBlockPopulator extends ChunkBlockPopulator {
	
	@Override
    public void populateChunk(ChunkBlockPopulatorArgs args) {
        World w = args.getWorld();
        Random rand = args.getRandom();
        Chunk chunk = args.getSourceChunk();
			
		// Construct the DMMazePopulatorArgs to use the the populateMaze method
		SurfaceBlockPopulatorArgs surfaceArgs = new SurfaceBlockPopulatorArgs(w, rand, chunk);
		
		// Populate the maze
		populateSurface(surfaceArgs);
	}
	
	/**
	 * Population method
	 * @param args Populator arguments
	 */
	public abstract void populateSurface(SurfaceBlockPopulatorArgs args);
}
