package com.hea3ven.buildingbricks.core.item;

import java.util.UUID;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
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
import net.minecraftforge.items.wrapper.InvWrapper;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.client.gui.GuiMaterialBag;
import com.hea3ven.buildingbricks.core.inventory.ItemHandlerMaterialBag;
import com.hea3ven.buildingbricks.core.inventory.SlotMaterialBag;
import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.buildingbricks.core.materials.MaterialStack.ItemMaterial;
import com.hea3ven.tools.commonutils.inventory.GenericContainer;
import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

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

	public void initUuid(ItemStack stack) {
		if (stack.getTagCompound() == null)
			stack.setTagCompound(new NBTTagCompound());

		if (!stack.getTagCompound().hasKey("UUID"))
			stack.getTagCompound().setString("UUID", UUID.randomUUID().toString());
	}

	public boolean areSameStack(ItemStack stack1, ItemStack stack2) {
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
		return new GenericContainer()
				.addGenericSlots(80, 44, 1, 1, (slot, x, y) -> new SlotMaterialBag(inv, slot, x, y))
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
		return new ICapabilityProvider() {
			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
				return capability == ITEM_HANDLER_CAPABILITY;
			}

			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
				if (capability == ITEM_HANDLER_CAPABILITY)
					return ITEM_HANDLER_CAPABILITY.cast(new ItemHandlerMaterialBag(stack));
				return null;
			}
		};
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
		BlockDescription desc = mat.getBlock(stack);
		if(desc == null)
			return;

		ItemStack bagStack = findBag(new InvWrapper(event.getEntityPlayer().inventory), mat);
		if (bagStack == null)
			return;

		int volume = getVolume(bagStack);

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
}
