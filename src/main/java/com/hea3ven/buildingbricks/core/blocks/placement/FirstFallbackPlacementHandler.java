package com.hea3ven.buildingbricks.core.blocks.placement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksCorner;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksStep;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.tools.commonutils.util.ItemBlockUtil;
import com.hea3ven.tools.commonutils.util.PlaceParams;

public class FirstFallbackPlacementHandler extends PlacementHandlerBase {
	@Override
	public boolean isHandled(ItemStack stack) {
		return stack.getItem() instanceof ItemBlock &&
				(((ItemBlock) stack.getItem()).block instanceof BlockSlab ||
						((ItemBlock) stack.getItem()).block instanceof BlockBuildingBricksStep ||
						((ItemBlock) stack.getItem()).block instanceof BlockBuildingBricksCorner) &&
				MaterialRegistry.getMaterialForStack(stack) != null;
	}

	@Override
	public EnumActionResult place(World world, ItemStack stack, EntityLivingBase placer, Material mat,
			IBlockState state, PlaceParams params) {
		if (params.depth < 1)
			return EnumActionResult.PASS;

		Block block = ((ItemBlock) stack.getItem()).getBlock();

		if (!world.canBlockBePlaced(block, params.pos, false, params.side, placer, stack))
			return EnumActionResult.PASS;

		IBlockState newState = calculatePlaceState(world, placer, params, stack, block);
		if (ItemBlockUtil.placeBlock(stack, placer, world, params.pos, newState))
			return EnumActionResult.SUCCESS;
		return EnumActionResult.PASS;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer placer,
			ItemStack stack) {
		Block block = world.getBlockState(pos).getBlock();

		if (!block.isReplaceable(world, pos))
			pos = pos.offset(side);

		if (world.canBlockBePlaced(((ItemBlock) stack.getItem()).block, pos, false, side, placer, stack))
			return true;

		return false;
	}
}
