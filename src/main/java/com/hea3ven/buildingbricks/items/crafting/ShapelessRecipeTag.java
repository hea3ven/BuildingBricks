package com.hea3ven.buildingbricks.items.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.ModBuildingBricks;

public class ShapelessRecipeTag extends ShapelessRecipes {

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

	public ShapelessRecipeTag(ItemStack output, Object... recipeComponents) {
		super(output, generateInputs(recipeComponents));
	}

	@Override
	public boolean matches(InventoryCrafting p_77569_1_, World worldIn) {
		ArrayList arraylist = Lists.newArrayList(this.recipeItems);

		for (int i = 0; i < p_77569_1_.getHeight(); ++i) {
			for (int j = 0; j < p_77569_1_.getWidth(); ++j) {
				ItemStack itemstack = p_77569_1_.getStackInRowAndColumn(j, i);

				if (itemstack != null) {
					boolean flag = false;
					Iterator iterator = arraylist.iterator();

					while (iterator.hasNext()) {
						ItemStack itemstack1 = (ItemStack) iterator.next();

						if (itemstack.getItem() == itemstack1.getItem()
								&& (itemstack1.getMetadata() == 32767
										|| itemstack.getMetadata() == itemstack1.getMetadata())) {
							if (itemstack.getItem() == ModBuildingBricks.trowel
									|| ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
								flag = true;
								arraylist.remove(itemstack1);
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

		return arraylist.isEmpty();
	}
}
