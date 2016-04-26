package com.hea3ven.buildingbricks.compat.mcmultipart.util;

import mcmultipart.microblock.IMicroMaterial;
import mcmultipart.microblock.MicroblockRegistry;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.tools.commonutils.util.ItemStackUtil;

public class MicroblockUtil {
	public static IMicroMaterial getMicroMaterial(Material mat) {
		ItemStack matStack = mat.getBlock(MaterialBlockType.FULL).getStack();
		for (IMicroMaterial microMat : MicroblockRegistry.getRegisteredMaterials()) {
			if (ItemStackUtil.areItemsEqual(matStack, microMat.getItem()))
				return microMat;
		}
		return null;
	}
}
