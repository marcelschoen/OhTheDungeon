package forge_sandbox.twilightforest.structures.darktower;

import forge_sandbox.StructureBoundingBox;
import forge_sandbox.twilightforest.TFFeature;
import forge_sandbox.twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.Random;
import otd.lib.async.AsyncWorldEditor;


public class ComponentTFDarkTowerRoofAntenna extends ComponentTFDarkTowerRoof {

	public ComponentTFDarkTowerRoofAntenna() {
	}

	public ComponentTFDarkTowerRoofAntenna(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(feature, i, wing);
	}

	/**
	 * Stick with a ball on top antenna
	 */
	@Override
	public boolean addComponentParts(AsyncWorldEditor world, Random rand, StructureBoundingBox sbb) {
		super.addComponentParts(world, rand, sbb);

		// antenna
		for (int y = 1; y < 10; y++) {
			setBlockState(world, deco.accentState, size / 2, y, size / 2, sbb);
		}

		setBlockState(world, deco.accentState, size / 2 - 1, 1, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 + 1, 1, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 1, size / 2 - 1, sbb);
		setBlockState(world, deco.accentState, size / 2, 1, size / 2 + 1, sbb);

		for (int y = 7; y < 10; y++) {
			setBlockState(world, deco.accentState, size / 2 - 1, y, size / 2, sbb);
			setBlockState(world, deco.accentState, size / 2 + 1, y, size / 2, sbb);
			setBlockState(world, deco.accentState, size / 2, y, size / 2 - 1, sbb);
			setBlockState(world, deco.accentState, size / 2, y, size / 2 + 1, sbb);
		}

		setBlockState(world, deco.accentState, size / 2 - 1, 8, size / 2 - 1, sbb);
		setBlockState(world, deco.accentState, size / 2 - 1, 8, size / 2 + 1, sbb);
		setBlockState(world, deco.accentState, size / 2 + 1, 8, size / 2 - 1, sbb);
		setBlockState(world, deco.accentState, size / 2 + 1, 8, size / 2 + 1, sbb);

		return true;
	}

}
