package com.hea3ven.buildingbricks.core.network;

import java.io.IOException;

import io.netty.buffer.ByteBuf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ChatComponentTranslation;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.mapping.MaterialIdMapping;

public class MaterialIdMappingCheckMessage implements IMessage {

	public static void send(MaterialIdMapping checksum, EntityPlayerMP player) {
		ModBuildingBricks.proxy.getNetChannel().sendTo(new MaterialIdMappingCheckMessage(checksum), player);
	}

	public static class Handler implements IMessageHandler<MaterialIdMappingCheckMessage, IMessage> {

		@Override
		public IMessage onMessage(MaterialIdMappingCheckMessage message, MessageContext ctx) {
			if (message.data != null && !MaterialIdMapping.isInvalid(message.data)) {
				EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
				while (player == null) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					player = Minecraft.getMinecraft().thePlayer;
				}
				player.addChatMessage(
						new ChatComponentTranslation("buildingbricks.chat.idMappingsInvalid"));
			}
			return null;
		}
	}

	private NBTTagCompound data;

	public MaterialIdMappingCheckMessage() {
	}

	private MaterialIdMappingCheckMessage(MaterialIdMapping idMapping) {
		data = new NBTTagCompound();
		for (Material mat : MaterialRegistry.getAll()) {
			data.setShort(mat.getMaterialId(), idMapping.getIdForMaterial(mat));
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			data = new PacketBuffer(buf).readNBTTagCompoundFromBuffer();
		} catch (IOException e) {
			MaterialIdMapping.logger.error("Could not load data from the server", e);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		new PacketBuffer(buf).writeNBTTagCompoundToBuffer(data);
	}
}
