package com.hea3ven.buildingbricks.compat.mcmultipart.blocks.placement;

import mcmultipart.MCMultiPartMod;
import mcmultipart.microblock.IMicroMaterial;
import mcmultipart.microblock.IMicroblock;
import mcmultipart.microblock.Microblock;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.PartSlot;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.property.IExtendedBlockState;

import com.hea3ven.buildingbricks.compat.mcmultipart.microblock.MicroblockBlockMaterial;
import com.hea3ven.buildingbricks.compat.mcmultipart.multipart.MultipartBlockMaterial;
import com.hea3ven.buildingbricks.compat.mcmultipart.multipart.MultipartBlockWrapper;
import com.hea3ven.buildingbricks.compat.mcmultipart.util.MicroblockUtil;
import com.hea3ven.buildingbricks.compat.mcmultipart.util.MultipartUtil;
import com.hea3ven.buildingbricks.core.blocks.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.blocks.placement.PlacementHandlerBase;
import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;
import com.hea3ven.buildingbricks.core.util.BlockPlacingUtil;
import com.hea3ven.tools.commonutils.util.PlaceParams;

public class SlabMultipartPlacementManager extends PlacementHandlerBase {
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
		IMicroMaterial microMat = MicroblockUtil.getMicroMaterial(mat);
		if(microMat == null)
			return EnumActionResult.PASS;
		IBlockState partState = calculatePlaceState(world, placer, params, stack, block);
//		MultipartBlockWrapper part;
//		if (partState.getBlock() instanceof BlockMaterial) {
			partState = ((IExtendedBlockState) partState)
					.withProperty(TileMaterial.MATERIAL, MaterialRegistry.getMaterialForStack(stack));
//			part = new MultipartBlockMaterial(block, (IExtendedBlockState) partState);
//		} else
//			part = new MultipartBlockWrapper(block, partState);

		IMicroblock part = new MicroblockBlockMaterial(microMat, PartSlot.DOWN, 4, world.isRemote, partState);

		if (!MultipartHelper.canAddPart(world, params.pos, part))
			return EnumActionResult.PASS;

		BlockPos pos = params.pos;
		if (!MultipartUtil.canPartFit(world, part, pos))
			return EnumActionResult.PASS;

		if (!world.isRemote) {
			MultipartHelper.addPart(world, params.pos, part);
		}
		--stack.stackSize;
		SoundType soundtype = state.getBlock().getSoundType();
		world.playSound((EntityPlayer) placer, params.pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
				(soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

		return EnumActionResult.SUCCESS;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer placer,
			ItemStack stack) {
		IBlockState state = world.getBlockState(pos);

		if (state.getBlock() == MCMultiPartMod.multipart)
			return true;

		if (MaterialBlockRegistry.instance.getAllBlocks().contains(state.getBlock()))
			return true;

		state = world.getBlockState(pos.offset(side));
		if (state.getBlock() == MCMultiPartMod.multipart)
			return true;

		if (MaterialBlockRegistry.instance.getAllBlocks().contains(state.getBlock()))
			return true;

		return false;
	}
}
