package com.hea3ven.buildingbricks.compat.vanilla.blocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksSlab;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public class BlockGrassSlab extends BlockBuildingBricksSlab {

	public Material mat;

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
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> stacks = new ArrayList<>();
		stacks.add(MaterialRegistry.get("buildingbrickscompatvanilla:dirt")
				.getBlock(MaterialBlockType.SLAB)
				.getStack()
				.copy());
		return stacks;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	@Override
	protected BlockState createBlockState() {
		Collection<IProperty> props = new ArrayList<>(super.createBlockState().getProperties());
		props.add(BlockGrass.SNOWY);
		return new BlockState(this, props.toArray(new IProperty[props.size()]));
	}

	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		Block blockN = worldIn.getBlockState(pos.north()).getBlock();
		Block blockS = worldIn.getBlockState(pos.south()).getBlock();
		Block blockE = worldIn.getBlockState(pos.east()).getBlock();
		Block blockW = worldIn.getBlockState(pos.west()).getBlock();
		return setSnowy(state,
				blockN == Blocks.snow || blockN == Blocks.snow_layer || blockS == Blocks.snow ||
						blockS == Blocks.snow_layer || blockE == Blocks.snow || blockE == Blocks.snow_layer ||
						blockW == Blocks.snow || blockW == Blocks.snow_layer);
	}

	public static boolean getSnowy(IBlockState state) {
		return state.getValue(BlockGrass.SNOWY);
	}

	public static IBlockState setSnowy(IBlockState state, boolean snowy) {
		return state.withProperty(BlockGrass.SNOWY, snowy);
	}
}
