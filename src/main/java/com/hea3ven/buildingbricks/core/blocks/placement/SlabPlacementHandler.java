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
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksVerticalSlab;
import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.util.BlockPlacingUtil;
import com.hea3ven.tools.commonutils.util.ItemBlockUtil;
import com.hea3ven.tools.commonutils.util.PlaceParams;

public class SlabPlacementHandler extends PlacementHandlerBase {
	@Override
	public boolean isHandled(ItemStack stack) {
		return stack.getItem() instanceof ItemBlock &&
				((ItemBlock) stack.getItem()).block instanceof BlockSlab &&
				MaterialRegistry.getMaterialForStack(stack) != null;
	}

	@Override
	public EnumActionResult place(World world, ItemStack stack, EntityLivingBase placer, Material mat,
			IBlockState state, PlaceParams params) {
		Block block;
		if (params.side.getAxis() == Axis.Y) {
			if (BlockPlacingUtil.isInnerRing(params)) {
				block = ((ItemBlock) stack.getItem()).getBlock();
			} else {
				block = mat.getBlock(MaterialBlockType.VERTICAL_SLAB).getBlock();
			}
		} else {
			if (BlockPlacingUtil.isInnerRing(params)) {
				block = mat.getBlock(MaterialBlockType.VERTICAL_SLAB).getBlock();
			} else {
				EnumFacing placeSide = BlockPlacingUtil.getClosestFace(params);
				if (placeSide.getAxis() == Axis.Y) {
					block = ((ItemBlock) stack.getItem()).getBlock();
				} else {
					block = mat.getBlock(MaterialBlockType.VERTICAL_SLAB).getBlock();
				}
			}
		}
		if (!world.canBlockBePlaced(block, params.pos, false, params.side, null, stack))
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

		if (!block.isReplaceable(world, pos) &&
				!(block instanceof BlockSlab || block instanceof BlockBuildingBricksVerticalSlab)) {
			pos = pos.offset(side);
		}

		if (world.canBlockBePlaced(((ItemBlock) stack.getItem()).block, pos, false, side, placer, stack))
			return true;

		Material mat = MaterialRegistry.getMaterialForStack(stack);
		if (mat == null)
			return false;
		BlockDescription blockDesc = mat.getBlock(MaterialBlockType.VERTICAL_SLAB);
		if (blockDesc == null)
			return false;
		return world.canBlockBePlaced(blockDesc.getBlock(), pos, false, side, placer, stack);
	}
}
