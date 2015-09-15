package com.hea3ven.transition.helpers;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.hea3ven.transition.m.block.state.IBlockState;
import com.hea3ven.transition.m.util.BlockPos;

public class BlockHelper {
	public static interface IBlock {

		IBlockState getDefaultState();

		boolean isReplaceable(World world, BlockPos pos);

		IBlockState getStateFromMeta(int meta);

		int getMetaFromState(IBlockState state);
	}

	public static IBlock get(Block block) {
		return (IBlock) block;
	}

}
