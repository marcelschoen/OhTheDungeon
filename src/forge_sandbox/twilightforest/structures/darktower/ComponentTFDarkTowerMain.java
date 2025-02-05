package forge_sandbox.twilightforest.structures.darktower;

import forge_sandbox.StructureBoundingBox;
import forge_sandbox.twilightforest.TFFeature;
import forge_sandbox.twilightforest.structures.StructureTFComponentOld;
import forge_sandbox.twilightforest.structures.StructureTFDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Orientable;
import otd.util.RotationMirror.Rotation;
import otd.lib.async.AsyncWorldEditor;
import forge_sandbox.twilightforest.TFTreasure;
import forge_sandbox.twilightforest.structures.RotationUtil;
import forge_sandbox.twilightforest.structures.StructureTFComponent;
import forge_sandbox.twilightforest.structures.TFMaze;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.block.data.type.Furnace;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.EntityType;

public class ComponentTFDarkTowerMain extends ComponentTFDarkTowerWing
{
    private boolean placedKeys = false;

    public ComponentTFDarkTowerMain()
    {
    }


    public ComponentTFDarkTowerMain(TFFeature feature, AsyncWorldEditor world, Random rand, int index, int x, int y, int z)
    {
        this(feature, world, rand, index, x + 10, y, z + 10, BlockFace.NORTH);
    }


    public ComponentTFDarkTowerMain(TFFeature feature, AsyncWorldEditor world, Random rand, int index, int x, int y, int z, BlockFace rotation)
    {
        super(feature, index, x, y, z, 19, 56 + ((rand.nextInt(32) / 5) * 5), rotation);

        // check to make sure we can build the whole tower
        if (this.boundingBox.maxY > 245)
        {
            int amtToLower = (((this.boundingBox.maxY - 245) / 5) * 5) + 5;

//            TwilightForestMod.LOGGER.info("Lowering Dark Tower max height by {} to be within world bounds", amtToLower);

            this.height -= amtToLower;
            this.boundingBox.maxY -= amtToLower;
        }

        // decorator
        if (this.deco == null)
        {
            this.deco = new StructureDecoratorDarkTower();
        }
    }

    @Override
    public void buildComponent(StructureTFComponent parent, List<StructureTFComponent> list, Random rand)
    {
        if (parent != null && parent instanceof StructureTFComponentOld)
        {
            this.deco = ((StructureTFComponentOld)parent).deco;
        }

        // if this is not the first main part, add one
        if (this.getComponentType() > 0)
        {
            addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);
        }

        // one direction gets a new main tower, unless we're at tower #3
        Rotation mainDir = null;

        if (this.getComponentType() < 2)
        {
            mainDir = RotationUtil.ROTATIONS[rand.nextInt(RotationUtil.ROTATIONS.length)];

            // make sub towers
            for (Rotation rotation : RotationUtil.ROTATIONS)
            {
                if (rotation == mainDir)
                {
                    continue;
                }

                int[] dest = getValidOpening(rand, rotation);

                int childHeight = validateChildHeight(21 + rand.nextInt(10), 11);

                makeTowerWing(list, rand, this.getComponentType(), dest[0], dest[1], dest[2], 11, childHeight, rotation);
            }
        }
        else
        {
            // make boss trap towers
            for (Rotation i : RotationUtil.ROTATIONS)
            {
                int[] dest = getValidOpening(rand, i);
                makeBossTrapWing(list, rand, this.getComponentType(), dest[0], dest[1], dest[2], i);
            }
        }

        if (this.getComponentType() > 0)
        {
            // sub towers at base of tower
            for (Rotation i : RotationUtil.ROTATIONS)
            {
                if (i == Rotation.CLOCKWISE_180)
                {
                    continue;
                }

                int[] dest = getValidOpening(rand, i);

                // move opening to tower base
                dest[1] = 1;

                int childHeight = validateChildHeight(21 + rand.nextInt(10), 11);

                makeTowerWing(list, rand, this.getComponentType(), dest[0], dest[1], dest[2], 11, childHeight, i);
            }

            // add a beard
            makeABeard(parent, list, rand);
        }
        else
        {
            // 2 entrance towers towers at base of tower
            for (Rotation rotation : new Rotation[] {Rotation.NONE, Rotation.CLOCKWISE_180})
            {

                int[] dest = getValidOpening(rand, rotation);

                // move opening to tower base
                dest[1] = 1;

                int childHeight = validateChildHeight(10 + rand.nextInt(5), 9);

                makeEntranceTower(list, rand, 5, dest[0], dest[1], dest[2], 9, childHeight, rotation);
            }
        }

        // actually make main tower
        if (mainDir != null)
        {
            int[] dest = getValidOpening(rand, mainDir);
            makeNewLargeTower(list, rand, this.getComponentType() + 1, dest[0], dest[1], dest[2], mainDir);
        }

        // add a roof?
        makeARoof(parent, list, rand);

