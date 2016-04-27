package com.hea3ven.buildingbricks.core.block.placement;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import com.hea3ven.tools.commonutils.util.PlaceParams;

@Cancelable
public class ItemBlockUseEvent extends Event {
	private final ItemStack stack;
	private final EntityPlayer player;
	private final World world;
	private final BlockPos pos;
	private final EnumHand hand;
	private final EnumFacing side;
	private final float hitX;
	private final float hitY;
	private final float hitZ;
	private final PlaceParams placeParams;

	private EnumActionResult actionResult;

	public ItemBlockUseEvent(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {

		this.stack = stack;
		this.player = player;
		this.world = world;
		this.pos = pos;
		this.hand = hand;
		this.side = side;
		this.hitX = hitX;
		this.hitY = hitY;
		this.hitZ = hitZ;
		placeParams = new PlaceParams(pos, side, hitX, hitY, hitZ);
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

	public EnumHand getHand() {
		return hand;
	}

	public EnumFacing getSide() {
		return side;
	}

	public float getHitX() {
		return hitX;
	}

	public float getHitY() {
		return hitY;
	}

	public float getHitZ() {
		return hitZ;
	}

	public PlaceParams getPlaceParams() {
		return placeParams;
	}

	public EnumActionResult getActionResult() {
		return actionResult;
	}

	public void setActionResult(EnumActionResult actionResult) {
		this.actionResult = actionResult;
	}
}
