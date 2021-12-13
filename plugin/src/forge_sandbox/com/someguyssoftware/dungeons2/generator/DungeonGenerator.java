/**
 * 
 */
package forge_sandbox.com.someguyssoftware.dungeons2.generator;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import forge_sandbox.com.someguyssoftware.dungeons2.Dungeons2;
import forge_sandbox.com.someguyssoftware.dungeons2.model.*;
import forge_sandbox.com.someguyssoftware.dungeons2.model.Room.Type;
import forge_sandbox.com.someguyssoftware.dungeons2.spawner.SpawnSheet;
import forge_sandbox.com.someguyssoftware.dungeons2.spawner.SpawnSheetLoader;
import forge_sandbox.com.someguyssoftware.dungeons2.style.*;
import forge_sandbox.com.someguyssoftware.dungeonsengine.chest.BossLootLoader;
import forge_sandbox.com.someguyssoftware.dungeonsengine.chest.LootLoader;
import forge_sandbox.com.someguyssoftware.dungeonsengine.config.ILevelConfig;
import otd.lib.async.AsyncWorldEditor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is responsible for building the dungeon in game.
 * 
 * @author Mark Gottschling on Jul 27, 2016
 *
 */
public class DungeonGenerator {
    // TODO either these shouldn't be static or they need to be loaded by static {}, not constructor or this class is singleton
    /*
     * default stylesheet that is within the classpath of the mod jar
     */
    private static StyleSheet defaultStyleSheet;
    
    /*
     * default spawnSheet that is within the classpath of the mod jar
     */
    private static SpawnSheet defaultSpawnSheet;
    
    // TODO should this be part of the RoomGeneratorFactory? - makes more sense
    private static Multimap<String, IRoomGenerator> roomGenerators = ArrayListMultimap.create();
    
    /**
     * TODO should throw custom exception
     * @throws Exception 
     * 
     */
    public DungeonGenerator() throws Exception {
        // load the default style sheet
        if (defaultStyleSheet == null) {
            setDefaultStyleSheet(StyleSheetLoader.load());
        }
        if (defaultSpawnSheet == null) {
            setDefaultSpawnSheet(SpawnSheetLoader.load());
        }        
    }

    /**
     * TODO return false if error or gen fails
     * @param world
     * @param random
     * @param dungeon
     * @param styleSheet
     * @param chestSheet
     * @param spawnSheet
     * @return
     * @throws FileNotFoundException
     */
    public boolean generate(AsyncWorldEditor world, Random random, 
            Dungeon dungeon, StyleSheet styleSheet, SpawnSheet spawnSheet) {

        // if styleSheet is null then use the default style sheet
        if (styleSheet == null) {
            Dungeons2.log.warn("Provided style sheet is null. Using default style sheet.");
            styleSheet = DungeonGenerator.getDefaultStyleSheet();
        }
        
        /*
         *  create a room generator factory
         */
        RoomGeneratorFactory factory = new RoomGeneratorFactory(roomGenerators);
        /*
         * 
         */
        IRoomGenerator roomGen;

        /*
         * a layout assigner. it determine the layout to use for each room and what design elements to enable
         */
        LayoutAssigner layoutAssigner = new LayoutAssigner(styleSheet);
        
        /*
         * create the room decorators
         */
        // TODO is many more decorators, they all should share the same set of loaders instead of creating new ones each.
        IRoomDecorator roomDecorator = new RoomDecorator(new LootLoader(), spawnSheet);
        IRoomDecorator bossRoomDecorator = new BossRoomDecorator(new BossLootLoader());
        IRoomDecorator libraryDecorator = new LibraryRoomDecorator(new LootLoader(), spawnSheet);
        
        /*
         *  NOTE careful here. IRoomGenerator can alter the state of the IGenerationStrategy with a
         *  IDungeonsBlockProvider of it's choosing. Don't share between generators or have to synchronize
         */        

        // build the entrance
        buildEntrance(world, random, dungeon, layoutAssigner, factory, roomDecorator, styleSheet);

        /*
         * build all the levels 
         */
        int levelCount = 0;
        int libraryCount = 0;
        // generate all the rooms
        for (Level level : dungeon.getLevels()) {
            Dungeons2.log.debug("Level -> {} ", new Object[] {
                            levelCount
                        });
            Theme levelTheme;
            if (level.getConfig().getTheme() != null && !level.getConfig().getTheme().equals("")) {
                // TODO select the theme - themes need to be mapped.
                levelTheme = styleSheet.getThemes().get(level.getConfig().getTheme());
            }
            else {
                levelTheme = dungeon.getTheme();
            }
            
            // build the rooms for the level
            for (Room room : level.getRooms()) {
                // assign a layout to the room
                layoutAssigner.assign(random, room);
                
                // get the room generator
                roomGen = factory.createRoomGenerator(random, room, level.getConfig().isSupport());
                
                // generate the room into the world
                roomGen.generate(world, random, room, levelTheme, styleSheet, level.getConfig());
    
                // TODO need a decorator factory
                if (room.getType() == Type.BOSS) {
                    Dungeons2.log.debug("Boss Room @ {}", new Object[] {
                        room.getCoords()
                    });
                    // TODO if going to integrate with Treasure2, need to know how big the dungeons is: size, # of levels
                    // in order to select the correct chest/loot table rarity
                    // TODO also should make it randomized ex 50% normal boss chest, 50% treasure2 chest
                    // extend the bossRoomDecorator -> TreasureBossRoomDecorator and set more properties
                    // TODO should return ICoords of boss chest so Dungeon can record
                    bossRoomDecorator.decorate(world, random, dungeon, roomGen.getGenerationStrategy().getBlockProvider(), room, level.getConfig());
                }
                
                /*
                 * TODO this should be a random selection of special rooms
                 * TODO there should be something to describe where a special room can occur (ex levels 4-6)
                 *  select any special decorators
                 */                    
                // ensure room fits the criteria to host a library    
                else if (room.getWidth() > 5
                        && room.getDepth() > 5
                        && room.getHeight() >=5
                        && !room.hasPillar()
                        && random.nextInt(100) < 10
                        && libraryCount < 3) {
                    Dungeons2.log.debug("Using library decorator for room @ " + room.getCoords().toShortString());
                        libraryDecorator.decorate(world, random, dungeon, roomGen.getGenerationStrategy().getBlockProvider(), room, level.getConfig());
                        libraryCount++;
                }
                else {
                    // decorate the room (spawners, chests, webbings, etc)
                    roomDecorator.decorate(world, random, dungeon, roomGen.getGenerationStrategy().getBlockProvider(), room, level.getConfig());
                }
            
                // TODO add to JSON output

            }
            // create a list of generated hallways
            List<Hallway> generatedHallways = new ArrayList<>();
            // generate the hallways
            for (Hallway hallway : level.getHallways()) {
                // assign a layout
                layoutAssigner.assign(random, hallway);
                // NOTE passing hallways here is a list of hallways (excluding the current one, to check if they intersect
                roomGen = factory.createHallwayGenerator(hallway, level.getRooms(), generatedHallways, level.getConfig().isSupport());
                roomGen.generate(world, random, hallway, levelTheme, styleSheet, level.getConfig());
                // add the hallway to the list of generated hallways
                generatedHallways.add(hallway);
            }
            
            // generate the shafts
            for (Shaft shaft : level.getShafts()) {
//                Dungeons2.log.debug("Building Shaft: " + shaft);
                // assign the layout
                shaft.setLayout(shaft.getParent().getLayout());
                roomGen = factory.createShaftGenerator(shaft, level.getConfig().isSupport());
                roomGen.generate(world, random, shaft, levelTheme, styleSheet, level.getConfig());
            }
            levelCount++;
        }
        return true;
    }

