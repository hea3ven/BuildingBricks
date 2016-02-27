package com.hea3ven.buildingbricks.core.tileentity;

import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import com.hea3ven.buildingbricks.core.blocks.base.BlockBuildingBricks;
import com.hea3ven.buildingbricks.core.blocks.properties.PropertyMaterial;
import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.buildingbricks.core.materials.mapping.MaterialIdMapping;

public class TileMaterial extends TileEntity {

	public static final PropertyMaterial MATERIAL = new PropertyMaterial();

	public static boolean blocksInCreative = true;

	private short materialId = 0;
	private Material material;

	public Material getMaterial() {
		if (material != null)
			return material;
		else
			return null;
	}

	public void setMaterial(Material material) {
		this.material = material;
		this.materialId = MaterialIdMapping.get().getIdForMaterial(material);
	}

	public static IExtendedBlockState setStateMaterial(IExtendedBlockState state, Material mat) {
		return state.withProperty(MATERIAL, mat);
	}

	public static TileMaterial getTile(IBlockAccess world, BlockPos pos) {
		TileMaterial tile;
		TileEntity te = world.getTileEntity(pos);
		if (!(te instanceof TileMaterial))
			return null;
		tile = (TileMaterial) te;
		return tile;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setShort("m", materialId);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		if (nbt.hasKey("material")) {
			String matId = nbt.getString("material");
			if (!matId.contains(":"))
				matId = "buildingbrickscompatvanilla:" + matId;
			setMaterial(MaterialRegistry.get(matId));
			materialId = MaterialIdMapping.get().getIdForMaterial(getMaterial());
		} else {
			materialId = nbt.getShort("m");
			setMaterial(MaterialIdMapping.get().getMaterialById(materialId));
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
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

	public static BlockState createBlockState(BlockState superState) {
		Collection props = superState.getProperties();
		return new ExtendedBlockState(superState.getBlock(),
				(IProperty[]) props.toArray(new IProperty[props.size()]),
				new IUnlistedProperty[] {TileMaterial.MATERIAL});
	}

	public static IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileMaterial tile = getTile(world, pos);
		if (tile == null || tile.getMaterial() == null)
			return state;
		return TileMaterial.setStateMaterial((IExtendedBlockState) state, tile.getMaterial());
	}

	public static void onBlockPlacedBy(Block block, World world, BlockPos pos, IBlockState state,
			EntityLivingBase placer, ItemStack stack) {
		TileMaterial.getTile(world, pos).setMaterial(MaterialStack.get(stack));
	}

	public static ItemStack getPickBlock(Block block, MovingObjectPosition target, World world,
			BlockPos pos) {
		ItemStack stack = new ItemStack(block, 1);
		MaterialStack.set(stack, TileMaterial.getTile(world, pos).getMaterial());
		return stack;
	}

	public static void getSubBlocks(Block block, Item item, CreativeTabs tab, List<ItemStack> list) {
		if (blocksInCreative) {
			for (Material mat : MaterialBlockRegistry.instance.getBlockMaterials(block)) {
				ItemStack stack = new ItemStack(item);
				MaterialStack.set(stack, mat);
				list.add(stack);
			}
		}
	}

	public static ItemStack getHarvestBlock(World world, BlockPos pos, EntityPlayer player) {
		Block block = world.getBlockState(pos).getBlock();
		if (!(block instanceof BlockBuildingBricks))
			return null;
		MaterialBlockType blockType = ((BlockBuildingBricks) block).getBlockLogic().getBlockType();

		TileMaterial te = TileMaterial.getTile(world, pos);
		if (te == null)
			return null;
		Material mat = te.getMaterial();
		if (mat == null)
			return null;

		boolean silk = mat.getSilkHarvestMaterial() != null && EnchantmentHelper.getSilkTouchModifier(player);
		String harvestMatId = silk ? mat.getSilkHarvestMaterial() : mat.getNormalHarvestMaterial();
		if (harvestMatId == null)
			return null;
		Material itemMat = MaterialRegistry.get(harvestMatId);
		if (itemMat == null)
			return null;

		return itemMat.getBlock(blockType).getStack().copy();
	}
}
