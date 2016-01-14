package com.hea3ven.buildingbricks.compat.vanilla;

import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.chunk.IChunkProvider;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.hea3ven.tools.commonutils.util.BlockPosUtil;
import com.hea3ven.tools.commonutils.util.ModifiableBlockPos;

public class GrassSlabWorldGen implements Consumer<Property> {

	private static GrassSlabWorldGen instance;

	public static GrassSlabWorldGen get() {
		if (instance == null)
			instance = new GrassSlabWorldGen();
		return instance;
	}

	@Override
	public void accept(Property property) {
		if (property.getBoolean())
			MinecraftForge.EVENT_BUS.register(this);
		else
			MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void onPopulateChunkPreEvent(PopulateChunkEvent.Pre event) {
		IChunkProvider chunkProvider = event.world.getChunkProvider();
		int x = event.chunkX << 4;
		int z = event.chunkZ << 4;
		int offX = 16;
		int offZ = 16;
		if (chunkProvider.chunkExists(event.chunkX - 1, event.chunkZ)) {
			x--;
			offX++;
		} else {
			x++;
			offX--;
		}
		if (chunkProvider.chunkExists(event.chunkX, event.chunkZ - 1)) {
			z--;
			offZ++;
		} else {
			z++;
			offZ--;
		}
		if (chunkProvider.chunkExists(event.chunkX + 1, event.chunkZ))
			offX++;
		else
			offX--;
		if (chunkProvider.chunkExists(event.chunkX, event.chunkZ + 1))
			offZ++;
		else
			offZ--;
		posLoop:
		for (ModifiableBlockPos pos : BlockPosUtil.getBox(new BlockPos(x, 0, z), offX, 1, offZ)) {
			while (pos.getY() < 255 && event.world.getBlockState(pos).getBlock() != Blocks.grass) {
				pos.up();
			}
			if (pos.getY() >= 255) {
				continue;
			}

			if (event.chunkX == -4 && event.chunkZ == 4)
			for (EnumFacing face : EnumFacing.HORIZONTALS) {
				pos.offset(face, 1);
				Block block = event.world.getBlockState(pos).getBlock();
				if (block.isReplaceable(event.world, pos) ||
						block == ModBuildingBricksCompatVanilla.grassSlab)
					continue posLoop;
				pos.offset(face, -1);
			}

			pos.up();

			for (EnumFacing face : EnumFacing.HORIZONTALS) {
				pos.offset(face, 1);
				if (event.world.getBlockState(pos).getBlock() == Blocks.grass) {
					pos.offset(face, -1);
					event.world.setBlockState(pos, ModBuildingBricksCompatVanilla.grassSlab.getDefaultState(),
							2);
					continue posLoop;
				}
				pos.offset(face, -1);
			}
		}
	}
}
