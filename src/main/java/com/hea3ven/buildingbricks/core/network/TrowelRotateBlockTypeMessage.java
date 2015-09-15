package com.hea3ven.buildingbricks.core.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

import com.hea3ven.buildingbricks.ModBuildingBricks;

public class TrowelRotateBlockTypeMessage implements IMessage {

	public static void send(boolean forward) {
		ModBuildingBricks.netChannel.sendToServer(new TrowelRotateBlockTypeMessage(forward));
	}

	public static class Handler implements IMessageHandler<TrowelRotateBlockTypeMessage, IMessage> {

		@Override
		public IMessage onMessage(TrowelRotateBlockTypeMessage message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			ItemStack stack = player.getCurrentEquippedItem();
			if (stack.getItem() == ModBuildingBricks.trowel) {
				if (message.forward) {
					ModBuildingBricks.trowel.setNextBlockRotation(stack);
				} else {
					ModBuildingBricks.trowel.setPrevBlockRotation(stack);
				}
			}
			return null;
		}
	}

	private boolean forward;

	public TrowelRotateBlockTypeMessage() {
	}

	private TrowelRotateBlockTypeMessage(boolean forward) {
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
