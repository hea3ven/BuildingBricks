package com.hea3ven.buildingbricks.core.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.tools.commonutils.util.PlayerUtil;
import com.hea3ven.tools.commonutils.util.PlayerUtil.HeldEquipment;

public class TrowelRotateBlockTypeMessage implements IMessage {

	public static void send(boolean forward, MaterialBlockType blockType) {
		ModBuildingBricks.proxy.getNetChannel()
				.sendToServer(new TrowelRotateBlockTypeMessage(forward, blockType));
	}

	public static class Handler implements IMessageHandler<TrowelRotateBlockTypeMessage, IMessage> {

		@Override
		public IMessage onMessage(TrowelRotateBlockTypeMessage message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			HeldEquipment equipment = PlayerUtil.getHeldEquipment(player, ModBuildingBricks.trowel);
			if (equipment != null && equipment.getStack().getItem() != null) {
				if (message.blockType != null) {
					ModBuildingBricks.trowel.setCurrentBlockType(equipment.getStack(), message.blockType);
				} else {
					if (message.forward) {
						ModBuildingBricks.trowel.setNextBlockRotation(equipment.getStack());
					} else {
						ModBuildingBricks.trowel.setPrevBlockRotation(equipment.getStack());
					}
				}
			}
			return null;
		}
	}

	private boolean forward;
	private MaterialBlockType blockType;

	public TrowelRotateBlockTypeMessage() {
	}

	private TrowelRotateBlockTypeMessage(boolean forward, MaterialBlockType blockType) {
		this.forward = forward;
		this.blockType = blockType;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		forward = buf.getBoolean(0);
		short blockTypeId = buf.getShort(1);
		blockType = blockTypeId >= 0 ? MaterialBlockType.getBlockType(blockTypeId) : null;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(forward);
		buf.writeShort(blockType != null ? blockType.ordinal() : -1);
	}
}
