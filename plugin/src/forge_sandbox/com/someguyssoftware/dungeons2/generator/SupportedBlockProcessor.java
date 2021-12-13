/**
 * 
 */
package forge_sandbox.com.someguyssoftware.dungeons2.generator;

import forge_sandbox.BlockPos;
import forge_sandbox.com.someguyssoftware.dungeons2.generator.blockprovider.IDungeonsBlockProvider;
import forge_sandbox.com.someguyssoftware.dungeons2.model.Room;
import forge_sandbox.com.someguyssoftware.dungeons2.style.DesignElement;
import forge_sandbox.com.someguyssoftware.gottschcore.positional.ICoords;
import org.bukkit.Material;
import otd.lib.async.AsyncWorldEditor;

/**
 * @author Mark Gottschling on Aug 1, 2016
 *
 */
public class SupportedBlockProcessor {
    // support matrix
    private ISupportedBlock[][][] supportMatrix;
    private IDungeonsBlockProvider blockProvider;
    private Room room;
    
    /**
     * 
     * @param xSize
     * @param ySize
     * @param zSize
     */
//    public SupportBlockProcessor(int xSize, int ySize, int zSize) {
//        supportMatrix = new SupportedBlock[xSize][ySize][zSize];
//    }

    /**
     * 
     * @param blockProvider
     * @param room
     */
    public SupportedBlockProcessor(IDungeonsBlockProvider blockProvider, Room room) {
        this.blockProvider = blockProvider;
        this.room = room;
        supportMatrix = new SupportedBlock[room.getHeight()][room.getDepth()][room.getWidth()];
    }
    
    /**
     * 
     * @param world
     * @param indexCoords
     * @param worldCoords
     * @param elem
     * @return
     */
    public int applySupportRulesPass1(final AsyncWorldEditor world, final ICoords indexCoords, 
            final ICoords worldCoords, final DesignElement elem) {
        
        int amount = 0;
        
        // NOTE every element is a consumer except AIR, NONE, 
        // if the block does not require support (ie AIR), return full amount
        if (elem == DesignElement.AIR || elem == DesignElement.SURFACE_AIR ||
                elem == DesignElement.NONE || elem.getFamily() == DesignElement.SURFACE_AIR) {
            return 100;
        }
        
        /*
         *  process each support direction that a block consumes
         */

        // UP
        // get the world block from below (y-1)
        amount += getYNegSupportAmount(world, indexCoords, worldCoords, elem);
        if (amount >= 100) {
            return 100;
        }

        // SIDES
        amount += getXNegSupportAmount(world, indexCoords, worldCoords, elem);
//                Plan.log.debug(String.format("Total %d support horizontal HORIZTONAL.", amount));
        if (amount >= 100) {
            return 100;
        }
        
        // get the support amount from behind z (z-1)
        amount += getZNegSupportAmount(world, indexCoords, worldCoords, elem);
//                Plan.log.debug(String.format("Total %d support after BEHIND.", amount));
//                Plan.log.info("Total Amount of support coming with the HORIZONTAL(ZNeg) support: " + amount);
        if (amount >= 100) {
            return 100;
        }
        // DOWN FOR SPECIAL CASES
        return amount;
    }
    
    /**
     * 
     * @param world
     * @param indexCoords
     * @param worldCoords
     * @return
     */
    public int applySupportRulesPass2(final AsyncWorldEditor world, final ICoords indexCoords, final ICoords worldCoords, final DesignElement elem) {
        
        int amount = 0;
        
        // NOTE every element is a consumer except AIR
        // if the block does not require support (ie AIR), return full amount
        if (elem == DesignElement.AIR) {
            return 100;
        }
        
        // NOTE only HORIZONTAL support is calculated in Pass2 as VERTICAL was already calculated in Pass1
        
        /*
         *  process each support direction that a block consumes
         */        
        // get the support amount from behind x (x+1)
        amount += getXPosSupportAmount(world, indexCoords, worldCoords, elem);
        //                Plan.log.info("Pass2 XPos Total Amount: " + amount);
        if (amount >= 100) {
            return 100;
        }

        // get the support amount from behind z (z+1)
        amount += getZPosSupportAmount(world, indexCoords, worldCoords, elem);
        //                Plan.log.info("Pass2 ZPos Total Amount: " + amount);
        if (amount >= 100) {
            return 100;
        }        
        return amount;
    }

