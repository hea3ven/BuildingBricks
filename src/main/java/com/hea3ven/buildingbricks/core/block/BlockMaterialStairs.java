package com.hea3ven.buildingbricks.core.block;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.block.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.block.behavior.BlockBehaviorBase;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;
import com.hea3ven.tools.commonutils.util.ItemStackUtil;
import com.hea3ven.tools.commonutils.util.WorldHelper;

public class BlockMaterialStairs extends BlockBuildingBricksStairs implements BlockMaterial {

	public BlockMaterialStairs(StructureMaterial material) {
		super(material);
	}

	//region COMMON TILE CODE

	@Override
	protected BlockStateContainer createBlockState() {
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
	public Material getMaterial(IBlockAccess world, BlockPos pos) {
		return WorldHelper.<TileMaterial>getTile(world, pos).getMaterial();
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
		Material mat = getMaterial(world, pos);
		if (mat.getBehavior() != null)
			mat.getBehavior().onBlockPlacedBy(world, pos, state, placer, stack);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return TileMaterial.getPickBlock(this, target, world, pos);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return Lists.newArrayList();
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest) {
		ItemStack stack = TileMaterial.getHarvestBlock(world, pos, player);
		boolean removed = super.removedByPlayer(state, world, pos, player, willHarvest);
		if (removed && !world.isRemote && !player.capabilities.isCreativeMode && stack != null)
			ItemStackUtil.dropFromBlock(world, pos, stack);
		return removed;
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		TileMaterial.getSubBlocks(this, itemIn, tab, list);
	}

	@Override
	public String getLocalizedName(Material mat) {
		if (I18n.canTranslate(getUnlocalizedName() + ".name"))
			return super.getLocalizedName();
		else
			return blockLogic.getLocalizedName(mat);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(IBlockState state, World world, RayTraceResult target,
			EffectRenderer effectRenderer) {
		return blockLogic.addHitEffects(world, target, effectRenderer);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer) {
		return blockLogic.addDestroyEffects(world, pos, effectRenderer);
	}

	@Override
	public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
		TileMaterial tile = TileMaterial.getTile(world, pos);
		if (tile == null)
			return super.getBlockHardness(state, world, pos);
		Material mat = tile.getMaterial();
		if (mat == null)
			return super.getBlockHardness(state, world, pos);
		return mat.getHardness();
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		TileMaterial tile = TileMaterial.getTile(world, pos);
		if (tile == null)
			return super.getExplosionResistance(world, pos, exploder, explosion);
		Material mat = tile.getMaterial();
		if (mat == null)
			return super.getExplosionResistance(world, pos, exploder, explosion);
		return mat.getResistance() * 3 / 5;
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
		BlockBehaviorBase behavior = getMaterial(world, pos).getBehavior();
		if (behavior != null)
			behavior.onNeighborBlockChange(world, pos, state, neighborBlock);
		else
			super.onNeighborBlockChange(world, pos, state, neighborBlock);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		Material mat = getMaterial(world, pos);
		if (mat == null)
			return super.getLightValue(state, world, pos);
		BlockBehaviorBase behavior = mat.getBehavior();
		if (behavior != null)
			return behavior.getLightValue(state, world, pos);
		else
			return super.getLightValue(state, world, pos);
	}
}
