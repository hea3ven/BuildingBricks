package com.hea3ven.buildingbricks.compat.mcmultipart.multipart;

import mcmultipart.microblock.Microblock;
import mcmultipart.multipart.Multipart;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class MultipartBlockWrapper extends Multipart {
	protected final Block block;
	protected IBlockState state;

	public MultipartBlockWrapper(Block block, IBlockState state) {
		this.block = block;
		this.state = state;
	}

	@Override
	public ResourceLocation getType() {
		return Block.REGISTRY.getNameForObject(block);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return block.getBlockState();
	}

	@Override
	public IBlockState getExtendedState(IBlockState state) {
		return this.state;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setByte("meta", (byte) block.getMetaFromState(state));
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		state = block.getStateFromMeta(tag.getByte("meta"));
	}

	@Override
	public void writeUpdatePacket(PacketBuffer buf) {
		buf.writeByte((byte) block.getMetaFromState(state));
	}

	@Override
	public void readUpdatePacket(PacketBuffer buf) {
		state = block.getStateFromMeta(buf.readByte());
	}

	@Override
	public ResourceLocation getModelPath() {
		return Block.REGISTRY.getNameForObject(block);
	}

	@Override
	public void addSelectionBoxes(List<AxisAlignedBB> list) {
		list.add(block.getSelectedBoundingBox(state, null, new BlockPos(0, 0, 0)));
	}

	@Override
	public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
		AxisAlignedBB box = block.getBoundingBox(state, null, null);
		if (box.intersectsWith(mask))
			list.add(box);
	}

	public AxisAlignedBB getBoundingBox() {
		return block.getBoundingBox(state, null, null);
	}
}