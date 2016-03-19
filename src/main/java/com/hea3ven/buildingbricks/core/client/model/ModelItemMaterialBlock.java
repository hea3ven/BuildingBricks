package com.hea3ven.buildingbricks.core.client.model;

import java.util.HashMap;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.block.model.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialStack;
import com.hea3ven.tools.commonutils.client.model.DelegatedSmartModel;

public class ModelItemMaterialBlock extends DelegatedSmartModel implements IBakedModel {

//	private static ItemCameraTransforms cameraTransforms = new ItemCameraTransforms(
//			new ItemTransformVec3f(new Vector3f(-20, 135, 180),
//					new Vector3f(0, 1.5f * 0.0625f, -2.75f * 0.0625f), new Vector3f(0.375f, 0.375f, 0.375f)),
//			new ItemTransformVec3f(new Vector3f(0, 180, 0), new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f)),
//			new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1, 1, 1)),
//			new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1, 1, 1)),
//			new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1, 1, 1)),
//			new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1, 1, 1)),
//			new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1, 1, 1)),
//			new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1, 1, 1)));

	private HashMap<String, IBakedModel> cache;
	private ItemOverrideList overrides = new ItemOverrideList(ImmutableList.<ItemOverride>of()) {
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world,
				EntityLivingBase entity) {
			Material mat = MaterialStack.get(stack);
			if (mat == null)
				return originalModel;

			ModelItemMaterialBlock model = (ModelItemMaterialBlock) originalModel;
			return model.cache.get(mat.getMaterialId());
		}
	};
//	private ImmutableMap<TransformType, TRSRTransformation> transforms = ImmutableMap.of(Tra	);

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

//	@Override
//	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type) {
//		return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, transforms, type);
//	}
}
