/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forge_sandbox.twilightforest.structures.minotaurmaze;

import forge_sandbox.twilightforest.TFFeature;
import forge_sandbox.twilightforest.structures.StructureStartTFAbstract;
import forge_sandbox.twilightforest.structures.StructureTFComponent;
import otd.lib.async.AsyncWorldEditor;

import java.util.Random;

import static forge_sandbox.twilightforest.TFFeature.LABYRINTH;

public class StructureStartLabyrinth extends StructureStartTFAbstract {
    public StructureStartLabyrinth() {
        super();
    }

    public StructureStartLabyrinth(AsyncWorldEditor world, TFFeature feature, Random rand, int chunkX, int chunkZ) {
        super(world, feature, rand, chunkX, chunkZ);
    }

    @Override
    protected StructureTFComponent makeFirstComponent(AsyncWorldEditor world, TFFeature feature, Random rand, int x, int y, int z) {
        return new ComponentTFMazeRuins(LABYRINTH, world, rand, 0, x, y, z);
    }
}
