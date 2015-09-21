package com.hea3ven.transition.helpers;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.hea3ven.transition.m.block.properties.IProperty;
import com.hea3ven.transition.m.block.state.IBlockState;
import com.hea3ven.transition.m.util.BlockPos;

public class BlockHelper {
	public static interface IBlock {

		IBlockState getDefaultState();

		boolean isReplaceable(World world, BlockPos pos);

		IBlockState getStateFromMeta(int meta);

		int getMetaFromState(IBlockState state);
	}

	public static class BlockWrapper implements IBlock {

		private Block block;
		private BlockWrapperBlockState defaultState;

		public BlockWrapper(Block block) {
			this.block = block;
			this.defaultState = new BlockWrapperBlockState(0);
		}

		@Override
		public IBlockState getDefaultState() {
			return defaultState;
		}

		@Override
		public boolean isReplaceable(World world, BlockPos pos) {
			return block.isReplaceable(world, pos.getX(), pos.getY(), pos.getZ());
		}

		@Override
		public IBlockState getStateFromMeta(int meta) {
			return new BlockWrapperBlockState(meta);
		}

		@Override
		public int getMetaFromState(IBlockState state) {
			return ((BlockWrapperBlockState) state).meta;
		}

		class BlockWrapperBlockState implements IBlockState {

			private int meta;

			public BlockWrapperBlockState(int meta) {
				this.meta = meta;
			}

			@Override
			public Object getValue(IProperty prop) {
				return null;
			}

			@Override
			public IBlockState withProperty(IProperty prop, Object value) {
				return this;
			}

			@Override
			public Block getBlock() {
				return block;
			}

		}
	}

	public static IBlock get(Block block) {
		if (block instanceof IBlock)
			return (IBlock) block;
		else
			return new BlockWrapper(block);
	}

}
