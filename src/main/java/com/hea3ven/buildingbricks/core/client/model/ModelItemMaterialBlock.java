package com.hea3ven.buildingbricks.core.client.model;

import java.util.HashMap;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialStack;
import com.hea3ven.tools.commonutils.client.model.DelegatedSmartModel;

public class ModelItemMaterialBlock extends DelegatedSmartModel implements IBakedModel {
	private HashMap<String, IBakedModel> cache;
	private ItemOverrideList overrides = new ItemOverrideList(ImmutableList.<ItemOverride>of()) {
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world,
				EntityLivingBase entity) {
			Material mat = MaterialStack.get(stack);
			if (mat == null)
				return originalModel;

			ModelItemMaterialBlock model = (ModelItemMaterialBlock) originalModel;
			IBakedModel bakedModel = model.cache.get(mat.getMaterialId());
			if (bakedModel != null)
				return bakedModel;
			else
				return originalModel;
		}
	};

	public ModelItemMaterialBlock(IBakedModel delegate) {
		super(delegate);
		cache = new HashMap<>();
	}

	public void put(String materialId, IBakedModel model) {
		cache.put(materialId, model);
	}

	@Override
	public ItemOverrideList getOverrides() {
		return overrides;
	}
}
