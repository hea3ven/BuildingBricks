package com.hea3ven.buildingbricks.core.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.util.Constants.NBT;

import com.hea3ven.buildingbricks.core.blocks.properties.PropertyMaterial;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRegistry;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.utils.ItemStackUtils;
import com.hea3ven.transition.f.common.property.ExtendedBlockState;
import com.hea3ven.transition.f.common.property.IUnlistedProperty;
import com.hea3ven.transition.m.block.properties.IExtendedBlockState;
import com.hea3ven.transition.m.block.properties.IProperty;
import com.hea3ven.transition.m.block.state.BlockState;
import com.hea3ven.transition.m.block.state.IBlockState;
import com.hea3ven.transition.m.util.BlockPos;

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
		TileEntity te = world.getTileEntity(pos.getX(), pos.getY(), pos.getZ());
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

		nbt.setString("material", getMaterial().materialId());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		setMaterial(MaterialRegistry.get(nbt.getString("material")));
	}

	@Override
	public boolean shouldRefresh(Block oldBlock, Block newBlock, int oldMeta, int newMeta,
			World world, int x, int y, int z) {
		return oldBlock != newBlock;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
	}

	public static BlockState createBlockState(BlockState superState) {
		return new ExtendedBlockState(superState.getBlock(),
				(IProperty[]) superState.getProperties().toArray(new IProperty[0]),
				new IUnlistedProperty[] {TileMaterial.MATERIAL});
	}

	public static IBlockState getExtendedState(IBlockState state, IBlockAccess world,
			BlockPos pos) {
		TileMaterial tile = getTile(world, pos);
		if (tile != null && tile.getMaterial() != null)
			return TileMaterial.setStateMaterial((IExtendedBlockState) state, tile.getMaterial());
		else
			return state;
	}

	public static void onBlockPlacedBy(Block block, World world, BlockPos pos, IBlockState state,
			EntityLivingBase placer, ItemStack stack) {
		TileMaterial.getTile(world, pos).setMaterial(TileMaterial.getStackMaterial(stack));
	}

	public static ItemStack getPickBlock(Block block, MovingObjectPosition target, World world,
			BlockPos pos) {
		ItemStack stack = new ItemStack(block, 1);
		TileMaterial.setStackMaterial(stack, TileMaterial.getTile(world, pos).getMaterial());
		return stack;
	}

	public static void breakBlock(Block block, World world, BlockPos pos, IBlockState state) {
		ItemStack stack = new ItemStack(block, 1);
		TileMaterial.setStackMaterial(stack, TileMaterial.getTile(world, pos).getMaterial());
		ItemStackUtils.dropFromBlock(world, pos, stack);
	}

	public static void getSubBlocks(Block block, Item item, CreativeTabs tab, List list) {
		for (Material mat : MaterialBlockRegistry.instance.getBlockMaterials(block)) {
			ItemStack stack = new ItemStack(item);
			TileMaterial.setStackMaterial(stack, mat);
			list.add(stack);
		}
	}
}
