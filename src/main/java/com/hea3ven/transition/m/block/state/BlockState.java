package com.hea3ven.transition.m.block.state;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

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
			if (values.get(prop) != null && values.get(prop).equals(value))
				return this;
			return blockState.getWithProperty(this, prop, value);
		}

		@Override
		public Block getBlock() {
			return blockState.getBlock();
		}

		void setValue(IProperty prop, Object value) {
			values.put(prop, value);
		}

	}

	private Block block;
	private IProperty[] props;
	private IBlockState defaultState;
	private Set<IBlockState> states = Sets.newHashSet();

	public BlockState(Block block, IProperty[] props) {
		this.block = block;
		this.props = props;
		this.defaultState = getBaseState();
	}

	public IBlockState getWithProperty(IBlockState baseState, IProperty newProp, Object value) {
		for (IBlockState state : states) {
			boolean matches = true;
			for (IProperty prop : props) {
				if (!prop.equals(newProp))
					matches = matches && state.getValue(prop).equals(baseState.getValue(prop));
			}
			if (matches)
				return state;
		}
		BlockStateImpl state = new BlockStateImpl(this);
		for (IProperty prop : props) {
			if (!prop.equals(newProp))
				state.setValue(prop, baseState.getValue(prop));
			else
				state.setValue(prop, value);
		}
		return state;
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
