package com.hea3ven.buildingbricks.core.blocks;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;

public class BlockBuildingBricksSlab extends BlockSlab {

	public BlockBuildingBricksSlab(StructureMaterial structureMaterial) {
		super(structureMaterial.getMcMaterial());
		useNeighborBrightness = true;
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
	public Object getVariant(ItemStack stack) {
		return null;
	}

	@Override
	protected BlockState createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] {HALF},
				new IUnlistedProperty[] {TileMaterial.MATERIAL});
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
		return (EnumBlockHalf) state.getValue(HALF);
	}

	public static IBlockState setHalf(IBlockState state, EnumBlockHalf half) {
		return state.withProperty(HALF, half);
	}

	@Override
	public boolean requiresUpdates() {
		return false;
	}
}
