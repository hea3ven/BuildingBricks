package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelManager;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;

@SideOnly(Side.CLIENT)
public class RenderDefinitionFenceGate extends RenderDefinitionSimple {

	public RenderDefinitionFenceGate(MaterialBlockType blockType) {
		super(blockType);
	}

	@Override
	protected IModel getModel(ModelManager modelManager, Material mat, IBlockState state) {
		if (state.getValue(BlockFenceGate.OPEN))
			return loadModel(modelManager, "block", mat, "fence_gate_open");
		else
			return loadModel(modelManager, "block", mat, "fence_gate_closed");
	}
}
