package com.hea3ven.buildingbricks.compat.mcmultipart.multipart;

import java.security.InvalidParameterException;

import com.google.common.base.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import net.minecraftforge.common.property.IExtendedBlockState;

import com.hea3ven.buildingbricks.core.blocks.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;

public class MultipartBlockMaterial extends MultipartBlockWrapper {
	public MultipartBlockMaterial(Block block, IExtendedBlockState state) {
		super(block, state);
		if (!(block instanceof BlockMaterial))
			throw new InvalidParameterException("the block must be a BlockMaterial");
	}

	private IExtendedBlockState getExState() {
		return (IExtendedBlockState) state;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if (getExState().getValue(TileMaterial.MATERIAL) != null)
			tag.setString("material", getExState().getValue(TileMaterial.MATERIAL).getMaterialId());
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.hasKey("material"))
			state = getExState().withProperty(TileMaterial.MATERIAL,
					MaterialRegistry.get(tag.getString("material")));
	}

	@Override
	public void writeUpdatePacket(PacketBuffer buf) {
		super.writeUpdatePacket(buf);
		if (getExState().getValue(TileMaterial.MATERIAL) != null)
			buf.writeString(getExState().getValue(TileMaterial.MATERIAL).getMaterialId());
		else
			buf.writeString("");
	}

	@Override
	public void readUpdatePacket(PacketBuffer buf) {
		super.readUpdatePacket(buf);
		String materialId = buf.readStringFromBuffer(255);
		if (!materialId.isEmpty())
			state = getExState().withProperty(TileMaterial.MATERIAL, MaterialRegistry.get(materialId));
	}
}
