package com.hea3ven.buildingbricks.core.client.settings;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;

import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.network.TrowelRotateBlockTypeMessage;

@SideOnly(Side.CLIENT)
public class TrowelKeyBindings {

	private static TrowelKeyBindings instance = new TrowelKeyBindings();
	private KeyBinding trowelPrevKey;
	private KeyBinding trowelNextKey;

	public static void init() {
		MinecraftForge.EVENT_BUS.register(instance);
		instance.registerBindings();
	}

	public TrowelKeyBindings() {
		trowelPrevKey = new KeyBinding("key.trowelPrev", Keyboard.KEY_J, "key.categories.trowel");
		trowelNextKey = new KeyBinding("key.trowelNext", Keyboard.KEY_K, "key.categories.trowel");
	}

	private void registerBindings() {
		ClientRegistry.registerKeyBinding(trowelPrevKey);
		ClientRegistry.registerKeyBinding(trowelNextKey);
	}

	@SubscribeEvent
	public void onKeyInputEvent(InputEvent.KeyInputEvent event) {
		if (trowelPrevKey.isPressed()) {
			ItemStack stack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
			if (stack != null && stack.getItem() == ModBuildingBricks.trowel) {
				TrowelRotateBlockTypeMessage.send(false);
			}
		} else if (trowelNextKey.isPressed()) {
			ItemStack stack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
			if (stack != null && stack.getItem() == ModBuildingBricks.trowel) {
				TrowelRotateBlockTypeMessage.send(true);
			}
		}
	}

	@SubscribeEvent
	public void onKeyInput(MouseEvent e) {
		if (e.dwheel != 0 && Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown()) {
			ItemStack stack = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
			if (stack != null && stack.getItem() == ModBuildingBricks.trowel) {
				TrowelRotateBlockTypeMessage.send(e.dwheel < 0);
				e.setCanceled(true);
			}
		}
	}
}