        // flag certain towers for keys
        if (!this.placedKeys && this.getComponentType() < 2)
        {
            // count how many size 9 towers we have hanging off us
            ArrayList<ComponentTFDarkTowerWing> possibleKeyTowers = new ArrayList<ComponentTFDarkTowerWing>();


            for (Object piece : list)
            {
                if (piece instanceof ComponentTFDarkTowerWing)
                {
                    ComponentTFDarkTowerWing wing = (ComponentTFDarkTowerWing)piece;

                    if (wing.size == 9 && wing.getComponentType() == this.getComponentType())
                        possibleKeyTowers.add(wing);
                }
            }

            for (int i = 0; i < 4; i++)
            {
                if (possibleKeyTowers.size() < 1)
                {
//                    TwilightForestMod.LOGGER.warn("Dark forest tower could not find four small towers to place keys in.");
                    break;
                }

                int towerNum = rand.nextInt(possibleKeyTowers.size());

                possibleKeyTowers.get(towerNum).setKeyTower(true);
                possibleKeyTowers.remove(towerNum);
            }

            this.placedKeys = true;
        }

    }

    /**
     * Make a bridge that leads to an entrance tower
     */
    private boolean makeEntranceTower(List<StructureTFComponent> list, Random rand, int index, int x, int y, int z, int childSize, int childHeight, Rotation rotation)
    {
        BlockFace direction = getStructureRelativeRotation(rotation);
        int[] dx = offsetTowerCoords(x, y, z, 5, direction);

        ComponentTFDarkTowerBridge bridge = new ComponentTFDarkTowerEntranceBridge(getFeatureType(), index, dx[0], dx[1], dx[2], childSize, childHeight, direction);
        // if I'm doing this right, the main towers can't intersect
        list.add(bridge);
        bridge.buildComponent(this, list, rand);
        addOpening(x, y, z, rotation);
        return true;
    }

    /**
     * Make a bridge that leads to a new large-size tower
     */
    private boolean makeNewLargeTower(List<StructureTFComponent> list, Random rand, int index, int x, int y, int z, Rotation rotation)
    {

        int wingSize = 15;
        int wingHeight = 56;

        BlockFace direction = getStructureRelativeRotation(rotation);
        int[] dx = offsetTowerCoords(x, y, z, 5, direction);

        ComponentTFDarkTowerMainBridge bridge = new ComponentTFDarkTowerMainBridge(getFeatureType(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
        // if I'm doing this right, the main towers can't intersect
        list.add(bridge);
        bridge.buildComponent(this, list, rand);
        // lock the door!
        addOpening(x, y, z, rotation, EnumDarkTowerDoor.LOCKED);
        return true;
    }

    /**
     * Make a bridge that leads to a boss trap tower
     */
    private boolean makeBossTrapWing(List<StructureTFComponent> list, Random rand, int index, int x, int y, int z, Rotation rotation)
    {

        int wingSize = 11;
        int wingHeight = 9;

        BlockFace direction = getStructureRelativeRotation(rotation);
        int[] dx = offsetTowerCoords(x, y, z, 5, direction);

        ComponentTFDarkTowerBossBridge bridge = new ComponentTFDarkTowerBossBridge(getFeatureType(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
        // if I'm doing this right, the main towers can't intersect
        list.add(bridge);
        bridge.buildComponent(this, list, rand);
        // lock the door!
        addOpening(x, y, z, rotation);
        return true;
    }

    /**
     * Attach a roof to this tower.
     */
    @Override
    public void makeARoof(StructureTFComponent parent, List<StructureTFComponent> list, Random rand)
    {
        if (this.getComponentType() < 2)
        {
            super.makeARoof(parent, list, rand);
        }
    }

    @Override
    public boolean addComponentParts(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb)
    {
        Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

        // make walls
        makeEncasedWalls(world, rand, sbb, 0, 0, 0, size - 1, height - 1, size - 1);

        // clear inside
        fillWithAir(world, sbb, 1, 1, 1, size - 2, height - 2, size - 2);

        if (this.getComponentType() == 0)
        {
            // deco to ground
            for (int x = 0; x < this.size; x++)
            {
                for (int z = 0; z < this.size; z++)
                {
                    this.setBlockState(world, deco.accentState, x, -1, z, sbb);
                }
            }
        }

        // nullify sky light
        this.nullifySkyLightForBoundingBox(world);

        // how many total floors do we have?
        int totalFloors = this.height / 5;

        // is the tower a beam maze?  false = builder platforms
        boolean beamMaze = decoRNG.nextBoolean();

        // how many floors should the center occupy?
        int centerFloors = beamMaze ? 4 : totalFloors / 2;

        // how many floors do we get on the top/bottom portion?
        int bottomFloors = (totalFloors - centerFloors) / 2;

        // readjust center floors if we have leftover floors
        centerFloors = totalFloors - (bottomFloors * 2);

        // where do the top floors start?
        int topFloorsStartY = height - (bottomFloors * 5 + 1);

        // add bottom and top floors
        addThreeQuarterFloors(world, decoRNG, sbb, 0, bottomFloors * 5);
        if (this.getComponentType() < 2)
        {
            addThreeQuarterFloors(world, decoRNG, sbb, topFloorsStartY, height - 1);
        }
        else
        {
            addThreeQuarterFloorsDecorateBoss(world, decoRNG, sbb, topFloorsStartY, height - 1);
            // boss destruction

            destroyTower(world, decoRNG, 12, height + 4, 3, 4, sbb);
            destroyTower(world, decoRNG, 3, height + 4, 12, 4, sbb);
            destroyTower(world, decoRNG, 3, height + 4, 3, 4, sbb);
            destroyTower(world, decoRNG, 12, height + 4, 12, 4, sbb);
            destroyTower(world, decoRNG, 8, height + 4, 8, 5, sbb);

            // make spawner where it will hopefully last
            decorateBossSpawner(world, decoRNG, sbb, Rotation.NONE, height - 6);

        }

        if (beamMaze)
        {
            addTimberMaze(world, decoRNG, sbb, (bottomFloors * 5), topFloorsStartY);
        }
        else
        {
            addBuilderPlatforms(world, decoRNG, sbb, (bottomFloors * 5), topFloorsStartY);
        }

        // openings
        makeOpenings(world, sbb);


        return true;
    }


    /**
     * Add a bunch of 3/4 floors
     */
    protected void addThreeQuarterFloors(AsyncWorldEditor world, Random decoRNG, StructureBoundingBox sbb, int bottom, int top)
    {

        int spacing = 5;
        Rotation rotation = RotationUtil.ROTATIONS[(this.boundingBox.minY + bottom) % 4];
        if (bottom == 0)
        {
            makeLargeStairsUp(world, sbb, rotation, 0);
            rotation = rotation.add(Rotation.COUNTERCLOCKWISE_90);
            makeBottomEntrance(world, decoRNG, sbb, rotation, bottom);
            bottom += spacing;
        }

        // fill with 3/4 floors
        for (int y = bottom; y < top; y += spacing)
        {
            boolean isBottomFloor = (y == bottom && bottom != spacing);
            boolean isTopFloor = (y >= top - spacing);
            boolean isTowerTopFloor = (y >= height - spacing - 2);

            makeThreeQuarterFloor(world, sbb, rotation, y, isBottomFloor, isTowerTopFloor);

            if (!isTopFloor)
            {
                makeLargeStairsUp(world, sbb, rotation, y);
            }

            if (!isTopFloor || isTowerTopFloor)
            {
                decorateFloor(world, decoRNG, sbb, rotation, y, isBottomFloor, isTopFloor);
            }

            rotation = rotation.add(Rotation.COUNTERCLOCKWISE_90);
        }
    }

    /**
     * Add a bunch of 3/4 floors
     */
    protected void addThreeQuarterFloorsDecorateBoss(AsyncWorldEditor world, Random decoRNG, StructureBoundingBox sbb, int bottom, int top)
    {

        int spacing = 5;
        Rotation rotation = RotationUtil.ROTATIONS[(this.boundingBox.minY + bottom) % 4];
        if (bottom == 0)
        {
            makeLargeStairsUp(world, sbb, rotation, 0);
            rotation = rotation.add(Rotation.COUNTERCLOCKWISE_90);
            bottom += spacing;
        }

        // fill with 3/4 floors
        for (int y = bottom; y < top; y += spacing)
        {
            boolean isBottomFloor = (y == bottom && bottom != spacing);
            boolean isTopFloor = (y >= top - spacing);
            boolean isTowerTopFloor = (y >= height - spacing - 2);

            makeThreeQuarterFloor(world, sbb, rotation, y, isBottomFloor, isTowerTopFloor);

            if (!isTopFloor)
            {
                makeLargeStairsUp(world, sbb, rotation, y);
                decorateExperiment(world, decoRNG, sbb, rotation, y);

            }

            rotation = rotation.add(Rotation.COUNTERCLOCKWISE_90);
        }
    }


    private void decorateFloor(AsyncWorldEditor world, Random decoRNG, StructureBoundingBox sbb, Rotation rotation, int y, boolean isBottom, boolean isTop)
    {
        // pick an appropriate decoration and use it

        if (isTop)
        {
            // there are a limited amount that can go at the top
            switch (decoRNG.nextInt(3))
            {
                default:
                case 0:
                    decorateAquarium(world, decoRNG, sbb, rotation, y);
                    break;
                case 1:
                    decorateBotanical(world, decoRNG, sbb, rotation, y);
                    break;
                case 2:
                    decorateNetherwart(world, decoRNG, sbb, rotation, y, isTop);
                    break;
            }
        }
        else if (isBottom)
        {
            // similarly some don't work on the bottom
            switch (decoRNG.nextInt(4))
            {
                default:
                case 0:
                    decorateAquarium(world, decoRNG, sbb, rotation, y);
                    break;
                case 1:
                    decorateBotanical(world, decoRNG, sbb, rotation, y);
                    break;
                case 2:
                    if (y + this.boundingBox.minY > 64)
                    {
                        decorateNetherwart(world, decoRNG, sbb, rotation, y, isTop);
                        break;
                    }
                case 3:
                    decorateForge(world, decoRNG, sbb, rotation, y);
                    break;
            }
        }
        else
        {
            // but in the middle, anything goes
            switch (decoRNG.nextInt(8))
            {
                default:
                case 0:
                case 1:
                    decorateReappearingMaze(world, decoRNG, sbb, rotation, y);
                    break;
                case 2:
                    decorateUnbuilderMaze(world, decoRNG, sbb, rotation, y);
                    break;
                case 3:
                    decorateAquarium(world, decoRNG, sbb, rotation, y);
                    break;
                case 4:
                    decorateBotanical(world, decoRNG, sbb, rotation, y);
                    break;
                case 5:
                    if (y + this.boundingBox.minY > 64)
                    {
                        decorateNetherwart(world, decoRNG, sbb, rotation, y, isTop);
                        break;
                    }
                case 6:
                    decorateLounge(world, decoRNG, sbb, rotation, y);
                    break;
                case 7:
                    decorateForge(world, decoRNG, sbb, rotation, y);
                    break;
            }
        }
    }


    /**
     * Make a single three quarter floor
     *
     * @param isTowerTopFloor
     */
    protected void makeThreeQuarterFloor(AsyncWorldEditor world, StructureBoundingBox sbb, Rotation rotation, int y, boolean isBottom, boolean isTowerTopFloor)
    {
        int half = size / 2;
        // fill the floor
        this.fillBlocksRotated(world, sbb, half + 1, y, 1, size - 2, y, half + 1, deco.blockState, rotation);
        this.fillBlocksRotated(world, sbb, 1, y, half + 1, size - 2, y, size - 2, deco.blockState, rotation);

        // don't make part of the fence if we have stairs coming up
        int startZ = isBottom ? 1 : 3;

        this.fillBlocksRotated(world, sbb, 1, y, half, half, y, half, deco.accentState, rotation);
        this.fillBlocksRotated(world, sbb, half, y, startZ, half, y, half, deco.accentState, rotation);
        this.fillBlocksRotated(world, sbb, 1, y + 1, half, half, y + 1, half, deco.fenceState, rotation);
        this.fillBlocksRotated(world, sbb, half, y + 1, startZ, half, y + 1, half, deco.fenceState, rotation);

        // little notch if we're at the tower top
        if (isTowerTopFloor)
        {
            this.fillBlocksRotated(world, sbb, 1, y + 0, half - 2, 3, y + 0, half, deco.accentState, rotation);
            this.fillBlocksRotated(world, sbb, 1, y + 1, half - 2, 3, y + 1, half, deco.fenceState, rotation);
            this.fillBlocksRotated(world, sbb, 1, y + 0, half - 1, 2, y + 0, half, deco.fenceState, rotation);
            this.fillBlocksRotated(world, sbb, 1, y + 1, half - 1, 2, y + 1, half, AIR, rotation);
        }

    }

//    protected void makeLargeStairsDown(AsyncWorldEditor world, StructureBoundingBox sbb, int rotation, int y) {
//        // stairs!
//        this.fillAirRotated(world, sbb, size / 2, y, 1, size / 2, y + 1, 2, rotation);
//        
//        for (int i = 0; i < 5; i++)
//        {
//            setBlockStateRotated(world, deco.stairID, getStairMeta(0 + rotation), size / 2 - i, y - i, 1, rotation, sbb);
//            setBlockStateRotated(world, deco.stairID, getStairMeta(0 + rotation), size / 2 - i, y - i, 2, rotation, sbb);
//            setBlockStateRotated(world, deco.blockID, deco.blockMeta, size / 2 - i + 1, y - i, 1, rotation, sbb);
//            setBlockStateRotated(world, deco.blockID, deco.blockMeta, size / 2 - i + 1, y - i, 2, rotation, sbb);
//            setBlockStateRotated(world, deco.blockID, deco.blockMeta, size / 2 - i + 1, y - i, 3, rotation, sbb);
//            
//            if (i > 0 && i < 4)
//            {
//                setBlockStateRotated(world, deco.accentID, deco.accentMeta, size / 2 - i, y - i, 3, rotation, sbb);
//                setBlockStateRotated(world, deco.fenceID, deco.fenceMeta, size / 2 - i, y - i + 1, 3, rotation, sbb);
//                setBlockStateRotated(world, deco.fenceID, deco.fenceMeta, size / 2 - i, y - i + 2, 3, rotation, sbb);
//            }
//            else if (i == 4)
//            {
//                setBlockStateRotated(world, deco.stairID, getStairMeta(3 + rotation), size / 2 - i, y - i, 3, rotation, sbb);
//            }
//        }
//    }

    protected void makeLargeStairsUp(AsyncWorldEditor world, StructureBoundingBox sbb, Rotation rotation, int y)
    {
        // stairs!
        //this.fillAirRotated(world, sbb, x, y, 1, x, y + 1, 2, rotation);

        for (int i = 0; i < 5; i++)
        {
            int z = size / 2 - i + 4;
            int sy = y + i + 1;

            setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.SOUTH, rotation, false), 1, sy, z, rotation, sbb);
            setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.SOUTH, rotation, false), 2, sy, z, rotation, sbb);
            setBlockStateRotated(world, deco.blockState, 1, sy, z - 1, rotation, sbb);
            setBlockStateRotated(world, deco.blockState, 2, sy, z - 1, rotation, sbb);
            setBlockStateRotated(world, deco.blockState, 3, sy, z - 1, rotation, sbb);

            if (i > 0 && i < 4)
            {
                setBlockStateRotated(world, deco.accentState, 3, sy, z, rotation, sbb);
                setBlockStateRotated(world, deco.fenceState, 3, sy + 1, z, rotation, sbb);
                setBlockStateRotated(world, deco.fenceState, 3, sy + 2, z, rotation, sbb);
            }
            else if (i == 0)
            {
                setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.EAST, rotation, false), 3, sy, z, rotation, sbb);
            }
        }
    }

    private void decorateReappearingMaze(AsyncWorldEditor world, Random decoRNG, StructureBoundingBox sbb, Rotation rotation, int y)
    {
        // make maze object
        int mazeSize = 6;
        TFMaze maze = new TFMaze(mazeSize, mazeSize);

        // set the seed to a fixed value based on this maze's x and z
        maze.setSeed(world.getSeed() + this.boundingBox.minX * 90342903 + y * 90342903 ^ this.boundingBox.minZ);

        // tell it not to make outside walls by making them "ROOMS"
        for (int i = 0; i < 13; i++)
        {
            maze.putRaw(i, 0, TFMaze.ROOM);
            maze.putRaw(i, 12, TFMaze.ROOM);
            maze.putRaw(0, i, TFMaze.ROOM);
            maze.putRaw(12, i, TFMaze.ROOM);
        }

        // we need to set this before generation
        maze.doorRarity = 0.3F;

        // set some areas out of bounds and make the maze depending on where we start
        switch (rotation)
        {
            case NONE:
                for (int x = 1; x < 6; x++)
                {
                    for (int z = 1; z < 6; z++)
                    {
                        maze.putRaw(x, z, TFMaze.ROOM);
                    }
                }
                maze.putRaw(1, 6, TFMaze.ROOM);
                maze.putRaw(1, 7, TFMaze.ROOM);
                maze.putRaw(1, 8, TFMaze.ROOM);
                maze.putRaw(1, 9, TFMaze.ROOM);
                maze.putRaw(1, 10, TFMaze.DOOR);
                maze.putRaw(6, 1, TFMaze.ROOM);
                maze.putRaw(7, 1, TFMaze.ROOM);
                maze.putRaw(8, 1, TFMaze.DOOR);
                maze.generateRecursiveBacktracker(0, 5);
                break;
            case CLOCKWISE_90:
                for (int x = 7; x < 12; x++)
                {
                    for (int z = 1; z < 6; z++)
                    {
                        maze.putRaw(x, z, TFMaze.ROOM);
                    }
                }
                maze.putRaw(6, 1, TFMaze.ROOM);
                maze.putRaw(5, 1, TFMaze.ROOM);
                maze.putRaw(4, 1, TFMaze.ROOM);
                maze.putRaw(3, 1, TFMaze.ROOM);
                maze.putRaw(2, 1, TFMaze.DOOR);
                maze.putRaw(11, 6, TFMaze.ROOM);
                maze.putRaw(11, 7, TFMaze.ROOM);
                maze.putRaw(11, 8, TFMaze.DOOR);
                maze.generateRecursiveBacktracker(0, 0);
                break;
            case CLOCKWISE_180:
                for (int x = 7; x < 12; x++)
                {
                    for (int z = 7; z < 12; z++)
                    {
                        maze.putRaw(x, z, TFMaze.ROOM);
                    }
                }
                maze.putRaw(11, 6, TFMaze.ROOM);
                maze.putRaw(11, 5, TFMaze.ROOM);
                maze.putRaw(11, 4, TFMaze.ROOM);
                maze.putRaw(11, 3, TFMaze.ROOM);
                maze.putRaw(11, 2, TFMaze.DOOR);
                maze.putRaw(6, 11, TFMaze.ROOM);
                maze.putRaw(5, 11, TFMaze.ROOM);
                maze.putRaw(4, 11, TFMaze.DOOR);
                maze.generateRecursiveBacktracker(5, 0);
                break;
            case COUNTERCLOCKWISE_90:
                for (int x = 1; x < 6; x++)
                {
                    for (int z = 7; z < 12; z++)
                    {
                        maze.putRaw(x, z, TFMaze.ROOM);
                    }
                }
                maze.putRaw(6, 11, TFMaze.ROOM);
                maze.putRaw(7, 11, TFMaze.ROOM);
                maze.putRaw(8, 11, TFMaze.ROOM);
                maze.putRaw(9, 11, TFMaze.ROOM);
                maze.putRaw(10, 11, TFMaze.DOOR);
                maze.putRaw(1, 6, TFMaze.ROOM);
                maze.putRaw(1, 5, TFMaze.ROOM);
                maze.putRaw(1, 4, TFMaze.DOOR);
                maze.generateRecursiveBacktracker(5, 5);
                break;
        }


        // copy the maze to us!
        maze.wallBlockState = deco.blockState;
        maze.headBlockState = deco.accentState;
        maze.pillarBlockState = deco.accentState;
//        maze.doorBlockState = TFBlocks.tower_device.getDefaultState().withProperty(BlockTFTowerDevice.VARIANT, TowerDeviceVariant.REAPPEARING_INACTIVE);
        maze.doorBlockState = Bukkit.createBlockData(Material.STONE);

        maze.torchRarity = 0;
        maze.tall = 3;
        maze.head = 1;
        maze.oddBias = 2;

        maze.copyToStructure(world, 0, y + 1, 0, this, sbb);

        decorateMazeDeadEnds(world, decoRNG, maze, y, rotation, sbb);

    }

    /**
     * Find dead ends and put something there
     */
    protected void decorateMazeDeadEnds(AsyncWorldEditor world, Random decoRNG, TFMaze maze, int y, Rotation rotation, StructureBoundingBox sbb)
    {
        for (int x = 0; x < maze.width; x++)
        {
            for (int z = 0; z < maze.depth; z++)
            {
                // dead ends
                if (!maze.isWall(x, z, x - 1, z) && maze.isWall(x, z, x + 1, z) && maze.isWall(x, z, x, z - 1) && maze.isWall(x, z, x, z + 1))
                {
                    decorateDeadEnd(world, decoRNG, maze, x, y, z, 3, rotation, sbb);
                }
                if (maze.isWall(x, z, x - 1, z) && !maze.isWall(x, z, x + 1, z) && maze.isWall(x, z, x, z - 1) && maze.isWall(x, z, x, z + 1))
                {
                    decorateDeadEnd(world, decoRNG, maze, x, y, z, 1, rotation, sbb);
                }
                if (maze.isWall(x, z, x - 1, z) && maze.isWall(x, z, x + 1, z) && !maze.isWall(x, z, x, z - 1) && maze.isWall(x, z, x, z + 1))
                {
                    decorateDeadEnd(world, decoRNG, maze, x, y, z, 0, rotation, sbb);
                }
                if (maze.isWall(x, z, x - 1, z) && maze.isWall(x, z, x + 1, z) && maze.isWall(x, z, x, z - 1) && !maze.isWall(x, z, x, z + 1))
                {
                    decorateDeadEnd(world, decoRNG, maze, x, y, z, 2, rotation, sbb);
                }
            }
        }
    }

    /**
     * Decorate a specific maze dead end
     */
    private void decorateDeadEnd(AsyncWorldEditor world, Random decoRNG, TFMaze maze, int mx, int y, int mz, int facing, Rotation rotation, StructureBoundingBox sbb)
    {
        int x = mx * 3 + 1;
        int z = mz * 3 + 1;

        switch (facing)
        {
            case 0:
                setBlockState(world, deco.accentState, x + 0, y + 1, z + 1, sbb);
                setBlockState(world, deco.accentState, x + 1, y + 1, z + 1, sbb);
                setBlockState(world, Material.CHEST, x + 0, y + 2, z + 1, sbb);
                this.placeTreasureAtCurrentPosition(world, decoRNG, x + 1, y + 2, z + 1, TFTreasure.darktower_cache, sbb);
                break;
            case 1:
                setBlockState(world, deco.accentState, x + 0, y + 1, z + 0, sbb);
                setBlockState(world, deco.accentState, x + 0, y + 1, z + 1, sbb);
                
                Chest chest1 = (Chest) Bukkit.createBlockData(Material.CHEST);
                chest1.setFacing(BlockFace.SOUTH);
                
                setBlockState(world, chest1, x + 0, y + 2, z + 0, sbb);
                this.placeTreasureAtCurrentPosition(world, decoRNG, x + 0, y + 2, z + 1, TFTreasure.darktower_cache, sbb);
                break;
            case 2:
                setBlockState(world, deco.accentState, x + 0, y + 1, z + 0, sbb);
                setBlockState(world, deco.accentState, x + 1, y + 1, z + 0, sbb);
                
                Chest chest2 = (Chest) Bukkit.createBlockData(Material.CHEST);
                chest2.setFacing(BlockFace.SOUTH);
                
                setBlockState(world, chest2, x + 0, y + 2, z + 0, sbb);
                this.placeTreasureAtCurrentPosition(world, decoRNG, x + 1, y + 2, z + 0, TFTreasure.darktower_cache, sbb);
                break;
            case 3:
                setBlockState(world, deco.accentState, x + 1, y + 1, z + 0, sbb);
                setBlockState(world, deco.accentState, x + 1, y + 1, z + 1, sbb);
                
                Chest chest3 = (Chest) Bukkit.createBlockData(Material.CHEST);
                chest3.setFacing(BlockFace.SOUTH);

                setBlockState(world, chest3, x + 1, y + 2, z + 0, sbb);
                this.placeTreasureAtCurrentPosition(world, decoRNG, x + 1, y + 2, z + 1, TFTreasure.darktower_cache, sbb);
                break;
        }

    }


    private void decorateUnbuilderMaze(AsyncWorldEditor world, Random decoRNG, StructureBoundingBox sbb, Rotation rotation, int y)
    {

        // fill in posts
        for (int x = size / 2; x < size - 1; x++)
        {
            for (int z = 3; z < size - 1; z++)
            {
                if (x % 2 == 1 && z % 2 == 1)
                {
                    for (int py = 1; py < 5; py++)
                    {
                        setBlockStateRotated(world, deco.pillarState, x, y + py, z, rotation, sbb);
                    }
                }
                else if (x % 2 == 1 || z % 2 == 1)
                {
                    for (int py = 1; py < 5; py++)
                    {
                        setBlockStateRotated(world, deco.fenceState, x, y + py, z, rotation, sbb);
                    }

                    if (x != size / 2 && x != size - 2 && z != size - 2)
                    {
                        int ay = decoRNG.nextInt(4) + 1;
                        setBlockStateRotated(world, AIR, x, y + ay, z, rotation, sbb);

                        if (x > size - 7)
                        {
                            ay = decoRNG.nextInt(3) + 1;
                            setBlockStateRotated(world, AIR, x, y + ay, z, rotation, sbb);
                        }
                    }
                }
            }
        }

//        final BlockData antiBuilderBlockState = TFBlocks.tower_device.getDefaultState().withProperty(BlockTFTowerDevice.VARIANT, TowerDeviceVariant.ANTIBUILDER);
//
//        // place unbuilders
//        setBlockStateRotated(world, antiBuilderBlockState, 15, y + 2, 7, rotation, sbb);
//        setBlockStateRotated(world, antiBuilderBlockState, 11, y + 3, 7, rotation, sbb);
//        setBlockStateRotated(world, antiBuilderBlockState, 15, y + 2, 13, rotation, sbb);
//        setBlockStateRotated(world, antiBuilderBlockState, 11, y + 3, 13, rotation, sbb);
//        setBlockStateRotated(world, antiBuilderBlockState, 5, y + 3, 13, rotation, sbb);
    }

    private void decorateLounge(AsyncWorldEditor world, Random decoRNG, StructureBoundingBox sbb, Rotation rotation, int y)
    {
        //  brewing area in corner - walls
        this.fillBlocksRotated(world, sbb, 17, y + 1, 1, 17, y + 4, 6, deco.pillarState, rotation);
        this.fillBlocksRotated(world, sbb, 12, y + 1, 1, 17, y + 4, 1, deco.pillarState, rotation);

        // floors
        this.fillBlocksRotated(world, sbb, 13, y + 1, 2, 16, y + 1, 5, deco.blockState, rotation);
        this.fillBlocksRotated(world, sbb, 12, y + 1, 2, 12, y + 1, 6, getStairState(deco.stairState, BlockFace.WEST, rotation, false), rotation);
        this.fillBlocksRotated(world, sbb, 12, y + 1, 6, 16, y + 1, 6, getStairState(deco.stairState, BlockFace.SOUTH, rotation, false), rotation);

        // furnaces
        this.makeDispenserPillar(world, deco, 13, y, 1, BlockFace.SOUTH, rotation, sbb);
        this.makeDispenserPillar(world, deco, 15, y, 1, BlockFace.SOUTH, rotation, sbb);
        this.makeDispenserPillar(world, deco, 17, y, 3, BlockFace.WEST, rotation, sbb);
        this.makeDispenserPillar(world, deco, 17, y, 5, BlockFace.WEST, rotation, sbb);

        // framing pillars
        this.makeStonePillar(world, deco, 12, y, 1, BlockFace.NORTH, rotation, sbb);
        this.makeStonePillar(world, deco, 17, y, 6, BlockFace.EAST, rotation, sbb);

        // cauldron and brewing stand
        this.setBlockStateRotated(world, Bukkit.createBlockData(Material.BREWING_STAND), 13, y + 2, 5, rotation, sbb);
        
        Levelled cauldron = (Levelled) Bukkit.createBlockData(Material.CAULDRON);
        cauldron.setLevel(3);
        
        this.setBlockStateRotated(world, cauldron, 15, y + 2, 3, rotation, sbb);

        // bookshelves in corner
        this.fillBlocksRotated(world, sbb, 10, y + 1, 17, 17, y + 4, 17, deco.blockState, rotation);
        this.fillBlocksRotated(world, sbb, 17, y + 1, 10, 17, y + 4, 17, deco.blockState, rotation);

        this.fillBlocksRotated(world, sbb, 11, y + 1, 17, 12, y + 4, 17, Bukkit.createBlockData(Material.BOOKSHELF), rotation);
        this.fillBlocksRotated(world, sbb, 14, y + 1, 17, 15, y + 4, 17, Bukkit.createBlockData(Material.BOOKSHELF), rotation);
        this.fillBlocksRotated(world, sbb, 17, y + 1, 11, 17, y + 4, 12, Bukkit.createBlockData(Material.BOOKSHELF), rotation);
        this.fillBlocksRotated(world, sbb, 17, y + 1, 14, 17, y + 4, 15, Bukkit.createBlockData(Material.BOOKSHELF), rotation);

        // table
        this.setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.SOUTH, rotation, true), 13, y + 1, 14, rotation, sbb);
        this.setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.EAST, rotation, true), 14, y + 1, 14, rotation, sbb);
        this.setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.NORTH, rotation, true), 14, y + 1, 13, rotation, sbb);
        this.setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.WEST, rotation, true), 13, y + 1, 13, rotation, sbb);

        // chair 1
        this.setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.EAST, rotation, false), 11, y + 1, 13, rotation, sbb);

        // chair 2
        this.setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.SOUTH, rotation, false), 13, y + 1, 11, rotation, sbb);

        // center lamp
        setBlockStateRotated(world, Bukkit.createBlockData(Material.REDSTONE_LAMP), 8, y + 3, 8, rotation, sbb);
        setBlockStateRotated(world, getLeverState(Bukkit.createBlockData(Material.LEVER), decoRNG.nextBoolean() ? BlockFace.EAST : BlockFace.SOUTH, Switch.Face.FLOOR, rotation, false), 8, y + 2, 8, rotation, sbb);

        // planter for trees
        placeTreePlanter(world, decoRNG.nextInt(5), 6, y + 1, 12, rotation, sbb);

    }

    private void makeDispenserPillar(AsyncWorldEditor world, StructureTFDecorator forgeDeco, int x, int y, int z, BlockFace stairMeta, Rotation rotation, StructureBoundingBox sbb)
    {
        this.setBlockStateRotated(world, getStairState(forgeDeco.stairState, stairMeta, rotation, true), x, y + 2, z, rotation, sbb);
        {
            Dispenser dispenser = (Dispenser) Bukkit.createBlockData(Material.DISPENSER);
            dispenser.setFacing(stairMeta);
            this.setBlockStateRotated(world, dispenser, x, y + 3, z, rotation, sbb);
        }
        this.setBlockStateRotated(world, getStairState(forgeDeco.stairState, stairMeta, rotation, false), x, y + 4, z, rotation, sbb);
    }


    private void decorateBossSpawner(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, Rotation rotation, int y)
    {
//        this.setBlockStateRotated(world, TFBlocks.boss_spawner.getDefaultState().withProperty(BlockTFBossSpawner.VARIANT, BossVariant.UR_GHAST), 9, y + 4, 9, rotation, sbb);
    }


    private void decorateExperiment(AsyncWorldEditor world, Random decoRNG, StructureBoundingBox sbb, Rotation rotation, int y)
    {
        final BlockData obsidian = Bukkit.createBlockData(Material.OBSIDIAN);
        final BlockData netherrack = Bukkit.createBlockData(Material.NETHERRACK);
        final BlockData redstone = Bukkit.createBlockData(Material.REDSTONE_BLOCK);
        final BlockData inactiveReactor = Bukkit.createBlockData(Material.STONE);

        //  crafting area in corner - walls
        this.fillBlocksRotated(world, sbb, 17, y + 1, 1, 17, y + 4, 6, deco.pillarState, rotation);
        this.fillBlocksRotated(world, sbb, 12, y + 1, 1, 17, y + 4, 1, deco.pillarState, rotation);

        // floors
        this.fillBlocksRotated(world, sbb, 13, y + 1, 2, 16, y + 1, 5, deco.blockState, rotation);
        this.fillBlocksRotated(world, sbb, 12, y + 1, 2, 12, y + 1, 6, getStairState(deco.stairState, BlockFace.WEST, rotation, false), rotation);
        this.fillBlocksRotated(world, sbb, 12, y + 1, 6, 16, y + 1, 6, getStairState(deco.stairState, BlockFace.SOUTH, rotation, false), rotation);

        this.makeWoodPillar(world, deco, 13, y, 1, rotation, sbb);
        this.makeWoodPillar(world, deco, 15, y, 1, rotation, sbb);
        this.makeWoodPillar(world, deco, 17, y, 3, rotation, sbb);
        this.makeWoodPillar(world, deco, 17, y, 5, rotation, sbb);

        // framing pillars
        this.makeStonePillar(world, deco, 12, y, 1, BlockFace.NORTH, rotation, sbb);
        this.makeStonePillar(world, deco, 17, y, 6, BlockFace.EAST, rotation, sbb);

        // workbench
        setBlockStateRotated(world, Bukkit.createBlockData(Material.CRAFTING_TABLE), 14, y + 2, 4, rotation, sbb);

//        // recipes in frames?
//        placeItemFrameRotated(world, 13, y + 2, 1, rotation, BlockFace.SOUTH, new ItemStack(TFItems.borer_essence), sbb);
//        placeItemFrameRotated(world, 14, y + 2, 1, rotation, BlockFace.SOUTH, new ItemStack(Items.REDSTONE), sbb);
//        placeItemFrameRotated(world, 15, y + 2, 1, rotation, BlockFace.SOUTH, new ItemStack(TFItems.borer_essence), sbb);
//        placeItemFrameRotated(world, 13, y + 3, 1, rotation, BlockFace.SOUTH, new ItemStack(Items.REDSTONE), sbb);
//        placeItemFrameRotated(world, 14, y + 3, 1, rotation, BlockFace.SOUTH, new ItemStack(Items.GHAST_TEAR), sbb);
//        placeItemFrameRotated(world, 15, y + 3, 1, rotation, BlockFace.SOUTH, new ItemStack(Items.REDSTONE), sbb);
//        placeItemFrameRotated(world, 13, y + 4, 1, rotation, BlockFace.SOUTH, new ItemStack(TFItems.borer_essence), sbb);
//        placeItemFrameRotated(world, 14, y + 4, 1, rotation, BlockFace.SOUTH, new ItemStack(Items.REDSTONE), sbb);
//        placeItemFrameRotated(world, 15, y + 4, 1, rotation, BlockFace.SOUTH, new ItemStack(TFItems.borer_essence), sbb);
//
//        placeItemFrameRotated(world, 17, y + 2, 3, rotation, WEST, new ItemStack(TFBlocks.tower_wood, 1, 1), sbb);
//        placeItemFrameRotated(world, 17, y + 2, 4, rotation, WEST, new ItemStack(TFBlocks.tower_wood, 1, 0), sbb);
//        placeItemFrameRotated(world, 17, y + 2, 5, rotation, WEST, new ItemStack(TFBlocks.tower_wood, 1, 1), sbb);
//        placeItemFrameRotated(world, 17, y + 3, 3, rotation, WEST, new ItemStack(TFBlocks.tower_wood, 1, 0), sbb);
//        placeItemFrameRotated(world, 17, y + 3, 4, rotation, WEST, new ItemStack(TFItems.carminite), sbb);
//        placeItemFrameRotated(world, 17, y + 3, 5, rotation, WEST, new ItemStack(TFBlocks.tower_wood, 1, 0), sbb);
//        placeItemFrameRotated(world, 17, y + 4, 3, rotation, WEST, new ItemStack(TFBlocks.tower_wood, 1, 1), sbb);
//        placeItemFrameRotated(world, 17, y + 4, 4, rotation, WEST, new ItemStack(TFBlocks.tower_wood, 1, 0), sbb);
//        placeItemFrameRotated(world, 17, y + 4, 5, rotation, WEST, new ItemStack(TFBlocks.tower_wood, 1, 1), sbb);

        if (y < this.height - 13)
        {
            // device bottom
            setBlockStateRotated(world, obsidian, 13, y + 1, 13, rotation, sbb);
            setBlockStateRotated(world, obsidian, 15, y + 1, 13, rotation, sbb);
            setBlockStateRotated(world, obsidian, 13, y + 1, 15, rotation, sbb);
            setBlockStateRotated(world, obsidian, 15, y + 1, 15, rotation, sbb);
            setBlockStateRotated(world, netherrack, 13, y + 1, 14, rotation, sbb);
            setBlockStateRotated(world, netherrack, 14, y + 1, 13, rotation, sbb);
            setBlockStateRotated(world, netherrack, 15, y + 1, 14, rotation, sbb);
            setBlockStateRotated(world, netherrack, 14, y + 1, 15, rotation, sbb);
            setBlockStateRotated(world, redstone, 14, y + 1, 14, rotation, sbb);

            // middle
            setBlockStateRotated(world, netherrack, 13, y + 2, 13, rotation, sbb);
            setBlockStateRotated(world, netherrack, 15, y + 2, 13, rotation, sbb);
            setBlockStateRotated(world, netherrack, 13, y + 2, 15, rotation, sbb);
            setBlockStateRotated(world, netherrack, 15, y + 2, 15, rotation, sbb);
            setBlockStateRotated(world, inactiveReactor, 14, y + 2, 14, rotation, sbb);

            //device top
            setBlockStateRotated(world, obsidian, 13, y + 3, 13, rotation, sbb);
            setBlockStateRotated(world, obsidian, 15, y + 3, 13, rotation, sbb);
            setBlockStateRotated(world, obsidian, 13, y + 3, 15, rotation, sbb);
            setBlockStateRotated(world, obsidian, 15, y + 3, 15, rotation, sbb);
            setBlockStateRotated(world, netherrack, 13, y + 3, 14, rotation, sbb);
            setBlockStateRotated(world, netherrack, 14, y + 3, 13, rotation, sbb);
            setBlockStateRotated(world, netherrack, 15, y + 3, 14, rotation, sbb);
            setBlockStateRotated(world, netherrack, 14, y + 3, 15, rotation, sbb);
            setBlockStateRotated(world, redstone, 14, y + 3, 14, rotation, sbb);
        }


        // short piston plunger 1
        setBlockStateRotated(world, deco.accentState, 14, y + 1, 17, rotation, sbb);
        setBlockStateRotated(world, getLeverState(Bukkit.createBlockData(Material.LEVER), BlockFace.EAST, Switch.Face.WALL, rotation, false), 13, y + 1, 17, rotation, sbb);
        {
            Piston piston = (Piston) Bukkit.createBlockData(Material.PISTON);
            piston.setFacing(BlockFace.SOUTH);
            setBlockStateRotated(world, piston, 14, y + 2, 17, rotation, sbb);
        }
        setBlockStateRotated(world, redstone, 14, y + 2, 16, rotation, sbb);


        // short piston plunger 2
        setBlockStateRotated(world, deco.accentState, 17, y + 1, 14, rotation, sbb);
        setBlockStateRotated(world, getLeverState(Bukkit.createBlockData(Material.LEVER), BlockFace.SOUTH, rotation, false), 17, y + 1, 13, rotation, sbb);
        {
            Piston piston = (Piston) Bukkit.createBlockData(Material.PISTON);
            piston.setFacing(BlockFace.EAST);
            setBlockStateRotated(world, piston, 17, y + 2, 14, rotation, sbb);
        }
        setBlockStateRotated(world, redstone, 16, y + 2, 14, rotation, sbb);

        // long piston plunger 1
        setBlockStateRotated(world, redstone, 14, y + 2, 11, rotation, sbb);
        setBlockStateRotated(world, deco.accentState, 14, y + 1, 11, rotation, sbb);
        setBlockStateRotated(world, getLeverState(Bukkit.createBlockData(Material.LEVER), BlockFace.EAST, rotation, true), 13, y + 1, 11, rotation, sbb);
        {
            Piston piston = (Piston) Bukkit.createBlockData(Material.PISTON);
            piston.setFacing(BlockFace.NORTH);
            setBlockStateRotated(world, piston, 14, y + 2, 10, rotation, sbb);
        }
        setBlockStateRotated(world, deco.accentState, 14, y + 1, 9, rotation, sbb);
        setBlockStateRotated(world, getLeverState(Bukkit.createBlockData(Material.LEVER), BlockFace.EAST, rotation, false), 13, y + 1, 9, rotation, sbb);
        {
            Piston piston = (Piston) Bukkit.createBlockData(Material.STICKY_PISTON);
            piston.setFacing(BlockFace.NORTH);
            setBlockStateRotated(world, piston, 14, y + 2, 9, rotation, sbb);
        }

        // long piston plunger 2
        setBlockStateRotated(world, redstone, 11, y + 2, 14, rotation, sbb);
        setBlockStateRotated(world, deco.accentState, 11, y + 1, 14, rotation, sbb);
        setBlockStateRotated(world, getLeverState(Bukkit.createBlockData(Material.LEVER), BlockFace.SOUTH, rotation, true), 11, y + 1, 13, rotation, sbb);
        {
            Piston piston = (Piston) Bukkit.createBlockData(Material.PISTON);
            piston.setFacing(BlockFace.WEST);
            setBlockStateRotated(world, piston, 10, y + 2, 14, rotation, sbb);
        }
        setBlockStateRotated(world, deco.accentState, 9, y + 1, 14, rotation, sbb);
        setBlockStateRotated(world, getLeverState(Bukkit.createBlockData(Material.LEVER), BlockFace.SOUTH, rotation, false), 9, y + 1, 13, rotation, sbb);
        {
            Piston piston = (Piston) Bukkit.createBlockData(Material.STICKY_PISTON);
            piston.setFacing(BlockFace.WEST);
            setBlockStateRotated(world, piston, 9, y + 2, 14, rotation, sbb);
        }
    }

    private void makeWoodPillar(AsyncWorldEditor world, StructureTFDecorator forgeDeco, int x, int y, int z, Rotation rotation, StructureBoundingBox sbb)
    {
        final BlockData log = Bukkit.createBlockData(Material.STRIPPED_DARK_OAK_WOOD);
        this.setBlockStateRotated(world, log, x, y + 2, z, rotation, sbb);
        this.setBlockStateRotated(world, log, x, y + 3, z, rotation, sbb);
        this.setBlockStateRotated(world, log, x, y + 4, z, rotation, sbb);
    }

