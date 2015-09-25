package com.hea3ven.buildingbricks.core.materials.rendering;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.materials.Material;

@SideOnly(Side.CLIENT)
public class RenderDefinitionSimple implements IRenderDefinition {

	protected static final Logger logger = LogManager.getLogger("BuildingBricks.RenderDefinition");

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
		try {
			return ModelLoaderRegistry.getModel(new ResourceLocation(
					modelLoc.replace(":block/", ":block/" + mat.materialId() + "_")));
		} catch (IOException e) {
			try {
				return ModelLoaderRegistry.getModel(new ResourceLocation(modelLoc));
			} catch (IOException e1) {
				logger.warn("Could not find model {}", modelLoc);
				return ModelLoaderRegistry.getMissingModel();
			}
		}
	}

}
