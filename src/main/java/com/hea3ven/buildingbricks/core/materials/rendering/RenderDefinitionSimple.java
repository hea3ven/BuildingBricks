package com.hea3ven.buildingbricks.core.materials.rendering;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.materials.Material;

@SideOnly(Side.CLIENT)
public class RenderDefinitionSimple extends RenderDefinition {

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
}
