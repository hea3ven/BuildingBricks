package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.state.IBlockState;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.Material;

@SideOnly(Side.CLIENT)
public class RenderDefinitionStep extends RenderDefinitionRotHalf {

	public RenderDefinitionStep() {
		super(null);
	}

	@Override
	public IModel getItemModel(Material mat) {
		return getModelOrDefault("buildingbricks:block/step_bottom", mat);
	}

	@Override
	public IModel getModel(IBlockState state, Material mat) {
		if (BlockProperties.getVertical(state))
			return getModelOrDefault("buildingbricks:block/step_vertical", mat);
		return getModelOrDefault("buildingbricks:block/step_bottom", mat);
	}
}
