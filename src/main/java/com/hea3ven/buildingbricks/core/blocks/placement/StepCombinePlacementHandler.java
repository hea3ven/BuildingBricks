package com.hea3ven.buildingbricks.core.blocks.placement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksStep;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.tools.commonutils.util.PlaceParams;

public class StepCombinePlacementHandler extends PlacementHandlerBase {
	@Override
	public boolean isHandled(ItemStack stack) {
		return stack.getItem() instanceof ItemBlock &&
				((ItemBlock) stack.getItem()).block instanceof BlockBuildingBricksStep &&
				MaterialRegistry.getMaterialForStack(stack) != null;
	}

	@Override
	public IBlockState place(World world, ItemStack stack, EntityLivingBase placer, Material mat,
			IBlockState state, PlaceParams params) {

		if (!(state.getBlock() instanceof BlockBuildingBricksStep))
			return null;

		// TODO: use math
		boolean vertical = state.getValue(BlockProperties.VERTICAL);
		EnumFacing blockSide = state.getValue(BlockProperties.ROTATION).getSide();
		if (!vertical) {
			EnumBlockHalf blockHalf = state.getValue(BlockProperties.HALF);
			BlockDescription blockDesc = null;
			PlaceParams newParams = null;
			if ((((blockHalf == EnumBlockHalf.BOTTOM && params.hit.yCoord >= 0.5f) ||
					(blockHalf == EnumBlockHalf.TOP && params.hit.yCoord <= 0.5f)))) {
				if ((blockSide.getDirectionVec().getX() > 0 && params.hit.xCoord >= 0.5f) ||
						(blockSide.getDirectionVec().getX() < 0 && params.hit.xCoord <= 0.5f) ||
						(blockSide.getDirectionVec().getZ() > 0 && params.hit.zCoord >= 0.5f) ||
						(blockSide.getDirectionVec().getZ() < 0 && params.hit.zCoord <= 0.5f)) {
					blockDesc = mat.getBlock(MaterialBlockType.VERTICAL_SLAB);
					newParams = new PlaceParams(params.pos, blockSide.getOpposite(),
							new Vec3d(0.5d + 0.5d * blockSide.getFrontOffsetX(), 0.5d,
									0.5d + 0.5d * blockSide.getFrontOffsetZ()), 1.0d);
				}
			} else {
				if ((blockSide.getDirectionVec().getX() > 0 && params.hit.xCoord <= 0.5f) ||
						(blockSide.getDirectionVec().getX() < 0 && params.hit.xCoord >= 0.5f) ||
						(blockSide.getDirectionVec().getZ() > 0 && params.hit.zCoord <= 0.5f) ||
						(blockSide.getDirectionVec().getZ() < 0 && params.hit.zCoord >= 0.5f)) {
					blockDesc = mat.getBlock(MaterialBlockType.SLAB);
					newParams = new PlaceParams(params.pos, blockSide.getOpposite(),
							new Vec3d(0.5d + 0.5d * blockSide.getFrontOffsetX(),
									blockHalf == EnumBlockHalf.TOP ? 0.6d : 0.4d,
									0.5d + 0.5d * blockSide.getFrontOffsetZ()), 1.0d);
				}
			}
			if (blockDesc != null) {
				return placeBlock(world, placer, newParams,
						mat.getBlock(blockDesc.getType().getStackType()).getStack().copy(),
						blockDesc.getBlock());
			}
		} else {
			EnumFacing newSide = null;
			if (blockSide == EnumFacing.NORTH && params.hit.zCoord <= 0.5f && params.hit.xCoord >= 0.5f)
				newSide = EnumFacing.NORTH;
			else if (blockSide == EnumFacing.EAST && params.hit.zCoord <= 0.5f && params.hit.xCoord <= 0.5f)
				newSide = EnumFacing.NORTH;
			else if (blockSide == EnumFacing.EAST && params.hit.xCoord >= 0.5 && params.hit.zCoord >= 0.5f)
				newSide = EnumFacing.EAST;
			else if (blockSide == EnumFacing.SOUTH && params.hit.xCoord >= 0.5 && params.hit.zCoord <= 0.5f)
				newSide = EnumFacing.EAST;
			else if (blockSide == EnumFacing.SOUTH && params.hit.xCoord <= 0.5f && params.hit.zCoord >= 0.5)
				newSide = EnumFacing.SOUTH;
			else if (blockSide == EnumFacing.WEST && params.hit.xCoord >= 0.5 && params.hit.zCoord >= 0.5)
				newSide = EnumFacing.SOUTH;
			else if (blockSide == EnumFacing.WEST && params.hit.xCoord <= 0.5 && params.hit.zCoord <= 0.5)
				newSide = EnumFacing.WEST;
			else if (blockSide == EnumFacing.NORTH && params.hit.xCoord <= 0.5 && params.hit.zCoord >= 0.5)
				newSide = EnumFacing.WEST;
			if (newSide != null) {
				BlockDescription blockDesc = mat.getBlock(MaterialBlockType.VERTICAL_SLAB);
				PlaceParams newParams = new PlaceParams(params.pos, newSide.getOpposite(),
						new Vec3d(0.5d + 0.5d * newSide.getFrontOffsetX(), 0.5d,
								0.5d + 0.5d * newSide.getFrontOffsetZ()), 1.0d);
				return placeBlock(world, placer, newParams,
						mat.getBlock(MaterialBlockType.VERTICAL_SLAB.getStackType()).getStack().copy(),
						blockDesc.getBlock());
			}
		}
		return null;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player,
			ItemStack stack) {
		Block block = world.getBlockState(pos).getBlock();

		if (!block.isReplaceable(world, pos) &&
				!(block instanceof BlockBuildingBricksStep)) {
			pos = pos.offset(side);
			block = world.getBlockState(pos).getBlock();
		}
		if (block instanceof BlockBuildingBricksStep)
			return true;

		return false;
	}
}
