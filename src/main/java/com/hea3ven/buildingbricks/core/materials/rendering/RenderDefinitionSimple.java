package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class RenderDefinitionSimple implements IRenderDefinition {

	private ResourceLocation modelLocation;

	public RenderDefinitionSimple(String modelLocation) {
		this.modelLocation = new ResourceLocation(modelLocation);
	}

	@Override
	public IModel getItemModel() {
		return ModelLoaderRegistry.getModel(modelLocation);
	}

	@Override
	public IModelState getItemModelState(IModelState modelState) {
		return new ModelLoader.UVLock(modelState);
	}

	@Override
	public IModel getModel(IBlockState state) {
		return ModelLoaderRegistry.getModel(modelLocation);
	}

	@Override
	public IModelState getModelState(IModelState modelState, IBlockState state) {
		return new ModelLoader.UVLock(modelState);
	}
}
