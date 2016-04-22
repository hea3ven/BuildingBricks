package com.hea3ven.buildingbricks.core.blocks.placement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ItemBlockCanPlaceBlockOnSideEvent extends Event {
	private final ItemStack stack;
	private final EntityPlayer player;
	private final World world;
	private final BlockPos pos;
	private final EnumFacing side;

	private boolean canPlaceBlockOnSide;

	public ItemBlockCanPlaceBlockOnSideEvent(World world, BlockPos pos, EnumFacing side, EntityPlayer player,
			ItemStack stack) {

		this.world = world;
		this.pos = pos;
		this.side = side;
		this.player = player;
		this.stack = stack;

		canPlaceBlockOnSide = false;
	}

	public ItemStack getStack() {
		return stack;
	}

	public EntityPlayer getPlayer() {
		return player;
	}

	public World getWorld() {
		return world;
	}

	public BlockPos getPos() {
		return pos;
	}

	public EnumFacing getSide() {
		return side;
	}

	public boolean isCanPlaceBlockOnSide() {
		return canPlaceBlockOnSide;
	}

	public void setCanPlaceBlockOnSide(boolean canPlaceBlockOnSide) {
		this.canPlaceBlockOnSide = canPlaceBlockOnSide;
	}
}
