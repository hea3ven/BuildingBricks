package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.client.model.IModelState;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksSlab;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;

public class RenderDefinitionSlab extends RenderDefinitionSimple {

	private boolean vertical;

	public RenderDefinitionSlab(boolean vertical) {
		super("minecraft:block/half_slab");
		this.vertical = vertical;
	}

	public RenderDefinitionSlab() {
		this(false);
	}

	@Override
	public IModelState getItemModelState(IModelState modelState) {
		if (vertical)
			return ModelRotation.X90_Y90;
		return super.getItemModelState(modelState);
	}

	@Override
	public IModelState getModelState(IModelState modelState, IBlockState state) {
		if (vertical) {
			switch (BlockProperties.getSide(state)) {
			default:
			case SOUTH:
				modelState = ModelRotation.X90_Y0;
				break;
			case WEST:
				modelState = ModelRotation.X90_Y90;
				break;
			case NORTH:
				modelState = ModelRotation.X90_Y180;
				break;
			case EAST:
				modelState = ModelRotation.X90_Y270;
				break;
			}
		} else {
			modelState = BlockBuildingBricksSlab.getHalf(state) == BlockSlab.EnumBlockHalf.BOTTOM
					? ModelRotation.X0_Y0 : ModelRotation.X180_Y0;
		}
		return super.getModelState(modelState, state);
	}
}
