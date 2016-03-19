package com.hea3ven.buildingbricks.core.client.model;

import net.minecraft.block.state.IBlockState;

import com.hea3ven.tools.commonutils.client.model.SmartModelCached;
import com.hea3ven.tools.commonutils.util.BlockStateUtil;

public class ModelMaterialBlock extends SmartModelCached<Integer> {
	@Override
	protected Integer getKey(IBlockState state) {
		return BlockStateUtil.getHashCode(state);
	}
}
