package com.hea3ven.buildingbricks.core.blocks;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
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

public class BlockBuildingBricksSlab extends BlockSlab implements BlockBuildingBricks {

	protected MaterialBlockLogic blockLogic;

	public BlockBuildingBricksSlab(StructureMaterial structMat) {
		super(structMat.getMcMaterial());
		useNeighborBrightness = true;

		blockLogic = new MaterialBlockLogic(structMat, MaterialBlockType.SLAB);
		blockLogic.initBlock(this);

		IBlockState state = getDefaultState();
		state = setHalf(state, EnumBlockHalf.BOTTOM);
		setDefaultState(state);
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return getUnlocalizedName();
	}

	@Override
	public boolean isDouble() {
		return false;
	}

	@Override
	public IProperty getVariantProperty() {
		return null;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return null;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, HALF);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta |= getHalf(state).ordinal();
		return meta;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = this.getDefaultState();
		state = setHalf(state, (meta & 0x1) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
		return state;
	}

	public static EnumBlockHalf getHalf(IBlockState state) {
		return state.getValue(HALF);
	}

	public static IBlockState setHalf(IBlockState state, EnumBlockHalf half) {
		return state.withProperty(HALF, half);
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos,
			EnumFacing face) {
		return getMaterial(state).isOpaque() && super.doesSideBlockRendering(state, world, pos, face);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos,
			EnumFacing side) {
		BlockPos otherPos = pos.offset(side);
		IBlockState otherState = world.getBlockState(otherPos);
		if (side.getAxis() != Axis.Y) {
			if (!(otherState.getBlock() instanceof BlockSlab) ||
					((BlockSlab) otherState.getBlock()).isDouble())
				return !otherState.isSideSolid(world, otherPos, side.getOpposite());
			else
				return getHalf(otherState) != getHalf(state) ||
						otherState.getMaterial() != state.getMaterial();
		}

		if (getHalf(state) == EnumBlockHalf.BOTTOM)
			return side != EnumFacing.DOWN || otherState.getMaterial() != state.getMaterial() ||
					!otherState.isSideSolid(world, pos, side.getOpposite());
		else
			return side != EnumFacing.UP || otherState.getMaterial() != state.getMaterial() ||
					!otherState.isSideSolid(world, pos, side.getOpposite());
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
