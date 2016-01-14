package com.hea3ven.buildingbricks.core.materials.mapping;

import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import com.hea3ven.buildingbricks.core.network.MaterialIdMappingCheckMessage;

public class MaterialIdMappingChecker {
	@SubscribeEvent
	public void onClientLogin(PlayerLoggedInEvent event) {
		MaterialIdMappingCheckMessage.send(IdMappingLoader.checksum, (EntityPlayerMP) event.player);
	}
}
