package com.hea3ven.buildingbricks.core.block;

import com.hea3ven.buildingbricks.core.block.base.BlockBuildingBricksBase;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public class BlockBuildingBricksBlock extends BlockBuildingBricksBase {

	public BlockBuildingBricksBlock(StructureMaterial structMat) {
		super(structMat, MaterialBlockType.FULL);
	}
}
