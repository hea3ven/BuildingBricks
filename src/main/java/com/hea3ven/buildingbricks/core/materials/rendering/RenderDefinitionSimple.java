package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import com.hea3ven.buildingbricks.core.materials.Material;

public class RenderDefinitionSimple implements IRenderDefinition {

	private String modelLocation;

	public RenderDefinitionSimple(String modelLocation) {
		this.modelLocation = modelLocation;
	}

	@Override
	public IModel getItemModel(Material mat) {
		return getModelOrDefault(modelLocation, mat);
	}

	@Override
	public IModelState getItemModelState(IModelState modelState) {
		return new ModelLoader.UVLock(modelState);
	}

	@Override
	public IModel getModel(IBlockState state, Material mat) {
		return getModelOrDefault(modelLocation, mat);
	}

	@Override
	public IModelState getModelState(IModelState modelState, IBlockState state) {
		return new ModelLoader.UVLock(modelState);
	}

	protected IModel getModelOrDefault(String modelLoc, Material mat) {
		IModel model = ModelLoaderRegistry.getModel(new ResourceLocation(
				modelLoc.replace(":block/", ":block/" + mat.materialId() + "_")));
		if (model != ModelLoaderRegistry.getMissingModel())
			return model;
		return ModelLoaderRegistry.getModel(new ResourceLocation(modelLoc));
	}

}
