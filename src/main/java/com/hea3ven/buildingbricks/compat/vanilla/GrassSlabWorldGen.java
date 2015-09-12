package com.hea3ven.buildingbricks.compat.vanilla;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GrassSlabWorldGen {

	@SubscribeEvent
	public void onPopulateChunkPreEvent(PopulateChunkEvent.Pre event) {
		BlockPos startPos = new BlockPos(event.chunkX << 4, 0, event.chunkZ << 4);
		for (int xOff = 0; xOff < 16; xOff++) {
			for (int zOff = 0; zOff < 16; zOff++) {
				BlockPos pos = startPos.add(xOff, 0, zOff);
				while (pos.getY() < 255
						&& event.world.getBlockState(pos).getBlock() != Blocks.grass) {
					pos = pos.up();
				}
				if (pos.getY() >= 255)
					continue;

				Block block = event.world.getBlockState(pos.north()).getBlock();
				if (block.isReplaceable(event.world, pos.north())
						|| block == ModBuildingBricksCompatVanilla.grassSlab)
					continue;
				block = event.world.getBlockState(pos.east()).getBlock();
				if (block.isReplaceable(event.world, pos.east())
						|| block == ModBuildingBricksCompatVanilla.grassSlab)
					continue;
				block = event.world.getBlockState(pos.south()).getBlock();
				if (block.isReplaceable(event.world, pos.south())
						|| block == ModBuildingBricksCompatVanilla.grassSlab)
					continue;
				block = event.world.getBlockState(pos.west()).getBlock();
				if (block.isReplaceable(event.world, pos.west())
						|| block == ModBuildingBricksCompatVanilla.grassSlab)
					continue;

				if (event.world.getBlockState(pos.north().up()).getBlock() == Blocks.grass) {
					event.world.setBlockState(pos.up(),
							ModBuildingBricksCompatVanilla.grassSlab.getDefaultState(), 2);
					continue;
				}
				if (event.world.getBlockState(pos.east().up()).getBlock() == Blocks.grass) {
					event.world.setBlockState(pos.up(),
							ModBuildingBricksCompatVanilla.grassSlab.getDefaultState(), 2);
					continue;
				}
				if (event.world.getBlockState(pos.south().up()).getBlock() == Blocks.grass) {
					event.world.setBlockState(pos.up(),
							ModBuildingBricksCompatVanilla.grassSlab.getDefaultState(), 2);
					continue;
				}
				if (event.world.getBlockState(pos.west().up()).getBlock() == Blocks.grass) {
					event.world.setBlockState(pos.up(),
							ModBuildingBricksCompatVanilla.grassSlab.getDefaultState(), 2);
					continue;
				}

			}
		}
	}
}
