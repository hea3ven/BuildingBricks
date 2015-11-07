package com.hea3ven.buildingbricks.core.items.crafting;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeBlockMaterial extends ShapedOreRecipe {

	public RecipeBlockMaterial(Object[] inputs, ItemStack output) {
		super(output, inputs);
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

		Object[] aitemstack = new ItemStack[j * k];

		for (int i1 = 0; i1 < j * k; ++i1) {
			char c0 = s.charAt(i1);

			if (hashmap.containsKey(Character.valueOf(c0))) {
				Object input = hashmap.get(Character.valueOf(c0));
				if (input instanceof ItemStack)
					aitemstack[i1] = ((ItemStack) input).copy();
				else
					aitemstack[i1] = input;
			} else {
				aitemstack[i1] = null;
			}
		}
		return new RecipeBlockMaterial(inputs, output);
	}

	@Override
	protected boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
		for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++) {
			for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++) {
				int subX = x - startX;
				int subY = y - startY;
				Object target = null;

				if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
					if (mirror) {
						target = input[width - subX - 1 + subY * width];
					} else {
						target = input[subX + subY * width];
					}
				}

				ItemStack slot = inv.getStackInRowAndColumn(x, y);

				if (target instanceof ItemStack) {
					if (!OreDictionary.itemMatches((ItemStack) target, slot, false)
							&& !ItemStack.areItemStackTagsEqual((ItemStack) target, slot)) {
						return false;
					}
				} else if (target instanceof List) {
					boolean matched = false;

					Iterator<ItemStack> itr = ((List<ItemStack>) target).iterator();
					while (itr.hasNext() && !matched) {
						ItemStack it = itr.next();
						matched =
								OreDictionary.itemMatches(it, slot, false) && ItemStack.areItemStackTagsEqual(
										it, slot);
					}

					if (!matched) {
						return false;
					}
				} else if (target == null && slot != null) {
					return false;
				}
			}
		}

		return true;
	}
}
