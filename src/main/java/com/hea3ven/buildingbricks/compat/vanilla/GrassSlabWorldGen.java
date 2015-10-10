package com.hea3ven.buildingbricks.compat.vanilla;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.hea3ven.tools.commonutils.util.BlockPosUtil;
import com.hea3ven.tools.commonutils.util.ModifiableBlockPos;

public class GrassSlabWorldGen {

	@SubscribeEvent
	public void onPopulateChunkPreEvent(PopulateChunkEvent.Pre event) {
		for (ModifiableBlockPos pos : BlockPosUtil
				.getBox(new BlockPos(event.chunkX << 4, 0, event.chunkZ << 4), 16, 1, 16)) {
			while (pos.getY() < 255 && event.world.getBlockState(pos).getBlock() != Blocks.grass) {
				pos.up();
			}
			if (pos.getY() >= 255)
				continue;

			pos.north();
			Block block = event.world.getBlockState(pos).getBlock();
			if (block.isReplaceable(event.world, pos)
					|| block == ModBuildingBricksCompatVanilla.grassSlab)
				continue;
			pos.south(2);
			block = event.world.getBlockState(pos).getBlock();
			if (block.isReplaceable(event.world, pos)
					|| block == ModBuildingBricksCompatVanilla.grassSlab)
				continue;
			pos.north();
			pos.east();
			block = event.world.getBlockState(pos).getBlock();
			if (block.isReplaceable(event.world, pos)
					|| block == ModBuildingBricksCompatVanilla.grassSlab)
				continue;
			pos.west(2);
			block = event.world.getBlockState(pos).getBlock();
			if (block.isReplaceable(event.world, pos)
					|| block == ModBuildingBricksCompatVanilla.grassSlab)
				continue;
			pos.east();

			pos.up();

			if (event.world.getBlockState(pos.north()).getBlock() == Blocks.grass) {
				event.world.setBlockState(pos.south(),
						ModBuildingBricksCompatVanilla.grassSlab.getDefaultState(), 2);
				continue;
			}
			if (event.world.getBlockState(pos.south(2)).getBlock() == Blocks.grass) {
				event.world.setBlockState(pos.north(),
						ModBuildingBricksCompatVanilla.grassSlab.getDefaultState(), 2);
				continue;
			}
			pos.north();
			if (event.world.getBlockState(pos.east()).getBlock() == Blocks.grass) {
				event.world.setBlockState(pos.west(),
						ModBuildingBricksCompatVanilla.grassSlab.getDefaultState(), 2);
				continue;
			}
			if (event.world.getBlockState(pos.west(2)).getBlock() == Blocks.grass) {
				event.world.setBlockState(pos.east(),
						ModBuildingBricksCompatVanilla.grassSlab.getDefaultState(), 2);
				continue;
			}
		}
	}
}