//    private void placeItemFrameRotated(AsyncWorldEditor world, int x, int y, int z, Rotation rotation, BlockFace direction, ItemStack itemStack, StructureBoundingBox sbb)
//    {
//
//        int dx = getXWithOffsetRotated(x, z, rotation);
//        int dy = getYWithOffset(y);
//        int dz = getZWithOffsetRotated(x, z, rotation);
//        BlockFace facing = this.rotation.add(rotation).rotate(direction).getOpposite();
//        final BlockPos pos = new BlockPos(dx, dy, dz).offset(facing);
//        if (sbb.isVecInside(pos))
//        {
//            EntityItemFrame frame = new EntityItemFrame(world, pos, facing);
//            if (!itemStack.isEmpty())
//            {
//                frame.setDisplayedItem(itemStack);
//            }
//
//            // check if the frame is on a valid surface or not?  The wall may not have been generated yet, on a chunk boundry
//            world.spawnEntity(frame);
//        }
//    }


    private void decorateAquarium(AsyncWorldEditor world, Random decoRNG, StructureBoundingBox sbb, Rotation rotation, int y)
    {
        BlockData water = Bukkit.createBlockData(Material.WATER);
        // main aquarium
        makePillarFrame(world, sbb, this.deco, rotation, 12, y, 3, 4, 4, 13, false);
        this.fillBlocksRotated(world, sbb, 13, y + 4, 4, 14, y + 4, 14, water, rotation);

        // little one
        makePillarFrame(world, sbb, this.deco, rotation, 6, y, 12, 4, 4, 4, false);
        this.fillBlocksRotated(world, sbb, 6, y + 5, 12, 9, y + 5, 15, deco.accentState, rotation);
        this.fillBlocksRotated(world, sbb, 7, y + 4, 13, 8, y + 5, 14, water, rotation);
    }

    private void decorateForge(AsyncWorldEditor world, Random decoRNG, StructureBoundingBox sbb, Rotation rotation, int y)
    {
//        StructureTFDecorator forgeDeco = new StructureTFDecorator();
//        forgeDeco = Blocks.COBBLESTONE;
//        forgeDeco.pillarID = Blocks.STONEBRICK;
//        forgeDeco.stairID = Blocks.STAIRCOMPACTCOBBLESTONE;
        StructureTFDecorator forgeDeco = this.deco;

        // stone walls in corner
        this.fillBlocksRotated(world, sbb, 17, y + 1, 1, 17, y + 4, 6, forgeDeco.pillarState, rotation);
        this.fillBlocksRotated(world, sbb, 12, y + 1, 1, 17, y + 4, 1, forgeDeco.pillarState, rotation);

        this.fillBlocksRotated(world, sbb, 12, y + 1, 17, 17, y + 4, 17, forgeDeco.pillarState, rotation);
        this.fillBlocksRotated(world, sbb, 17, y + 1, 12, 17, y + 4, 17, forgeDeco.pillarState, rotation);

        // floors
        this.fillBlocksRotated(world, sbb, 13, y + 1, 2, 16, y + 1, 5, forgeDeco.blockState, rotation);
        this.fillBlocksRotated(world, sbb, 12, y + 1, 2, 12, y + 1, 6, getStairState(deco.stairState, BlockFace.WEST, rotation, false), rotation);
        this.fillBlocksRotated(world, sbb, 12, y + 1, 6, 16, y + 1, 6, getStairState(deco.stairState, BlockFace.SOUTH, rotation, false), rotation);

        this.fillBlocksRotated(world, sbb, 13, y + 1, 13, 16, y + 1, 16, forgeDeco.blockState, rotation);
        this.fillBlocksRotated(world, sbb, 12, y + 1, 12, 12, y + 1, 16, getStairState(deco.stairState, BlockFace.WEST, rotation, false), rotation);
        this.fillBlocksRotated(world, sbb, 12, y + 1, 12, 16, y + 1, 12, getStairState(deco.stairState, BlockFace.NORTH, rotation, false), rotation);


        // furnaces
        this.makeFurnacePillar(world, forgeDeco, decoRNG, 13, y, 1, BlockFace.SOUTH, rotation, sbb);
        this.makeFurnacePillar(world, forgeDeco, decoRNG, 15, y, 1, BlockFace.SOUTH, rotation, sbb);
        this.makeFurnacePillar(world, forgeDeco, decoRNG, 17, y, 3, BlockFace.WEST, rotation, sbb);
        this.makeFurnacePillar(world, forgeDeco, decoRNG, 17, y, 5, BlockFace.WEST, rotation, sbb);

        this.makeFurnacePillar(world, forgeDeco, decoRNG, 13, y, 17, BlockFace.NORTH, rotation, sbb);
        this.makeFurnacePillar(world, forgeDeco, decoRNG, 15, y, 17, BlockFace.NORTH, rotation, sbb);
        this.makeFurnacePillar(world, forgeDeco, decoRNG, 17, y, 13, BlockFace.WEST, rotation, sbb);
        this.makeFurnacePillar(world, forgeDeco, decoRNG, 17, y, 15, BlockFace.WEST, rotation, sbb);

        // framing pillars
        //this.makeStonePillar(world, forgeDeco, 12, y, 1, BlockFace.SOUTH, rotation, sbb);
        this.makeStonePillar(world, forgeDeco, 17, y, 6, BlockFace.WEST, rotation, sbb);

        this.makeStonePillar(world, forgeDeco, 12, y, 17, BlockFace.NORTH, rotation, sbb);
        this.makeStonePillar(world, forgeDeco, 17, y, 12, BlockFace.WEST, rotation, sbb);

        // extra pillars
        this.makeStonePillar(world, forgeDeco, 17, y, 9, BlockFace.WEST, rotation, sbb);
        this.makeStonePillar(world, forgeDeco, 9, y, 17, BlockFace.NORTH, rotation, sbb);

        // anvils        
        Directional anvil = (Directional) Bukkit.createBlockData(Material.CHIPPED_ANVIL);
        anvil.setFacing(HORIZONTALS[decoRNG.nextInt(4)]);
        
        this.setBlockStateRotated(world, anvil, 13, y + 2, 5, rotation, sbb);
        
        anvil = (Directional) Bukkit.createBlockData(Material.CHIPPED_ANVIL);
        anvil.setFacing(HORIZONTALS[decoRNG.nextInt(4)]);
        this.setBlockStateRotated(world, anvil, 13, y + 2, 13, rotation, sbb);

        // fire pit
        makeFirePit(world, forgeDeco, 6, y + 1, 12, rotation, sbb);
    }

    private void makeFurnacePillar(AsyncWorldEditor world, StructureTFDecorator forgeDeco, Random rand, int x, int y, int z, BlockFace direction, Rotation rotation, StructureBoundingBox sbb)
    {

        this.setBlockStateRotated(world, getStairState(deco.stairState, direction, rotation, true), x, y + 2, z, rotation, sbb);
        Furnace furnace = (Furnace) Bukkit.createBlockData(Material.FURNACE);
        furnace.setFacing(direction);
        this.setBlockStateRotated(world, furnace, x, y + 3, z, rotation, sbb);

//        // randomly put some charcoal in the furnace burn slot
//        int amount = rand.nextBoolean() ? rand.nextInt(5) + 4 : 0;
//        if (amount > 0)
//        {
//            int dx = getXWithOffsetRotated(x, z, rotation);
//            int dy = getYWithOffset(y + 3);
//            int dz = getZWithOffsetRotated(x, z, rotation);
//
//            BlockPos pos = new BlockPos(dx, dy, dz);
//
//            if (sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() == Blocks.FURNACE)
//            {
//                // put charcoal in the oven
//                IInventory inv = (IInventory)world.getTileEntity(pos);
//
//                inv.setInventorySlotContents(1, new ItemStack(Items.COAL, amount, 1));
//            }
//        }

        this.setBlockStateRotated(world, getStairState(deco.stairState, direction, rotation, false), x, y + 4, z, rotation, sbb);
    }


    private void makeStonePillar(AsyncWorldEditor world, StructureTFDecorator forgeDeco, int x, int y, int z, BlockFace stairDirection, Rotation rotation, StructureBoundingBox sbb)
    {
        for (int py = 1; py <= 4; py++)
        {
            this.setBlockStateRotated(world, forgeDeco.pillarState, x, y + py, z, rotation, sbb);
        }
        // we used to have caps here, but the code was too complex to be worthwhile
    }

    private void makeFirePit(AsyncWorldEditor world, StructureTFDecorator myDeco, int x, int y, int z, Rotation rotation, StructureBoundingBox sbb)
    {
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.WEST, rotation, false), x - 1, y, z + 0, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.EAST, rotation, false), x + 1, y, z + 0, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.SOUTH, rotation, false), x + 0, y, z + 1, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.NORTH, rotation, false), x + 0, y, z - 1, rotation, sbb);
        setBlockStateRotated(world, myDeco.pillarState, x + 1, y, z + 1, rotation, sbb);
        setBlockStateRotated(world, myDeco.pillarState, x + 1, y, z - 1, rotation, sbb);
        setBlockStateRotated(world, myDeco.pillarState, x - 1, y, z + 1, rotation, sbb);
        setBlockStateRotated(world, myDeco.pillarState, x - 1, y, z - 1, rotation, sbb);

        setBlockStateRotated(world, Bukkit.createBlockData(Material.NETHERRACK), x, y, z, rotation, sbb);
        setBlockStateRotated(world, Bukkit.createBlockData(Material.FIRE), x, y + 1, z, rotation, sbb);
    }


    private void decorateNetherwart(AsyncWorldEditor world, Random decoRNG, StructureBoundingBox sbb, Rotation rotation, int y, boolean isTop)
    {
//        StructureTFDecorator netherDeco = new StructureTFDecorator();
//        netherDeco = Blocks.NETHERBRICK;
//        netherDeco.pillarID = Blocks.NETHERBRICK;
//        netherDeco.stairID = Blocks.STAIRSNETHERBRICK;
//        netherDeco.fenceID = Blocks.NETHERFENCE;
        StructureTFDecorator netherDeco = this.deco;

//        // lava container
//        makePillarFrame(world, sbb, netherDeco, rotation, 12, y, 3, 4, 4, 4, true);
//        this.fillBlocksRotated(world, sbb, 13, y + 1, 4, 14, y + 1, 5, Blocks.LAVAMOVING, 0, rotation);
//        this.fillBlocksRotated(world, sbb, 12, y + 3, 3, 15, y + 4, 6, 0, 0, rotation);

        //makeNetherburst(world, decoRNG, 8, 100, 20, 12, y + 3, 3, rotation, sbb);

        // wart container
        makePillarFrame(world, sbb, netherDeco, rotation, 12, y, 9, 4, 4, 7, true);
        this.fillBlocksRotated(world, sbb, 13, y + 1, 10, 14, y + 1, 14, Bukkit.createBlockData(Material.SOUL_SAND), rotation);
        this.fillBlocksRotated(world, sbb, 13, y + 2, 10, 14, y + 2, 14, Bukkit.createBlockData(Material.NETHER_WART), rotation);
        this.fillBlocksRotated(world, sbb, 13, y + 4, 10, 14, y + 4, 14, Bukkit.createBlockData(Material.SOUL_SAND), rotation);

        // blaze container
        makePillarFrame(world, sbb, netherDeco, rotation, 5, y, 12, 3, (isTop ? 4 : 9), 3, true);
        this.setBlockStateRotated(world, netherDeco.blockState, 6, y + 1, 13, rotation, sbb);
        this.setBlockStateRotated(world, netherDeco.blockState, 6, y + (isTop ? 4 : 9), 13, rotation, sbb);
        setSpawner(world, 6, y + 3, 13, sbb, EntityType.BLAZE);

        // destruction blob
        destroyTower(world, decoRNG, 12, y, 3, 2, sbb);

    }

    private void decorateBotanical(AsyncWorldEditor world, Random decoRNG, StructureBoundingBox sbb, Rotation rotation, int y)
    {
        // main part
        makePillarFrame(world, sbb, this.deco, rotation, 12, y, 12, 4, 4, 4, true);
        this.fillBlocksRotated(world, sbb, 13, y + 1, 13, 14, y + 1, 14, deco.blockState, rotation);
        this.fillBlocksRotated(world, sbb, 13, y + 4, 13, 14, y + 4, 14, deco.blockState, rotation);

        placeRandomPlant(world, decoRNG, 13, y + 2, 13, rotation, sbb);
        placeRandomPlant(world, decoRNG, 13, y + 2, 14, rotation, sbb);
        placeRandomPlant(world, decoRNG, 14, y + 2, 13, rotation, sbb);
        placeRandomPlant(world, decoRNG, 14, y + 2, 14, rotation, sbb);

        // toolbench
        for (int py = 1; py <= 4; py++)
        {
            setBlockStateRotated(world, deco.pillarState, 12, y + py, 4, rotation, sbb);
            setBlockStateRotated(world, deco.pillarState, 15, y + py, 4, rotation, sbb);
        }
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.EAST, rotation, true), 13, y + 1, 4, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.WEST, rotation, true), 14, y + 1, 4, rotation, sbb);

        //setBlockStateRotated(world, Blocks.CHEST, 0, 13, y + 2, 4, rotation, sbb);
        placeTreasureRotated(world, decoRNG, 13, y + 2, 4, rotation, TFTreasure.basement, sbb);
        setBlockStateRotated(world, Bukkit.createBlockData(Material.CRAFTING_TABLE), 14, y + 2, 4, rotation, sbb);

        Slab slab = (Slab) Bukkit.createBlockData(Material.SPRUCE_SLAB);
        slab.setType(Slab.Type.TOP);

        // bench 2
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.EAST, rotation, true), 12, y + 1, 7, rotation, sbb);
        setBlockStateRotated(world, slab, 13, y + 1, 7, rotation, sbb);
        setBlockStateRotated(world, slab, 14, y + 1, 7, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.WEST, rotation, true), 15, y + 1, 7, rotation, sbb);

        // bench 2
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.EAST, rotation, true), 12, y + 1, 10, rotation, sbb);
        setBlockStateRotated(world, slab, 13, y + 1, 10, rotation, sbb);
        setBlockStateRotated(world, slab, 14, y + 1, 10, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.WEST, rotation, true), 15, y + 1, 10, rotation, sbb);

        // plants on benches
        for (int x = 12; x <= 15; x++)
        {
            placeRandomPlant(world, decoRNG, x, y + 2, 7, rotation, sbb);
            placeRandomPlant(world, decoRNG, x, y + 2, 10, rotation, sbb);
        }

        // planter for trees
        placeTreePlanter(world, decoRNG.nextInt(5), 6, y + 1, 12, rotation, sbb);
    }


    private void placeTreePlanter(AsyncWorldEditor world, int treeNum, int x, int y, int z, Rotation rotation, StructureBoundingBox sbb)
    {
        setBlockStateRotated(world, deco.pillarState, x + 1, y, z + 1, rotation, sbb);
        setBlockStateRotated(world, deco.pillarState, x + 1, y, z - 1, rotation, sbb);
        setBlockStateRotated(world, deco.pillarState, x - 1, y, z + 1, rotation, sbb);
        setBlockStateRotated(world, deco.pillarState, x - 1, y, z - 1, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.WEST, rotation, false), x - 1, y, z + 0, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.EAST, rotation, false), x + 1, y, z + 0, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.SOUTH, rotation, false), x + 0, y, z + 1, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.NORTH, rotation, false), x + 0, y, z - 1, rotation, sbb);

        setBlockStateRotated(world, Bukkit.createBlockData(Material.DIRT), x, y, z, rotation, sbb);
        setBlockStateRotated(world, Bukkit.createBlockData(Material.OAK_SAPLING), x, y + 1, z, rotation, sbb);

