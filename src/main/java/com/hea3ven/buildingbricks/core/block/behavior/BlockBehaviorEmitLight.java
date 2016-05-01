package com.hea3ven.buildingbricks.core.block.behavior;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBehaviorEmitLight extends BlockBehaviorBase {
	private final int lightLevel;

	public BlockBehaviorEmitLight(int lightLevel) {
		this.lightLevel = lightLevel;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		world.checkLightFor(EnumSkyBlock.BLOCK, pos);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return lightLevel;
	}
}
