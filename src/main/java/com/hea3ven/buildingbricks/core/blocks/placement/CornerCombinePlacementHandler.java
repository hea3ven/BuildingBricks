package com.hea3ven.buildingbricks.core.blocks.placement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksCorner;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.tools.commonutils.util.ItemBlockUtil;
import com.hea3ven.tools.commonutils.util.PlaceParams;

public class CornerCombinePlacementHandler extends PlacementHandlerBase {
	@Override
	public boolean isHandled(ItemStack stack) {
		return stack.getItem() instanceof ItemBlock &&
				((ItemBlock) stack.getItem()).block instanceof BlockBuildingBricksCorner &&
				MaterialRegistry.getMaterialForStack(stack) != null;
	}

	@Override
	public EnumActionResult place(World world, ItemStack stack, EntityLivingBase placer, Material mat,
			IBlockState state, PlaceParams params) {
		if (!(state.getBlock() instanceof BlockBuildingBricksCorner))
			return null;

		IBlockState newState = null;
		// TODO: use math
		EnumFacing blockSide = state.getValue(BlockProperties.ROTATION).getSide();
		EnumBlockHalf blockHalf = state.getValue(BlockProperties.HALF);
		if ((((blockHalf == EnumBlockHalf.BOTTOM && params.hit.yCoord >= 0.5f) ||
				(blockHalf == EnumBlockHalf.TOP && params.hit.yCoord <= 0.5f)))) {
			boolean join = false;
			PlaceParams newParams = null;
			if (blockSide == EnumFacing.NORTH && params.hit.xCoord <= 0.5f && params.hit.zCoord <= 0.5f) {
				join = true;
			} else if (blockSide == EnumFacing.EAST && params.hit.xCoord >= 0.5f &&
					params.hit.zCoord <= 0.5f) {
				join = true;
			} else if (blockSide == EnumFacing.SOUTH && params.hit.xCoord >= 0.5f &&
					params.hit.zCoord >= 0.5f) {
				join = true;
			} else if (blockSide == EnumFacing.WEST && params.hit.xCoord <= 0.5f &&
					params.hit.zCoord >= 0.5f) {
				join = true;
			}
			if (join) {
				// Vertical step
				BlockDescription blockDesc = mat.getBlock(MaterialBlockType.STEP);
				newState = calculatePlaceState(world, placer, new PlaceParams(params.pos, EnumFacing.DOWN,
								0.5d + 0.4d * blockSide.getFrontOffsetX(), 0.0d,
								0.5d + 0.4d * blockSide.getFrontOffsetZ()),
						mat.getBlock(blockDesc.getType().getStackType()).getStack().copy(),
						blockDesc.getBlock());
			}
		} else {
			EnumFacing newSide = null;
			if (blockSide == EnumFacing.NORTH && params.hit.xCoord >= 0.5f && params.hit.zCoord <= 0.5f)
				newSide = EnumFacing.NORTH;
			else if (blockSide == EnumFacing.NORTH && params.hit.xCoord <= 0.5f && params.hit.zCoord >= 0.5f)
				newSide = EnumFacing.WEST;
			else if (blockSide == EnumFacing.EAST && params.hit.xCoord >= 0.5f && params.hit.zCoord >= 0.5f)
				newSide = EnumFacing.EAST;
			else if (blockSide == EnumFacing.EAST && params.hit.xCoord <= 0.5f && params.hit.zCoord <= 0.5f)
				newSide = EnumFacing.NORTH;
			else if (blockSide == EnumFacing.SOUTH && params.hit.xCoord <= 0.5f && params.hit.zCoord >= 0.5f)
				newSide = EnumFacing.SOUTH;
			else if (blockSide == EnumFacing.SOUTH && params.hit.xCoord >= 0.5f && params.hit.zCoord <= 0.5f)
				newSide = EnumFacing.EAST;
			else if (blockSide == EnumFacing.WEST && params.hit.xCoord <= 0.5f && params.hit.zCoord <= 0.5f)
				newSide = EnumFacing.WEST;
			else if (blockSide == EnumFacing.WEST && params.hit.xCoord >= 0.5f && params.hit.zCoord >= 0.5f)
				newSide = EnumFacing.SOUTH;
			if (newSide != null) {
				BlockDescription blockDesc = mat.getBlock(MaterialBlockType.STEP);

				PlaceParams newParams = new PlaceParams(params.pos, newSide.getOpposite(),
						new Vec3d(0.5d + 0.5d * newSide.getFrontOffsetX(),
								blockHalf == EnumBlockHalf.TOP ? 0.6d : 0.4d,
								0.5d + 0.5d * newSide.getFrontOffsetZ()), 1.0d);
				newState = calculatePlaceState(world, placer, newParams, blockDesc.getStack().copy(),
						blockDesc.getBlock());
			}
		}

		if(newState == null)
			return EnumActionResult.PASS;
		if (ItemBlockUtil.placeBlock(stack, placer, world, params.pos, newState))
			return EnumActionResult.SUCCESS;
		return EnumActionResult.PASS;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer placer,
			ItemStack stack) {
		Block block = world.getBlockState(pos).getBlock();

		if (!block.isReplaceable(world, pos) &&
				!(block instanceof BlockBuildingBricksCorner)) {
			pos = pos.offset(side);
			block = world.getBlockState(pos).getBlock();
		}
		if (block instanceof BlockBuildingBricksCorner)
			return true;

		return false;
	}
}
