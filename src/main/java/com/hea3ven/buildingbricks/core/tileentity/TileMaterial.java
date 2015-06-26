package com.hea3ven.buildingbricks.core.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.util.Constants.NBT;

import com.hea3ven.buildingbricks.core.blocks.properties.PropertyMaterial;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class TileMaterial extends TileEntity {

	public static final PropertyMaterial MATERIAL = new PropertyMaterial();

	private Material material;

	public Material getMaterial() {
		if (material != null)
			return material;
		else
			return MaterialRegistry.get("stone");
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public static IExtendedBlockState setStateMaterial(IExtendedBlockState state, Material mat) {
		return state.withProperty(MATERIAL, mat);
	}

	public static TileMaterial getTile(IBlockAccess world, BlockPos pos) {
		TileMaterial tile;
		TileEntity te = world.getTileEntity(pos);
		if (!(te instanceof TileMaterial))
			tile = null;
		tile = (TileMaterial) te;
		return tile;
	}

	public static Material getStackMaterial(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null || !nbt.hasKey("material", NBT.TAG_STRING)) {
			return MaterialRegistry.get("stone");
		}
		return MaterialRegistry.get(nbt.getString("material"));
	}

	public static void setStackMaterial(ItemStack stack, Material mat) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		nbt.setString("material", mat.materialId());
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

	public static IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileMaterial tile = getTile(world, pos);
		if (tile != null && tile.getMaterial() != null)
			return TileMaterial.setStateMaterial((IExtendedBlockState) state, tile.getMaterial());
		else
			return state;
	}
}
