/**
 * 
 */
package forge_sandbox.com.someguyssoftware.dungeonsengine.builder;

import forge_sandbox.com.someguyssoftware.dungeonsengine.model.Boundary;
import forge_sandbox.com.someguyssoftware.gottschcore.positional.Coords;
import forge_sandbox.com.someguyssoftware.gottschcore.positional.ICoords;
import forge_sandbox.com.someguyssoftware.gottschcore.random.RandomHelper;

import java.util.Random;

/**
 * @author Mark Gottschling on Jan 10, 2019
 *
 */
public class GenUtil {

	/**
	 * 
	 */
	private GenUtil() {}

	/**
	 * 
	 * @param random
	 * @param boundary
	 * @return
	 */
	public static ICoords randomizeCoords(Random random, Boundary boundary) {
		int x = RandomHelper.randomInt(random, 0, (int) (boundary.getMaxCoords().getX() - boundary.getMinCoords().getX()));
		int z = RandomHelper.randomInt(random, 0, (int) (boundary.getMaxCoords().getZ() - boundary.getMinCoords().getZ()));
		return new Coords((int)boundary.getMinCoords().getX(), 0, (int)boundary.getMinCoords().getZ()).add(x, 0, z);
	}
}
