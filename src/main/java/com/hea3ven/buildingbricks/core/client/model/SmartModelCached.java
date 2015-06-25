package com.hea3ven.buildingbricks.core.client.model;

import java.util.HashMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;

import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

@SuppressWarnings("deprecation")
public class SmartModelCached extends DelegatedSmartModel implements ISmartBlockModel {

	private HashMap<Integer, SmartModelCached> cache;

	public SmartModelCached() {
		super(null);
		cache = new HashMap<Integer, SmartModelCached>();
	}

	public SmartModelCached(IFlexibleBakedModel delegate) {
		super(delegate);
	}

	public void put(IBlockState state, IFlexibleBakedModel model) {
		cache.put(calculateHash(state), new SmartModelCached(model));
	}

	public void setDefault(IFlexibleBakedModel model) {
		delegate = model;
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
