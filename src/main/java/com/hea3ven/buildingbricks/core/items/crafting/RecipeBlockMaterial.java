package com.hea3ven.buildingbricks.core.items.crafting;

import java.util.HashMap;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;

public class RecipeBlockMaterial extends ShapedRecipes {

	public RecipeBlockMaterial(int width, int height, ItemStack[] inputs, ItemStack output) {
		super(width, height, inputs, output);
	}

	public static IRecipe createRecipe(ItemStack output, Object[] inputs) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;

		if (inputs[i] instanceof String[]) {
			String[] astring = ((String[]) inputs[i++]);

			for (int l = 0; l < astring.length; ++l) {
				String s1 = astring[l];
				++k;
				j = s1.length();
				s = s + s1;
			}
		} else {
			while (inputs[i] instanceof String) {
				String s2 = (String) inputs[i++];
				++k;
				j = s2.length();
				s = s + s2;
			}
		}

		HashMap hashmap;

		for (hashmap = Maps.newHashMap(); i < inputs.length; i += 2) {
			Character character = (Character) inputs[i];
			ItemStack itemstack1 = null;

			if (inputs[i + 1] instanceof Item) {
				itemstack1 = new ItemStack((Item) inputs[i + 1]);
			} else if (inputs[i + 1] instanceof Block) {
				itemstack1 = new ItemStack((Block) inputs[i + 1], 1, 32767);
			} else if (inputs[i + 1] instanceof ItemStack) {
				itemstack1 = (ItemStack) inputs[i + 1];
			}

			hashmap.put(character, itemstack1);
		}

		ItemStack[] aitemstack = new ItemStack[j * k];

		for (int i1 = 0; i1 < j * k; ++i1) {
			char c0 = s.charAt(i1);

			if (hashmap.containsKey(Character.valueOf(c0))) {
				aitemstack[i1] = ((ItemStack) hashmap.get(Character.valueOf(c0))).copy();
			} else {
				aitemstack[i1] = null;
			}
		}
		return new RecipeBlockMaterial(j, k, aitemstack, output);
	}

	public boolean matches(InventoryCrafting inv, World world) {
		for (int i = 0; i <= 3 - this.recipeWidth; ++i) {
			for (int j = 0; j <= 3 - this.recipeHeight; ++j) {
				if (this.checkMatch(inv, i, j, true)) {
					return true;
				}

				if (this.checkMatch(inv, i, j, false)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks if the region of a crafting inventory is match for the recipe.
	 */
	private boolean checkMatch(InventoryCrafting inv, int i, int j, boolean p_77573_4_) {
		for (int k = 0; k < 3; ++k) {
			for (int l = 0; l < 3; ++l) {
				int i1 = k - i;
				int j1 = l - j;
				ItemStack itemstack = null;

				if (i1 >= 0 && j1 >= 0 && i1 < this.recipeWidth && j1 < this.recipeHeight) {
					if (p_77573_4_) {
						itemstack = this.recipeItems[this.recipeWidth - i1 - 1
								+ j1 * this.recipeWidth];
					} else {
						itemstack = this.recipeItems[i1 + j1 * this.recipeWidth];
					}
				}

				ItemStack itemstack1 = inv.getStackInRowAndColumn(k, l);

				if (itemstack1 != null || itemstack != null) {
					if (itemstack1 == null && itemstack != null
							|| itemstack1 != null && itemstack == null) {
						return false;
					}

					if (itemstack.getItem() != itemstack1.getItem()) {
						return false;
					}

					if (itemstack.getItemDamage() != 32767
							&& itemstack.getItemDamage() != itemstack1.getItemDamage()) {
						return false;
					}

					if (!itemstack.hasTagCompound()) {
						if (itemstack1.hasTagCompound())
							return false;
					} else if (!itemstack1.hasTagCompound()
							|| !itemstack.getTagCompound().equals(itemstack1.getTagCompound())) {
						return false;
					}
				}
			}
		}

		return true;
	}

}
