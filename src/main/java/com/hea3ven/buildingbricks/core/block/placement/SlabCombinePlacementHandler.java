package com.hea3ven.buildingbricks.core.block.placement;

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

import com.hea3ven.buildingbricks.core.block.BlockBuildingBricksVerticalSlab;
import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.tools.commonutils.util.ItemBlockUtil;
import com.hea3ven.tools.commonutils.util.PlaceParams;

public class SlabCombinePlacementHandler extends PlacementHandlerBase {
	@Override
	public boolean isHandled(ItemStack stack) {
		return stack.getItem() instanceof ItemBlock &&
				((ItemBlock) stack.getItem()).block instanceof BlockSlab &&
				MaterialRegistry.getMaterialForStack(stack) != null;
	}

	@Override
	public EnumActionResult place(World world, ItemStack stack, EntityLivingBase placer, Material mat,
			IBlockState state, PlaceParams params) {
		if (!(state.getBlock() instanceof BlockSlab) &&
				!(state.getBlock() instanceof BlockBuildingBricksVerticalSlab))
			return EnumActionResult.PASS;

		ItemStack targetStack = state.getBlock().getPickBlock(state, null, world, params.pos, null);
		Material targetMat = MaterialRegistry.getMaterialForStack(targetStack);
		if(mat != targetMat)
			return EnumActionResult.PASS;

		BlockDescription blockDesc = mat.getBlock(MaterialBlockType.FULL);
		ItemStack placeStack = blockDesc.getStack();
		Block block = ((ItemBlock) placeStack.getItem()).getBlock();
		IBlockState newState = calculatePlaceState(world, placer, params, placeStack, block);
		if (ItemBlockUtil.placeBlock(stack, placer, world, params.pos, newState))
			return EnumActionResult.SUCCESS;
		return EnumActionResult.PASS;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer placer,
			ItemStack stack) {
		Block block = world.getBlockState(pos).getBlock();

		if (!block.isReplaceable(world, pos) &&
				!(block instanceof BlockSlab || block instanceof BlockBuildingBricksVerticalSlab)) {
			pos = pos.offset(side);
			block = world.getBlockState(pos).getBlock();
		}

		if (block instanceof BlockSlab || block instanceof BlockBuildingBricksVerticalSlab)
			return true;

		return false;
	}
}
