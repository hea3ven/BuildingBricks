package com.hea3ven.buildingbricks.core.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import net.minecraftforge.client.model.IPerspectiveAwareModel;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialStack;

public class ModelItemMaterial implements IBakedModel {
	public HashMap<Material, ModelItemMaterial> cache;

	private IPerspectiveAwareModel base;
	private IPerspectiveAwareModel mat;
	private Map<EnumFacing, List<BakedQuad>> quads;

	private ItemOverrideList overrides = new ItemOverrideList(ImmutableList.<ItemOverride>of()) {
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world,
				EntityLivingBase entity) {
			Material mat = MaterialStack.get(stack);
			if (mat == null)
				return originalModel;

			ModelItemMaterial model = (ModelItemMaterial) originalModel;
			return model.cache.get(mat);
		}
	};

	public ModelItemMaterial(IBakedModel baseModel) {
		this(baseModel, null);
		cache = new HashMap<>();
	}

	public ModelItemMaterial(IBakedModel baseModel, IBakedModel matModel) {
		base = (IPerspectiveAwareModel) baseModel;
		mat = (IPerspectiveAwareModel) matModel;
		quads = new HashMap<>();
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		if (!quads.containsKey(side)) {
			ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
			if (base != null) {
				builder.addAll(base.getQuads(state, side, 0));
			}
			if (mat != null) {
				builder.addAll(mat.getQuads(state, side, 0));
			}
			quads.put(side, builder.build());
		}
		return quads.get(side);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture(){
		return base.getParticleTexture();
	}

	@Override
	@SuppressWarnings("deprecation")
	public ItemCameraTransforms getItemCameraTransforms() {
		return base.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return overrides;
	}

//	@Override
//	public IBakedModel handleItemState(ItemStack stack) {
//		Material mat = MaterialStack.get(stack);
//		if (mat == null)
//			return this;
//		else {
//			return models.get(mat);
//		}
//	}

	public void put(Material material, ModelItemMaterial model) {
		cache.put(material, model);
	}
}
