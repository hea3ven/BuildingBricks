package com.hea3ven.buildingbricks.core.materials.rendering;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.materials.Material;

@SideOnly(Side.CLIENT)
public abstract class RenderDefinition {

	protected static final Logger logger = LogManager.getLogger("BuildingBricks.RenderDefinition");

	protected IModel getModel(String modelLoc) {
		return getModel(new ResourceLocation(modelLoc));
	}

	protected IModel getModel(ResourceLocation modelLoc) {
		try {
			return ModelLoaderRegistry.getModel(modelLoc);
		} catch (Exception e) {
			return null;
		}
	}

	public abstract IBakedModel bakeItem(ModelManager modelManager, Material mat);

	public abstract IBakedModel bake(ModelManager modelManager, Material mat, IBlockState state);

	protected IModel loadModel(ModelManager modelManager, String type, Material mat, String modelName) {
		String path = type + "/" + mat.getMaterialId().replace(':', '/') + "/" + modelName;
		IModel model = getModel(new ResourceLocation("buildingbricks", path));
		if (model == null) {
			path = type + "/base/" + modelName;
			model = getModel(new ResourceLocation("buildingbricks", path));
		}
		if (model == null) {
			model = ModelLoaderRegistry.getMissingModel();
		}
		return model;
	}
}
