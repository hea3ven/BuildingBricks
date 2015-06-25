package com.hea3ven.buildingbricks.core.client.model;

import java.util.HashMap;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;

import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.util.Constants.NBT;

@SuppressWarnings("deprecation")
public class ModelItemMaterialBlock extends DelegatedSmartModel implements ISmartItemModel {

	private HashMap<String, ModelItemMaterialBlock> models;

	public ModelItemMaterialBlock() {
		super(null);

		models = new HashMap<String, ModelItemMaterialBlock>();
	}

	public ModelItemMaterialBlock(IFlexibleBakedModel delegate) {
		super(delegate);
	}

	public void put(String materialId, IFlexibleBakedModel model) {
		models.put(materialId, new ModelItemMaterialBlock(model));
		if (materialId.equals("stone"))
			delegate = model;
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			return models.get(stack.getTagCompound().getString("material"));
		else
			return this;
	}

}
