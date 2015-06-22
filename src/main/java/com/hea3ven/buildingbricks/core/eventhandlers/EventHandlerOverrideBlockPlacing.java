package com.hea3ven.buildingbricks.core.eventhandlers;


import java.util.HashMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.hea3ven.buildingbricks.ModBuildingBricks;

public class EventHandlerOverrideBlockPlacing {
	public class SlabOverride {

		public EnumFacing facing;

		public SlabOverride(EnumFacing face) {
			facing = face;
		}

	}

	private HashMap<EntityPlayer, SlabOverride> slabOverrides = new HashMap<EntityPlayer, SlabOverride>();
	@SubscribeEvent
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (event.action == Action.RIGHT_CLICK_BLOCK) {
			ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
			if (stack != null && stack.getItem() == Item.getItemFromBlock(Blocks.stone_slab)) {
				if (!event.world.isRemote) {
					if (event.face.getAxis() != Axis.Y) {
						slabOverrides.put(event.entityPlayer, new SlabOverride(event.face.getOpposite()));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlaceEvent(PlaceEvent event){
		if (event.state.getBlock() == Blocks.stone_slab) {
			SlabOverride override = slabOverrides.get(event.player);
			if (override != null) {
				IBlockState state = ModBuildingBricks.andesiteSlab.getDefaultState();
				state = ModBuildingBricks.andesiteSlab.setStateFacing(state, override.facing);
				event.world.setBlockState(event.pos, state);
				slabOverrides.remove(event.player);
			}
		}
	}
}
