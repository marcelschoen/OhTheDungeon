package forge_sandbox.greymerk.roguelike.dungeon.base;


import forge_sandbox.greymerk.roguelike.dungeon.rooms.*;

import java.util.Random;


public enum DungeonRoom {
	
	BRICK, CREEPER, CRYPT, ENCHANT, ENDER, FIRE, MUSIC, NETHER, NETHERFORT, PIT, PRISON,
	SLIME, SMITH, SPIDER, CAKE, LAB, CORNER, MESS, ETHO, ENIKO, BTEAM, OSSUARY, OBSIDIAN,
	AVIDYA, STORAGE, ASHLEA, FIREWORK, BEDROOM, REWARD, LIBRARY, PYRAMIDTOMB, DARKHALL,
	TREETHO, LINKER, LINKERTOP, PYRAMIDSPAWNER, PYRAMIDCORNER, BLAZE;
	
	public static DungeonRoom[] intersectionRooms = {
			BRICK, CREEPER, CRYPT, ENDER, FIRE, MUSIC, NETHER, NETHERFORT, PIT, PRISON,
			SLIME, SPIDER, CAKE, LAB, MESS, OSSUARY, OBSIDIAN, STORAGE, PYRAMIDTOMB,
			DARKHALL, PYRAMIDSPAWNER, PYRAMIDCORNER, BLAZE
		};
	
	public static DungeonRoom[] secrets = {
			ENCHANT, SMITH, CAKE, BEDROOM
		};
	
	public static IDungeonRoom getInstance(DungeonRoom choice){
		switch(choice){
		case BRICK: return new DungeonsBrick();
		case CREEPER: return new DungeonsCreeperDen();
		case CRYPT: return new DungeonsCrypt();
		case ENCHANT: return new DungeonsEnchant();
		case ENDER: return new DungeonsEnder();
		case FIRE: return new DungeonsFire();
		case MUSIC: return new DungeonsMusic();
		case NETHER: return new DungeonsNetherBrick();
		case NETHERFORT: return new DungeonsNetherBrickFortress();
		case PIT: return new DungeonsPit();
		case PRISON: return new DungeonsPrison();
		case SLIME: return new DungeonsSlime();
		case SMITH: return new DungeonsSmithy();
		case SPIDER: return new DungeonsSpiderNest();
		case CAKE: return new DungeonsWood();
		case LAB: return new DungeonLab();
		case CORNER: return new DungeonCorner();
		case MESS: return new DungeonMess();
		case ETHO: return new DungeonEtho();
		case ENIKO: return new DungeonEniko();
		case BTEAM: return new DungeonBTeam();
		case OSSUARY: return new DungeonOssuary();
		case OBSIDIAN: return new DungeonObsidian();
		case AVIDYA: return new DungeonAvidya();
		case STORAGE: return new DungeonStorage();
		case ASHLEA: return new DungeonAshlea();
		case FIREWORK: return new DungeonFirework();
		case BEDROOM: return new DungeonBedRoom();
		case REWARD: return new DungeonReward();
		case LIBRARY: return new DungeonLibrary();
		case PYRAMIDTOMB: return new DungeonPyramidTomb();
		case DARKHALL: return new DungeonDarkHall();
		case TREETHO: return new DungeonTreetho();
		case LINKER: return new DungeonLinker();
		case LINKERTOP: return new DungeonLinkerTop();
		case PYRAMIDSPAWNER: return new DungeonPyramidSpawner();
		case PYRAMIDCORNER: return new DungeonPyramidCorner();
		case BLAZE: return new DungeonBlaze();
		default: return null;
		}
	}
	
	public static boolean contains(String name){
		for(DungeonRoom value : values()){
			if(value.toString().equals(name)) return true;
		}
		return false;
	}

	public static DungeonRoom getRandomRoom(Random rand) {
		return intersectionRooms[rand.nextInt(intersectionRooms.length)];
	}

	public static DungeonRoom getRandomSecret(Random rand) {
		return secrets[rand.nextInt(secrets.length)];
	}
}
