package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.state.IBlockState;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.materials.Material;

@SideOnly(Side.CLIENT)
public interface IRenderDefinition {

	IModel getItemModel(Material mat);

	IModelState getItemModelState(IModelState modelState);

	IModel getModel(IBlockState state, Material mat);

	IModelState getModelState(IModelState modelState, IBlockState state);

}
