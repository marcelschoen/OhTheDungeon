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
package otd.dungeon.dungeonmaze.populator.surface.plants;

import org.bukkit.Chunk;
import org.bukkit.Material;
import otd.dungeon.dungeonmaze.populator.surface.SurfaceBlockPopulator;
import otd.dungeon.dungeonmaze.populator.surface.SurfaceBlockPopulatorArgs;

import java.util.Random;

public class TallGrassPopulator extends SurfaceBlockPopulator {

    /** General populator constants. */
    private static final int CHUNK_ITERATIONS = 100;
    private static final float CHUNK_ITERATIONS_CHANCE = .35f;

	@Override
	public void populateSurface(SurfaceBlockPopulatorArgs args) {
		final Chunk chunk = args.getSourceChunk();
		final Random rand = args.getRandom();
        final int xGrass = rand.nextInt(16);
        final int zGrass = rand.nextInt(16);

        // Get the surface level
        int ySurface = args.getSurfaceLevel(xGrass, zGrass);

        if(chunk.getBlock(xGrass, ySurface, zGrass).getType() == Material.GRASS_BLOCK) {
            final int yGrass = ySurface + 1;

            chunk.getBlock(xGrass, yGrass, zGrass).setType(Material.FERN);
//            chunk.getBlock(xGrass, yGrass, zGrass).setData((byte) 1);
        }
	}

    @Override
    public int getChunkIterations() {
        return CHUNK_ITERATIONS;
    }

    @Override
    public float getChunkIterationsChance() {
        return CHUNK_ITERATIONS_CHANCE;
    }
}