    /**
     * 
     * @param world
     * @param indexCoords
     * @param worldCoords
     * @param elem
     * @return
     */
    public int getYNegSupportAmount(final AsyncWorldEditor world, final ICoords indexCoords, final ICoords worldCoords, DesignElement elem) {
        
        int amount = 0;
        
        // select the block/blockState under the current worldCoords
        BlockPos supportPos = new BlockPos(worldCoords.getX(), worldCoords.getY()-1, worldCoords.getZ());
        Material worldBlockState = world.getBlockState(supportPos);
        
        // there exists a block and it is a type that can be used as a supporting block
        if (worldBlockState != Material.AIR && isSupportingBlock(world, supportPos)) {
//            Plan.log.info("YNeg World block " + worldBlockState.getBlock().getUnlocalizedName() + " is a supporting type block.");
            if (indexCoords.getY() == 0) {
                // since the floor (y ==0) is being processed AND there is a supporting world block underneath, return 100 support for this location
                return 100;
            }
            else {
                /*
                 *  Check if the worldBlockState was there originally or placed there by plans.
                 *  This can be achieved by looking at the supportedBlocks matrix - if an entry is null,
                 *  that indicates that that block was replaced/set by the processor - either it was a null_block or other preservation rule. 
                 */
                ISupportedBlock supportBlock = this.supportMatrix[indexCoords.getY()-1][indexCoords.getZ()][indexCoords.getX()];
                if (supportBlock == null) {
//                    Plan.log.info("SupportedBlocks Matrix did not have entry - return 100.");
                    return 100;
                }

                // get the design element @ the support position
                DesignElement supportElement = blockProvider.getDesignElement(worldCoords.add(0, -1, 0), room, room.getLayout());

                // get the support amount from the design element that is "behind" the index coords
                amount += supportElement.getVerticalSupport();
            }
        }
        return amount;
    }
    
    /**
     * 
     * @param world
     * @param indexCoords
     * @param worldCoords
     * @param elem
     * @return
     */
    public int getXNegSupportAmount(final AsyncWorldEditor world, final ICoords indexCoords, final ICoords worldCoords, DesignElement elem) {
        int amount = 0;
        BlockPos supportPos = new BlockPos(worldCoords.getX()-1, worldCoords.getY(), worldCoords.getZ());
        Material worldBlockState = world.getBlockState(supportPos);


        if (worldBlockState != Material.AIR && isSupportingBlock(world, supportPos)) {
//            Dungeons2.log.info("XNeg World block " + worldBlockState.getBlock().getUnlocalizedName() + " is a supporting type block.");
            if (indexCoords.getX() == 0) {
                return 100;
            }
            else {
                ISupportedBlock supportBlock = this.supportMatrix[indexCoords.getY()][indexCoords.getZ()][indexCoords.getX()-1];
                if (supportBlock == null) {
                    return 100;
                }
                
                // get the design element @ the support position
                DesignElement supportElement = blockProvider.getDesignElement(worldCoords.add(-1, 0, 0), room, room.getLayout());
                                
                // get the support amount from the design element that is "behind" the index coords
                if (supportElement == DesignElement.FACADE_SUPPORT) {
                    if (elem.getFamily() == DesignElement.FACADE) {
//                        Dungeons2.log.debug("Getting FACADE_SUPPORT amount for FACADE:" + supportElement.getHorizontalSupport());
//                        Dungeons2.log.debug("Type of element:" + elem.getName());
                        amount += supportElement.getHorizontalSupport();
                    }
                    else {
//                        Dungeons2.log.debug("Getting FACADE_SUPPORT amount for NON-FACADE:" + supportElement.getFamily().getHorizontalSupport());
//                        Dungeons2.log.debug("Type of element:" + elem.getName());
                        amount += supportElement.getFamily().getHorizontalSupport();
                    }
                }
                else {
//                    Dungeons2.log.debug("Getting regular support amount for block:" + supportElement.getHorizontalSupport());
                    amount += supportElement.getHorizontalSupport();
                }
            }
        }        
        return amount;
    }
    
    /**
     * 
     * @param world
     * @param indexCoords
     * @param worldCoords
     * @param elem
     * @return
     */
    public int getZNegSupportAmount(final AsyncWorldEditor world, final ICoords indexCoords, final ICoords worldCoords, DesignElement elem) {
        int amount = 0;
        BlockPos supportPos = new BlockPos(worldCoords.getX(), worldCoords.getY(), worldCoords.getZ()-1);
        Material worldBlockState = world.getBlockState(supportPos);

        if (worldBlockState != Material.AIR && isSupportingBlock(world, supportPos)) {
            if (indexCoords.getZ() == 0) {
                return 100;
            }
            else {
                ISupportedBlock supportBlock = this.supportMatrix[indexCoords.getY()][indexCoords.getZ()-1][indexCoords.getX()];
                if (supportBlock == null) {
                    return 100;
                }
                
                // get the design element @ the support position
                DesignElement supportElement = blockProvider.getDesignElement(worldCoords.add(0, 0, -1), room, room.getLayout());

                // get the support amount from the design element that is "behind" the index coords
                if (supportElement == DesignElement.FACADE_SUPPORT && elem.getFamily() != DesignElement.FACADE) {
                    // get the base element support value (true for most elements)
                    amount += supportElement.getFamily().getHorizontalSupport();
                }
                else {
                    amount += supportElement.getHorizontalSupport();
                }
            }
        }        
        return amount;
    }
    
