package com.hea3ven.buildingbricks.core.block.placement;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.block.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.item.ItemMaterialBlock;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.tools.commonutils.util.PlaceParams;

public abstract class PlacementHandlerBase implements IBlockPlacementHandler {
	protected IBlockState calculatePlaceState(World world, EntityLivingBase placer, PlaceParams params,
			ItemStack stack,
			Block block) {
		int meta = stack.getItem().getMetadata(stack.getMetadata());
		if (block instanceof BlockMaterial && !(stack.getItem() instanceof ItemMaterialBlock))
			meta = MaterialRegistry.getMeta(MaterialRegistry.getMaterialForStack(stack));
		return block.onBlockPlaced(world, params.pos, params.side, (float) params.hit.xCoord,
				(float) params.hit.yCoord, (float) params.hit.zCoord, meta, placer);
	}
}
