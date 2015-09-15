package com.hea3ven.buildingbricks.core.blocks.blockstate;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;

import com.hea3ven.transition.m.block.properties.IProperty;
import com.hea3ven.transition.m.block.state.BlockState;

public class BlockStateUtil {

	public static BlockState addProperties(Block block, BlockState parentBlockState,
			IProperty[] properties) {
		List<IProperty> newProperties = new ArrayList<IProperty>(parentBlockState.getProperties());
		for (IProperty prop : properties) {
			newProperties.add(prop);
		}
		return new BlockState(block, newProperties.toArray(new IProperty[0]));
	}

}
