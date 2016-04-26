package com.hea3ven.buildingbricks.core.eventhandlers;

import net.minecraft.util.text.translation.I18n;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class EventHandlerShowMaterialTooltip {

	private static EventHandlerShowMaterialTooltip instance = new EventHandlerShowMaterialTooltip();

	public static EventHandlerShowMaterialTooltip getInstance() {
		return instance;
	}

	@SubscribeEvent
	public void onItemTooltipEvent(ItemTooltipEvent event) {
		Material mat = MaterialRegistry.getMaterialForStack(event.getItemStack());
		if (mat != null) {
			if (event.isShowAdvancedItemTooltips())
				event.getToolTip()
						.add(I18n.translateToLocalFormatted("buildingbricks.tooltip.material",
								mat.getLocalizedName(), mat.getMaterialId()));
		}
	}
}
