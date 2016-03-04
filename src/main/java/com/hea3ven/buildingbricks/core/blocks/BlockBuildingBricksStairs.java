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
	public boolean doesSideBlockRendering(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return getMaterial().isOpaque() && super.doesSideBlockRendering(world, pos, face);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {
		Block block = world.getBlockState(pos).getBlock();
		if (!isBlockStairs(block)) {
			return !block.isSideSolid(world, pos, side);
		}

		if (isSideSolid(world, pos, side.getOpposite()) &&
				isSideSolid(world, pos.offset(side.getOpposite()), side))
			return false;

		BlockPos ownPos = pos.offset(side.getOpposite());
		IBlockState ownState = world.getBlockState(ownPos);
		if (side.getAxis() != Axis.Y)
			return !isSameStair(world, pos, ownState);
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
	public int getBlockColor() {
		return blockLogic.getBlockColor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state) {
		return blockLogic.getRenderColor(state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		return blockLogic.colorMultiplier(worldIn, pos, renderPass);
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
