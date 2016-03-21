package com.hea3ven.buildingbricks.core.materials.rendering;

import javax.vecmath.Vector3f;

import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelRotation;

import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksSlab;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;

@SideOnly(Side.CLIENT)
public class RenderDefinitionSlab extends RenderDefinitionSimple {

	public RenderDefinitionSlab(MaterialBlockType blockType) {
		super(blockType);
	}

	@Override
	protected IModelState getItemModelState() {
		return ModelRotation.X0_Y270;
	}

	@Override
	protected IModelState getModelState(IBlockState state) {
		if (blockType == MaterialBlockType.SLAB) {
			if (BlockBuildingBricksSlab.getHalf(state) == EnumBlockHalf.BOTTOM)
				return ModelRotation.X0_Y0;
			else
				return new TRSRTransformation(new Vector3f(0, 0.5f, 0), null, null, null);
		} else {
			switch (BlockProperties.getSide(state)) {
				default:
				case NORTH:
					return ModelRotation.X0_Y0;
				case EAST:
					return ModelRotation.X0_Y90;
				case SOUTH:
					return ModelRotation.X0_Y180;
				case WEST:
					return ModelRotation.X0_Y270;
			}
		}
	}
}
