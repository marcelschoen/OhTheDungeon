package forge_sandbox.jaredbgreat.dldungeons;

import java.util.HashSet;
import java.util.Random;

/**
 * The class responcible for determine where dungeons generate.  More 
 * specifically, it wil(IWorldGenerator) l determine if a chunk being generated should be 
 * the technical (not geometric) center of a dungeon.
 * 
 * @author Jared Blackburn
 *
 */
public class GenerationHandler {	
	private static int frequency;
	private static int factor = 6;
	private static int minXZ;
	private static Random mrand;
	private static HashSet<Integer> dimensions;
		
	/**
	 * Sets the frequency scale and converts into the 
	 * width (and height) of the areas for which dungeons
	 * are generated.
	 * 
	 * @param freqScale
	 */
	public static void setFrequency(int freqScale) {
		if((freqScale % 2) == 0) factor = 2;
		else factor = 3;
		System.out.println("freqScale = " + freqScale);
		factor = (1 << (freqScale / 2)) * factor;
		System.out.println("factor = " + factor);
	}
	
	
	/**
	 * Sets the minimum number of chunks from spawn a dungeon center must be.
	 * 
	 * @param value
	 */
	public static void setMinXZ(int value) {
		minXZ = value;		
	}
	
	
	/**
	 * Sets list of dimension id's to check for dungeons being allowed.
	 * 
	 * @param value
	 */
	public static void setDimensions(int[] value) {
		dimensions = new HashSet<>();
		for(int i = 0; i < value.length; i++) {
			dimensions.add(value[i]);
		}
	}
	
	
	/**
	 * Add a dimension id to the list of dimension to consider when 
	 * check if a dungeon is allowed.  (What this means can vary.)
	 * 
	 * @param value
	 */
	public static void addDimension(int value) {
		dimensions.add(value);
	}
	
	
	/**
	 * Remove a dimension id from the list of those to consider when potentially 
	 * placing a dungeons.  (What this means can vary.)
	 * 
	 * @param value
	 */
	public static void subDimension(int value) {
		dimensions.remove(value);
	}
}
