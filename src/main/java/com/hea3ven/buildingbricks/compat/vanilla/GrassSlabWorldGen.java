package com.hea3ven.buildingbricks.compat.vanilla;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GrassSlabWorldGen {

	@SubscribeEvent
	public void asd(PopulateChunkEvent.Pre event) {
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

				pos = pos.up();
				int top = 0;
				if (event.world.getBlockState(pos.north()).getBlock() == Blocks.grass)
					top++;
				if (event.world.getBlockState(pos.east()).getBlock() == Blocks.grass)
					top++;
				if (event.world.getBlockState(pos.south()).getBlock() == Blocks.grass)
					top++;
				if (event.world.getBlockState(pos.west()).getBlock() == Blocks.grass)
					top++;
				pos = pos.down();
				int bottom = 0;
				if (event.world.getBlockState(pos.north()).getBlock() == Blocks.grass)
					bottom++;
				if (event.world.getBlockState(pos.east()).getBlock() == Blocks.grass)
					bottom++;
				if (event.world.getBlockState(pos.south()).getBlock() == Blocks.grass)
					bottom++;
				if (event.world.getBlockState(pos.west()).getBlock() == Blocks.grass)
					bottom++;
				if (top != 0 && (top - bottom) >=-2) {
					event.world.setBlockState(pos.up(),
							ModBuildingBricksCompatVanilla.grassSlab.getDefaultState(), 2);
				}
			}
		}
	}
}
