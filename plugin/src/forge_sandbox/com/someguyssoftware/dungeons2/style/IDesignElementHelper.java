/**
 * 
 */
package forge_sandbox.com.someguyssoftware.dungeons2.style;

import forge_sandbox.com.someguyssoftware.dungeons2.model.Room;

/**
 * @author Mark Gottschling on Sep 3, 2016
 *
 */
@Deprecated
public interface IDesignElementHelper {

    default public DesignElement getDesignElement(int x, int y, int z, Room room, Layout layout){
        return null;
    }
    default public DesignElement getDesignElement(int x, int y, int z, Room room){
        return null;
    }

}
