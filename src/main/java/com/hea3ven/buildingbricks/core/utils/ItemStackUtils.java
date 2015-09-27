package com.hea3ven.buildingbricks.core.utils;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ItemStackUtils {
	private static final Random RANDOM = new Random();

	public static void dropFromBlock(World world, BlockPos pos, ItemStack stack) {
		if (world.isRemote)
			return;

		float xOff = RANDOM.nextFloat() * 0.8F + 0.1F;
		float yOff = RANDOM.nextFloat() * 0.8F + 0.1F;
		float zOff = RANDOM.nextFloat() * 0.8F + 0.1F;

		EntityItem entityitem = new EntityItem(world, pos.getX() + (double) xOff,
				pos.getY() + (double) yOff, pos.getZ() + (double) zOff, stack.copy());

		if (stack.hasTagCompound()) {
			entityitem
					.getEntityItem()
					.setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
		}

		entityitem.motionX = RANDOM.nextGaussian() * 0.05d;
		entityitem.motionY = RANDOM.nextGaussian() * 0.05d + 0.20000000298023224d;
		entityitem.motionZ = RANDOM.nextGaussian() * 0.05d;
		world.spawnEntityInWorld(entityitem);
	}
}
