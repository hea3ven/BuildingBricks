package com.hea3ven.buildingbricks.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.IBlockAccess;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.block.base.BlockBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockLogic;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public class BlockBuildingBricksStairs extends BlockStairs implements BlockBuildingBricks {

	protected MaterialBlockLogic blockLogic;

	public BlockBuildingBricksStairs(StructureMaterial structMat) {
		super(new Block(structMat.getMcMaterial()) {
		}.getDefaultState());
		useNeighborBrightness = true;

		blockLogic = new MaterialBlockLogic(structMat, MaterialBlockType.STAIRS);
		blockLogic.initBlock(this);
	}

	public static EnumFacing getFacing(IBlockState state) {
		return state.getValue(FACING);
	}

	public static EnumShape getShape(IBlockState state) {
		return state.getValue(SHAPE);
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos,
			EnumFacing face) {
		return state.getMaterial().isOpaque() && super.doesSideBlockRendering(state, world, pos, face);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos,
			EnumFacing side) {
		BlockPos otherPos = pos.offset(side);
		IBlockState otherState = world.getBlockState(otherPos);
		if (!isBlockStairs(otherState)) {
			return !otherState.isSideSolid(world, otherPos, side.getOpposite());
		}

		if (state.isSideSolid(world, pos, side) &&
				otherState.isSideSolid(world, otherPos, side.getOpposite()))
			return false;

		if (side.getAxis() != Axis.Y)
			return state.getValue(FACING) != otherState.getValue(FACING) ||
					state.getValue(HALF) != otherState.getValue(HALF);
		else
			return true;
	}

	public EnumHalf getHalf(IBlockState state) {
		return state.getValue(HALF);
	}

	//region COMMON BLOCK CODE

	@Override
	public MaterialBlockLogic getBlockLogic() {
		return blockLogic;
	}

	@Override
	public boolean requiresUpdates() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return blockLogic.getBlockLayer();
	}

	@Override
	public int getHarvestLevel(IBlockState state) {
		return 0;
	}

	@Override
	public String getHarvestTool(IBlockState state) {
		return blockLogic.getHarvestTool(state);
	}

	//endregion COMMON BLOCK CODE
}
