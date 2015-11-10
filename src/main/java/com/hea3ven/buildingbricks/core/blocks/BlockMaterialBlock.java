package com.hea3ven.buildingbricks.core.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.blocks.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;
import com.hea3ven.buildingbricks.core.utils.ItemStackUtils;

public class BlockMaterialBlock extends BlockBuildingBricksBlock implements BlockMaterial {

	public BlockMaterialBlock(StructureMaterial structMat) {
		super(structMat);
	}

	//region COMMON TILE CODE

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
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		TileMaterial.onBlockPlacedBy(this, world, pos, state, placer, stack);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
		return TileMaterial.getPickBlock(this, target, world, pos);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return Lists.newArrayList();
	}

	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		ItemStack stack = TileMaterial.getHarvestBlock(world, pos, player);
		boolean removed = super.removedByPlayer(world, pos, player, willHarvest);
		if (removed && !world.isRemote && !player.capabilities.isCreativeMode)
			ItemStackUtils.dropFromBlock(world, pos, stack);
		return removed;
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

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer) {
		return blockLogic.addHitEffects(world, target, effectRenderer);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer) {
		return blockLogic.addDestroyEffects(world, pos, effectRenderer);
	}

	//endregion COMMON TILE CODE
}
