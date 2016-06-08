package com.hea3ven.buildingbricks.core.tileentity;

import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.util.Constants.NBT;

import com.hea3ven.buildingbricks.core.block.base.BlockBuildingBricks;
import com.hea3ven.buildingbricks.core.block.properties.PropertyMaterial;
import com.hea3ven.buildingbricks.core.materials.*;

public class TileMaterial extends TileEntity {

	private static final Logger logger = LogManager.getLogger("BuildingBricks.TileMaterial");

	public static final PropertyMaterial MATERIAL = new PropertyMaterial();

	public static boolean blocksInCreative = true;

	private Material material;
	private String materialId;

	public Material getMaterial() {
		if (material != null)
			return material;
		else
			return null;
	}

	public void setMaterial(Material material) {
		if (material != null)
			materialId = material.getMaterialId();
		else
			logger.error("The material of a block was directly set to null");
		this.material = material;
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
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);

		if (materialId != null)
			nbt.setString("material", materialId);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		if (nbt.hasKey("material", NBT.TAG_STRING)) {
			String matId = nbt.getString("material");
			if (!matId.contains(":"))
				matId = "minecraft:" + matId;
			materialId = matId;
			setMaterial(MaterialRegistry.get(matId));
		} else {
			logger.error("Could not load the material of a block");
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt = writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	public static BlockStateContainer createBlockState(BlockStateContainer superState) {
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
		TileMaterial te = TileMaterial.getTile(world, pos);
		if (te != null)
			te.setMaterial(MaterialRegistry.getMaterialForStack(stack));
	}

	public static ItemStack getPickBlock(Block block, RayTraceResult target, World world, BlockPos pos) {
		ItemStack stack = new ItemStack(block, 1);
		TileMaterial te = TileMaterial.getTile(world, pos);
		if (te != null)
			MaterialStack.set(stack, te.getMaterial());
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

		boolean silk = mat.getSilkHarvestMaterial() != null &&
				EnchantmentHelper.getEnchantedItem(Enchantments.SILK_TOUCH, player) != null;
		String harvestMatId = silk ? mat.getSilkHarvestMaterial() : mat.getNormalHarvestMaterial();
		if (harvestMatId == null)
			return null;
		Material itemMat = MaterialRegistry.get(harvestMatId);
		if (itemMat == null)
			return null;

		return itemMat.getBlock(blockType.getStackType()).getStack().copy();
	}
}
