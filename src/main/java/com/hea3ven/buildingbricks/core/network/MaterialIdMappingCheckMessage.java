package com.hea3ven.buildingbricks.core.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.mapping.IdMappingLoader;

public class MaterialIdMappingCheckMessage implements IMessage {

	public static void send(byte[] checksum, EntityPlayerMP player) {
		ModBuildingBricks.proxy.getNetChannel().sendTo(new MaterialIdMappingCheckMessage(checksum), player);
	}

	public static class Handler implements IMessageHandler<MaterialIdMappingCheckMessage, IMessage> {

		@Override
		public IMessage onMessage(MaterialIdMappingCheckMessage message, MessageContext ctx) {
			if (IdMappingLoader.isInvalid(message.checksum)) {
				EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
				if (player != null)
					player.addChatMessage(
							new ChatComponentTranslation("buildingbricks.chat.idMappingsInvalid"));
			}
			return null;
		}
	}

	private byte[] checksum;

	public MaterialIdMappingCheckMessage() {
	}

	private MaterialIdMappingCheckMessage(byte[] checksum) {
		this.checksum = checksum;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		checksum = new byte[16];
		buf.getBytes(0, checksum);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBytes(checksum);
	}
}
