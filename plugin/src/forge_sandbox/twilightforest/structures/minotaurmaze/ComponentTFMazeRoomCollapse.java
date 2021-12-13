package forge_sandbox.twilightforest.structures.minotaurmaze;

//import net.minecraft.init.Blocks;
//import net.minecraft.world.World;
//import net.minecraft.world.gen.structure.StructureBoundingBox;

import forge_sandbox.StructureBoundingBox;
import forge_sandbox.twilightforest.TFFeature;
import otd.lib.async.AsyncWorldEditor;

import java.util.Random;

//import static twilightforest.enums.PlantVariant.ROOT_STRAND;

public class ComponentTFMazeRoomCollapse extends ComponentTFMazeRoom {

    public ComponentTFMazeRoomCollapse() {
        super();
    }


    public ComponentTFMazeRoomCollapse(TFFeature feature, int i, Random rand, int x, int y, int z) {
        super(feature, i, rand, x, y, z);
    }


    @Override
    public boolean addComponentParts(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb) {

        //
        for (int x = 1; x < 14; x++) {
            for (int z = 1; z < 14; z++) {
                // calculate distance from middle
                int dist = (int) Math.round(7 / Math.sqrt((7.5 - x) * (7.5 - x) + (7.5 - z) * (7.5 - z)));
                int gravel = rand.nextInt(dist);
                int root = rand.nextInt(dist);

                if (gravel > 0) {
                    gravel++; // get it out of the floor
                    this.fillWithBlocks(world, sbb, x, 1, z, x, gravel, z, Blocks.gravel, AIR, false);
                    this.fillWithAir(world, sbb, x, gravel, z, x, gravel + 5, z);
                } else if (root > 0) {
                    this.fillWithBlocks(world, sbb, x, 5, z, x, 5 + root, z, Blocks.gravel, AIR, true);
//                    this.fillWithBlocks(world, sbb, x, 5 - rand.nextInt(5), z, x, 5, z, TFBlocks.twilight_plant.getDefaultState().withProperty(BlockTFPlant.VARIANT, ROOT_STRAND), AIR, false);
                } else if (rand.nextInt(dist + 1) > 0) {
                    // remove ceiling
                    this.fillWithAir(world, sbb, x, 5, z, x, 5, z);
                }
            }
        }

        return true;
    }
}
