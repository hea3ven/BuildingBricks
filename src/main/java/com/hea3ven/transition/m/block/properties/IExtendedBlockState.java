package com.hea3ven.transition.m.block.properties;

import com.hea3ven.transition.f.common.property.IUnlistedProperty;
import com.hea3ven.transition.m.block.state.IBlockState;

public interface IExtendedBlockState extends IBlockState {

	IExtendedBlockState withProperty(IUnlistedProperty<?> material, Object value);

}
