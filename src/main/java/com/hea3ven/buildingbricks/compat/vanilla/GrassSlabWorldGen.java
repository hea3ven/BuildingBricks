package com.hea3ven.buildingbricks.compat.vanilla;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.event.terraingen.PopulateChunkEvent;

import com.hea3ven.transition.helpers.BlockHelper;
import com.hea3ven.transition.helpers.WorldHelper;
import com.hea3ven.transition.m.util.BlockPos;

public class GrassSlabWorldGen {

	@SubscribeEvent
	public void onPopulateChunkPreEvent(PopulateChunkEvent.Pre event) {
		BlockPos startPos = new BlockPos(event.chunkX << 4, 0, event.chunkZ << 4);
		for (int xOff = 0; xOff < 16; xOff++) {
			for (int zOff = 0; zOff < 16; zOff++) {
				BlockPos pos = startPos.add(xOff, 0, zOff);
				while (pos.getY() < 255
						&& WorldHelper.get(event.world).getBlockState(pos).getBlock() != Blocks.grass) {
					pos = pos.up();
				}
				if (pos.getY() >= 255)
					continue;

				Block block = WorldHelper.get(event.world).getBlockState(pos.north()).getBlock();
				if (BlockHelper.get(block).isReplaceable(event.world, pos.north())
						|| block == ModBuildingBricksCompatVanilla.grassSlab)
					continue;
				block = WorldHelper.get(event.world).getBlockState(pos.east()).getBlock();
				if (BlockHelper.get(block).isReplaceable(event.world, pos.east())
						|| block == ModBuildingBricksCompatVanilla.grassSlab)
					continue;
				block = WorldHelper.get(event.world).getBlockState(pos.south()).getBlock();
				if (BlockHelper.get(block).isReplaceable(event.world, pos.south())
						|| block == ModBuildingBricksCompatVanilla.grassSlab)
					continue;
				block = WorldHelper.get(event.world).getBlockState(pos.west()).getBlock();
				if (BlockHelper.get(block).isReplaceable(event.world, pos.west())
						|| block == ModBuildingBricksCompatVanilla.grassSlab)
					continue;

				if (WorldHelper.get(event.world).getBlockState(pos.north().up()).getBlock() == Blocks.grass) {
					WorldHelper.get(event.world).setBlockState(pos.up(),
							BlockHelper.get(ModBuildingBricksCompatVanilla.grassSlab).getDefaultState(), 2);
					continue;
				}
				if (WorldHelper.get(event.world).getBlockState(pos.east().up()).getBlock() == Blocks.grass) {
					WorldHelper.get(event.world).setBlockState(pos.up(),
							BlockHelper.get(ModBuildingBricksCompatVanilla.grassSlab).getDefaultState(), 2);
					continue;
				}
				if (WorldHelper.get(event.world).getBlockState(pos.south().up()).getBlock() == Blocks.grass) {
					WorldHelper.get(event.world).setBlockState(pos.up(),
							BlockHelper.get(ModBuildingBricksCompatVanilla.grassSlab).getDefaultState(), 2);
					continue;
				}
				if (WorldHelper.get(event.world).getBlockState(pos.west().up()).getBlock() == Blocks.grass) {
					WorldHelper.get(event.world).setBlockState(pos.up(),
							BlockHelper.get(ModBuildingBricksCompatVanilla.grassSlab).getDefaultState(), 2);
					continue;
				}

			}
		}
	}
}
