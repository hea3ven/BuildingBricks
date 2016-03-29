package com.hea3ven.buildingbricks.compat.mcmultipart.multipart;

import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IPartFactory;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.property.IExtendedBlockState;

import com.hea3ven.buildingbricks.core.blocks.base.BlockMaterial;

public class MultipartBlockMaterialFactory implements IPartFactory {
	@Override
	public IMultipart createPart(ResourceLocation type, boolean client) {
		Block block = Block.REGISTRY.getObject(type);
		if (block instanceof BlockMaterial)
			return new MultipartBlockMaterial(block, (IExtendedBlockState) block.getDefaultState());
		else
			return new MultipartBlockWrapper(block, block.getDefaultState());
	}
}
