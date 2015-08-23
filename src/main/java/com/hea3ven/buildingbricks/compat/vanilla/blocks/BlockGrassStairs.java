package com.hea3ven.buildingbricks.compat.vanilla.blocks;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGrassStairs extends BlockStairs {

	public BlockGrassStairs(IBlockState state) {
		super(state);
		useNeighborBrightness = true;
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
		props.add(BlockGrass.SNOWY);
		return new BlockState(this, props.toArray(new IProperty[0]));
	}

	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		state = super.getActualState(state, world, pos);
		Block blockN = world.getBlockState(pos.north()).getBlock();
		Block blockS = world.getBlockState(pos.south()).getBlock();
		Block blockE = world.getBlockState(pos.east()).getBlock();
		Block blockW = world.getBlockState(pos.west()).getBlock();
		return setSnowy(state,
				blockN == Blocks.snow || blockN == Blocks.snow_layer || blockS == Blocks.snow
						|| blockS == Blocks.snow_layer || blockE == Blocks.snow
						|| blockE == Blocks.snow_layer || blockW == Blocks.snow
						|| blockW == Blocks.snow_layer);
	}

	public static boolean getSnowy(IBlockState state) {
		return (Boolean) state.getValue(BlockGrass.SNOWY);
	}

	public static IBlockState setSnowy(IBlockState state, boolean snowy) {
		return state.withProperty(BlockGrass.SNOWY, snowy);
	}

}
