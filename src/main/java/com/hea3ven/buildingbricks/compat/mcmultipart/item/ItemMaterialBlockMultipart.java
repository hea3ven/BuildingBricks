package com.hea3ven.buildingbricks.compat.mcmultipart.item;

import mcmultipart.multipart.MultipartHelper;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.minecraftforge.common.property.IExtendedBlockState;

import com.hea3ven.buildingbricks.compat.mcmultipart.multipart.MultipartBlockWrapper;
import com.hea3ven.buildingbricks.core.blocks.BlockMaterialSlab;
import com.hea3ven.buildingbricks.core.blocks.base.BlockBuildingBricks;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;
import com.hea3ven.buildingbricks.core.items.ItemMaterialBlock;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;
import com.hea3ven.buildingbricks.core.util.BlockPlacingUtil;
import com.hea3ven.tools.commonutils.util.PlaceParams;

public class ItemMaterialBlockMultipart extends ItemMaterialBlock {
	public ItemMaterialBlockMultipart(Block block) {
		super(block);
	}

	protected boolean placeMultipart(ItemStack stack, EntityPlayer player, World world, PlaceParams params) {
		IExtendedBlockState state;
		MaterialBlockType thisType = ((BlockBuildingBricks) getBlock()).getBlockLogic().getBlockType();
		if (thisType == MaterialBlockType.SLAB)
			state = calculatePlaceStateSlab(params.side, params.hit);
		else if (thisType == MaterialBlockType.VERTICAL_SLAB)
			state = calculatePlaceStateVerticalSlab(params);
		else if (thisType == MaterialBlockType.STEP)
			state = calculatePlaceStateStep(params);
		else if (thisType == MaterialBlockType.CORNER)
			state = calculatePlaceStateCorner(params.side, params.hit);
		else
			return false;
		state = state.withProperty(TileMaterial.MATERIAL, getMaterial(stack));
		MultipartBlockWrapper part = new MultipartBlockWrapper(block, state);
		if (!MultipartHelper.canAddPart(world, params.pos, part))
			return false;

		List<AxisAlignedBB> boxes = new ArrayList<>();
		AxisAlignedBB partBox = part.getBoundingBox().offset(params.pos);
		world.getBlockState(params.pos).addCollisionBoxToList(world, params.pos, partBox, boxes, null);
		if (boxes.size() > 0)
			return false;

		if (!world.isRemote) {
			MultipartHelper.addPart(world, params.pos, part);
		}
		--stack.stackSize;
		SoundType soundtype = this.block.getSoundType();
		world.playSound(player, params.pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
				(soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

		return true;
	}

	private IExtendedBlockState calculatePlaceStateSlab(EnumFacing side, Vec3d hit) {
		EnumBlockHalf half;
		switch (side) {
			case UP:
				half = hit.yCoord < 0.5 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP;
				break;
			case DOWN:
				half = hit.yCoord > 0.5 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM;
				break;
			default:
				half = hit.yCoord > .5d ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM;
		}
		return (IExtendedBlockState) block.getDefaultState().withProperty(BlockMaterialSlab.HALF, half);
	}

	private IExtendedBlockState calculatePlaceStateVerticalSlab(PlaceParams params) {
		EnumFacing side;
		if (params.side.getHorizontalIndex() != -1 && BlockPlacingUtil.isInnerRing(params)) {
			side = params.side.getOpposite();
		} else {
			side = BlockPlacingUtil.getClosestSide(params);
		}
		return (IExtendedBlockState) block.getDefaultState().withProperty(BlockProperties.SIDE, side);
	}

	private IExtendedBlockState calculatePlaceStateStep(PlaceParams params) {
		boolean vertical;
		if (BlockPlacingUtil.isInnerRing(params)) {
			EnumFacing closeFace = BlockPlacingUtil.getClosestFace(params);
			vertical = params.side.getAxis() != Axis.Y && closeFace.getAxis() != Axis.Y;
		} else {
			vertical = params.side.getAxis() == Axis.Y;
		}

		EnumBlockHalf half;
		if (!vertical) {
			switch (params.side) {
				case UP:
					half = params.hit.yCoord < 0.5 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP;
					break;
				case DOWN:
					half = params.hit.yCoord > 0.5 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM;
					break;
				default:
					half = params.hit.yCoord > .5d ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM;
			}
		} else
			half = EnumBlockHalf.BOTTOM;

		EnumRotation rot;
		if (params.depth < 1) {
			if (params.hit.xCoord <= 0.5) {
				if (params.hit.zCoord <= 0.5)
					rot = EnumRotation.ROT0;
				else
					rot = EnumRotation.ROT270;
			} else {
				if (params.hit.zCoord <= 0.5)
					rot = EnumRotation.ROT90;
				else
					rot = EnumRotation.ROT180;
			}
		} else {
			rot = BlockPlacingUtil.getClosestCorner(params);
		}
//		switch (side) {
//			case DOWN:
//			case UP:
//				if (BlockPlacingUtil.isInnerRing(side, hit.xCoord, hit.yCoord, hit.zCoord)) {
//					vertical = false;
//					half = side == EnumFacing.DOWN ?
//							EnumBlockHalf.BOTTOM :
//							EnumBlockHalf.TOP;
//					rot = EnumRotation.getRotation(
//							BlockPlacingUtil.getClosestFace(side, hit.xCoord, hit.yCoord, hit.zCoord));
//				} else {
//					vertical = true;
//					half = EnumBlockHalf.BOTTOM;
//					rot = BlockPlacingUtil.getClosestCorner(side, hit.xCoord, hit.yCoord, hit.zCoord);
//				}
//				break;
//			default:
//				vertical = false;
//				half = hit.yCoord > .5d ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM;

//				rot = EnumRotation.getRotation(side);
//		}
		return (IExtendedBlockState) block.getDefaultState()
				.withProperty(BlockProperties.HALF, half)
				.withProperty(BlockProperties.VERTICAL, vertical)
				.withProperty(BlockProperties.ROTATION, rot);
	}

	private IExtendedBlockState calculatePlaceStateCorner(EnumFacing side, Vec3d hit) {
		EnumBlockHalf half;
		switch (side) {
			case UP:
				half = hit.yCoord < 0.5 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP;
				break;
			case DOWN:
				half = hit.yCoord > 0.5 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM;
				break;
			default:
				half = hit.yCoord > .5d ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM;
		}
		EnumRotation rot;
		if (hit.xCoord <= 0.5) {
			if (hit.zCoord <= 0.5)
				rot = EnumRotation.ROT0;
			else
				rot = EnumRotation.ROT270;
		} else {
			if (hit.zCoord <= 0.5)
				rot = EnumRotation.ROT90;
			else
				rot = EnumRotation.ROT180;
		}
		return (IExtendedBlockState) block.getDefaultState()
				.withProperty(BlockProperties.HALF, half)
				.withProperty(BlockProperties.ROTATION, rot);
	}

	@Override
	public boolean canItemEditBlocks() {
		return true;
	}
}
