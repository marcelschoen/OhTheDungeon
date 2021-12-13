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
package otd.dungeon.dungeonmaze.populator.maze;

public enum MazeStructureType {

	UNSTRUCTURE("Unstructure", 0),
	ABANDONED_DEFENCE_CASTLE_ROOM("Abandoned_Defence_Castle_Room", 2),
	ARMORY_ROOM("Armory_Room", 3),
	BLAZE_SPAWNER_ROOM("Blaze_Spawner_Room", 20),
	ENTRANCE("Entrance", 4),
	FLOODED_ROOM("Flooded_Room", 5),
	GRAVEL_WALL("Gravel_Wall", 6),
	GREAT_FURNACE_ROOM("Great_Furnace_room", 7),
	HIGH_ROOM("High_Room", 8),
	LIBRARY_ROOM("Library_Room", 9),
	MASSIVE_ROOM("Massive_Room", 10),
	OASIS_CHUNK("Oasis_Chunk", 11),
	RAIL("Rail", 12),
	RUIN("Ruin", 13),
	SANCTUARY_ROOM("Sanctuary_Room", 14),
	SAND_WALL("Sand_Wall", 15),
	SPAWN_ROOM("Spawn_Room", 16),
	STAIRS("Stairs", 17),
	STRUT("Strut", 18),
	WATER_WELL_ROOM("Water_Well_Room", 19),
	CUSTOM_STRUCTURE("Custom_Structure", 1);

	private String name;
	private int id;
	
	/**
	 * Constructor
	 * @param name Structure type name
	 * @param id Structure type ID
	 */
	MazeStructureType(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	/**
	 * Get the structure type name
	 * @return Structure type name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Get the structure type ID
	 * @return Structure type ID
	 */
	public int getId() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
