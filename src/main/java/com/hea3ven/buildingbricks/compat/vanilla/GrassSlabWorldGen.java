package com.hea3ven.buildingbricks.compat.vanilla;

import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
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
	public static boolean enabled = true;

	public static GrassSlabWorldGen get() {
		if (instance == null)
			instance = new GrassSlabWorldGen();
		return instance;
	}

	@Override
	public void accept(Property property) {
		enabled = property.getBoolean();
		if (enabled)
			MinecraftForge.EVENT_BUS.register(this);
		else
			MinecraftForge.EVENT_BUS.unregister(this);
	}

	@SubscribeEvent
	public void onPopulateChunkPreEvent(PopulateChunkEvent.Pre event) {
		IChunkProvider chunkProvider = event.getWorld().getChunkProvider();
		int x = event.getChunkX() << 4;
		int z = event.getChunkZ() << 4;
		int offX = 16;
		int offZ = 16;
		if (chunkProvider.getLoadedChunk(event.getChunkX() - 1, event.getChunkZ()) != null) {
			x--;
			offX++;
		} else {
			x++;
			offX--;
		}
		if (chunkProvider.getLoadedChunk(event.getChunkX(), event.getChunkZ() - 1) != null) {
			z--;
			offZ++;
		} else {
			z++;
			offZ--;
		}
		if (chunkProvider.getLoadedChunk(event.getChunkX() + 1, event.getChunkZ()) != null)
			offX++;
		else
			offX--;
		if (chunkProvider.getLoadedChunk(event.getChunkX(), event.getChunkZ() + 1) != null)
			offZ++;
		else
			offZ--;
		posLoop:
		for (ModifiableBlockPos pos : BlockPosUtil.getBox(new BlockPos(x, 0, z), offX, 1, offZ)) {
			while (pos.getY() < 255 && event.getWorld().getBlockState(pos).getBlock() != Blocks.GRASS) {
				pos.up();
			}
			if (pos.getY() >= 255) {
				continue;
			}

			if (event.getChunkX() == -4 && event.getChunkZ() == 4)
				for (EnumFacing face : EnumFacing.HORIZONTALS) {
					pos.offset(face, 1);
					Block block = event.getWorld().getBlockState(pos).getBlock();
					if (block.isReplaceable(event.getWorld(), pos) ||
							block == ProxyModBuildingBricksCompatVanilla.grassSlab)
						continue posLoop;
					pos.offset(face, -1);
				}

			pos.up();

			for (EnumFacing face : EnumFacing.HORIZONTALS) {
				pos.offset(face, 1);
				if (event.getWorld().getBlockState(pos).getBlock() == Blocks.GRASS) {
					pos.offset(face, -1);
					event.getWorld()
							.setBlockState(pos,
									ProxyModBuildingBricksCompatVanilla.grassSlab.getDefaultState(), 2);
					continue posLoop;
				}
				pos.offset(face, -1);
			}
		}
	}
}