    /**
     * 
     * @param world
     * @param random
     * @param dungeon
     * @param layoutAssigner
     * @param factory
     * @param roomDecorator
     */
    private void buildEntrance(AsyncWorldEditor world, Random random,
            Dungeon dungeon, LayoutAssigner layoutAssigner, RoomGeneratorFactory factory,
            IRoomDecorator roomDecorator, StyleSheet styleSheet) {
        
        Room entranceRoom = dungeon.getEntrance();
        // create and setup a config for entrance
        Dungeons2.log.debug("Dungeon -> {}", new Object[] {
                    dungeon
                });
        Dungeons2.log.debug("Levels -> {}", new Object[] {
                    dungeon.getLevels()
                });
        Dungeons2.log.debug("Levels.size -> {}", new Object[] {
                    dungeon.getLevels().size()
                });
        Dungeons2.log.debug("Levels[0].config -> {}", new Object[] {
                    dungeon.getLevels().get(0).getConfig()
                });
        ILevelConfig entranceLevelConfig = dungeon.getLevels().get(0).getConfig().copy();
        entranceLevelConfig.setDecayMultiplier(Math.min(5, entranceLevelConfig.getDecayMultiplier())); // increase the decay multiplier to a minimum of 5
        // assign a layout to the entrance room
        layoutAssigner.assign(random, entranceRoom);
        IRoomGenerator roomGen = factory.createRoomGenerator(random, entranceRoom, dungeon.getLevels().get(0).getConfig().isSupport());
        // TODO need to provide the entrance room generator with a different level config that uses a higher decay multiplier
        // to create a much more decayed surface structure.
        roomGen.generate(world, random, entranceRoom, dungeon.getTheme(), styleSheet, entranceLevelConfig);
        roomDecorator.decorate(world, random, dungeon, roomGen.getGenerationStrategy().getBlockProvider(), entranceRoom, entranceLevelConfig);
    }

    /**
     * @return the defaultStyleSheet
     */
    public static StyleSheet getDefaultStyleSheet() {
        return defaultStyleSheet;
    }

    /**
     * @param defaultStyleSheet the defaultStyleSheet to set
     */
    private void setDefaultStyleSheet(StyleSheet defaultStyleSheet) {
        DungeonGenerator.defaultStyleSheet = defaultStyleSheet;
    }

    /**
     * @return the defaultSpawnSheet
     */
    public static SpawnSheet getDefaultSpawnSheet() {
        return defaultSpawnSheet;
    }

    /**
     * @param defaultSpawnSheet the defaultSpawnSheet to set
     */
    public static void setDefaultSpawnSheet(SpawnSheet defaultSpawnSheet) {
        DungeonGenerator.defaultSpawnSheet = defaultSpawnSheet;
    }
}
