/**
 * 
 */
package forge_sandbox.com.someguyssoftware.dungeons2.generator.strategy;

import forge_sandbox.com.someguyssoftware.dungeons2.model.Room;

import java.util.List;

/**
 * @author Mark Gottschling on Aug 30, 2016
 *
 */
public interface IHallwayGenerationStrategy extends IRoomGenerationStrategy {
    public List<Room> getHallways();
    public void setHallways(List<Room> hallways);
}
