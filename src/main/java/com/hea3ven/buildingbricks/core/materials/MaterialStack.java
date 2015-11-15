package com.hea3ven.buildingbricks.core.materials;

import net.minecraft.item.ItemStack;

public class MaterialStack {
	public interface ItemMaterial {

		void setMaterial(ItemStack stack, Material mat);

		Material getMaterial(ItemStack stack);
	}

	public static void set(ItemStack stack, Material material) {
		if (stack.getItem() instanceof ItemMaterial) {
			((ItemMaterial) stack.getItem()).setMaterial(stack, material);
		}
	}

	public static Material get(ItemStack stack) {
		if (stack.getItem() instanceof ItemMaterial) {
			return ((ItemMaterial) stack.getItem()).getMaterial(stack);
		}
		return null;
	}
}
