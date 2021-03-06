package com.hea3ven.buildingbricks.core.client.settings;

import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.client.Minecraft;

import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.network.TrowelRotateBlockTypeMessage;
import com.hea3ven.tools.commonutils.util.PlayerUtil;
import com.hea3ven.tools.commonutils.util.PlayerUtil.HeldEquipment;

@SideOnly(Side.CLIENT)
public class TrowelKeyBindings {
	public static Consumer<KeyInputEvent> getOnTrowelPrev() {
		return event -> {
			HeldEquipment equipment =
					PlayerUtil.getHeldEquipment(Minecraft.getMinecraft().thePlayer, ModBuildingBricks.trowel);
			if (equipment != null && equipment.getStack().getItem() != null) {
				TrowelRotateBlockTypeMessage.send(false, null);
			}
		};
	}

	public static Consumer<KeyInputEvent> getOnTrowelNext() {
		return event -> {
			HeldEquipment equipment =
					PlayerUtil.getHeldEquipment(Minecraft.getMinecraft().thePlayer, ModBuildingBricks.trowel);
			if (equipment != null && equipment.getStack().getItem() != null) {
				TrowelRotateBlockTypeMessage.send(true, null);
			}
		};
	}

	public static Function<MouseEvent, Boolean> getOnTrowelScroll() {
		return event -> {
			TrowelRotateBlockTypeMessage.send(event.getDwheel() < 0, null);
			return true;
		};
	}

	public static Consumer<KeyInputEvent> getOnTrowelSelect(final MaterialBlockType blockType) {
		return event -> TrowelRotateBlockTypeMessage.send(false, blockType);
	}
}
