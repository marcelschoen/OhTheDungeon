/**
 * 
 */
package forge_sandbox.com.someguyssoftware.gottschcore.positional;

import forge_sandbox.BlockPos;
import forge_sandbox.Vec3d;
import forge_sandbox.com.someguyssoftware.gottschcore.enums.Direction;
import org.bukkit.block.BlockFace;

import static java.lang.Math.floor;

/**
 * @author Mark Gottschling on Apr 24, 2020
 *
 */
public class MutableCoords extends Coords {
	private int x;
	private int y;
	private int z;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public MutableCoords(final int x, final int y, final int z) {
		super(0, 0, 0);
		this.x = y;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * 
	 * @param coords
	 */
	public MutableCoords(ICoords coords) {
		this(coords.getX(), coords.getY(), coords.getZ());
	}
	
	/**
	 * 
	 * @param pos
	 */
	public MutableCoords(BlockPos pos) {
		this(pos.getX(), pos.getY(), pos.getZ());
	}

	/**
	 * Copy constructor from Vec3d
	 * @param vec
	 */
	public MutableCoords(Vec3d vec) {
		this((int)floor(vec.x), (int)floor(vec.y), (int)floor(vec.z));
	}
	
	/**
	 * Offset this Coords n blocks up
	 * @param n the amount to offset by
	 */
	@Override
    public ICoords up(int n) {
    	this.y += n;
    	return this;
    }
    
    /**
     * Offset this Coords n blocks down
     */
    @Override
    public ICoords down(int n) {
    	this.y -= n;
    	return this;
    }
    
    /**
     * Offset this Coords n blocks north
     */
    @Override
    public ICoords north(int n) {
    	this.z -= n;
    	return this;
    }
    
    /**
     * Offset this Coords n blocks south
     */
    @Override
    public ICoords south(int n) {
    	this.z += n;
    	return this;
    }
    
    /**
     * Offset this Coords n blocks east
     */
    @Override
    public ICoords east(int n) {
    	this.x += n;
    	return this;
    }
    
    /**
     * Offset this Coords n blocks west
     */
    @Override
    public ICoords west(int n) {
    	this.x -= n;
    	return this;
    }
    
    /**
     * Offset this Coords 1 block in the given direction
     */
    @Override
    public ICoords offset(BlockFace facing) {
        return this.offset(facing, 1);
    }

    /**
     * Offsets this Coords n blocks in the given direction
     */
    @Override
    public ICoords offset(BlockFace facing, int n) {
    	if (n == 0) {
    		return this;
    	}
    	
    	this.x += facing.getModX() * n;
    	this.y += facing.getModY() * n;
    	this.z += facing.getModZ() * n;
    	
    	return this;
    }
    
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return new instance
	 */
	@Override
	public ICoords add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	/**
	 * 
	 * @param coords
	 * @return new instance
	 */
	@Override
	public ICoords add(ICoords coords) {
		this.x += coords.getX();
		this.y += coords.getY();
		this.z += coords.getZ();
		return this;
	}
	
	/**
	 * 
	 * @param direction
	 * @param n
	 * @return
	 */
	@Override
	public ICoords add(Direction direction, int n) {
		switch(direction) {
		case NORTH: return this.north(n);
		case EAST: return this.east(n);
		case SOUTH: return this.south(n);
		case WEST: return this.west(n);
		case UP: return this.up(n);
		case DOWN: return this.down(n);
		
		default: return this;
		}		
	}
	
	// NOTE the with's for a mutable object, as the same as a setter
	@Override
	public ICoords withX(ICoords coords) {
		setX(coords.getX());
		return this;
	}	
	@Override
	public ICoords withX(int x) {
		setX(x);
		return this;
	}
	
	@Override
	public ICoords withY(ICoords coords) {
		setY(coords.getY());
		return this;
	}	
	
	@Override
	public ICoords withY(int y) {
		setY(y);
		return this;
	}
	
	@Override
	public ICoords withZ(ICoords coords) {
		setZ(coords.getZ());
		return this;
	}	
	
	@Override
	public ICoords withZ(int z) {
		setZ(z);
		return this;
	}
	
	/**
	 * NOTE delta for mutable object is same ass add(-coords);
	 * Delta between this and input ie. this.[xyz] - input.[xyz]
	 * @param coords
	 * @return new instance
	 */
	@Override
	public ICoords delta(ICoords coords) {
		this.x -= coords.getX();
		this.y -= coords.getY();
		this.z -= coords.getZ();
		return this;
	}
	
	/**
	 * 
	 * @param xlen
	 * @param zlen
	 * @param degrees
	 * @return
	 */
	@Override
	public ICoords rotate(double xlen, double zlen, final double degrees) {
		// convert degrees to radian		
		double s = Math.sin(Math.toRadians(degrees));
		double c = Math.cos(Math.toRadians(degrees));

		// rotate point
		double xnew = xlen * c - zlen * s;
		double znew = xlen * s + zlen * c;

		// translate point back:
		return this.add((int)xnew, 0,(int)znew);
	}
	
	/**
	 * Rotate 90 degrees.
	 * @param width
	 * @return new instance
	 */
	@Override
	public ICoords rotate90(int width) {
		this.x = width - this.getZ() - 1;
		return this;
	}
	
	/**
	 * Rotate 180 degrees
	 * @param depth
	 * @param width
	 * @return new instance
	 */
	@Override
	public ICoords rotate180(int depth, int width) {
		this.x = width - this.getX() - 1;
		this.z = depth - this.getZ() - 1;
		return this;
	}
	
	/**
	 * 
	 * @param depth
	 * @return new instance
	 */
	@Override
	public ICoords rotate270(int depth) {
		this.z = depth - this.getX() - 1;
		return this;
	}
	
	@Deprecated
	@Override
	public ICoords resetX(int x) {
    	return withX(x);
	}
	
	@Deprecated
	@Override
	public ICoords resetY(int y) {
    	return withY(y);
	}
	
	@Deprecated
	@Override
	public ICoords resetZ(int z) {
    	return withZ(z);
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setZ(int z) {
		this.z = z;
	}
	
	@Override
	public String toString() {
		return "MutableCoords [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
}
