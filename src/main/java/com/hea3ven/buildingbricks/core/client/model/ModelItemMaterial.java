package com.hea3ven.buildingbricks.core.client.model;

import javax.vecmath.Matrix4f;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.client.model.IPerspectiveAwareModel;

import com.hea3ven.buildingbricks.core.materials.Material;

@SuppressWarnings("deprecation")
public class ModelItemMaterial implements IPerspectiveAwareModel {
	public HashMap<Material, ModelItemMaterial> models;

	private IPerspectiveAwareModel base;
	private IPerspectiveAwareModel mat;
	private TextureAtlasSprite texture;
	private Map<EnumFacing, List<BakedQuad>> quads;

	public ModelItemMaterial(IBakedModel baseModel) {
		this(baseModel, null);
		models = new HashMap<>();
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
	public TextureAtlasSprite getParticleTexture() {
		return texture;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
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

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		return Pair.of(this, base.handlePerspective(cameraTransformType).getRight());
	}
}
