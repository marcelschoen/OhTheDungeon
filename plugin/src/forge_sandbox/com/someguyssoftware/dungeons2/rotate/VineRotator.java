/**
 * 
 */
package forge_sandbox.com.someguyssoftware.dungeons2.rotate;


import forge_sandbox.com.someguyssoftware.gottschcore.enums.Direction;
import forge_sandbox.com.someguyssoftware.gottschcore.enums.Rotate;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Mark Gottschling on Sep 9, 2016
 *
 */
public class VineRotator implements IRotator {    
    /**
     * 
     * @param blockState
     * @param direction the direction the blockState should face.
     * @return
     */
    @Override
    public BlockData rotate(BlockData blockState, Direction direction) {
        MultipleFacing rotatedState = (MultipleFacing) blockState.clone();
        switch(direction) {
        case NORTH:
                        rotatedState.setFace(BlockFace.NORTH, true);
            break;
        case EAST:
                        rotatedState.setFace(BlockFace.EAST, true);
            break;
        case SOUTH:
                        rotatedState.setFace(BlockFace.SOUTH, true);
            break;
        case WEST:
                        rotatedState.setFace(BlockFace.WEST, true);
            break;
            default: break;
        }
        return rotatedState;
    }
    
    @Override
    public BlockData rotate(BlockData blockState, Rotate rotate) {
        MultipleFacing rotatedMeta = (MultipleFacing) blockState.clone();
//        EnumFacing base = blockState.getValue(FACING);
        Set<BlockFace> base = new HashSet<>();
        if (rotatedMeta.hasFace(BlockFace.NORTH)) base.add(BlockFace.NORTH);
        else if (rotatedMeta.hasFace(BlockFace.SOUTH)) base.add(BlockFace.SOUTH);
        else if (rotatedMeta.hasFace(BlockFace.EAST)) base.add(BlockFace.EAST);
        else if (rotatedMeta.hasFace(BlockFace.WEST)) base.add(BlockFace.WEST);
        
        // switch on the rotation
        switch (rotate) {
        case ROTATE_90:
            // switch on the current facing direction
            if (base.contains(BlockFace.NORTH)) 
                rotatedMeta.setFace(BlockFace.EAST, true);
            else 
                rotatedMeta.setFace(BlockFace.EAST, false);
            if (base.contains(BlockFace.EAST)) 
                rotatedMeta.setFace(BlockFace.SOUTH, true);
            else 
                rotatedMeta.setFace(BlockFace.SOUTH, false);
            if (base.contains(BlockFace.SOUTH)) 
                rotatedMeta.setFace(BlockFace.WEST, true);
            else 
                rotatedMeta.setFace(BlockFace.WEST, false);
            if (base.contains(BlockFace.WEST)) 
                rotatedMeta.setFace(BlockFace.NORTH, true);
            else 
                rotatedMeta.setFace(BlockFace.NORTH, false);
                        
            break;
        case ROTATE_180:
            if (base.contains(BlockFace.NORTH)) 
                rotatedMeta.setFace(BlockFace.SOUTH, true);
            else 
                rotatedMeta.setFace(BlockFace.SOUTH, false);
            if (base.contains(BlockFace.EAST)) 
                rotatedMeta.setFace(BlockFace.WEST, true);
            else 
                rotatedMeta.setFace(BlockFace.WEST, false);
            if (base.contains(BlockFace.SOUTH)) 
                rotatedMeta.setFace(BlockFace.NORTH, true);
            else 
                rotatedMeta.setFace(BlockFace.NORTH, false);
            if (base.contains(BlockFace.WEST)) 
                rotatedMeta.setFace(BlockFace.EAST, true);
            else 
                rotatedMeta.setFace(BlockFace.EAST, false);
                        
            break;
        case ROTATE_270:
            if (base.contains(BlockFace.NORTH)) 
                rotatedMeta.setFace(BlockFace.WEST, true);
            else 
                rotatedMeta.setFace(BlockFace.WEST, false);
            if (base.contains(BlockFace.EAST)) 
                rotatedMeta.setFace(BlockFace.NORTH, true);
            else 
                rotatedMeta.setFace(BlockFace.NORTH, false);
            if (base.contains(BlockFace.SOUTH)) 
                rotatedMeta.setFace(BlockFace.EAST, true);
            else 
                rotatedMeta.setFace(BlockFace.EAST, false);
            if (base.contains(BlockFace.WEST)) 
                rotatedMeta.setFace(BlockFace.SOUTH, true);
            else 
                rotatedMeta.setFace(BlockFace.SOUTH, false);
                        
            break;
        default: break;
        }        
        return rotatedMeta;        
    }
}
