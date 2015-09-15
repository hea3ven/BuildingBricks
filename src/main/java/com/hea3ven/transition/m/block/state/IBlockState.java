package com.hea3ven.transition.m.block.state;

import net.minecraft.block.Block;

import com.hea3ven.transition.m.block.properties.IProperty;

public interface IBlockState {

	Object getValue(IProperty prop);

	IBlockState withProperty(IProperty prop, Object value);

	Block getBlock();

}
