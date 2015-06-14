package com.hea3ven.buildingbricks.core.client.model;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.client.model.ISmartItemModel;

import com.hea3ven.buildingbricks.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.Material;

@SuppressWarnings("deprecation")
public class ModelTrowel implements ISmartItemModel {
	public static HashMap<Material, ModelTrowel> models = new HashMap<Material, ModelTrowel>();

	private TextureAtlasSprite texture;
	private ItemCameraTransforms cameraTransforms;
	private HashMap<EnumFacing, List> faces = new HashMap<EnumFacing, List>();
	private List quads;

	public ModelTrowel(IBakedModel baseModel) {
		this(baseModel, null);
	}

	public ModelTrowel(IBakedModel baseModel, IBakedModel matModel) {
		cameraTransforms = baseModel.getItemCameraTransforms();
		texture = baseModel.getTexture();
		for (EnumFacing side : EnumFacing.VALUES) {
			List sideFaces = baseModel.getFaceQuads(side);
			if (matModel != null) {
				sideFaces.addAll(matModel.getFaceQuads(side));
			}
			faces.put(side, sideFaces);
		}
		quads = baseModel.getGeneralQuads();
		if (matModel != null)
			quads.addAll(matModel.getGeneralQuads());
	}

	@Override
	public List getFaceQuads(EnumFacing side) {
		return faces.get(side);
	}

	@Override
	public List getGeneralQuads() {
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
	public TextureAtlasSprite getTexture() {
		return texture;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return cameraTransforms;
	}

	@Override
	public IBakedModel handleItemState(ItemStack stack) {
		Material mat = ModBuildingBricks.trowel.getBindedMaterial(stack);
		if (mat == null)
			return this;
		else {
			return models.get(mat);
		}
	}

}
