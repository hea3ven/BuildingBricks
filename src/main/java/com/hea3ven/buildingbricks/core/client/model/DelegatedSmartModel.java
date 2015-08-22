package com.hea3ven.buildingbricks.core.client.model;

import java.util.List;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.client.model.IFlexibleBakedModel;

@SuppressWarnings("deprecation")
public class DelegatedSmartModel implements IBakedModel {

	private IFlexibleBakedModel delegate;

	public DelegatedSmartModel(IFlexibleBakedModel delegate) {
		this.delegate = delegate;
	}

	public IFlexibleBakedModel getDelegate() {
		return delegate;
	}

	public void setDelegate(IFlexibleBakedModel delegate) {
		this.delegate = delegate;
	}

	@Override
	public List getFaceQuads(EnumFacing side) {
		return delegate.getFaceQuads(side);
	}

	@Override
	public List getGeneralQuads() {
		return delegate.getGeneralQuads();
	}

	@Override
	public boolean isAmbientOcclusion() {
		return delegate.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return delegate.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return delegate.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getTexture() {
		return delegate.getTexture();
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return delegate.getItemCameraTransforms();
	}

}