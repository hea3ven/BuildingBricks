package com.hea3ven.buildingbricks.core.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.wrapper.InvWrapper;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.client.gui.GuiTrowel;
import com.hea3ven.buildingbricks.core.inventory.MaterialItemStackConsumer;
import com.hea3ven.buildingbricks.core.inventory.SlotTrowelBlockType;
import com.hea3ven.buildingbricks.core.inventory.SlotTrowelMaterial;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.MaterialStack.ItemMaterial;
import com.hea3ven.buildingbricks.core.materials.mapping.MaterialIdMapping;
import com.hea3ven.tools.commonutils.inventory.GenericContainer;
import com.hea3ven.tools.commonutils.inventory.GenericContainer.SlotType;

public class ItemTrowel extends Item implements ItemMaterial {

	public ItemTrowel() {
	}

	@Override
	public void setMaterial(ItemStack stack, Material mat) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			updateStack(stack); // convert from 1.0.x format

		if (mat == null)
			stack.setItemDamage(0);
		else
			stack.setItemDamage(MaterialIdMapping.get().getIdForMaterial(mat));
	}

	@Override
	public Material getMaterial(ItemStack stack) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			updateStack(stack); // convert from 1.0.x format

		short matId = (short) getMetadata(stack);
		if (matId == 0)
			return null;
		else
			return MaterialIdMapping.get().getMaterialById(matId);
	}

	public MaterialBlockType getCurrentBlockType(ItemStack stack) {
		if (stack.getTagCompound() == null) {
			NBTTagCompound nbt = new NBTTagCompound();
			Material result;
			if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
				updateStack(stack); // convert from 1.0.x format

			short matId = (short) getMetadata(stack);
			if (matId == 0)
				result = null;
			else
				result = MaterialIdMapping.get().getMaterialById(matId);
			nbt.setInteger("blockType", result.getBlockRotation().getFirst().ordinal());
			stack.setTagCompound(nbt);
		}
		MaterialBlockType blockType =
				MaterialBlockType.getBlockType(stack.getTagCompound().getInteger("blockType"));
		Material result;
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			updateStack(stack); // convert from 1.0.x format

		short matId = (short) getMetadata(stack);
		if (matId == 0)
			result = null;
		else
			result = MaterialIdMapping.get().getMaterialById(matId);
		Material mat = result;
		if (mat != null && mat.getBlock(blockType) == null) {
			blockType = mat.getBlockRotation().getNext(blockType);
			setCurrentBlockType(stack, blockType);
		}
		return blockType;
	}

	public void setCurrentBlockType(ItemStack stack, MaterialBlockType blockType) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setInteger("blockType", blockType.ordinal());
	}

	public void setNextBlockRotation(ItemStack stack) {
		Material result;
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			updateStack(stack); // convert from 1.0.x format

		short matId = (short) getMetadata(stack);
		if (matId == 0)
			result = null;
		else
			result = MaterialIdMapping.get().getMaterialById(matId);
		Material mat = result;
		if (mat != null) {
			MaterialBlockType blockType = getCurrentBlockType(stack);
			blockType = mat.getBlockRotation().getNext(blockType);
			setCurrentBlockType(stack, blockType);
		}
	}

	public void setPrevBlockRotation(ItemStack stack) {
		Material result;
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			updateStack(stack); // convert from 1.0.x format

		short matId = (short) getMetadata(stack);
		if (matId == 0)
			result = null;
		else
			result = MaterialIdMapping.get().getMaterialById(matId);
		Material mat = result;
		if (mat != null) {
			MaterialBlockType blockType = getCurrentBlockType(stack);
			blockType = mat.getBlockRotation().getPrev(blockType);
			setCurrentBlockType(stack, blockType);
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (playerIn.isSneaking()) {
			playerIn.openGui(ModBuildingBricks.MODID, GuiTrowel.ID, worldIn, MathHelper.floor_double(playerIn.posX),
					MathHelper.floor_double(playerIn.posY), MathHelper.floor_double(playerIn.posZ));
		}
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		Material result;
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			updateStack(stack); // convert from 1.0.x format

		short matId = (short) getMetadata(stack);
		if (matId == 0)
			result = null;
		else
			result = MaterialIdMapping.get().getMaterialById(matId);
		Material mat = result;
		if (mat == null)
			return super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
		else {
			MaterialBlockType blockType = getCurrentBlockType(stack);
			ItemStack useStack = mat.getBlock(blockType).getStack().copy();
			MaterialItemStackConsumer consumer =
					new MaterialItemStackConsumer(blockType, mat, new InvWrapper(player.inventory));
			if (!player.capabilities.isCreativeMode && consumer.failed())
				return false;
			if (!useStack.onItemUse(player, world, pos, side, hitX, hitY, hitZ))
				return false;
			if (!player.capabilities.isCreativeMode)
				consumer.apply(world, player.getPosition());
			return true;
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		Material result;
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			updateStack(stack); // convert from 1.0.x format

		short matId = (short) getMetadata(stack);
		if (matId == 0)
			result = null;
		else
			result = MaterialIdMapping.get().getMaterialById(matId);
		Material mat = result;
		if (mat == null)
			return super.getItemStackDisplayName(stack);
		else
			return StatCollector.translateToLocalFormatted("item.trowelBinded.name", mat.getLocalizedName());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int tintIndex) {
		if (tintIndex == 1)
			return super.getColorFromItemStack(stack, tintIndex);
		Material result;
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			updateStack(stack); // convert from 1.0.x format

		short matId = (short) getMetadata(stack);
		if (matId == 0)
			result = null;
		else
			result = MaterialIdMapping.get().getMaterialById(matId);
		Material mat = result;
		if (mat == null)
			return super.getColorFromItemStack(stack, tintIndex);

		return mat.getFirstBlock().getItem().getColorFromItemStack(stack, tintIndex);
	}

	private void updateStack(ItemStack stack) {
		String matId = stack.getTagCompound().getString("material");
		stack.getTagCompound().removeTag("material");
		setMaterial(stack, MaterialRegistry.get(matId));
	}

	public Container getContainer(EntityPlayer player) {
		return new GenericContainer().addSlots(SlotType.DISPLAY, 0, 44, 36, 1, 1, SlotTrowelMaterial.class,
				player)
				.addSlots(SlotType.DISPLAY, 0, 98, 9, 4, 4, SlotTrowelBlockType.class, player)
				.addPlayerSlots(player.inventory);
	}

	public ItemStack createStack() {
		return createStack(null);
	}

	public ItemStack createStack(Material mat) {
		if (mat == null)
			return new ItemStack(this);

		ItemStack stack = new ItemStack(this);
		setMaterial(stack, mat);
		return stack;
	}
}
