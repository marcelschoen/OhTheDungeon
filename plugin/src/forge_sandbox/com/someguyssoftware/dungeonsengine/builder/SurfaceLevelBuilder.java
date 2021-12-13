/**
 * 
 */
package forge_sandbox.com.someguyssoftware.dungeonsengine.builder;

import otd.lib.async.AsyncWorldEditor;

import java.util.Random;

/**
 * @author Mark Gottschling on Jan 13, 2019
 *
 */
public class SurfaceLevelBuilder extends LevelBuilder {

	/**
	 * 
	 * @param world
	 */
	public SurfaceLevelBuilder(AsyncWorldEditor world) {
		super(world);
	}

	/**
	 * 
	 * @param world
	 * @param random
	 */
	public SurfaceLevelBuilder(AsyncWorldEditor world, Random random) {
		super(world, random);
	}
}
