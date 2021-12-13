/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge_sandbox.com.someguyssoftware.dungeons2.builder;

import forge_sandbox.BlockPos;
import forge_sandbox.com.someguyssoftware.gottschcore.positional.ICoords;

/**
 *
 * @author
 */
public class WorldInfo {
    private static final int MAX_HEIGHT = 256;
    private static final int MIN_HEIGHT = 1;
    
    public static boolean isValidY(final ICoords coords) {
        return isValidY(coords.toPos());
    }
    
    private static boolean isValidY(final BlockPos blockPos) {
        if ((blockPos.getY() < MIN_HEIGHT || blockPos.getY() > MAX_HEIGHT)) {
            return false;
        }
        return true;
    }

}
