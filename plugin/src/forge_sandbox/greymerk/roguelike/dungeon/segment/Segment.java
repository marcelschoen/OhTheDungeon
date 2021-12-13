package forge_sandbox.greymerk.roguelike.dungeon.segment;

import forge_sandbox.greymerk.roguelike.dungeon.segment.part.*;

import java.util.Random;

public enum Segment {

	ARCH, FIREARCH, FIREPLACE, SHELF, INSET, MOSSYARCH, MUSHROOM, NETHERARCH,
	NETHERSTRIPE, NETHERWART, NETHERLAVA, JUNGLE, BOOKS, SPAWNER, 
	WHEAT, TOMB, CHEST, SILVERFISH, SKULL, FLOWERS, DOOR, ANKH, CAVE,
	SEWER, SEWERARCH, SEWERDOOR, SEWERDRAIN, MINESHAFT, LAMP, ARROW, SQUAREARCH,
	CELL, WALL, PLANT;
	
	public static Segment[] segments = {
			FIREPLACE, SHELF, INSET, MOSSYARCH, MUSHROOM,
			NETHERSTRIPE, NETHERWART, NETHERLAVA, JUNGLE, BOOKS, SPAWNER, 
			WHEAT, TOMB, CHEST, SILVERFISH, SKULL, FLOWERS, DOOR, ANKH, CAVE,
			SEWER, SEWERARCH, SEWERDOOR, SEWERDRAIN, MINESHAFT, LAMP, ARROW, SQUAREARCH,
			CELL, WALL, PLANT
		};
	
	public static ISegment getSegment(Segment choice){
		
		switch(choice){
		case ARCH: return new SegmentArch();
		case FIREARCH: return new SegmentFireArch();
		case FIREPLACE: return new SegmentFirePlace();
		case SHELF: return new SegmentShelf();
		case INSET: return new SegmentInset();
		case MOSSYARCH: return new SegmentMossyArch();
		case MUSHROOM: return new SegmentMushrooms();
		case NETHERARCH: return new SegmentNetherArch();
		case NETHERSTRIPE: return new SegmentNetherStripes();
		case NETHERWART: return new SegmentNetherWart();
		case NETHERLAVA: return new SegmentNetherLava();
		case JUNGLE: return new SegmentJungle();
		case BOOKS: return new SegmentBooks();
		case SPAWNER: return new SegmentSpawner();
		case WHEAT: return new SegmentWheat();
		case TOMB: return new SegmentTomb();
		case CHEST: return new SegmentChest();
		case SILVERFISH: return new SegmentSilverfish();
		case SKULL: return new SegmentSkull();
		case FLOWERS: return new SegmentFlowers();
		case DOOR: return new SegmentDoor();
		case ANKH: return new SegmentAnkh();
		case CAVE: return new SegmentCave();
		case SEWER: return new SegmentSewer();
		case SEWERARCH: return new SegmentSewerArch();
		case SEWERDOOR: return new SegmentSewerDoor();
		case SEWERDRAIN: return new SegmentSewerDrain();
		case MINESHAFT: return new SegmentMineShaft();
		case LAMP: return new SegmentLamp();
		case ARROW: return new SegmentTrap();
		case SQUAREARCH: return new SegmentSquareArch();
		case CELL: return new SegmentPrisonCell();
		case WALL: return new SegmentWall();
		case PLANT: return new SegmentPlant();
		}
		
		return null;
		
	}

	public static Segment getRandom(Random rand) {
		return segments[rand.nextInt(segments.length)];
	}
}
