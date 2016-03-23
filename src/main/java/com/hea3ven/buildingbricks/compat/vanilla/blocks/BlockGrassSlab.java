package com.hea3ven.buildingbricks.compat.vanilla.blocks;

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
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksSlab;
import com.hea3ven.buildingbricks.core.blocks.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;

public class BlockGrassSlab extends BlockBuildingBricksSlab implements BlockMaterial {

	private Material mat;

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
