package com.hea3ven.buildingbricks.core.items;

import java.util.UUID;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.client.gui.GuiMaterialBag;
import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.buildingbricks.core.materials.MaterialStack.ItemMaterial;
import com.hea3ven.tools.commonutils.inventory.GenericContainer;
import com.hea3ven.tools.commonutils.inventory.GenericContainer.SlotType;
import com.hea3ven.tools.commonutils.inventory.SlotItemHandlerBase;
import com.hea3ven.tools.commonutils.util.PlayerUtil;
import com.hea3ven.tools.commonutils.util.PlayerUtil.HeldEquipment;

public class ItemMaterialBag extends Item implements ItemMaterial {

	public static final int BAG_VOLUME = 3 * 9 * 64 * 1000;

	@Override
	public void setMaterial(ItemStack stack, Material mat) {
		if (mat == null) {
			if (stack.hasTagCompound())
				stack.getTagCompound().removeTag("material");
		} else {
			if (!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("material", mat.getMaterialId());
		}
	}

	@Override
	public Material getMaterial(ItemStack stack) {
		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			return MaterialRegistry.get(stack.getTagCompound().getString("material"));
		else
			return null;
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
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World worldIn, EntityPlayer player,
			EnumHand hand) {
		player.openGui(ModBuildingBricks.MODID, GuiMaterialBag.ID, worldIn,
				MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY),
				MathHelper.floor_double(player.posZ));
		return super.onItemRightClick(stack, worldIn, player, hand);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		Material mat = getMaterial(stack);
		if (mat == null)
			return super.getItemStackDisplayName(stack);
		else
			return I18n.translateToLocalFormatted("item.buildingbricks.materialBagBinded.name",
					mat.getLocalizedName());
	}

	public Container getContainer(EntityPlayer player) {
		ItemHandlerMaterialBag inv = new ItemHandlerMaterialBag(player);
		return new ContainerMaterialBag().addSlots(SlotType.DISPLAY, 0, 80, 44, 1, 1,
				SlotItemHandlerBase.class, inv)
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

				if (!this.mergeItemStack(stack, getPlayerSlotsStart(), getPlayerSlotsStart() + 9 * 4, true))
					return null;

				slot.onSlotChange(stack, originalStack);
				slot.onSlotChanged();
				return originalStack;
			} else {
				return super.transferStackInSlot(player, index);
			}
		}
	}

	@SubscribeEvent
	public void onItemPickup(EntityItemPickupEvent event) {
		if (event.isCanceled())
			return;

		ItemStack stack = event.getItem().getEntityItem();
		if (stack == null || stack.stackSize <= 0)
			return;
		Material mat = MaterialRegistry.getMaterialForStack(stack);
		if (mat == null)
			return;

		ItemStack bagStack = findBag(new InvWrapper(event.getEntityPlayer().inventory), mat);
		if (bagStack == null)
			return;

		int volume = getVolume(bagStack);
		BlockDescription desc = mat.getBlock(stack);

		if (volume + desc.getType().getVolume() * stack.stackSize <= BAG_VOLUME) {
			setVolume(bagStack, volume + desc.getType().getVolume() * stack.stackSize);
			stack.stackSize = 0;
		} else {
			int countAdd = ((BAG_VOLUME - volume) / desc.getType().getVolume());
			setVolume(bagStack, volume + countAdd * desc.getType().getVolume());
			stack.stackSize -= countAdd;
		}

		if (stack.stackSize <= 0)
			event.setResult(Result.ALLOW);
	}

	private ItemStack findBag(InvWrapper itemHandler, Material mat) {
		for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
			ItemStack bagStack = itemHandler.getStackInSlot(slot);
			if (bagStack == null)
				continue;
			if (bagStack.getItem() != this)
				continue;

			if (getMaterial(bagStack) == mat)
				return bagStack;
		}
		return null;
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
		private final EntityPlayer player;
		private final HeldEquipment equipment;
		private final ItemStack origStack;
		private Material mat;
		private MaterialBlockType[] slots;

		public ItemHandlerMaterialBag(EntityPlayer player) {
			equipment = PlayerUtil.getHeldEquipment(player, ModBuildingBricks.materialBag);
			this.player = player;
			origStack = initStack(equipment.stack);
		}

		public ItemHandlerMaterialBag(ItemStack stack) {
			equipment = null;
			player = null;
			origStack = initStack(stack);
		}

		private ItemStack initStack(ItemStack stack) {
			ModBuildingBricks.materialBag.initUuid(stack);
			mat = MaterialStack.get(stack);
			slots = MaterialBlockType.getStackValues();
			return stack;
		}

		private ItemStack getBagStack() {
			if (player != null) {
				ItemStack playerStack = equipment.getCurrent();

				if (ModBuildingBricks.materialBag.areSameItem(equipment.stack, playerStack)) {
					return playerStack;
				}
			}
			return equipment.stack;
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
					return mat.getFirstBlock().getStack();
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
				MaterialBlockType type = MaterialBlockType.getBestForVolume(mat, volume);
				if (type == null)
					return null;
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
