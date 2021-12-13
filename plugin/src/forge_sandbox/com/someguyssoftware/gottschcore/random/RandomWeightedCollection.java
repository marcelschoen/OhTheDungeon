/**
 * 
 */
package forge_sandbox.com.someguyssoftware.gottschcore.random;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

// TODO review if all ints need to change to double
/**
 * Similar to RandomProbabilityCollection, but removes the requirement
 *  that collection items implement the IRandomProbabilityItem interface.
 * This type of weighted collection must use positive integers.
 * @author Mark Gottschling on Jan 21, 2018
 *
 */
public class RandomWeightedCollection<T> {
	private final NavigableMap<Integer, T> map = new TreeMap<Integer, T>();
	private Random random;
	private int total = 0;
	
	/**
	 * 
	 */
	public RandomWeightedCollection() {
		setRandom(new Random());
	}
	
	/**
	 * 
	 * @param random
	 */
	public RandomWeightedCollection(Random random) {
		setRandom(random);
	}
	
	/**
	 * Add an item with a given weight.
	 * @param weight
	 * @param item
	 */
	public void add(int weight, T item) {
		if (weight <= 0) return;
		total += weight;
		map.put(total, item);
	}
	
	/**
	 * Get the next random value.
	 * @return
	 */
	public T next() {
		if (map.isEmpty() || total < 1) return null;
		int value = getRandom().nextInt(total);
		return map.ceilingEntry(value).getValue();
	}
	
	/**
	 * 
	 */
	public void clear() {
		map.clear();
		setTotal(0);
	}
	
	/**
	 * 
	 * @return
	 */
	public int size() {
		if (map == null) return 0;
		return map.size();
	}
	
	/**
	 * @return the random
	 */
	public Random getRandom() {
		return random;
	}

	/**
	 * @param random the random to set
	 */
	public void setRandom(Random random) {
		this.random = random;
	}
	
	/**
	 * 
	 * @return
	 */
	private int getTotal() {
		return this.total;
	}
	
	/**
	 * 
	 * @param total
	 */
	private void setTotal(int total) {
		this.total = total;
	}
	
	public Map<Integer, T> getMap() {
		return map;
	}
}
