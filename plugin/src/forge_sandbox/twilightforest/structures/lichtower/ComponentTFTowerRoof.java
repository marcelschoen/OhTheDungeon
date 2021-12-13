package forge_sandbox.twilightforest.structures.lichtower;

import forge_sandbox.StructureBoundingBox;
import forge_sandbox.twilightforest.TFFeature;
import forge_sandbox.twilightforest.structures.StructureTFComponent;
import forge_sandbox.twilightforest.structures.StructureTFComponentOld;
import otd.lib.async.AsyncWorldEditor;

import java.util.List;
import java.util.Random;


public abstract class ComponentTFTowerRoof extends StructureTFComponentOld {

    protected int size;
    protected int height;

    public ComponentTFTowerRoof() {
        super();
    }

    public ComponentTFTowerRoof(TFFeature feature, int i, ComponentTFTowerWing wing) {
        super(feature, i);

        this.spawnListIndex = -1;

        // inheritors need to add a bounding box or die~!
    }

    /**
     * Makes a bounding box that hangs forwards off of the tower wing we are on.  This is for attached roofs.
     *
     * @param wing
     */
    protected void makeAttachedOverhangBB(ComponentTFTowerWing wing) {
        // just hang out at the very top of the tower
        switch (getCoordBaseMode()) {
            case SOUTH:
                this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX, wing.getBoundingBox().maxY, wing.getBoundingBox().minZ - 1, wing.getBoundingBox().maxX + 1, wing.getBoundingBox().maxY + this.height - 1, wing.getBoundingBox().maxZ + 1);
                break;
            case WEST:
                this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX - 1, wing.getBoundingBox().maxY, wing.getBoundingBox().minZ, wing.getBoundingBox().maxX + 1, wing.getBoundingBox().maxY + this.height - 1, wing.getBoundingBox().maxZ + 1);
                break;
            case EAST:
                this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX - 1, wing.getBoundingBox().maxY, wing.getBoundingBox().minZ - 1, wing.getBoundingBox().maxX, wing.getBoundingBox().maxY + this.height - 1, wing.getBoundingBox().maxZ + 1);
                break;
            case NORTH:
                this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX - 1, wing.getBoundingBox().maxY, wing.getBoundingBox().minZ - 1, wing.getBoundingBox().maxX + 1, wing.getBoundingBox().maxY + this.height - 1, wing.getBoundingBox().maxZ);
                break;
            default:
                break;
        }
    }


    /**
     * Makes a bounding box that sits at the top of the tower.  Works for attached or freestanding roofs.
     *
     * @param wing
     */
    protected void makeCapBB(ComponentTFTowerWing wing) {
        this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX, wing.getBoundingBox().maxY, wing.getBoundingBox().minZ, wing.getBoundingBox().maxX, wing.getBoundingBox().maxY + this.height, wing.getBoundingBox().maxZ);
    }


    /**
     * Make a bounding box that hangs over the sides of the tower 1 block.  Freestanding towers only.
     *
     * @param wing
     */
    protected void makeOverhangBB(ComponentTFTowerWing wing) {
        this.boundingBox = new StructureBoundingBox(wing.getBoundingBox().minX - 1, wing.getBoundingBox().maxY, wing.getBoundingBox().minZ - 1, wing.getBoundingBox().maxX + 1, wing.getBoundingBox().maxY + this.height - 1, wing.getBoundingBox().maxZ + 1);
    }


    @Override
    public boolean addComponentParts(AsyncWorldEditor world, Random random, StructureBoundingBox structureboundingbox) {
        return false;
    }

    /**
     * Does this roof intersect anything except the parent tower?
     */
    public boolean fits(ComponentTFTowerWing parent, List<StructureTFComponent> list, Random rand) {
        return StructureTFComponent.findIntersecting(list, this.boundingBox) == parent;
    }

}
