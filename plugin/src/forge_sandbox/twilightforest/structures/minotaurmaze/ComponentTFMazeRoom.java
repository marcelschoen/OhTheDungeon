package forge_sandbox.twilightforest.structures.minotaurmaze;

import forge_sandbox.StructureBoundingBox;
import forge_sandbox.twilightforest.TFFeature;
import forge_sandbox.twilightforest.structures.StructureTFComponent;
import forge_sandbox.twilightforest.structures.StructureTFComponentOld;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import otd.lib.async.AsyncWorldEditor;

import java.util.List;
import java.util.Random;

public class ComponentTFMazeRoom extends StructureTFComponentOld {

    public ComponentTFMazeRoom() {
        super();
    }

    public ComponentTFMazeRoom(TFFeature feature, int i, Random rand, int x, int y, int z) {
        super(feature, i);
        this.setCoordBaseMode(HORIZONTALS[rand.nextInt(4)]);

        this.boundingBox = new StructureBoundingBox(x, y, z, x + 15, y + 4, z + 15);
    }

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    @Override
    public void buildComponent(StructureTFComponent structurecomponent, List<StructureTFComponent> list, Random random) {
        ;
    }

    @Override
    public boolean addComponentParts(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb) {
        // floor border
        fillWithBlocks(world, sbb, 1, 0, 1, 14, 0, 14, Blocks.chiseled_maze_stone, AIR, true);
        fillWithBlocks(world, sbb, 2, 0, 2, 13, 0, 13, Blocks.maze_stone, AIR, true);

        // doorways
        if (this.getBlockStateFromPos(world, 7, 1, 0, sbb) == Material.AIR) {
            fillWithBlocks(world, sbb, 6, 1, 0, 9, 4, 0, Bukkit.createBlockData(Material.OAK_FENCE), AIR, false);
            fillWithAir(world, sbb, 7, 1, 0, 8, 3, 0);
        }

        if (this.getBlockStateFromPos(world, 7, 1, 15, sbb) == Material.AIR) {
            fillWithBlocks(world, sbb, 6, 1, 15, 9, 4, 15, Bukkit.createBlockData(Material.OAK_FENCE), AIR, false);
            fillWithAir(world, sbb, 7, 1, 15, 8, 3, 15);
        }

        if (this.getBlockStateFromPos(world, 0, 1, 7, sbb) == Material.AIR) {
            fillWithBlocks(world, sbb, 0, 1, 6, 0, 4, 9, Bukkit.createBlockData(Material.OAK_FENCE), AIR, false);
            fillWithAir(world, sbb, 0, 1, 7, 0, 3, 8);
        }

        if (this.getBlockStateFromPos(world, 15, 1, 7, sbb) == Material.AIR) {
            fillWithBlocks(world, sbb, 15, 1, 6, 15, 4, 9, Bukkit.createBlockData(Material.OAK_FENCE), AIR, false);
            fillWithAir(world, sbb, 15, 1, 7, 15, 3, 8);
        }
        return true;
    }
}
