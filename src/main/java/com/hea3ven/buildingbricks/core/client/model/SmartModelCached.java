package com.hea3ven.buildingbricks.core.client.model;

import java.util.Collection;
import java.util.HashMap;

import com.google.common.base.Optional;

import org.apache.commons.lang3.builder.HashCodeBuilder;

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
		cache = new HashMap<>();
	}

	public SmartModelCached(IFlexibleBakedModel delegate) {
		super(delegate);
	}

	public void put(IBlockState state, IFlexibleBakedModel model) {
		cache.put(calculateHash((IExtendedBlockState) state), new SmartModelCached(model));
	}

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		SmartModelCached model = cache.get(calculateHash((IExtendedBlockState) state));
		if (model != null)
			return model;
		else
			return this;
	}

	private int calculateHash(IExtendedBlockState state) {
		HashCodeBuilder hash = new HashCodeBuilder();
		for (Comparable value : (Collection<Comparable>) state.getProperties().values()) {
			hash.append(value);
		}
		for (Optional<?> value : state.getUnlistedProperties().values()) {
			hash.append(value);
		}
		return hash.build();
	}

}
