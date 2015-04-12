package com.hea3ven.buildingbricks.core.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.hea3ven.buildingbricks.ModBuildingBricks;

public class ArchToolsRotateBlockTypeMessage implements IMessage {

	public static void send(boolean forward) {
		ModBuildingBricks.netChannel.sendToServer(new ArchToolsRotateBlockTypeMessage(forward));
	}

	public static class Handler implements IMessageHandler<ArchToolsRotateBlockTypeMessage, IMessage> {

		@Override
		public IMessage onMessage(ArchToolsRotateBlockTypeMessage message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			ItemStack stack = player.getCurrentEquippedItem();
			if (stack.getItem() == ModBuildingBricks.architectTools) {
				if (message.forward) {
					ModBuildingBricks.architectTools.setNextBlockRotation(stack);
				} else {
					ModBuildingBricks.architectTools.setPrevBlockRotation(stack);
				}
			}
			return null;
		}
	}

	private boolean forward;

	public ArchToolsRotateBlockTypeMessage() {
	}

	private ArchToolsRotateBlockTypeMessage(boolean forward) {
		this.forward = forward;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		forward = buf.getBoolean(0);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(forward);
	}

}
