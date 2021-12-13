package forge_sandbox.com.someguyssoftware.dungeonsengine.model;

import forge_sandbox.com.someguyssoftware.gottschcore.positional.ICoords;

import java.util.List;

public interface ISpace {

	/**
	 * @return the coords
	 */
	ICoords getCoords();

	/**
	 * @param coords the coords to set
	 */
	void setCoords(ICoords coords);

	/**
	 * @return the depth
	 */
	int getDepth();

	/**
	 * @param depth the depth to set
	 */
	void setDepth(int depth);

	/**
	 * @return the width
	 */
	int getWidth();

	/**
	 * @param width the width to set
	 */
	void setWidth(int width);

	/**
	 * @return the height
	 */
	int getHeight();

	/**
	 * @param height the height to set
	 */
	void setHeight(int height);

	/**
	 * @return the exits
	 */
	List<IExit> getExits();

	/**
	 * @param exits the exits to set
	 */
	void setExits(List<IExit> exits);

}