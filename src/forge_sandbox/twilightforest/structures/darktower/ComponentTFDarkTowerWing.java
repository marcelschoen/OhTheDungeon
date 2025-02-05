package forge_sandbox.twilightforest.structures.darktower;

import forge_sandbox.StructureBoundingBox;
import forge_sandbox.twilightforest.structures.StructureTFComponentOld;
import forge_sandbox.twilightforest.structures.StructureTFDecorator;
import forge_sandbox.twilightforest.structures.lichtower.ComponentTFTowerRoof;
import forge_sandbox.twilightforest.structures.lichtower.ComponentTFTowerRoofAttachedSlab;
import forge_sandbox.twilightforest.structures.lichtower.ComponentTFTowerRoofFence;
import forge_sandbox.twilightforest.structures.lichtower.ComponentTFTowerRoofGableForwards;
import forge_sandbox.twilightforest.structures.lichtower.ComponentTFTowerRoofSlabForwards;
import forge_sandbox.twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.block.BlockFace;
import otd.util.RotationMirror.Rotation;
import otd.lib.async.AsyncWorldEditor;
import forge_sandbox.twilightforest.TFFeature;
import forge_sandbox.twilightforest.structures.StructureTFComponent;
import forge_sandbox.twilightforest.structures.RotationUtil;
import forge_sandbox.BlockPos;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.EntityType;
import forge_sandbox.twilightforest.TFTreasure;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Switch;

public class ComponentTFDarkTowerWing extends ComponentTFTowerWing {
    protected boolean keyTower = false;

    protected ArrayList<EnumDarkTowerDoor> openingTypes = new ArrayList<>();

    public ComponentTFDarkTowerWing() {
    }

    protected ComponentTFDarkTowerWing(TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, BlockFace direction) {
        super(feature, i, x, y, z, pSize, pHeight, direction);
    }

    /**
     * Turn the openings array into an array of ints.
     */
    private int[] getDoorsTypesAsIntArray() {
        int[] ret = new int[this.openingTypes.size()];

        int idx = 0;

        for (EnumDarkTowerDoor doorType : openingTypes) {
            ret[idx++] = doorType.ordinal();
        }

        return ret;
    }

    /**
     * Read in opening types from int array
     */
    private void readDoorsTypesFromArray(int[] intArray) {
        for (int typeInt : intArray) {
            this.openingTypes.add(EnumDarkTowerDoor.values()[typeInt]);
        }
    }

    @Override
    public void buildComponent(StructureTFComponent parent, List<StructureTFComponent> list, Random rand) {
        if (parent != null && parent instanceof StructureTFComponentOld) {
            this.deco = ((StructureTFComponentOld) parent).deco;
        }

        // we should have a door where we started
        addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);

        // add a roof?
        makeARoof(parent, list, rand);

        // add a beard
        makeABeard(parent, list, rand);


