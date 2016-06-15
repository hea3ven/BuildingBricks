package com.hea3ven.buildingbricks.compat.vanilla;

import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import com.hea3ven.buildingbricks.compat.vanilla.block.BlockGrassSlab;
import com.hea3ven.tools.commonutils.util.BlockPosUtil;
import com.hea3ven.tools.commonutils.util.ModifiableBlockPos;

public class GrassSlabWorldGen implements Consumer<Property> {

	private static GrassSlabWorldGen instance;
	public static boolean enabled = true;

	@ObjectHolder("buildingbricks:grass_slab")
	public static final BlockGrassSlab grassSlab = new BlockGrassSlab();

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
		World world = event.getWorld();
		if (world.provider.getDimensionType() != DimensionType.OVERWORLD)
			return;
		IChunkProvider chunkProvider = world.getChunkProvider();
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
			while (pos.getY() < 255 && world.getBlockState(pos).getMaterial() != Material.GRASS) {
				pos.up();
			}
			if (pos.getY() >= 255) {
				continue;
			}

			for (EnumFacing face : EnumFacing.HORIZONTALS) {
				pos.offset(face, 1);
				Block block = world.getBlockState(pos).getBlock();
				if (block.isReplaceable(world, pos) || block == grassSlab)
					continue posLoop;
				pos.offset(face, -1);
			}

			pos.up();
			IBlockState replaceState = world.getBlockState(pos);
			if (!replaceState.getBlock().isReplaceable(world, pos) || replaceState.getBlock() == Blocks.WATER)
				continue;

			for (EnumFacing face : EnumFacing.HORIZONTALS) {
				pos.offset(face, 1);
				if (world.getBlockState(pos).getBlock() == Blocks.GRASS) {
					pos.offset(face, -1);
					world.setBlockState(pos, grassSlab.getDefaultState(), 2);
					pos.up();
					IBlockState upperBlockState = world.getBlockState(pos);
					if (!upperBlockState.getBlock().isAir(upperBlockState, world, pos) &&
							replaceState.getBlock() == upperBlockState.getBlock()) {
						world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
					}
					continue posLoop;
				}
				pos.offset(face, -1);
			}
		}
	}
}
