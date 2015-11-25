package com.hea3ven.buildingbricks.core.blocks.blockstate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;

public class BlockStateUtil {

	public static BlockState addProperties(Block block, BlockState parentBlockState, IProperty[] properties) {
		List<IProperty> newProperties = new ArrayList<>(parentBlockState.getProperties());
		Collections.addAll(newProperties, properties);
		return new BlockState(block, newProperties.toArray(new IProperty[newProperties.size()]));
	}
}
