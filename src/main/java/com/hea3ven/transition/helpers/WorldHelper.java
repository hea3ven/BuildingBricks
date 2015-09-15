package com.hea3ven.transition.helpers;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.hea3ven.transition.m.block.state.IBlockState;
import com.hea3ven.transition.m.util.BlockPos;

public class WorldHelper {

	public static class WorldWrapper {

		private IBlockAccess world;

		public WorldWrapper(IBlockAccess world) {
			this.world = world;
		}

		public IBlockState getBlockState(BlockPos pos) {
			return BlockHelper
					.get(world.getBlock(pos.getX(), pos.getY(), pos.getZ()))
					.getStateFromMeta(world.getBlockMetadata(pos.getX(), pos.getY(), pos.getZ()));
		}

		public void setBlockState(BlockPos pos, IBlockState state, int flag) {
			((World) world).setBlock(pos.getX(), pos.getY(), pos.getZ(), state.getBlock(),
					BlockHelper.get(state.getBlock()).getMetaFromState(state), flag);
		}
	}

	public static WorldWrapper get(IBlockAccess world) {
		return new WorldWrapper(world);
	}

}
