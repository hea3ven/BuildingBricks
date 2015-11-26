package com.hea3ven.buildingbricks.core.materials.rendering;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.materials.Material;

@SideOnly(Side.CLIENT)
public abstract class RenderDefinition {

	protected static final Logger logger = LogManager.getLogger("BuildingBricks.RenderDefinition");

	public abstract IModel getItemModel(Material mat);

	public abstract IModelState getItemModelState(IModelState modelState);

	public abstract IModel getModel(IBlockState state, Material mat);

	public abstract IModelState getModelState(IModelState modelState, IBlockState state);

	protected IModel getModelOrDefault(String modelLoc, Material mat) {
		try {
			ResourceLocation matLoc = new ResourceLocation(mat.getMaterialId());
			return ModelLoaderRegistry.getModel(new ResourceLocation(
					modelLoc.replace("minecraft:", "buildingbricks:")
							.replace(":block/",
									":block/" + matLoc.getResourceDomain() + "/" + matLoc.getResourcePath() +
											"/")));
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
