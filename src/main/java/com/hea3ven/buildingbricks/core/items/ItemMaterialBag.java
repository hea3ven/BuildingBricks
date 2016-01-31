package com.hea3ven.buildingbricks.core.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.client.gui.GuiMaterialBag;
import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.buildingbricks.core.materials.MaterialStack.ItemMaterial;
import com.hea3ven.buildingbricks.core.materials.mapping.MaterialIdMapping;
import com.hea3ven.tools.commonutils.inventory.GenericContainer;
import com.hea3ven.tools.commonutils.inventory.GenericContainer.SlotType;
import com.hea3ven.tools.commonutils.inventory.InventoryGeneric;
import com.hea3ven.tools.commonutils.inventory.SlotInputOutput;

public class ItemMaterialBag extends Item implements ItemMaterial {

	public static final int BAG_VOLUME = 3 * 9 * 64 * 1000;

	public ItemMaterialBag() {
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

	public void setVolume(ItemStack stack, int volume) {
		if (volume == 0) {
			stack.setTagCompound(null);
			setMaterial(stack, null);
			return;
		}

		if (stack.getTagCompound() == null)
			stack.setTagCompound(new NBTTagCompound());

		stack.getTagCompound().setInteger("Volume", volume);
	}

	public int getVolume(ItemStack stack) {
		if (stack.getTagCompound() == null)
			return 0;

		return stack.getTagCompound().getInteger("Volume");
	}

	@Override
	public int getDamage(ItemStack stack) {
		return BAG_VOLUME - getVolume(stack);
	}

	@Override
	public int getMaxDamage() {
		return BAG_VOLUME;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		playerIn.openGui(ModBuildingBricks.MODID, GuiMaterialBag.ID, worldIn, MathHelper.floor_double(playerIn.posX),
				MathHelper.floor_double(playerIn.posY), MathHelper.floor_double(playerIn.posZ));
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
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
			return StatCollector.translateToLocalFormatted("item.materialBagBinded.name",
					mat.getLocalizedName());
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

		return mat.getBlock(MaterialBlockType.FULL).getItem().getColorFromItemStack(stack, tintIndex);
	}

	private void updateStack(ItemStack stack) {
		String matId = stack.getTagCompound().getString("material");
		stack.getTagCompound().removeTag("material");
		setMaterial(stack, MaterialRegistry.get(matId));
	}

	public Container getContainer(EntityPlayer player) {
		InventoryMaterialBag inv = new InventoryMaterialBag(player);
		return new ContainerMaterialBag().addSlots(SlotType.DISPLAY, 0, 80, 44, 1, 1, SlotInputOutput.class,
				inv).addInputOutputSlots(inv, 1, 10000, 62, 1, 1).addPlayerSlots(player.inventory);
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

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new CapabilityProviderMaterialBag(stack, nbt);
	}

	public class InventoryMaterialBag extends InventoryGeneric {
		private final EntityPlayer player;
		private ItemStack stack;
		private Material mat;
		private int volume;

		public InventoryMaterialBag(EntityPlayer player) {
			super(2, 1728);
			this.player = player;
			this.stack = player.getCurrentEquippedItem();
			mat = getMaterial(stack);
			volume = getVolume(stack);
			if (mat != null && volume > 0) {
				inv[0] = mat.getBlock(MaterialBlockType.FULL).getStack().copy();
			}
		}

		public int getCurrentVolume() {
			return volume;
		}

		@Override
		public String getName() {
			return "buildingbricks.container.materialBag";
		}

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) {
			if (index == 0)
				return false;
			if (mat == null)
				return MaterialRegistry.getMaterialForStack(stack) != null;
			BlockDescription desc = mat.getBlock(stack);
			return desc != null && volume + desc.getType().getVolume() < BAG_VOLUME;
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack) {
			if (stack == null)
				return;
			if (index != 1)
				return;
			if (mat == null) {
				mat = MaterialRegistry.getMaterialForStack(stack);
				inv[0] = mat.getBlock(MaterialBlockType.FULL).getStack().copy();
			}
			BlockDescription desc = mat.getBlock(stack);
			volume += stack.stackSize * desc.getType().getVolume();
		}

		@Override
		public ItemStack decrStackSize(int index, int count) {
			if (index == 0) {
				MaterialBlockType type = MaterialBlockType.getBestForVolume(volume);
				ItemStack stack = ItemHandlerHelper.copyStackWithSize(mat.getBlock(type).getStack(),
						volume / type.getVolume());
				if (stack.stackSize > stack.getMaxStackSize())
					stack.stackSize = stack.getMaxStackSize();
				volume -= type.getVolume() * stack.stackSize;
				if (volume <= 0) {
					mat = null;
					volume = 0;
					inv[0] = null;
				}
				return stack;
			} else if (index == 1)
				return null;
			else
				return super.decrStackSize(index, count);
		}

		@Override
		public void markDirty() {
			super.markDirty();
			setMaterial(stack, mat);
			setVolume(stack, volume);
			player.setCurrentItemOrArmor(0, stack);
		}
	}

