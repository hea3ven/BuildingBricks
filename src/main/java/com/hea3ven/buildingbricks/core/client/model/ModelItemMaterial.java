package com.hea3ven.buildingbricks.core.client.model;

import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartItemModel;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialStack;

@SuppressWarnings("deprecation")
public class ModelItemMaterial implements ISmartItemModel, IPerspectiveAwareModel {
	public HashMap<Material, ModelItemMaterial> models;

	private IPerspectiveAwareModel base;
	private TextureAtlasSprite texture;
	private HashMap<EnumFacing, List<BakedQuad>> faces = new HashMap<>();
	private List<BakedQuad> quads;

	public ModelItemMaterial(IBakedModel baseModel) {
		this(baseModel, null);
		models = new HashMap<>();
	}

	public ModelItemMaterial(IBakedModel baseModel, IBakedModel matModel) {
		base = (IPerspectiveAwareModel) baseModel;
		texture = baseModel.getParticleTexture();
		for (EnumFacing side : EnumFacing.VALUES) {
			List<BakedQuad> sideFaces = new ArrayList<>(baseModel.getFaceQuads(side));
			if (matModel != null) {
				sideFaces.addAll(matModel.getFaceQuads(side));
			}
			faces.put(side, sideFaces);
		}
		quads = new ArrayList<>(baseModel.getGeneralQuads());
		if (matModel != null)
			quads.addAll(matModel.getGeneralQuads());
	}

	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing side) {
		return faces.get(side);
	}

	@Override
	public List<BakedQuad> getGeneralQuads() {
		return quads;
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
	public VertexFormat getFormat() {
		return base.getFormat();
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		Material mat = MaterialStack.get(stack);
		if (mat == null)
			return this;
		else {
			return models.get(mat);
		}
	}

	@Override
	public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(
			TransformType cameraTransformType) {
		return Pair.of(this, base.handlePerspective(cameraTransformType).getRight());
	}
}