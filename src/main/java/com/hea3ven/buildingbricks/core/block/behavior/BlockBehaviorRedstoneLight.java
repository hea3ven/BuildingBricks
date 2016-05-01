package com.hea3ven.buildingbricks.core.block.behavior;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBehaviorRedstoneLight extends BlockBehaviorBase {
	private final int lightLevel;

	public BlockBehaviorRedstoneLight(int lightLevel) {
		this.lightLevel = lightLevel;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		world.checkLightFor(EnumSkyBlock.BLOCK, pos);
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
		Minecraft.getMinecraft().setIngameNotInFocus();
//		world.addBlockEvent(pos, state.getBlock(), 0, 0);
//		world.markBlockRangeForRenderUpdate(pos, world.getChunkFromBlockCoords(pos), state, state, 2);
		world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 2);
//		world.scheduleUpdate(pos, state.getBlock(), 4);
//			world.checkLightFor(EnumSkyBlock.BLOCK, pos);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		world.checkLightFor(EnumSkyBlock.BLOCK, pos);
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		for (EnumFacing face : EnumFacing.VALUES) {
			IBlockState iblockstate = world.getBlockState(pos.offset(face));
			int power = iblockstate.getBlock().shouldCheckWeakPower(iblockstate, world, pos, face) ?
					world.getStrongPower(pos, face) : iblockstate.getWeakPower(world, pos, face);
			if (power > 0)
				return lightLevel;
		}
		return 0;
	}
}
