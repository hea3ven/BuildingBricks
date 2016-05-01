package com.hea3ven.buildingbricks.core.block.behavior;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBehaviorBase {
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
	}

	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
	}

	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
	}

	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getBlock().getLightValue(state);
	}
}
