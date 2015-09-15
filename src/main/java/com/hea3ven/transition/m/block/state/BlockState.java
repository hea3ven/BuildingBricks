package com.hea3ven.transition.m.block.state;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;

import com.hea3ven.transition.m.block.properties.IProperty;

public class BlockState {

	public class BlockStateImpl implements IBlockState {

		private BlockState blockState;
		private Map<IProperty, Object> values = Maps.newLinkedHashMap();

		public BlockStateImpl(BlockState blockState) {
			this.blockState = blockState;
		}

		@Override
		public Object getValue(IProperty prop) {
			return values.get(prop);
		}

		@Override
		public IBlockState withProperty(IProperty prop, Object value) {
			values.put(prop, value);
			return this;
		}

		@Override
		public Block getBlock() {
			return blockState.getBlock();
		}

	}

	private Block block;
	private IProperty[] props;
	private IBlockState defaultState;

	public BlockState(Block block, IProperty[] props) {
		this.block = block;
		this.props = props;
		this.defaultState = getBaseState();
	}

	public IBlockState getBaseState() {
		return new BlockStateImpl(this);
	}

	public Collection<IProperty> getProperties() {
		return Lists.newArrayList(props);
	}

	public Block getBlock() {
		return block;
	}

	public void setDefault(IBlockState state) {
		defaultState = state;
	}

	public IBlockState getDefault() {
		return defaultState;
	}

}
