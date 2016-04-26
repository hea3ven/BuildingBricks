package com.hea3ven.buildingbricks.compat.mcmultipart.microblock;

import mcmultipart.microblock.IMicroMaterial;
import mcmultipart.microblock.IMicroblockPlacementGrid;
import mcmultipart.microblock.MicroblockClass;
import mcmultipart.microblock.MicroblockPlacement;
import mcmultipart.multipart.IMultipart;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import net.minecraftforge.common.property.IExtendedBlockState;

import com.hea3ven.buildingbricks.compat.mcmultipart.multipart.MultipartBlockMaterial;

public class MicroblockClassBlockMaterial extends MicroblockClass {
	private final Block block;

	public MicroblockClassBlockMaterial(Block block) {

		this.block = block;
	}

	@Override
	public String getType() {
		return Block.REGISTRY.getNameForObject(block).toString();
	}

	@Override
	public String getLocalizedName(IMicroMaterial material, int size) {
		return null;
	}

	@Override
	public ItemStack createStack(IMicroMaterial material, int size, int stackSize) {
		return null;
	}

	@Override
	public MicroblockPlacement getPlacement(World world, BlockPos pos, IMicroMaterial material, int size,
			RayTraceResult hit, EntityPlayer player) {
		return null;
	}

	@Override
	public IMicroblockPlacementGrid getPlacementGrid() {
		return null;
	}

	@Override
	public IMultipart create(boolean client) {
		return new MultipartBlockMaterial(block, (IExtendedBlockState) block.getDefaultState());
	}
}