//        int dx = getXWithOffsetRotated(x, z, rotation);
//        int dy = getYWithOffset(y + 1);
//        int dz = getZWithOffsetRotated(x, z, rotation);
//        if (sbb.isVecInside(new BlockPos(dx, dy, dz)))
//        {
//            WorldGenerator treeGen;
//            // grow a tree
//            switch (treeNum)
//            {
//                case 0:
//                default:
//                    // oak tree
//                    treeGen = new WorldGenTrees(false);
//                    break;
//                case 1:
//                    final BlockData leaves = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, false);
//                    final BlockData wood = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
//
//                    // jungle tree
//                    treeGen = new WorldGenTrees(true, 3, wood, leaves, false);
//                    break;
//                case 2:
//                    // birch
//                    treeGen = new WorldGenBirchTree(true, false);
//                    break;
//                case 3:
//                    treeGen = new TFGenSmallTwilightOak(false);
//                    break;
//                case 4:
//                    treeGen = new TFGenSmallRainboak(false);
//                    break;
//            }
//
//            for (int i = 0; i < 100; i++)
//            {
//                if (treeGen.generate(world, world.rand, new BlockPos(dx, dy, dz)))
//                {
//                    break;
//                }
//            }
//        }

    }


    // TODO : make this work with async world editor
    private void placeRandomPlant(AsyncWorldEditor world, Random decoRNG, int x, int y, int z, Rotation rotation, StructureBoundingBox sbb)
    {
//        int potMeta = decoRNG.nextInt(15); // this seems to be the only way to set the flower pre-placement
//        final BlockData flowerPotState = Blocks.FLOWER_POT.getDefaultState().withProperty(BlockFlowerPot.LEGACY_DATA, potMeta);
//        setBlockStateRotated(world, flowerPotState, x, y, z, rotation, sbb);
    }


    private void makeBottomEntrance(AsyncWorldEditor world, Random decoRNG, StructureBoundingBox sbb, Rotation rotation, int y)
    {
        makeFirePit(world, this.deco, 13, y + 1, 3, rotation, sbb);
        makeFirePit(world, this.deco, 3, y + 1, 13, rotation, sbb);
        makeFirePit(world, this.deco, 13, y + 1, 13, rotation, sbb);

        makePillarFrame(world, sbb, this.deco, rotation, 7, y, 7, 3, 4, 3, false);

    }


    /**
     * Add a bunch of timber beams
     */
    protected void addTimberMaze(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, int bottom, int top)
    {

        int spacing = 5;
        Rotation floorside = Rotation.NONE;
        if (bottom == 0)
        {
            bottom += spacing;
        }

        // fill with beam maze
        for (int y = bottom; y < top; y += spacing)
        {
            floorside = floorside.add(Rotation.CLOCKWISE_90);
            makeTimberBeams(world, rand, sbb, floorside, y, y == bottom && bottom != spacing, y >= (top - spacing), top);
        }
    }


    /**
     * Make a lattice of log blocks
     *
     * @param top
     */
    protected void makeTimberBeams(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, Rotation rotation, int y, boolean isBottom, boolean isTop, int top)
    {
        BlockData beamID = Bukkit.createBlockData(Material.DARK_OAK_LOG);
        Orientable beamStateNS = (Orientable) beamID.clone();
        beamStateNS.setAxis(Axis.Z);
        Orientable beamStateUD = (Orientable) beamID.clone();
        beamStateNS.setAxis(Axis.Y);
        Orientable beamStateEW = (Orientable) beamID.clone();
        beamStateNS.setAxis(Axis.X);
        
        // three beams going n/s
        for (int z = 1; z < size - 1; z++)
        {
            setBlockStateRotated(world, beamStateNS, 4, y, z, rotation, sbb);
            setBlockStateRotated(world, beamStateNS, 9, y, z, rotation, sbb);
            setBlockStateRotated(world, beamStateNS, 14, y, z, rotation, sbb);
        }

        // a few random cross beams
        int z = pickBetweenExcluding(3, size - 3, rand, 4, 9, 14);
        for (int x = 5; x < 9; x++)
        {
            setBlockStateRotated(world, beamStateEW, x, y, z, rotation, sbb);
        }

        z = pickBetweenExcluding(3, size - 3, rand, 4, 9, 14);
        for (int x = 10; x < 14; x++)
        {
            setBlockStateRotated(world, beamStateEW, x, y, z, rotation, sbb);
        }


        // beams going down
        int x1 = 4;
        int z1 = pickFrom(rand, 4, 9, 14);
        int x2 = 9;
        int z2 = pickFrom(rand, 4, 9, 14);
        int x3 = 14;
        int z3 = pickFrom(rand, 4, 9, 14);

        for (int by = 1; by < 5; by++)
        {
            if (!isBottom || checkPost(world, x1, y - 5, z1, rotation, sbb))
            {
                setBlockStateRotated(world, beamStateUD, x1, y - by, z1, rotation, sbb);
                {
                    Ladder ladder = (Ladder) Bukkit.createBlockData(Material.LADDER);
                    ladder.setFacing(BlockFace.WEST);
                    setBlockStateRotated(world, ladder, x1 + 1, y - by, z1, rotation, sbb);
                }
            }
            if (!isBottom || checkPost(world, x2, y - 5, z2, rotation, sbb))
            {
                setBlockStateRotated(world, beamStateUD, x2, y - by, z2, rotation, sbb);
            }
            if (!isBottom || checkPost(world, x3, y - 5, z3, rotation, sbb))
            {
                setBlockStateRotated(world, beamStateUD, x3, y - by, z3, rotation, sbb);
                {
                    Ladder ladder = (Ladder) Bukkit.createBlockData(Material.LADDER);
                    ladder.setFacing(BlockFace.EAST);
                    setBlockStateRotated(world, ladder, x3 - 1, y - by, z3, rotation, sbb);
                }
            }
        }

        // do we need a beam going up?
        if (isTop)
        {
            Rotation topFloorRotation = RotationUtil.ROTATIONS[(this.boundingBox.minY + top + 1) % 4];

            int ladderX = 4;
            int ladderZ = 10;
            for (int by = 1; by < 5; by++)
            {
                setBlockStateRotated(world, beamStateUD, ladderX, y + by, 9, topFloorRotation, sbb);
                {
                    Ladder ladder = (Ladder) Bukkit.createBlockData(Material.LADDER);
                    ladder.setFacing(BlockFace.NORTH);
                    setBlockStateRotated(world, ladder, ladderX, y + by, ladderZ, topFloorRotation, sbb);
                }
            }

            // fence thing
            setBlockStateRotated(world, AIR, ladderX, y + 6, 9, topFloorRotation, sbb);
            setBlockStateRotated(world, deco.fenceState, ladderX + 1, y + 5, ladderZ, topFloorRotation, sbb);
            setBlockStateRotated(world, deco.fenceState, ladderX - 1, y + 5, ladderZ, topFloorRotation, sbb);
            setBlockStateRotated(world, deco.fenceState, ladderX + 1, y + 6, ladderZ, topFloorRotation, sbb);
            setBlockStateRotated(world, deco.fenceState, ladderX - 1, y + 6, ladderZ, topFloorRotation, sbb);
        }

        if (!isBottom && !isTop)
        {
            // spawners
            int sx = pickFrom(rand, 6, 7, 11);
            int sz = pickFrom(rand, 6, 11, 12);

            makeMiniGhastSpawner(world, rand, y, sx, sz, sbb);

        }

        // lamps
        int lx = pickFrom(rand, 2, 12, 16);
        int lz = 2 + rand.nextInt(15);

        setBlockStateRotated(world, Bukkit.createBlockData(Material.REDSTONE_LAMP), lx, y + 2, lz, rotation, sbb);
        setBlockStateRotated(world, getLeverState(Bukkit.createBlockData(Material.LEVER), rand.nextBoolean() ? BlockFace.EAST : BlockFace.NORTH, Switch.Face.FLOOR, rotation, false), lx, y + 1, lz, rotation, sbb);
    }


    // TODO : make this work with async world editor
    /**
     * Make a mini ghast spawner and then set the spawn range and max entities for that spawner
     */
    private void makeMiniGhastSpawner(AsyncWorldEditor world, Random rand, int y, int sx, int sz, StructureBoundingBox sbb)
    {
    }


    /**
     * Add a bunch of platforms accessible with the block builder
     */
    protected void addBuilderPlatforms(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, int bottom, int top)
    {

        int spacing = 5;
        Rotation floorside = Rotation.NONE;
        if (bottom == 0)
        {
            bottom += spacing;
        }


        // fill platforms, aside from bottom and top platform
        for (int y = bottom; y < (top - spacing); y += spacing)
        {
            makeBuilderPlatforms(world, rand, sbb, floorside, y, y == bottom && bottom != spacing, y >= (top - spacing));
            floorside = floorside.add(Rotation.CLOCKWISE_90);
            floorside = floorside.add(RotationUtil.ROTATIONS[rand.nextInt(3)]);
        }

        // add the bottom platforms
        makeBuilderPlatform(world, rand, Rotation.CLOCKWISE_90, bottom, 5, true, sbb);
        makeBuilderPlatform(world, rand, Rotation.COUNTERCLOCKWISE_90, bottom, 5, true, sbb);

        for (int y = bottom - 4; y < bottom; y++)
        {
            Ladder ladder = (Ladder) Bukkit.createBlockData(Material.LADDER);
            ladder.setFacing(BlockFace.WEST);
            setBlockStateRotated(world, ladder, 1, y, 5, Rotation.CLOCKWISE_90, sbb);
            setBlockStateRotated(world, ladder, 1, y, 5, Rotation.COUNTERCLOCKWISE_90, sbb);
        }


        // top platform
        addTopBuilderPlatform(world, rand, bottom, top, spacing, sbb);

    }


    /**
     * Make platform with a block builder
     */
    protected void makeBuilderPlatforms(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, Rotation rotation, int y, boolean bottom, boolean top)
    {
        int z = size / 2 + rand.nextInt(5) - rand.nextInt(5);

        // bottom platform
        makeBuilderPlatform(world, rand, rotation, y, z, false, sbb);

        // ladder
        Ladder ladder = (Ladder) Bukkit.createBlockData(Material.LADDER);
        ladder.setFacing(BlockFace.WEST);
        setBlockStateRotated(world, ladder, 1, y + 1, z, rotation, sbb);
        setBlockStateRotated(world, ladder, 1, y + 2, z, rotation, sbb);
        setBlockStateRotated(world, ladder, 1, y + 3, z, rotation, sbb);
        setBlockStateRotated(world, ladder, 1, y + 4, z, rotation, sbb);

        makeBuilderPlatform(world, rand, rotation, y + 5, z, true, sbb);

        if (y % 2 == 1)
        {
            // reverter blocks
            int sx = pickFrom(rand, 5, 9, 13);
            int sz = (sx == 9) ? (rand.nextBoolean() ? 5 : 13) : 9;

            final BlockData antibuilder = Bukkit.createBlockData(Material.CHISELED_STONE_BRICKS);
            setBlockStateRotated(world, antibuilder, sx, y + 2, sz, rotation, sbb);
        }
        else
        {
            // lamp cluster
            int sx = rand.nextBoolean() ? 5 : 13;
            int sz = rand.nextBoolean() ? 5 : 13;

            makeLampCluster(world, rand, sx, y, sz, rotation, sbb);
        }
    }


    /**
     * Add the top floating platform in the builder platforms area
     */
    private void addTopBuilderPlatform(AsyncWorldEditor world, Random rand, int bottom, int top, int spacing, StructureBoundingBox sbb)
    {
        Rotation rotation = RotationUtil.ROTATIONS[(this.boundingBox.minY + top + 1) % 4];

        // platform
        this.fillBlocksRotated(world, sbb, 5, top - spacing, 9, 7, top - spacing, 11, deco.accentState, rotation);
        // ladder ascender
        this.fillBlocksRotated(world, sbb, 6, top - spacing, 9, 6, top, 9, deco.accentState, rotation);
        {
            Ladder ladder = (Ladder) Bukkit.createBlockData(Material.LADDER);
            ladder.setFacing(BlockFace.NORTH);
            this.fillBlocksRotated(world, sbb, 6, top - spacing + 1, 10, 6, top - 1, 10, ladder, rotation);
        }
        setBlockStateRotated(world, AIR, 6, top + 1, 9, rotation, sbb);
        setBlockStateRotated(world, deco.fenceState, 5, top + 0, 10, rotation, sbb);
        setBlockStateRotated(world, deco.fenceState, 7, top + 0, 10, rotation, sbb);
        setBlockStateRotated(world, deco.fenceState, 5, top + 1, 10, rotation, sbb);
        setBlockStateRotated(world, deco.fenceState, 7, top + 1, 10, rotation, sbb);
        // builder & lever
        final BlockData inactiveBuilder = Bukkit.createBlockData(Material.CHISELED_STONE_BRICKS);
        setBlockStateRotated(world, inactiveBuilder, 7, top - spacing, 10, rotation, sbb);
        setBlockStateRotated(world, getLeverState(Bukkit.createBlockData(Material.LEVER), rand.nextBoolean() ? BlockFace.SOUTH : BlockFace.EAST, Switch.Face.CEILING, rotation, false), 7, top - spacing + 1, 11, rotation, sbb);

    }


    private void makeBuilderPlatform(AsyncWorldEditor world, Random rand, Rotation rotation, int y, int z, boolean hole, StructureBoundingBox sbb)
    {
        // top platform
        setBlockStateRotated(world, deco.accentState, 1, y, z - 1, rotation, sbb);
        if (!hole)
        {
            setBlockStateRotated(world, deco.accentState, 1, y, z - 0, rotation, sbb);
        }
        setBlockStateRotated(world, deco.accentState, 1, y, z + 1, rotation, sbb);
        setBlockStateRotated(world, deco.accentState, 2, y, z - 1, rotation, sbb);
        setBlockStateRotated(world, deco.accentState, 2, y, z - 0, rotation, sbb);
        setBlockStateRotated(world, deco.accentState, 2, y, z + 1, rotation, sbb);

        // builder & lever
        final BlockData inactiveBuilder = Bukkit.createBlockData(Material.CHISELED_STONE_BRICKS);
        setBlockStateRotated(world, inactiveBuilder, 2, y, hole ? z + 1 : z - 1, rotation, sbb);
        setBlockStateRotated(world, getLeverState(Bukkit.createBlockData(Material.LEVER), rand.nextBoolean() ? BlockFace.SOUTH : BlockFace.EAST, Switch.Face.CEILING, rotation, false), 2, y + 1, z + 0, rotation, sbb);
    }


    /**
     * Make a cluster of redstone lamps w/ switches as decoration for the empty areas in towers
     */
    private void makeLampCluster(AsyncWorldEditor world, Random rand, int sx, int y, int sz, Rotation rotation, StructureBoundingBox sbb)
    {
        int radius = 4;

        // make lamps
        for (int i = 0; i < 5; i++)
        {
            int lx = sx;
            int ly = y;
            int lz = sz;

            for (int move = 0; move < 10; move++)
            {
                // place lamp
                setBlockStateRotated(world, Bukkit.createBlockData(Material.REDSTONE_LAMP), lx, ly, lz, rotation, sbb);

                // move randomly
                int direction = rand.nextInt(8);

                if (direction > 5)
                {
                    direction -= 2;
                }
                BlockFace facing = BlockFace.values()[direction];


                lx += facing.getModX();
                ly += facing.getModY();
                lz += facing.getModZ();

                // if we are out of bounds, stop iterating
                if (lx > sx + radius || lx < sx - radius || ly > y + radius || ly < y - radius || lz > sz + radius || lz < sz - radius)
                {
                    break;
                }
            }
        }

        // make switches
        for (int i = 0; i < 5; i++)
        {
            int lx = sx;
            int ly = y;
            int lz = sz;

            // we need to always call rand.nextInt the same amount of times
            BlockFace[] directions = new BlockFace[10];
            for (int move = 0; move < 10; move++)
            {
                int direction = rand.nextInt(8);

                if (direction > 5)
                {
                    direction -= 2;
                }
                directions[move] = BlockFace.values()[direction];
            }

            for (int move = 0; move < 10; move++)
            {
                // move randomly
                BlockFace direction = directions[move];

                lx += direction.getModX();
                ly += direction.getModY();
                lz += direction.getModZ();

                // if we are out of bounds, stop iterating
                if (lx > sx + radius || lx < sx - radius || ly > y + radius || ly < y - radius || lz > sz + radius || lz < sz - radius)
                {
                    break;
                }

                // if there is no lamp, place a switch and quit
                if (getBlockStateFromPosRotated(world, lx, ly, lz, sbb, rotation).getMaterial() != Material.REDSTONE_LAMP)
                {
                    Switch.Face orientation;
                    BlockFace switch_face;
                    switch (direction)
                    {
                        case NORTH:
                            orientation = Switch.Face.WALL;
                            switch_face = BlockFace.SOUTH;
                            break;
                        case SOUTH:
                            orientation = Switch.Face.WALL;
                            switch_face = BlockFace.NORTH;
                            break;
                        case EAST:
                            orientation = Switch.Face.WALL;
                            switch_face = BlockFace.WEST;
                            break;
                        case WEST:
                            orientation = Switch.Face.WALL;
                            switch_face = BlockFace.EAST;
                            break;
                        case UP:
                            orientation = Switch.Face.CEILING;
                            switch_face = BlockFace.SOUTH;
                            break;
                        case DOWN:
                        default:
                            orientation = Switch.Face.FLOOR;
                            switch_face = BlockFace.EAST;
                            break;
                    }


                    setBlockStateRotated(world, getLeverState(Bukkit.createBlockData(Material.LEVER), switch_face, orientation, rotation, false), lx, ly, lz, rotation, sbb);
                    break;
                }
            }
        }

    }
}
