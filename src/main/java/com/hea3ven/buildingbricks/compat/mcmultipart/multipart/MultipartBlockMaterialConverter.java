package com.hea3ven.buildingbricks.compat.mcmultipart.multipart;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IPartConverter;

import java.util.Collection;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import net.minecraftforge.common.property.IExtendedBlockState;

import com.hea3ven.buildingbricks.core.blocks.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRegistry;

public class MultipartBlockMaterialConverter implements IPartConverter {
	@Override
	public Collection<Block> getConvertableBlocks() {
		return MaterialBlockRegistry.instance.getAllBlocks();
	}

	@Override
	public Collection<? extends IMultipart> convertBlock(IBlockAccess world, BlockPos pos,
			boolean simulated) {
		IBlockState state = world.getBlockState(pos);
		state = state.getBlock().getExtendedState(state, world, pos);
		if (state.getBlock() instanceof BlockMaterial)
			return Lists.newArrayList(
					new MultipartBlockMaterial(state.getBlock(), (IExtendedBlockState) state));
		else
			return Lists.newArrayList(new MultipartBlockWrapper(state.getBlock(), state));
	}
}
