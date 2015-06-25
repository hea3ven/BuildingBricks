package com.hea3ven.buildingbricks.core.client.model;

import java.util.HashMap;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

@SuppressWarnings("deprecation")
public class SmartModelCached implements ISmartBlockModel {

	private HashMap<Integer, SmartModelCached> cache;
	private IFlexibleBakedModel delegate;

	public SmartModelCached() {
		cache = new HashMap<Integer, SmartModelCached>();
	}

	public void setDefault(IFlexibleBakedModel model) {
		delegate = model;
	}

	public SmartModelCached(IFlexibleBakedModel delegate) {
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

	public void put(IBlockState state, IFlexibleBakedModel model) {
		cache.put(calculateHash(state), new SmartModelCached(model));
	}

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		SmartModelCached model = cache.get(calculateHash(state));
		if (model != null)
			return model;
		else
			return this;
	}

	private int calculateHash(IBlockState state) {
		return state.hashCode() + ((IExtendedBlockState) state).getUnlistedProperties().hashCode();
	}
}
