package com.hea3ven.buildingbricks.core.items;

import java.util.UUID;

import com.google.common.collect.ImmutableSet;

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
import net.minecraftforge.items.*;

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
			setMaterial(stack, null);
		}

		if (stack.getTagCompound() == null)
			stack.setTagCompound(new NBTTagCompound());

		stack.getTagCompound().setInteger("Volume", volume);
	}

	public int getVolume(ItemStack stack) {
		if (stack.getTagCompound() == null)
			return 0;
		if (!stack.getTagCompound().hasKey("Volume"))
			return 0;

		return stack.getTagCompound().getInteger("Volume");
	}

	private void initUuid(ItemStack stack) {
		if (stack.getTagCompound() == null)
			stack.setTagCompound(new NBTTagCompound());

		if (!stack.getTagCompound().hasKey("UUID"))
			stack.getTagCompound().setString("UUID", UUID.randomUUID().toString());
	}

	private boolean areSameItem(ItemStack stack1, ItemStack stack2) {
		NBTTagCompound nbt1 = stack1.getTagCompound();
		NBTTagCompound nbt2 = stack2.getTagCompound();
		return nbt1 != null && nbt2 != null && nbt1.getString("UUID").equals(nbt2.getString("UUID"));
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
		playerIn.openGui(ModBuildingBricks.MODID, GuiMaterialBag.ID, worldIn,
				MathHelper.floor_double(playerIn.posX), MathHelper.floor_double(playerIn.posY),
				MathHelper.floor_double(playerIn.posZ));
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
		ItemHandlerMaterialBag inv = new ItemHandlerMaterialBag(player);
		return new ContainerMaterialBag().addSlots(SlotType.DISPLAY, 0, 80, 44, 1, 1, SlotItemHandler.class,
				inv)
				.addInputOutputSlots(inv, 1, 10000, 62, MaterialBlockType.values().length, 1)
				.addPlayerSlots(player.inventory, ImmutableSet.of(player.inventory.currentItem));
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

	public class ItemHandlerMaterialBag implements IItemHandler {
		private EntityPlayer player = null;
		private final ItemStack origStack;
		private Material mat;
		private MaterialBlockType[] slots;

		public ItemHandlerMaterialBag(EntityPlayer player) {
			this(player.getCurrentEquippedItem());
			this.player = player;
		}

		public ItemHandlerMaterialBag(ItemStack stack) {
			ModBuildingBricks.materialBag.initUuid(stack);
			origStack = stack;
			mat = MaterialStack.get(stack);
			slots = MaterialBlockType.values();
		}

		private ItemStack getBagStack() {
			if (player != null) {
				ItemStack playerStack = player.getCurrentEquippedItem();

				if (ModBuildingBricks.materialBag.areSameItem(origStack, playerStack)) {
					return playerStack;
				}
			}
			return origStack;
		}

		public float getCurrentVolume() {
			return ModBuildingBricks.materialBag.getVolume(getBagStack());
		}

		@Override
		public int getSlots() {
			return slots.length + 1;
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			if (slot == 0) {
				if (mat == null)
					return null;
				else
					return mat.getBlock(mat.getBlockRotation().getFirst()).getStack();
			} else {
				slot--;

				if (mat == null)
					return null;

				if (mat.getBlock(slots[slot]) == null) // Invalid block type for the material
					return null;

				return ItemHandlerHelper.copyStackWithSize(mat.getBlock(slots[slot]).getStack(),
						ModBuildingBricks.materialBag.getVolume(getBagStack()) / slots[slot].getVolume());
			}
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if (slot == 0)
				return stack;
			else {
				slot--;

				Material stackMat = MaterialRegistry.getMaterialForStack(stack);
				if (stackMat == null) // Input stack doesn't have a material
					return stack;

				BlockDescription desc = stackMat.getBlock(stack);
				if (desc.getType() != slots[slot]) // Input stack doesn't match the MaterialBlockType
					return stack;

				if (mat == null) {
					if (!simulate) {
						mat = stackMat;
						ItemStack bagStack = getBagStack();
						ModBuildingBricks.materialBag.setMaterial(bagStack, mat);
						ModBuildingBricks.materialBag.setVolume(bagStack,
								stack.stackSize * slots[slot].getVolume());
					}
					return null;
				} else {
					if (mat.getBlock(slots[slot]) == null) // Invalid block type for the material
						return stack;

					if (mat != stackMat) // Materials don't match
						return stack;

					ItemStack bagStack = getBagStack();
					int volume = ModBuildingBricks.materialBag.getVolume(bagStack);
					int capacity = (BAG_VOLUME - volume) / slots[slot].getVolume();
					if (stack.stackSize < capacity) {
						if (!simulate) {
							volume += stack.stackSize * slots[slot].getVolume();
							ModBuildingBricks.materialBag.setVolume(bagStack, volume);
						}
						return null;
					} else {
						stack.stackSize -= capacity;
						if (!simulate) {
							stack.splitStack(capacity);
							volume += capacity * slots[slot].getVolume();
							ModBuildingBricks.materialBag.setVolume(bagStack, volume);
						}
						return stack;
					}
				}
			}
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (slot == 0) {
				if (mat == null)
					return null;

				ItemStack bagStack = getBagStack();
				int volume = ModBuildingBricks.materialBag.getVolume(bagStack);
				MaterialBlockType type = MaterialBlockType.getBestForVolume(volume);
				ItemStack stack = ItemHandlerHelper.copyStackWithSize(mat.getBlock(type).getStack(),
						volume / type.getVolume());
				if (stack.stackSize > stack.getMaxStackSize())
					stack.stackSize = stack.getMaxStackSize();
				if (!simulate) {
					volume -= type.getVolume() * stack.stackSize;
					if (volume <= 0) {
						mat = null;
						ModBuildingBricks.materialBag.setMaterial(bagStack, mat);
						ModBuildingBricks.materialBag.setVolume(bagStack, 0);
					} else {
						ModBuildingBricks.materialBag.setVolume(bagStack, volume);
					}
				}
				return stack;
			}
			return null;
		}
	}
}
