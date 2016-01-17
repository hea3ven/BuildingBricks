package com.hea3ven.buildingbricks.core.items.crafting;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;

import net.minecraftforge.oredict.OreDictionary;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.Material;

public class RecipeBindTrowel extends ShapelessRecipes {
	public RecipeBindTrowel(Material mat, ItemStack stack) {
		super(ModBuildingBricks.trowel.createStack(mat), getIngredients(stack));
	}

	private static List<ItemStack> getIngredients(ItemStack stack) {
		ItemStack trowelStack = ModBuildingBricks.trowel.createStack();
		trowelStack.setItemDamage(OreDictionary.WILDCARD_VALUE);
		return Lists.newArrayList(trowelStack, stack);
	}

	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		ItemStack[] stacks = new ItemStack[inv.getSizeInventory()];

		for (int i = 0; i < stacks.length; ++i) {
			ItemStack stack = inv.getStackInSlot(i);
			stacks[i] = null;
			if (stack != null && stack.getItem() != ModBuildingBricks.trowel)
				stack.stackSize++;
		}

		return stacks;
	}
}
