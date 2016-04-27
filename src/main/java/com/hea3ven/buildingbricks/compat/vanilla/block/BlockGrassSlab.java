package com.hea3ven.buildingbricks.compat.vanilla.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.block.BlockBuildingBricksSlab;
import com.hea3ven.buildingbricks.core.block.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;

public class BlockGrassSlab extends BlockBuildingBricksSlab implements BlockMaterial {

	private Material mat;

	public BlockGrassSlab() {
		super(StructureMaterial.GRASS);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> stacks = new ArrayList<>();
		BlockDescription slabBlock = MaterialRegistry.get("minecraft:dirt").getBlock(MaterialBlockType.SLAB);
		if (slabBlock != null)
			stacks.add(slabBlock.getStack().copy());
		return stacks;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		Collection<IProperty<?>> props = new ArrayList<>(super.createBlockState().getProperties());
		props.add(BlockGrass.SNOWY);
		return new BlockStateContainer(this, props.toArray(new IProperty[props.size()]));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		Block blockN = worldIn.getBlockState(pos.north()).getBlock();
		Block blockS = worldIn.getBlockState(pos.south()).getBlock();
		Block blockE = worldIn.getBlockState(pos.east()).getBlock();
		Block blockW = worldIn.getBlockState(pos.west()).getBlock();
		return setSnowy(state,
				blockN == Blocks.SNOW || blockN == Blocks.SNOW_LAYER || blockS == Blocks.SNOW ||
						blockS == Blocks.SNOW_LAYER || blockE == Blocks.SNOW || blockE == Blocks.SNOW_LAYER ||
						blockW == Blocks.SNOW || blockW == Blocks.SNOW_LAYER);
	}

	public static boolean getSnowy(IBlockState state) {
		return state.getValue(BlockGrass.SNOWY);
	}

	public static IBlockState setSnowy(IBlockState state, boolean snowy) {
		return state.withProperty(BlockGrass.SNOWY, snowy);
	}

	@Override
	public String getLocalizedName(Material mat) {
		if (I18n.canTranslate(getUnlocalizedName() + ".name"))
			return super.getLocalizedName();
		else
			return blockLogic.getLocalizedName(mat);
	}

	@Override
	public Material getMaterial(IBlockAccess world, BlockPos pos) {
		return getGrassMaterial();
	}

	private Material getGrassMaterial() {
		if (mat == null)
			mat = MaterialRegistry.get("minecraft:grass");
		return mat;
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		if (TileMaterial.blocksInCreative) {
			super.getSubBlocks(itemIn, tab, list);
		}
	}
}
