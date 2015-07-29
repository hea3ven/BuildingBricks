package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.state.IBlockState;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;

public interface IRenderDefinition {

	IModel getItemModel();

	IModelState getItemModelState(IModelState modelState);

	IModel getModel(IBlockState state);

	IModelState getModelState(IModelState modelState, IBlockState state);

}
