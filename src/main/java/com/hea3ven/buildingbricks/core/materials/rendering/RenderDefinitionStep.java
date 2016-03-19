package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelRotation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;

@SideOnly(Side.CLIENT)
public class RenderDefinitionStep extends RenderDefinitionRotHalf {

	public RenderDefinitionStep() {
		super(MaterialBlockType.STEP);
	}

	@Override
	public IModelState getItemModelState() {
		return ModelRotation.X0_Y90;
	}

	@Override
	public IModel getModel(ModelManager modelManager, Material mat, IBlockState state) {
		return loadModel(modelManager, "block", mat,
				!state.getValue(BlockProperties.VERTICAL) ? "step" : "step_vertical");
	}
}
