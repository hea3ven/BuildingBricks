package com.hea3ven.buildingbricks.core.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.common.property.IExtendedBlockState;

import com.hea3ven.buildingbricks.core.blocks.properties.PropertyMaterial;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class TileEntityMaterial extends TileEntity {

	public static final PropertyMaterial MATERIAL = new PropertyMaterial();

	private Material material;

	public static IExtendedBlockState setStateMaterial(IExtendedBlockState state, Material mat) {
		return state.withProperty(MATERIAL, mat);
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Material material() {
		if (material != null)
			return material;
		else
			return MaterialRegistry.get("andesite");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setString("material", material.materialId());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		material = MaterialRegistry.get(nbt.getString("material"));
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(pos, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}
}
