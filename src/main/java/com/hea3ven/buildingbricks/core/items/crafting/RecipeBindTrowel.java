package com.hea3ven.buildingbricks.core.items.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.ModBuildingBricks;

public class RecipeBindTrowel extends ShapelessRecipes {

	private static List generateInputs(Object[] recipeComponents) {
		ArrayList arraylist = Lists.newArrayList();
		Object[] aobject = recipeComponents;
		int i = recipeComponents.length;

		for (int j = 0; j < i; ++j) {
			Object object1 = aobject[j];

			if (object1 instanceof ItemStack) {
				arraylist.add(((ItemStack) object1).copy());
			} else if (object1 instanceof Item) {
				arraylist.add(new ItemStack((Item) object1));
			} else {
				if (!(object1 instanceof Block)) {
					throw new IllegalArgumentException("Invalid shapeless recipe: unknown type "
							+ object1.getClass().getName() + "!");
				}

				arraylist.add(new ItemStack((Block) object1));
			}
		}
		return arraylist;
	}

	public RecipeBindTrowel(ItemStack output, Object... recipeComponents) {
		super(output, generateInputs(recipeComponents));
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		ItemStack[] remainingItems = new ItemStack[inv.getSizeInventory()];

		for (int i = 0; i < remainingItems.length; ++i) {
			ItemStack stack = inv.getStackInSlot(i);
			remainingItems[i] = null;
			if (stack != null && stack.getItem() != ModBuildingBricks.trowel)
				stack.stackSize++;
		}

		return remainingItems;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		ArrayList<ItemStack> requiredItems = Lists.newArrayList(this.recipeItems);

		for (int i = 0; i < inv.getHeight(); ++i) {
			for (int j = 0; j < inv.getWidth(); ++j) {
				ItemStack invStack = inv.getStackInRowAndColumn(j, i);

				if (invStack != null) {
					boolean flag = false;
					for (ItemStack requiredStack : requiredItems) {
						if (invStack.getItem() == requiredStack.getItem()
								&& (requiredStack.getMetadata() == 32767
										|| invStack.getMetadata() == requiredStack.getMetadata())) {
							if (invStack.getItem() == ModBuildingBricks.trowel
									|| ItemStack.areItemStackTagsEqual(invStack, requiredStack)) {
								flag = true;
								requiredItems.remove(requiredStack);
								break;
							}
						}
					}

					if (!flag) {
						return false;
					}
				}
			}
		}

		return requiredItems.isEmpty();
	}
}
