package com.hea3ven.buildingbricks.core.blocks.base;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.transition.m.util.BlockPos;

public class BlockBuildingBricksNonSolid extends BlockBuildingBricksBase {

	public BlockBuildingBricksNonSolid(StructureMaterial structMat, MaterialBlockType blockType) {
		super(structMat, blockType);
	}

	public boolean isFullCube() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}
}