        if (size > 10) {
            // sub towers
            for (Rotation direction : RotationUtil.ROTATIONS) {
                int[] dest = getValidOpening(rand, direction);
                int childSize = size - 2;
                int childHeight = validateChildHeight(height - 4 + rand.nextInt(10) - rand.nextInt(10), childSize);

                boolean madeWing = makeTowerWing(list, rand, this.getComponentType(), dest[0], dest[1], dest[2], size - 2, childHeight, direction);

                // occasional balcony
                if (!madeWing && (direction == Rotation.CLOCKWISE_180 || rand.nextBoolean())) {
                    makeTowerBalcony(list, rand, this.getComponentType(), dest[0], dest[1], dest[2], direction);
                }
            }
        } else if (rand.nextInt(4) == 0) {
            // occasional balcony on small towers too
            Rotation direction = RotationUtil.ROTATIONS[rand.nextInt(4)];
            int[] dest = getValidOpening(rand, direction);
            makeTowerBalcony(list, rand, this.getComponentType(), dest[0], dest[1], dest[2], direction);
        }


    }

    protected int validateChildHeight(int childHeight, int childSize) {
        return (childHeight / 4) * 4 + 1;

//        if (childSize > 9)
//        {
//            return (childHeight / 4) * 4 + 1;
//        }
//        else
//        {
//            return (childHeight / 3) * 3; 
//        }
    }

    /**
     * Attach a roof to this tower.
     */
    @Override
    public void makeARoof(StructureTFComponent parent, List<StructureTFComponent> list, Random rand) {
        int index = this.getComponentType();

        ComponentTFTowerRoof roof;

        switch (rand.nextInt(5)) {
            case 0:
            case 1:
            default:
                roof = new ComponentTFDarkTowerRoofAntenna(getFeatureType(), index, this);
                break;
            case 2:
                roof = new ComponentTFDarkTowerRoofCactus(getFeatureType(), index, this);
                break;
            case 3:
                roof = new ComponentTFDarkTowerRoofRings(getFeatureType(), index, this);
                break;
            case 4:
                roof = new ComponentTFDarkTowerRoofFourPost(getFeatureType(), index, this);
                break;
        }

        list.add(roof);
        roof.buildComponent(this, list, rand);
        roofType = roof.getClass();
    }

    @Override
    protected void makeAttachedRoof(List<StructureTFComponent> list, Random rand) {
        int index = this.getComponentType();
        ComponentTFTowerRoof roof;

        // this is our preferred roof type:
        if (roofType == null && rand.nextInt(32) != 0) {
            tryToFitRoof(list, rand, new ComponentTFTowerRoofGableForwards(getFeatureType(), index + 1, this));
        }

        // this is for roofs that don't fit.
        if (roofType == null && rand.nextInt(8) != 0) {
            tryToFitRoof(list, rand, new ComponentTFTowerRoofSlabForwards(getFeatureType(), index + 1, this));
        }

        // finally, if we're cramped for space, try this
        if (roofType == null && rand.nextInt(32) != 0) {
            // fall through to this next roof
            roof = new ComponentTFTowerRoofAttachedSlab(getFeatureType(), index + 1, this);
            tryToFitRoof(list, rand, roof);
        }

        // last resort
        if (roofType == null) {
            // fall through to this next roof
            roof = new ComponentTFTowerRoofFence(getFeatureType(), index + 1, this);
            tryToFitRoof(list, rand, roof);
        }
    }


    /**
     * Add a beard to this structure.  There is only one type of beard.
     */
    @Override
    public void makeABeard(StructureTFComponent parent, List<StructureTFComponent> list, Random rand) {
        ComponentTFDarkTowerBeard beard = new ComponentTFDarkTowerBeard(getFeatureType(), this.getComponentType() + 1, this);
        list.add(beard);
        beard.buildComponent(this, list, rand);
    }

    /**
     * Make another wing just like this one
     */
    @Override
    public boolean makeTowerWing(List<StructureTFComponent> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
        // kill too-small towers
        if (wingHeight < 8) {
            return false;
        }

        BlockFace direction = getStructureRelativeRotation(rotation);
        int[] dx = offsetTowerCoords(x, y, z, 5, direction);

        if (dx[1] + wingHeight > 250) {
            // end of the world!
            return false;
        }

        ComponentTFDarkTowerBridge bridge = new ComponentTFDarkTowerBridge(getFeatureType(), index, dx[0], dx[1], dx[2], wingSize, wingHeight, direction);
        // check to see if it intersects something already there
        StructureTFComponent intersect = StructureTFComponent.findIntersecting(list, bridge.getBoundingBox());
        if (intersect == null || intersect == this) {
            intersect = StructureTFComponent.findIntersecting(list, bridge.getWingBB());
        } else {
            return false;
        }
        if (intersect == null || intersect == this) {
            list.add(bridge);
            bridge.buildComponent(this, list, rand);
            addOpening(x, y, z, rotation);
            return true;
        } else {
            return false;
        }
    }

    protected boolean makeTowerBalcony(List<StructureTFComponent> list, Random rand, int index, int x, int y, int z, Rotation rotation) {
        BlockFace direction = getStructureRelativeRotation(rotation);
        int[] dx = offsetTowerCoords(x, y, z, 5, direction);


        ComponentTFDarkTowerBalcony balcony = new ComponentTFDarkTowerBalcony(getFeatureType(), index, dx[0], dx[1], dx[2], direction);
        // check to see if it intersects something already there
        StructureTFComponent intersect = StructureTFComponent.findIntersecting(list, balcony.getBoundingBox());
        if (intersect == null || intersect == this) {
            list.add(balcony);
            balcony.buildComponent(this, list, rand);
            addOpening(x, y, z, rotation, EnumDarkTowerDoor.REAPPEARING);
            return true;
        } else {
            return false;
        }

    }


    @Override
    public boolean addComponentParts(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb) {
        Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

        // make walls
        makeEncasedWalls(world, rand, sbb, 0, 0, 0, size - 1, height - 1, size - 1);

        // clear inside
        fillWithAir(world, sbb, 1, 1, 1, size - 2, height - 2, size - 2);

        // sky light
        nullifySkyLightForBoundingBox(world);

        if (this.size > 9) {
            // half floors, always starting at y = 4
            addHalfFloors(world, decoRNG, sbb, 4, height - 1);
        } else {
            if (decoRNG.nextInt(3) == 0) {
                addSmallTimberBeams(world, decoRNG, sbb, 4, height - 1);
            } else {
                addHalfFloors(world, decoRNG, sbb, 4, height - 1);
            }
        }

        // openings
        makeOpenings(world, sbb);

        // destroy some towers
        if (decoRNG.nextBoolean() && !this.isKeyTower() && this.height > 8) {
            int blobs = 1;

            if (this.size > 9 && decoRNG.nextBoolean()) {
                blobs++;
            }

            for (int i = 0; i < blobs; i++) {

                // find a random spot in the tower
                int x = decoRNG.nextInt(size);
                int y = decoRNG.nextInt(height - 7) + 2;
                int z = decoRNG.nextInt(size);

                destroyTower(world, decoRNG, x, y, z, 3, sbb);
            }

        }

        return true;
    }

    /**
     * Add a destruction burst
     */
    protected void destroyTower(AsyncWorldEditor world, Random decoRNG, int x, int y, int z, int amount, StructureBoundingBox sbb) {
        //makeNetherburst(world, decoRNG, 16, 100, 40, x, y, z, 0, sbb);

        int initialRadius = decoRNG.nextInt(amount) + amount;

        drawBlob(world, x, y, z, initialRadius, AIR, sbb);

        for (int i = 0; i < 3; i++) {
            int dx = x + (initialRadius - 1) * (decoRNG.nextBoolean() ? 1 : -1);
            int dy = y + (initialRadius - 1) * (decoRNG.nextBoolean() ? 1 : -1);
            int dz = z + (initialRadius - 1) * (decoRNG.nextBoolean() ? 1 : -1);

            netherTransformBlob(world, decoRNG, dx, dy, dz, initialRadius - 1, sbb);
            drawBlob(world, dx, dy, dz, initialRadius - 2, AIR, sbb);
        }

    }

    private void netherTransformBlob(AsyncWorldEditor world, Random inRand, int sx, int sy, int sz, int rad, StructureBoundingBox sbb) {

        Random rand = new Random(inRand.nextLong());

        // then trace out a quadrant
        for (byte dx = 0; dx <= rad; dx++) {
            for (byte dy = 0; dy <= rad; dy++) {
                for (byte dz = 0; dz <= rad; dz++) {
                    // determine how far we are from the center.
                    byte dist = 0;
                    if (dx >= dy && dx >= dz) {
                        dist = (byte) (dx + (byte) ((Math.max(dy, dz) * 0.5) + (Math.min(dy, dz) * 0.25)));
                    } else if (dy >= dx && dy >= dz) {
                        dist = (byte) (dy + (byte) ((Math.max(dx, dz) * 0.5) + (Math.min(dx, dz) * 0.25)));
                    } else {
                        dist = (byte) (dz + (byte) ((Math.max(dx, dy) * 0.5) + (Math.min(dx, dy) * 0.25)));
                    }


                    // if we're inside the blob, fill it
                    if (dist <= rad) {
                        // do eight at a time for easiness!
                        testAndChangeToNetherrack(world, rand, sx + dx, sy + dy, sz + dz, sbb);
                        testAndChangeToNetherrack(world, rand, sx + dx, sy + dy, sz + dz, sbb);
                        testAndChangeToNetherrack(world, rand, sx + dx, sy + dy, sz - dz, sbb);
                        testAndChangeToNetherrack(world, rand, sx - dx, sy + dy, sz + dz, sbb);
                        testAndChangeToNetherrack(world, rand, sx - dx, sy + dy, sz - dz, sbb);
                        testAndChangeToNetherrack(world, rand, sx + dx, sy - dy, sz + dz, sbb);
                        testAndChangeToNetherrack(world, rand, sx + dx, sy - dy, sz - dz, sbb);
                        testAndChangeToNetherrack(world, rand, sx - dx, sy - dy, sz + dz, sbb);
                        testAndChangeToNetherrack(world, rand, sx - dx, sy - dy, sz - dz, sbb);
                    }
                }
            }
        }
    }


    private void testAndChangeToNetherrack(AsyncWorldEditor world, Random rand, int x, int y, int z, StructureBoundingBox sbb) {
        if (this.getBlockStateFromPos(world, x, y, z, sbb) != Material.AIR) {
            this.setBlockState(world, Material.NETHERRACK, x, y, z, sbb);

            if (this.getBlockStateFromPos(world, x, y + 1, z, sbb) == Material.AIR && rand.nextBoolean()) {
                this.setBlockState(world, Material.FIRE, x, y + 1, z, sbb);
            }
        }
    }


    /**
     * Draw a giant blob of whatevs (okay, it's going to be leaves).
     */
    private void drawBlob(AsyncWorldEditor world, int sx, int sy, int sz, int rad, BlockData state, StructureBoundingBox sbb) {
        // then trace out a quadrant
        for (byte dx = 0; dx <= rad; dx++) {
            for (byte dy = 0; dy <= rad; dy++) {
                for (byte dz = 0; dz <= rad; dz++) {
                    // determine how far we are from the center.
                    byte dist = 0;
                    if (dx >= dy && dx >= dz) {
                        dist = (byte) (dx + (byte) ((Math.max(dy, dz) * 0.5) + (Math.min(dy, dz) * 0.25)));
                    } else if (dy >= dx && dy >= dz) {
                        dist = (byte) (dy + (byte) ((Math.max(dx, dz) * 0.5) + (Math.min(dx, dz) * 0.25)));
                    } else {
                        dist = (byte) (dz + (byte) ((Math.max(dx, dy) * 0.5) + (Math.min(dx, dy) * 0.25)));
                    }


                    // if we're inside the blob, fill it
                    if (dist <= rad) {
                        // do eight at a time for easiness!
                        this.setBlockState(world, state, sx + dx, sy + dy, sz + dz, sbb);
                        this.setBlockState(world, state, sx + dx, sy + dy, sz - dz, sbb);
                        this.setBlockState(world, state, sx - dx, sy + dy, sz + dz, sbb);
                        this.setBlockState(world, state, sx - dx, sy + dy, sz - dz, sbb);
                        this.setBlockState(world, state, sx + dx, sy - dy, sz + dz, sbb);
                        this.setBlockState(world, state, sx + dx, sy - dy, sz - dz, sbb);
                        this.setBlockState(world, state, sx - dx, sy - dy, sz + dz, sbb);
                        this.setBlockState(world, state, sx - dx, sy - dy, sz - dz, sbb);
                    }
                }
            }
        }
    }

    /**
     * Add a bunch of random half floors
     */
    private void addHalfFloors(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, int bottom, int top) {

        int spacing = 4;//this.size > 9 ? 4 : 3;
        Rotation rotation = RotationUtil.ROTATIONS[(this.boundingBox.minY + bottom) % 3];

        if (bottom == 0) {
            bottom += spacing;
        }

        // fill with half floors
        for (int y = bottom; y < top; y += spacing) {
            rotation = rotation.add(Rotation.CLOCKWISE_180);

            if (y >= top - spacing) {
                makeFullFloor(world, sbb, rotation, y, spacing);
                if (isDeadEnd()) {
                    decorateTreasureRoom(world, sbb, rotation, y, 4, this.deco);
                }
            } else {
                makeHalfFloor(world, sbb, rotation, y, spacing);

                // decorate
                switch (rand.nextInt(8)) {
                    case 0:
                        if (this.size < 11) {
                            decorateReappearingFloor(world, rand, sbb, rotation, y);
                            break;
                        }
                    case 1:
                        decorateSpawner(world, rand, sbb, rotation, y);
                        break;
                    case 2:
                        decorateLounge(world, rand, sbb, rotation, y);
                        break;
                    case 3:
                        decorateLibrary(world, rand, sbb, rotation, y);
                        break;
                    case 4:
                        decorateExperimentPulser(world, rand, sbb, rotation, y);
                        break;
                    case 5:
                        decorateExperimentLamp(world, rand, sbb, rotation, y);
                        break;
                    case 6:
                        decoratePuzzleChest(world, rand, sbb, rotation, y);
                        break;
                }

            }

            addStairsDown(world, sbb, rotation, y, size - 2, spacing);
            if (this.size > 9) {
                // double wide staircase
                addStairsDown(world, sbb, rotation, y, size - 3, spacing);
            }
        }

        rotation = rotation.add(Rotation.CLOCKWISE_180);

        // stairs to roof
        addStairsDown(world, sbb, rotation, this.height - 1, size - 2, spacing);
    }

    /**
     * Dark tower half floors
     */
    protected void makeHalfFloor(AsyncWorldEditor world, StructureBoundingBox sbb, Rotation rotation, int y, int spacing) {
        this.fillBlocksRotated(world, sbb, size / 2, y, 1, size - 2, y, size - 2, deco.blockState, rotation);
        this.fillBlocksRotated(world, sbb, size / 2 - 1, y, 1, size / 2 - 1, y, size - 2, deco.accentState, rotation);
    }

    /**
     * Dark tower full floors
     */
    protected void makeFullFloor(AsyncWorldEditor world, StructureBoundingBox sbb, Rotation rotation, int y, int spacing) {
        // half floor
        this.fillWithBlocks(world, sbb, 1, y, 1, size - 2, y, size - 2, deco.blockState, AIR, false);
        this.fillWithBlocks(world, sbb, size / 2, y, 1, size / 2, y, size - 2, deco.accentState, AIR, true);
    }

    /**
     * Dark tower treasure rooms!
     *
     * @param myDeco
     */
    protected void decorateTreasureRoom(AsyncWorldEditor world, StructureBoundingBox sbb, Rotation rotation, int y, int spacing, StructureTFDecorator myDeco) {
        //treasure chest!
        int x = this.size / 2;
        int z = this.size / 2;

        this.makePillarFrame(world, sbb, this.deco, rotation, x - 1, y, z - 1, true);

        setBlockStateRotated(world, myDeco.platformState, x, y + 1, z, rotation, sbb);

        placeTreasureAtCurrentPosition(world, null, x, y + 2, z, this.isKeyTower() ? TFTreasure.darktower_key : TFTreasure.darktower_cache, sbb);
    }

    private void decorateSpawner(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, Rotation rotation, int y) {
        int x = this.size > 9 ? 4 : 3;
        int z = this.size > 9 ? 5 : 4;

        EntityType mobID;

        if (this.size > 9) {
            mobID = rand.nextBoolean() ? EntityType.ZOMBIE : EntityType.CAVE_SPIDER;
        } else {
            mobID = EntityType.CAVE_SPIDER;
        }

        // pillar frame
        this.makePillarFrame(world, sbb, this.deco, rotation, x, y, z, true);
        this.setSpawner(world, x + 1, y + 2, z + 1, sbb, mobID);
    }

    /**
     * A lounge with a couch and table
     */
    private void decorateLounge(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, Rotation rotation, int y) {
        int cx = this.size > 9 ? 9 : 7;
        int cz = this.size > 9 ? 4 : 3;

        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.SOUTH, rotation, false), cx, y + 1, cz + 0, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.WEST, rotation, false), cx, y + 1, cz + 1, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.NORTH, rotation, false), cx, y + 1, cz + 2, rotation, sbb);

        cx = this.size > 9 ? 5 : 3;

        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.SOUTH, rotation, true), cx, y + 1, cz + 0, rotation, sbb);
        setBlockStateRotated(world, getSlabState(Bukkit.createBlockData(Material.SPRUCE_SLAB), Slab.Type.TOP), cx, y + 1, cz + 1, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.NORTH, rotation, true), cx, y + 1, cz + 2, rotation, sbb);
    }

    /**
     * Decorate with a pressure plate triggered reappearing floor.  Only suitable for small towers
     */
    private void decorateReappearingFloor(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, Rotation rotation, int y) {
        final BlockData inactiveReappearing = Bukkit.createBlockData(Material.STONE_BRICKS);
        final BlockData woodenPressurePlate = Bukkit.createBlockData(Material.OAK_PRESSURE_PLATE);
        // floor
        this.fillBlocksRotated(world, sbb, 4, y, 3, 7, y, 5, inactiveReappearing, rotation);
        // plates
        this.fillBlocksRotated(world, sbb, 4, y + 1, 2, 7, y + 1, 2, woodenPressurePlate, rotation);
        this.fillBlocksRotated(world, sbb, 4, y + 1, 6, 7, y + 1, 6, woodenPressurePlate, rotation);

    }

    /**
     * Decorate with a redstone device that turns a lamp on or off
     */
    private void decorateExperimentLamp(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, Rotation rotation, int y) {

        int cx = this.size > 9 ? 5 : 3;
        int cz = this.size > 9 ? 5 : 4;

        final BlockData redstoneLamp = Bukkit.createBlockData(Material.REDSTONE_LAMP);

        {
            Piston piston = (Piston) Bukkit.createBlockData(Material.STICKY_PISTON);
            piston.setFacing(BlockFace.UP);
            
            setBlockStateRotated(world, piston, cx, y + 1, cz, rotation, sbb);
        }
        setBlockStateRotated(world, redstoneLamp, cx, y + 2, cz, rotation, sbb);
        setBlockStateRotated(world, deco.accentState, cx, y + 1, cz + 1, rotation, sbb);
        setBlockStateRotated(world, getLeverState(Bukkit.createBlockData(Material.LEVER), BlockFace.NORTH, rotation, false), cx, y + 1, cz + 2, rotation, sbb);
        setBlockStateRotated(world, deco.accentState, cx, y + 3, cz - 1, rotation, sbb);
        setBlockStateRotated(world, getLeverState(Bukkit.createBlockData(Material.LEVER), BlockFace.SOUTH, rotation, true), cx, y + 3, cz - 2, rotation, sbb);
    }

    protected static BlockData getLeverState(BlockData initialState, BlockFace direction, Rotation rotation, boolean isPowered) {
        return getLeverState(initialState, direction, Switch.Face.WALL, rotation, isPowered);
    }

    protected static BlockData getLeverState(BlockData initialState, BlockFace direction, Switch.Face face, Rotation rotation, boolean isPowered) {
        
        if(face != Switch.Face.WALL) {
            if(direction == BlockFace.EAST || direction == BlockFace.WEST) {
                if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90) {
                    direction = BlockFace.NORTH;
                }
            } else if(direction == BlockFace.NORTH || direction == BlockFace.SOUTH) {
                if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90) {
                    direction = BlockFace.EAST;
                }
            }
        }
        
        Switch lever = (Switch) initialState;
        lever.setFacing(direction);
        lever.setFace(face);
        lever.setPowered(isPowered);
        
        return lever;
    }

    /**
     * Decorate with a redstone device that pulses a block back and forth
     */
    private void decorateExperimentPulser(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, Rotation rotation, int y) {

        int cx = this.size > 9 ? 6 : 5;
        int cz = this.size > 9 ? 4 : 3;

        BlockData redstoneWire = Bukkit.createBlockData(Material.REDSTONE_WIRE);
        BlockData woodenPressurePlate = Bukkit.createBlockData(Material.OAK_PRESSURE_PLATE);
        Piston stickyPiston = (Piston) Bukkit.createBlockData(Material.STICKY_PISTON);
        stickyPiston.setFacing(BlockFace.SOUTH);
        Repeater unpoweredRepeater = (Repeater) Bukkit.createBlockData(Material.REPEATER);
        unpoweredRepeater.setPowered(false);
        unpoweredRepeater.setFacing(BlockFace.WEST);
        
        setBlockStateRotated(world, stickyPiston, cx, y + 1, cz + 1, rotation, sbb);
        setBlockStateRotated(world, deco.accentState, cx, y + 1, cz, rotation, sbb);
        setBlockStateRotated(world, redstoneWire, cx + 1, y + 1, cz, rotation, sbb);
        setBlockStateRotated(world, woodenPressurePlate, cx + 2, y + 1, cz, rotation, sbb);
        setBlockStateRotated(world, unpoweredRepeater, cx - 1, y + 1, cz, rotation, sbb);
        setBlockStateRotated(world, redstoneWire, cx - 2, y + 1, cz, rotation, sbb);
        setBlockStateRotated(world, redstoneWire, cx - 2, y + 1, cz + 1, rotation, sbb);
        setBlockStateRotated(world, redstoneWire, cx - 1, y + 1, cz + 1, rotation, sbb);
    }

    /**
     * Decorate with some bookshelves
     */
    private void decorateLibrary(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, Rotation rotation, int y) {
        int bx = this.size > 9 ? 4 : 3;
        int bz = this.size > 9 ? 3 : 2;

        makeSmallBookshelf(world, sbb, rotation, y, bx, bz);

        bx = this.size > 9 ? 9 : 7;
        bz = this.size > 9 ? 3 : 2;
        makeSmallBookshelf(world, sbb, rotation, y, bx, bz);
    }

    protected void makeSmallBookshelf(AsyncWorldEditor world, StructureBoundingBox sbb, Rotation rotation, int y, int bx, int bz) {
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.NORTH, rotation, false), bx, y + 1, bz + 0, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.NORTH, rotation, true), bx, y + 2, bz + 0, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.SOUTH, rotation, false), bx, y + 1, bz + 3, rotation, sbb);
        setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.SOUTH, rotation, true), bx, y + 2, bz + 3, rotation, sbb);
        BlockData bookshelf = Bukkit.createBlockData(Material.BOOKSHELF);
        setBlockStateRotated(world, bookshelf, bx, y + 1, bz + 1, rotation, sbb);
        setBlockStateRotated(world, bookshelf, bx, y + 2, bz + 1, rotation, sbb);
        setBlockStateRotated(world, bookshelf, bx, y + 1, bz + 2, rotation, sbb);
        setBlockStateRotated(world, bookshelf, bx, y + 2, bz + 2, rotation, sbb);
    }


    /**
     * A chest with an extremely simple puzzle
     */
    private void decoratePuzzleChest(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, Rotation rotation, int y) {
        int x = this.size > 9 ? 4 : 3;
        int z = this.size > 9 ? 5 : 4;
        // pillar frameframe
        this.makePillarFrame(world, sbb, this.deco, rotation, x, y, z, true);

        // reinforce with towerwood
        setBlockStateRotated(world, deco.platformState, x + 1, y + 1, z + 1, rotation, sbb);
        setBlockStateRotated(world, deco.blockState, x + 2, y + 1, z + 1, rotation, sbb);
        setBlockStateRotated(world, deco.blockState, x + 0, y + 1, z + 1, rotation, sbb);
        setBlockStateRotated(world, deco.blockState, x + 1, y + 1, z + 2, rotation, sbb);
        setBlockStateRotated(world, deco.blockState, x + 1, y + 1, z + 0, rotation, sbb);

        setBlockStateRotated(world, deco.blockState, x + 2, y + 3, z + 1, rotation, sbb);
        setBlockStateRotated(world, deco.blockState, x + 0, y + 3, z + 1, rotation, sbb);
        setBlockStateRotated(world, deco.blockState, x + 1, y + 3, z + 2, rotation, sbb);
        setBlockStateRotated(world, AIR, x + 1, y + 3, z + 0, rotation, sbb);
        setBlockStateRotated(world, deco.blockState, x + 1, y + 3, z + 1, rotation, sbb);
        
        Piston piston = (Piston) Bukkit.createBlockData(Material.STICKY_PISTON);
        piston.setFacing(BlockFace.NORTH);
        
        setBlockStateRotated(world, piston, x + 1, y + 3, z - 1, rotation, sbb);
        setBlockStateRotated(world, deco.accentState, x + 1, y + 3, z - 2, rotation, sbb);
        
        setBlockStateRotated(world, getLeverState(Bukkit.createBlockData(Material.LEVER), BlockFace.WEST, Switch.Face.WALL, rotation, false), x + 2, y + 3, z - 2, rotation, sbb);

        placeTreasureRotated(world, rand, x + 1, y + 2, z + 1, rotation, TFTreasure.darktower_cache, sbb);
    }


    /**
     * Make a 3x3x3 pillar frame
     */
    protected void makePillarFrame(AsyncWorldEditor world, StructureBoundingBox sbb, StructureTFDecorator myDeco, Rotation rotation, int x, int y, int z, boolean fenced) {
        makePillarFrame(world, sbb, myDeco, rotation, x, y, z, 3, 3, 3, fenced);
    }

    /**
     * Place one of the architectural features that I frequently overuse in my structures
     */
    protected void makePillarFrame(AsyncWorldEditor world, StructureBoundingBox sbb, StructureTFDecorator myDeco, Rotation rotation, int x, int y, int z, int width, int height, int length, boolean fenced) {
        // fill in posts
        for (int dx = 0; dx < width; dx++) {
            for (int dz = 0; dz < length; dz++) {
                if ((dx % 3 == 0 || dx == width - 1) && (dz % 3 == 0 || dz == length - 1)) {
                    for (int py = 1; py <= height; py++) {
                        setBlockStateRotated(world, myDeco.pillarState, x + dx, y + py, z + dz, rotation, sbb);
                    }
                } else {
                    if (dx == 0) {
                        final BlockData southStairs = getStairState(deco.stairState, BlockFace.WEST, rotation, false);
                        setBlockStateRotated(world, southStairs, x + dx, y + 1, z + dz, rotation, sbb);
                        Stairs stair = (Stairs) southStairs.clone();
                        stair.setHalf(Bisected.Half.TOP);
                        setBlockStateRotated(world, stair, x + dx, y + height, z + dz, rotation, sbb);
                    } else if (dx == width - 1) {
                        final BlockData northStairs = getStairState(deco.stairState, BlockFace.EAST, rotation, false);
                        Stairs stair = (Stairs) northStairs.clone();
                        stair.setHalf(Bisected.Half.TOP);
                        setBlockStateRotated(world, northStairs, x + dx, y + 1, z + dz, rotation, sbb);
                        setBlockStateRotated(world, stair, x + dx, y + height, z + dz, rotation, sbb);
                    } else if (dz == 0) {
                        final BlockData westStairs = getStairState(deco.stairState, BlockFace.NORTH, rotation, false);
                        Stairs stair = (Stairs) westStairs.clone();
                        stair.setHalf(Bisected.Half.TOP);
                        setBlockStateRotated(world, westStairs, x + dx, y + 1, z + dz, rotation, sbb);
                        setBlockStateRotated(world, stair, x + dx, y + height, z + dz, rotation, sbb);
                    } else if (dz == length - 1) {
                        final BlockData eastStairs = getStairState(deco.stairState, BlockFace.SOUTH, rotation, false);
                        Stairs stair = (Stairs) eastStairs.clone();
                        stair.setHalf(Bisected.Half.TOP);
                        setBlockStateRotated(world, eastStairs, x + dx, y + 1, z + dz, rotation, sbb);
                        setBlockStateRotated(world, stair, x + dx, y + height, z + dz, rotation, sbb);
                    }

                    if (fenced && (dx == 0 || dx == width - 1 || dz == 0 || dz == length - 1)) {
                        for (int fy = 2; fy <= height - 1; fy++) {
                            setBlockStateRotated(world, myDeco.fenceState, x + dx, y + fy, z + dz, rotation, sbb);
                        }
                    }
                }
            }
        }
    }

    /**
     * Dark tower half floors
     */
    protected void addStairsDown(AsyncWorldEditor world, StructureBoundingBox sbb, Rotation rotation, int y, int sz, int spacing) {
        // stairs
        for (int i = 0; i < spacing; i++) {
            int sx = size - 3 - i;

            this.setBlockStateRotated(world, getStairState(deco.stairState, BlockFace.WEST, rotation, false), sx, y - i, sz, rotation, sbb);
            this.setBlockStateRotated(world, deco.accentState, sx, y - 1 - i, sz, rotation, sbb);
            this.setBlockStateRotated(world, AIR, sx, y + 1 - i, sz, rotation, sbb);
            this.setBlockStateRotated(world, AIR, sx, y + 2 - i, sz, rotation, sbb);
            this.setBlockStateRotated(world, AIR, sx - 1, y + 2 - i, sz, rotation, sbb);
            this.setBlockStateRotated(world, AIR, sx, y + 3 - i, sz, rotation, sbb);
            this.setBlockStateRotated(world, AIR, sx - 1, y + 3 - i, sz, rotation, sbb);
        }
    }

    /**
     * Add a bunch of timber beams
     */
    protected void addSmallTimberBeams(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, int bottom, int top) {

        int spacing = 4;
        Rotation rotation = Rotation.NONE;
        if (bottom == 0) {
            bottom += spacing;
        }


        // fill with 3/4 floors
        for (int y = bottom; y < top; y += spacing) {
            rotation = rotation.add(Rotation.CLOCKWISE_90);

            if (y >= top - spacing && isDeadEnd()) {
                makeTimberFloor(world, rand, sbb, rotation, y, spacing);

                StructureTFDecorator logDeco = new StructureDecoratorDarkTower();

                logDeco.pillarState = Bukkit.createBlockData(Material.STRIPPED_DARK_OAK_WOOD);
                logDeco.platformState = Bukkit.createBlockData(Material.STRIPPED_DARK_OAK_WOOD);

                decorateTreasureRoom(world, sbb, rotation, y, 4, logDeco);
            } else {
                makeSmallTimberBeams(world, rand, sbb, rotation, y, y == bottom && bottom != spacing, y >= (top - spacing));
            }
        }


    }

    /**
     * Make a mostly soid timber floor
     */
    protected void makeTimberFloor(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, Rotation rotation, int y, int spacing) {
        BlockData beamID = Bukkit.createBlockData(Material.DARK_OAK_LOG);
        Orientable beamStateNS = (Orientable) beamID.clone();
        beamStateNS.setAxis(Axis.Z);
        Orientable beamStateUD = (Orientable) beamID.clone();
        beamStateNS.setAxis(Axis.Y);
        Orientable beamStateEW = (Orientable) beamID.clone();
        beamStateNS.setAxis(Axis.X);
        
        for (int z = 1; z < size - 1; z++) {
            for (int x = 1; x < size - 1; x++) {
                if (x < z) {
                    setBlockStateRotated(world, beamStateNS, x, y, z, rotation, sbb);
                } else {
                    setBlockStateRotated(world, beamStateEW, x, y, z, rotation, sbb);
                }
            }
        }

        // beams going down
        for (int by = 1; by < 4; by++) {
            Ladder ladder1 = (Ladder) Bukkit.createBlockData(Material.LADDER);
            ladder1.setFacing(BlockFace.WEST);
            
            Ladder ladder2 = (Ladder) Bukkit.createBlockData(Material.LADDER);
            ladder2.setFacing(BlockFace.EAST);
            
            setBlockStateRotated(world, beamStateUD, 2, y - by, 2, rotation, sbb);
            setBlockStateRotated(world, ladder1, 2 + 1, y - by, 2, rotation, sbb);
            setBlockStateRotated(world, beamStateUD, 6, y - by, 6, rotation, sbb);
            setBlockStateRotated(world, ladder2, 6 - 1, y - by, 6, rotation, sbb);
        }

        // holes for entrance
        setBlockStateRotated(world, AIR, 3, y, 2, rotation, sbb);
        setBlockStateRotated(world, AIR, 5, y, 6, rotation, sbb);
    }


    /**
     * Make a lattice of log blocks
     */
    protected void makeSmallTimberBeams(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, Rotation rotation, int y, boolean bottom, boolean top) {
        BlockData beamID = Bukkit.createBlockData(Material.DARK_OAK_LOG);
        Orientable beamStateNS = (Orientable) beamID.clone();
        beamStateNS.setAxis(Axis.Z);
        Orientable beamStateUD = (Orientable) beamID.clone();
        beamStateNS.setAxis(Axis.Y);
        Orientable beamStateEW = (Orientable) beamID.clone();
        beamStateNS.setAxis(Axis.X);

        /*int beamMetaNS = ((this.coordBaseMode + rotation) % 2 == 0) ? 4 : 8;
        int beamMetaEW = (beamMetaNS == 4) ? 8 : 4;
        int beamMetaUD = 0;*/


        // two beams going e/w
        for (int z = 1; z < size - 1; z++) {
            setBlockStateRotated(world, beamStateEW, 2, y, z, rotation, sbb);
            setBlockStateRotated(world, beamStateEW, 6, y, z, rotation, sbb);
        }

        // a few random cross beams
        int z = pickBetweenExcluding(3, size - 3, rand, 2, 2, 6);
        for (int x = 3; x < 6; x++) {
            setBlockStateRotated(world, beamStateNS, x, y, z, rotation, sbb);
        }

        // beams going down
        int x1 = 2;
        int z1 = rand.nextBoolean() ? 2 : 6;
        int x3 = 6;
        int z3 = rand.nextBoolean() ? 2 : 6;

        for (int by = 1; by < 4; by++) {
            if (!bottom || checkPost(world, x1, y - 4, z1, rotation, sbb)) {
                
                Ladder ladder = (Ladder) Bukkit.createBlockData(Material.LADDER);
                ladder.setFacing(BlockFace.WEST);
                
                setBlockStateRotated(world, beamStateUD, x1, y - by, z1, rotation, sbb);
                setBlockStateRotated(world, ladder, x1 + 1, y - by, z1, rotation, sbb);
            }
            if (!bottom || checkPost(world, x3, y - 4, z3, rotation, sbb)) {
                
                Ladder ladder = (Ladder) Bukkit.createBlockData(Material.LADDER);
                ladder.setFacing(BlockFace.EAST);
                
                setBlockStateRotated(world, beamStateUD, x3, y - by, z3, rotation, sbb);
                setBlockStateRotated(world, ladder, x3 - 1, y - by, z3, rotation, sbb);
            }


        }

//        // do we need a beam going up?
//        if (top)
//        {
//            int ladderX = !checkPost(world, 4, y + 5, 9) ? 4 : 14;
//            int ladderZ = !checkPost(world, ladderX, y + 5, 10) ? 10 : 8;
//            int ladderMeta = (ladderZ == 10) ? 3 : 5;
//            for (int by = 1; by < 5; by++)
//            {
//                setBlockState(world, beamID, beamMetaBase + beamMetaUD, ladderX, y + by, 9, sbb);
//                setBlockState(world, Blocks.LADDER, getLadderMeta(ladderMeta), ladderX, y + by, ladderZ, sbb);
//            }
//
//            // fence thing
//            setBlockState(world, AIR, ladderX, y + 6, 9, sbb);
//            setBlockState(world, deco.fenceID, deco.fenceMeta, ladderX + 1, y + 5, ladderZ, sbb);
//            setBlockState(world, deco.fenceID, deco.fenceMeta, ladderX - 1, y + 5, ladderZ, sbb);
//            setBlockState(world, deco.fenceID, deco.fenceMeta, ladderX + 1, y + 6, ladderZ, sbb);
//            setBlockState(world, deco.fenceID, deco.fenceMeta, ladderX - 1, y + 6, ladderZ, sbb);
//        }
//        
//        if (!bottom && !top)
//        {
//            // spawners
//            int sx = pickFrom(rand, 6, 7, 11);
//            int sz = pickFrom(rand, 6, 11, 12);
//
//            TileEntityMobSpawner spawner = placeSpawnerAtCurrentPosition(world, rand, sx, y + 2, sz, TFCreatures.getSpawnerNameFor("Mini Ghast"), sbb);
//            
//            if (spawner != null)
//            {
//                ;
//            }
//            
//        }
//
//        // lamps
//        int lx = pickFrom(rand, 2, 12, 16);
//        int lz = 2 + rand.nextInt(15);
//
//        setBlockState(world, Blocks.REDSTONE_LAMP, 0, lx, y + 2, lz, sbb);
//        setBlockState(world, Blocks.LEVER, 7, lx, y + 1, lz, sbb);
    }


    /**
     * Utility function to pick a random number between two values, excluding three specified values
     */
    protected int pickBetweenExcluding(int low, int high, Random rand, int k, int l, int m) {
        int result;

        do {
            result = rand.nextInt(high - low) + low;
        }
        while (result == k || result == l || result == m);

        return result;
    }


    /**
     * Pick one of the three specified values at random
     */
    protected int pickFrom(Random rand, int i, int j, int k) {
        switch (rand.nextInt(3)) {
            case 0:
            default:
                return i;
            case 1:
                return j;
            case 2:
                return k;
        }
    }


    /**
     * Utility function for beam maze that checks if we should build a beam all the way down -- is there a valid spot to end it?
     */
    protected boolean checkPost(AsyncWorldEditor world, int x, int y, int z, Rotation rotation, StructureBoundingBox sbb) {
        int worldX = this.getXWithOffsetRotated(x, z, rotation);
        int worldY = this.getYWithOffset(y);
        int worldZ = this.getZWithOffsetRotated(x, z, rotation);
        final BlockPos vec = new BlockPos(worldX, worldY, worldZ);
        if (!sbb.isVecInside(vec)) return false;
        Material blockState = world.getBlockState(vec);
        return blockState != Material.AIR && blockState != deco.accentState.getMaterial();
    }


    /**
     * Generate walls for the tower with the distinct pattern of blocks and accent blocks
     *
     * @param rand
     */
    protected void makeEncasedWalls(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (x != minX && x != maxX && y != minY && y != maxY && z != minZ && z != maxZ) {
                        ;
                    } else {
                        // wall
                        if (((x == minY || x == maxX) && ((y == minY || y == maxY) || (z == minZ || z == maxZ)))
                                || ((y == minY || y == maxY) && ((x == minY || x == maxX) || (z == minZ || z == maxZ)))
                                || ((z == minZ || z == maxZ) && ((x == minY || x == maxX) || (y == minY || y == maxY)))) {
                            this.setBlockState(world, deco.accentState, x, y, z, sbb);
                        }
//                        else if (this.isKeyTower())
//                        {
//                            this.setBlockState(world, Blocks.DIAMOND_BLOCK, 0,  x, y, z, sbb);
//                        }
                        else {
                            StructureTFComponent.BlockSelector blocker = deco.randomBlocks;

                            blocker.selectBlocks(rand, x, y, z, true);
                            this.setBlockState(world, blocker.getBlockState(), x, y, z, sbb);


                            //this.setBlockState(world, deco.blockID, deco.blockMeta, x, y, z, sbb);
                        }
                    }
                }
            }
        }

        // corners
        this.setBlockState(world, deco.accentState, minX + 1, minY + 1, minZ, sbb);
        this.setBlockState(world, deco.accentState, minX + 1, minY + 1, maxZ, sbb);
        this.setBlockState(world, deco.accentState, maxX - 1, minY + 1, minZ, sbb);
        this.setBlockState(world, deco.accentState, maxX - 1, minY + 1, maxZ, sbb);
        this.setBlockState(world, deco.accentState, minX + 1, maxY - 1, minZ, sbb);
        this.setBlockState(world, deco.accentState, minX + 1, maxY - 1, maxZ, sbb);
        this.setBlockState(world, deco.accentState, maxX - 1, maxY - 1, minZ, sbb);
        this.setBlockState(world, deco.accentState, maxX - 1, maxY - 1, maxZ, sbb);

        this.setBlockState(world, deco.accentState, minX, minY + 1, minZ + 1, sbb);
        this.setBlockState(world, deco.accentState, minX, minY + 1, maxZ - 1, sbb);
        this.setBlockState(world, deco.accentState, maxX, minY + 1, minZ + 1, sbb);
        this.setBlockState(world, deco.accentState, maxX, minY + 1, maxZ - 1, sbb);
        this.setBlockState(world, deco.accentState, minX, maxY - 1, minZ + 1, sbb);
        this.setBlockState(world, deco.accentState, minX, maxY - 1, maxZ - 1, sbb);
        this.setBlockState(world, deco.accentState, maxX, maxY - 1, minZ + 1, sbb);
        this.setBlockState(world, deco.accentState, maxX, maxY - 1, maxZ - 1, sbb);

        this.setBlockState(world, deco.accentState, minX + 1, minY, minZ + 1, sbb);
        this.setBlockState(world, deco.accentState, minX + 1, minY, maxZ - 1, sbb);
        this.setBlockState(world, deco.accentState, maxX - 1, minY, minZ + 1, sbb);
        this.setBlockState(world, deco.accentState, maxX - 1, minY, maxZ - 1, sbb);
        this.setBlockState(world, deco.accentState, minX + 1, maxY, minZ + 1, sbb);
        this.setBlockState(world, deco.accentState, minX + 1, maxY, maxZ - 1, sbb);
        this.setBlockState(world, deco.accentState, maxX - 1, maxY, minZ + 1, sbb);
        this.setBlockState(world, deco.accentState, maxX - 1, maxY, maxZ - 1, sbb);

    }


    /**
     * Gets a random position in the specified direction that connects to a floor currently in the tower.
     */
    @Override
    public int[] getValidOpening(Random rand, Rotation direction) {
        int verticalOffset = this.size == 19 ? 5 : 4;

        // for directions 0 or 2, the wall lies along the z axis
        if (direction == Rotation.NONE || direction == Rotation.CLOCKWISE_180) {
            int rx = direction == Rotation.NONE ? size - 1 : 0;
            int rz = this.size / 2;
            int ry = this.height - verticalOffset;

            return new int[]{rx, ry, rz};
        }

        // for directions 1 or 3, the wall lies along the x axis
        if (direction == Rotation.CLOCKWISE_90 || direction == Rotation.COUNTERCLOCKWISE_90) {
            int rx = this.size / 2;
            int rz = direction == Rotation.CLOCKWISE_90 ? size - 1 : 0;
            int ry = this.height - verticalOffset;

            return new int[]{rx, ry, rz};
        }


        return new int[]{0, 0, 0};
    }

    /**
     * Add an opening to the outside (or another tower) in the specified direction.
     */
    @Override
    public void addOpening(int dx, int dy, int dz, Rotation direction) {
        this.addOpening(dx, dy, dz, direction, EnumDarkTowerDoor.VANISHING);
    }


    /**
     * Add an opening where we keep track of the kind of opening
     * TODO: we could make a type of object that stores all these values
     * TODO: also use an Enum for the kinds of openings?
     */
    protected void addOpening(int dx, int dy, int dz, Rotation direction, EnumDarkTowerDoor type) {
        super.addOpening(dx, dy, dz, direction);
        this.openingTypes.add(openings.indexOf(new BlockPos(dx, dy, dz)), type);
    }

    /**
     * Iterate through the openings on our list and add them to the tower
     */
    @Override
    protected void makeOpenings(AsyncWorldEditor world, StructureBoundingBox sbb) {
        for (int i = 0; i < openings.size(); i++) {
            BlockPos doorCoords = openings.get(i);

            EnumDarkTowerDoor doorType;
            if (openingTypes.size() > i) {
                doorType = openingTypes.get(i);
            } else {
                doorType = EnumDarkTowerDoor.VANISHING;
            }

            switch (doorType) {
                case VANISHING:
                default:
                    makeDoorOpening(world, doorCoords.getX(), doorCoords.getY(), doorCoords.getZ(), sbb);
                    break;
                case REAPPEARING:
                    makeReappearingDoorOpening(world, doorCoords.getX(), doorCoords.getY(), doorCoords.getZ(), sbb);
                    break;
                case LOCKED:
                    makeLockedDoorOpening(world, doorCoords.getX(), doorCoords.getY(), doorCoords.getZ(), sbb);
                    break;
            }

        }
    }

    /**
     * Make an opening in this tower for a door.
     */
    @Override
    protected void makeDoorOpening(AsyncWorldEditor world, int dx, int dy, int dz, StructureBoundingBox sbb) {
//        // nullify sky light
//        nullifySkyLightAtCurrentPosition(world, dx - 3, dy - 1, dz - 3, dx + 3, dy + 3, dz + 3);
//
//        final IBlockState inactiveVanish = TFBlocks.tower_device.getDefaultState().withProperty(BlockTFTowerDevice.VARIANT, TowerDeviceVariant.VANISH_INACTIVE);
//
//        // clear the door
//        if (dx == 0 || dx == size - 1) {
//            this.fillWithBlocks(world, sbb, dx, dy - 1, dz - 2, dx, dy + 3, dz + 2, deco.accentState, AIR, false);
//            this.fillWithBlocks(world, sbb, dx, dy, dz - 1, dx, dy + 2, dz + 1, inactiveVanish, AIR, false);
//        }
//        if (dz == 0 || dz == size - 1) {
//            this.fillWithBlocks(world, sbb, dx - 2, dy - 1, dz, dx + 2, dy + 3, dz, deco.accentState, AIR, false);
//            this.fillWithBlocks(world, sbb, dx - 1, dy, dz, dx + 1, dy + 2, dz, inactiveVanish, AIR, false);
//        }
    }

    /**
     * Make a 3x3 tower door that reappears
     */
    protected void makeReappearingDoorOpening(AsyncWorldEditor world, int dx, int dy, int dz, StructureBoundingBox sbb) {
//        // nullify sky light
//        nullifySkyLightAtCurrentPosition(world, dx - 3, dy - 1, dz - 3, dx + 3, dy + 3, dz + 3);
//
//        final IBlockState inactiveReappearing = TFBlocks.tower_device.getDefaultState().withProperty(BlockTFTowerDevice.VARIANT, TowerDeviceVariant.REAPPEARING_INACTIVE);
//
//        // clear the door
//        if (dx == 0 || dx == size - 1) {
//            this.fillWithBlocks(world, sbb, dx, dy - 1, dz - 2, dx, dy + 3, dz + 2, deco.accentState, AIR, false);
//            this.fillWithBlocks(world, sbb, dx, dy, dz - 1, dx, dy + 2, dz + 1, inactiveReappearing, AIR, false);
//        }
//        if (dz == 0 || dz == size - 1) {
//            this.fillWithBlocks(world, sbb, dx - 2, dy - 1, dz, dx + 2, dy + 3, dz, deco.accentState, AIR, false);
//            this.fillWithBlocks(world, sbb, dx - 1, dy, dz, dx + 1, dy + 2, dz, inactiveReappearing, AIR, false);
//        }
    }


    /**
     * Make a 3x3 tower door that is locked
     */
    protected void makeLockedDoorOpening(AsyncWorldEditor world, int dx, int dy, int dz, StructureBoundingBox sbb) {
//        // nullify sky light
//        nullifySkyLightAtCurrentPosition(world, dx - 3, dy - 1, dz - 3, dx + 3, dy + 3, dz + 3);
//
//        // clear the door
//        final IBlockState lockedVanish = TFBlocks.tower_device.getDefaultState().withProperty(BlockTFTowerDevice.VARIANT, TowerDeviceVariant.VANISH_LOCKED);
//        final IBlockState inactiveVanish = TFBlocks.tower_device.getDefaultState().withProperty(BlockTFTowerDevice.VARIANT, TowerDeviceVariant.VANISH_INACTIVE);
//
//        if (dx == 0 || dx == size - 1) {
//            this.fillWithBlocks(world, sbb, dx, dy - 1, dz - 2, dx, dy + 3, dz + 2, deco.accentState, AIR, false);
//            this.fillWithBlocks(world, sbb, dx, dy, dz - 1, dx, dy + 2, dz + 1, inactiveVanish, AIR, false);
//            this.setBlockState(world, lockedVanish, dx, dy + 0, dz + 1, sbb);
//            this.setBlockState(world, lockedVanish, dx, dy + 0, dz - 1, sbb);
//            this.setBlockState(world, lockedVanish, dx, dy + 2, dz + 1, sbb);
//            this.setBlockState(world, lockedVanish, dx, dy + 2, dz - 1, sbb);
//        }
//        if (dz == 0 || dz == size - 1) {
//            this.fillWithBlocks(world, sbb, dx - 2, dy - 1, dz, dx + 2, dy + 3, dz, deco.accentState, AIR, false);
//            this.fillWithBlocks(world, sbb, dx - 1, dy, dz, dx + 1, dy + 2, dz, inactiveVanish, AIR, false);
//            this.setBlockState(world, lockedVanish, dx + 1, dy + 0, dz, sbb);
//            this.setBlockState(world, lockedVanish, dx - 1, dy + 0, dz, sbb);
//            this.setBlockState(world, lockedVanish, dx + 1, dy + 2, dz, sbb);
//            this.setBlockState(world, lockedVanish, dx - 1, dy + 2, dz, sbb);
//        }
    }


    /**
     * Returns true if this tower has only one exit.
     * <p>
     * TODO: is this really the best way?
     */
    @Override
    public boolean isDeadEnd() {
        // we have to modify this to ignore door type 2 since that leads to balconies
        int nonBalconies = 0;

        for (EnumDarkTowerDoor type : openingTypes) {
            if (type != EnumDarkTowerDoor.REAPPEARING) {
                nonBalconies++;
            }
        }

        return nonBalconies <= 1;
    }


    public boolean isKeyTower() {
        return keyTower;
    }


    public void setKeyTower(boolean keyTower) {
        this.keyTower = keyTower;
    }
}