	class ContainerMaterialBag extends GenericContainer {
		@Override
		public ItemStack transferStackInSlot(EntityPlayer player, int index) {
			if (index == 0) {

				Slot slot = inventorySlots.get(0);
				ItemStack stack = slot.decrStackSize(1);
				ItemStack originalStack = stack.copy();

				if (!this.mergeItemStack(stack, 2, 2 + 9 * 4, true)) {
					return null;
				}
				slot.onSlotChange(stack, originalStack);
				slot.onSlotChanged();
				return originalStack;
			} else {
				return super.transferStackInSlot(player, index);
			}
		}
	}

	private class CapabilityProviderMaterialBag implements ICapabilityProvider {
		private final ItemStack stack;

		public CapabilityProviderMaterialBag(ItemStack stack, NBTTagCompound nbt) {
			this.stack = stack;
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
				return (T) new ItemHandlerMaterialBag(stack);
			return null;
		}
	}

	private class ItemHandlerMaterialBag implements IItemHandler {
		private final ItemStack stack;
		private final Material mat;
		private final MaterialBlockType[] slots;

		public ItemHandlerMaterialBag(ItemStack stack) {
			this.stack = stack;
			mat = MaterialStack.get(stack);
			if (mat != null) {
				slots = new MaterialBlockType[mat.getBlockRotation().getAll().size()];
				slots[0] = mat.getBlockRotation().getFirst();
				for (int i = 0; i < slots.length; i++) {
					slots[i] = mat.getBlockRotation().get(i).getType();
				}
			} else {
				slots = new MaterialBlockType[0];
			}
		}

		@Override
		public int getSlots() {
			return slots.length;
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			return ItemHandlerHelper.copyStackWithSize(mat.getBlock(slots[slot]).getStack(),
					ModBuildingBricks.materialBag.getVolume(stack) / slots[slot].getVolume());
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (mat == null)
				return stack;

			Material stackMat = MaterialRegistry.getMaterialForStack(stack);
			if (mat != stackMat)
				return stack;

			int volume = ModBuildingBricks.materialBag.getVolume(stack);
			int capacity = (BAG_VOLUME - volume) / slots[slot].getVolume();
			if (stack.stackSize < capacity) {
				if (!simulate) {
					volume += stack.stackSize * slots[slot].getVolume();
					ModBuildingBricks.materialBag.setVolume(stack, volume);
				}
				return null;
			} else {
				if (!simulate) {
					stack.splitStack(capacity);
					volume += capacity * slots[slot].getVolume();
					ModBuildingBricks.materialBag.setVolume(stack, volume);
				} else {
					stack.stackSize -= capacity;
				}
				return stack;
			}
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			int volume = ModBuildingBricks.materialBag.getVolume(stack);
			int volumeToExtract = amount * slots[slot].getVolume();
			if (volumeToExtract > volume) {
				amount = volume / slots[slot].getVolume();
				volumeToExtract = amount * slots[slot].getVolume();
			}
			if (!simulate) {
				volume -= volumeToExtract;
				ModBuildingBricks.materialBag.setVolume(stack, volume);
			}
			return ItemHandlerHelper.copyStackWithSize(mat.getBlock(slots[slot]).getStack(), amount);
		}
	}
}
