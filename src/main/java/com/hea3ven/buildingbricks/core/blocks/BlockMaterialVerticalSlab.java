package com.hea3ven.buildingbricks.core.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.blocks.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;
import com.hea3ven.transition.m.block.state.BlockState;
import com.hea3ven.transition.m.block.state.IBlockState;
import com.hea3ven.transition.m.util.BlockPos;

public class BlockMaterialVerticalSlab extends BlockBuildingBricksVerticalSlab
		implements BlockMaterial {

	public BlockMaterialVerticalSlab(StructureMaterial material) {
		super(material);
	}

	@Override
	protected BlockState createBlockState() {
		return TileMaterial.createBlockState(super.createBlockState());
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMaterial();
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return TileMaterial.getExtendedState(state, world, pos);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state,
			EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		TileMaterial.onBlockPlacedBy(this, world, pos, state, placer, stack);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
		return TileMaterial.getPickBlock(this, target, world, pos);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		return Lists.newArrayList();
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileMaterial.breakBlock(this, world, pos, state);
		super.breakBlock(world, pos, state);
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		TileMaterial.getSubBlocks(this, itemIn, tab, list);
	}

	@Override
	public String getLocalizedName(Material mat) {
		if (StatCollector.canTranslate(getUnlocalizedName() + ".name"))
			return super.getLocalizedName();
		else
			return blockLogic.getLocalizedName(mat);
	}
}
