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
package otd.dungeon.dungeonmaze.populator.maze.decoration;

import org.bukkit.Chunk;
import org.bukkit.Material;
import otd.dungeon.dungeonmaze.populator.ChunkBlockPopulator;
import otd.dungeon.dungeonmaze.populator.ChunkBlockPopulatorArgs;

import java.util.Random;

public class OresInGroundPopulator extends ChunkBlockPopulator {

    /** Populator constants. */
	private static final float ORE_CHANCE = .005f;

    @Override
    public void populateChunk(ChunkBlockPopulatorArgs args) {
        final Chunk chunk = args.getSourceChunk();
        final Random rand = args.getRandom();

        // The layers for each 4 rooms in the variable y
        for(int y = 1; y <= 29; y += 1) {
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    if(rand.nextFloat() < ORE_CHANCE) {
                        switch (rand.nextInt(9)) {
                        case 0:
                            chunk.getBlock(x, y, z).setType(Material.GOLD_ORE);
                            break;

                        case 1:
                            chunk.getBlock(x, y, z).setType(Material.IRON_ORE);
                            break;

                        case 2:
                            chunk.getBlock(x, y, z).setType(Material.COAL_ORE);
                            break;

                        case 3:
                            chunk.getBlock(x, y, z).setType(Material.LAPIS_ORE);
                            break;

                        case 4:
                            chunk.getBlock(x, y, z).setType(Material.DIAMOND_ORE);
                            break;

                        case 5:
                            chunk.getBlock(x, y, z).setType(Material.REDSTONE_ORE);
                            break;

                        case 6:
                            chunk.getBlock(x, y, z).setType(Material.EMERALD_ORE);
                            break;

                        case 7:
                            chunk.getBlock(x, y, z).setType(Material.CLAY);
                            break;

                        case 8:
                            chunk.getBlock(x, y, z).setType(Material.COAL_ORE);
                            break;

                        default:
                            chunk.getBlock(x, y, z).setType(Material.COAL_ORE);
                        }
                    }
                }
            }
        }
    }
}