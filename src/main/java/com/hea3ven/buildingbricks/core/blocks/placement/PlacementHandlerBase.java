package com.hea3ven.buildingbricks.core.blocks.placement;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.hea3ven.tools.commonutils.util.PlaceParams;

public abstract class PlacementHandlerBase implements IBlockPlacementHandler {
	protected IBlockState calculatePlaceState(World world, EntityLivingBase placer, PlaceParams params,
			ItemStack stack,
			Block block) {
		int meta = stack.getItem().getMetadata(stack.getMetadata());
		return block.onBlockPlaced(world, params.pos, params.side, (float) params.hit.xCoord,
				(float) params.hit.yCoord, (float) params.hit.zCoord, meta, placer);
	}
}
