package com.hea3ven.buildingbricks.core.blocks;

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

import com.hea3ven.buildingbricks.core.blocks.base.BlockBuildingBricks;
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
		return getMaterial(state).isOpaque() && super.doesSideBlockRendering(state, world, pos, face);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos,
			EnumFacing side) {
//		if (!isBlockStairs(state.getBlock())) {
		if (!func_185709_i(state)) {
			return !state.getBlock().isSideSolid(state, world, pos, side);
		}

		if (isSideSolid(state, world, pos, side.getOpposite()) &&
				isSideSolid(world.getBlockState(pos.offset(side.getOpposite())), world,
						pos.offset(side.getOpposite()), side))
			return false;

		BlockPos ownPos = pos.offset(side.getOpposite());
		IBlockState ownState = world.getBlockState(ownPos);
		if (side.getAxis() != Axis.Y)
//			return !isSameStair(world, pos, ownState);
			return !func_185709_i(ownState);
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
