package com.hea3ven.buildingbricks.core.blocks.base;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.transition.helpers.WorldHelper;
import com.hea3ven.transition.m.block.state.IBlockState;
import com.hea3ven.transition.m.util.BlockPos;
import com.hea3ven.transition.m.util.EnumFacing;

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

	public AxisAlignedBB getBoundingBox(IBlockState state) {
		return AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		IBlockState state = WorldHelper.get(world).getBlockState(pos);
		AxisAlignedBB bb = getBoundingBox(state);

		setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX,
				(float) bb.maxY, (float) bb.maxZ);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return getBoundingBox(state).offset(pos.getX(), pos.getY(), pos.getZ());
	}
}
