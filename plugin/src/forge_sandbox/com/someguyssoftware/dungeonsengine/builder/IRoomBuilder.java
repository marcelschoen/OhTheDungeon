package forge_sandbox.com.someguyssoftware.dungeonsengine.builder;

import forge_sandbox.com.someguyssoftware.dungeonsengine.model.Boundary;
import forge_sandbox.com.someguyssoftware.dungeonsengine.model.IRoom;
import forge_sandbox.com.someguyssoftware.gottschcore.positional.ICoords;

import java.util.List;

public interface IRoomBuilder {
	
	public Boundary getBoundary();
	public void setBoundary(Boundary boundary);
	
	/*
	 * build a generic space
	 */
	IRoom buildRoom(ICoords startPoint, IRoom spaceIn);
	
	/*
	 * build a 'planned' space, using existing spaces to determine where and if it can be placed within the boundary
	 */
	IRoom buildPlannedRoom(ICoords startPoint, List<IRoom> predefinedSpaces);
	
	IRoom buildStartRoom(ICoords startPoint);
	
	IRoom buildEndRoom(ICoords startPoint, List<IRoom> predefinedSpaces);
	
	/**
	 * 
	 * @param random
	 * @param startPoint
	 * @param config
	 * @param predefinedSpaces
	 * @return
	 */
	IRoom buildTreasureRoom(ICoords startPoint, List<IRoom> predefinedSpaces);
	
}