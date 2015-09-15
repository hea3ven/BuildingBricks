package com.hea3ven.buildingbricks.compat.vanilla.blocks;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksSlab;
import com.hea3ven.buildingbricks.core.blocks.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.transition.helpers.WorldHelper;
import com.hea3ven.transition.m.block.properties.IProperty;
import com.hea3ven.transition.m.block.properties.PropertyBool;
import com.hea3ven.transition.m.block.state.BlockState;
import com.hea3ven.transition.m.block.state.IBlockState;
import com.hea3ven.transition.m.util.BlockPos;
import com.hea3ven.transition.m.util.EnumWorldBlockLayer;
import com.hea3ven.transition.m.world.biome.BiomeColorHelper;

public class BlockGrassSlab extends BlockBuildingBricksSlab implements BlockMaterial {

	public BlockGrassSlab() {
		super(StructureMaterial.GRASS);
	}

	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		return ColorizerGrass.getGrassColor(0.5D, 1.0D);
	}

	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state) {
		return this.getBlockColor();
	}

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	@Override
	protected BlockState createBlockState() {
		Collection<IProperty> props = new ArrayList<IProperty>(
				super.createBlockState().getProperties());
		props.add(SNOWY);
		return new BlockState(this, props.toArray(new IProperty[0]));
	}

	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		Block blockN = WorldHelper.get(worldIn).getBlockState(pos.north()).getBlock();
		Block blockS = WorldHelper.get(worldIn).getBlockState(pos.south()).getBlock();
		Block blockE = WorldHelper.get(worldIn).getBlockState(pos.east()).getBlock();
		Block blockW = WorldHelper.get(worldIn).getBlockState(pos.west()).getBlock();
		return setSnowy(state,
				blockN == Blocks.snow || blockN == Blocks.snow_layer || blockS == Blocks.snow
						|| blockS == Blocks.snow_layer || blockE == Blocks.snow
						|| blockE == Blocks.snow_layer || blockW == Blocks.snow
						|| blockW == Blocks.snow_layer);
	}

	public static boolean getSnowy(IBlockState state) {
		return (Boolean) state.getValue(SNOWY);
	}

	public static IBlockState setSnowy(IBlockState state, boolean snowy) {
		return state.withProperty(SNOWY, snowy);
	}

	@Override
	public String getLocalizedName(Material mat) {
		if (StatCollector.canTranslate(getUnlocalizedName() + ".name"))
			return super.getLocalizedName();
		else
			return blockLogic.getLocalizedName(mat);
	}

	/**************** 1.7.10 ****************/

	public static PropertyBool SNOWY = PropertyBool.create("snowed");
}
