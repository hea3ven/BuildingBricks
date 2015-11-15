package com.hea3ven.buildingbricks.core.items.crafting;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.BlockDescription;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.mapping.IdMappingLoader;

public class RecipeBindTrowel extends ShapelessRecipes {

	private static List<ItemStack> generateInputs(Material mat, BlockDescription blockDesc) {
		List<ItemStack> inputs = Lists.newArrayList();
		inputs.add(new ItemStack(ModBuildingBricks.trowel));
		inputs.add(blockDesc.getStack());
		return inputs;
	}

	public RecipeBindTrowel(Material mat, BlockDescription blockDesc) {
		super(new ItemStack(ModBuildingBricks.trowel), generateInputs(mat, blockDesc));
		IdMappingLoader.dynamicStacks.add(Pair.of(getRecipeOutput(), mat));
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
						if (invStack.getItem() == requiredStack.getItem() && (
								requiredStack.getMetadata() == 32767
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
