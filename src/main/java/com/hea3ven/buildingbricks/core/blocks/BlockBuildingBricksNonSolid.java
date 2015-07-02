package com.hea3ven.buildingbricks.core.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public class BlockBuildingBricksNonSolid extends BlockBuildingBricksBase {

	public BlockBuildingBricksNonSolid(StructureMaterial structureMaterial, String name) {
		super(structureMaterial, name);
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public int getHarvestLevel(IBlockState state) {
		return 0;
	}

	@Override
	public String getHarvestTool(IBlockState state) {
		return "pickaxe";
	}

}