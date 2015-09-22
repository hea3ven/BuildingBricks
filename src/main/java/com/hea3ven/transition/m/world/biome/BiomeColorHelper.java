package com.hea3ven.transition.m.world.biome;

import net.minecraft.world.IBlockAccess;

import com.hea3ven.transition.m.util.BlockPos;

public class BiomeColorHelper {

	public static int getGrassColorAtPos(IBlockAccess world, BlockPos pos) {
		int l = 0;
		int i1 = 0;
		int j1 = 0;

		for (int k1 = -1; k1 <= 1; ++k1) {
			for (int l1 = -1; l1 <= 1; ++l1) {
				int i2 = world
						.getBiomeGenForCoords(pos.getX() + l1, pos.getZ() + k1)
						.getBiomeGrassColor(pos.getX() + l1, pos.getY(), pos.getZ() + k1);
				l += (i2 & 16711680) >> 16;
				i1 += (i2 & 65280) >> 8;
				j1 += i2 & 255;
			}
		}

		return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
	}

}