    /**
     * 
     * @param world
     * @param indexCoords
     * @param worldCoords
     * @param elem
     * @return
     */
    public int getXPosSupportAmount(final AsyncWorldEditor world, final ICoords indexCoords, final ICoords worldCoords, DesignElement elem) {
        int amount = 0;
        BlockPos supportPos = new BlockPos(worldCoords.getX()+1, worldCoords.getY(), worldCoords.getZ());
        Material worldBlockState = world.getBlockState(supportPos);

        if (worldBlockState != Material.AIR && isSupportingBlock(world, supportPos)) {
//            Plan.log.info("XPos World block " + worldBlockState.getBlock().getUnlocalizedName() + " is a supporting type block.");
            if (indexCoords.getX() == getSupportMatrix()[0][0].length-1) {
                return 100;
            }
            else {
                ISupportedBlock supportBlock = getSupportMatrix()[indexCoords.getY()][indexCoords.getZ()][indexCoords.getX()+1];
                if (supportBlock == null) {
                    return 100;
                }
                
                // get the design element @ the support position
                DesignElement supportElement = blockProvider.getDesignElement(worldCoords.add(1, 0, 0), room, room.getLayout());

                // get the support amount from the design element that is "behind" the index coords
                if (supportElement == DesignElement.FACADE_SUPPORT && elem.getFamily() != DesignElement.FACADE) {
                    // get the base element support value (true for most elements)
                    amount += supportElement.getFamily().getHorizontalSupport();
                }
                else {
                    amount += supportElement.getHorizontalSupport();
                }
            }
        }
        return amount;
    }
    
    /**
     * 
     * @param world
     * @param indexCoords
     * @param worldCoords
     * @param elem
     * @return
     */
    public int getZPosSupportAmount(final AsyncWorldEditor world, final ICoords indexCoords, final ICoords worldCoords, DesignElement elem) {        
        int amount = 0;        
        BlockPos supportPos = new BlockPos(worldCoords.getX(), worldCoords.getY(), worldCoords.getZ()+1);
        Material worldBlockState = world.getBlockState(supportPos);

        if (worldBlockState != Material.AIR && isSupportingBlock(world, supportPos)) {
            if (indexCoords.getZ() == getSupportMatrix()[0].length-1) {
                return 100;
            }
            else {
                ISupportedBlock supportBlock = getSupportMatrix()[indexCoords.getY()][indexCoords.getZ()+1][indexCoords.getX()];
                if (supportBlock == null) {
                    return 100;
                }
                // get the design element @ the support position
                DesignElement supportElement = blockProvider.getDesignElement(worldCoords.add(0, 0, 1), room, room.getLayout());

                // get the support amount from the design element that is "behind" the index coords
                if (supportElement == DesignElement.FACADE_SUPPORT && elem.getFamily() != DesignElement.FACADE) {
                    // get the base element support value (true for most elements)
                    amount += supportElement.getFamily().getHorizontalSupport();
                }
                else {
                    amount += supportElement.getHorizontalSupport();
                }
            }
        }        
        return amount;
    }
    
    /**
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean isSupportingBlock(final AsyncWorldEditor world, final int x, final int y, final int z) {
        return isSupportingBlock(world, new BlockPos(x, y, z));
    }
    
    /**
     * 
     * @param world
     * @param coords
     * @return
     */
    public boolean isSupportingBlock(final AsyncWorldEditor world, final ICoords coords) {
        return isSupportingBlock(world, new BlockPos(coords.getX(), coords.getY(), coords.getZ()));
    }
    
    /**
     * Determines if the block is that type that can support other blocks. ie. a solid, non-replacable block
     * @param world
     * @param blockPos
     * @return
     */
    public boolean isSupportingBlock(final AsyncWorldEditor world, final BlockPos blockPos) {
        Material block = world.getBlockState(blockPos);
        
                return block.isSolid();
    }
    
    /**
     * @return the supportMatrix
     */
    public ISupportedBlock[][][] getSupportMatrix() {
        return supportMatrix;
    }

    /**
     * @param supportMatrix the supportMatrix to set
     */
    public void setSupportMatrix(ISupportedBlock[][][] supportMatrix) {
        this.supportMatrix = supportMatrix;
    }

    /**
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * @return the blockProvider
     */
    public IDungeonsBlockProvider getBlockProvider() {
        return blockProvider;
    }

    /**
     * @param blockProvider the blockProvider to set
     */
    public void setBlockProvider(IDungeonsBlockProvider blockProvider) {
        this.blockProvider = blockProvider;
    }
}
