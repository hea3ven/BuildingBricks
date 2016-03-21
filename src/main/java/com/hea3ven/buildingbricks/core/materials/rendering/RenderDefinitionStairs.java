package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStairs.EnumShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;

@SideOnly(Side.CLIENT)
public class RenderDefinitionStairs extends RenderDefinitionSimple {

	public RenderDefinitionStairs() {
		super(MaterialBlockType.STAIRS);
	}

	@Override
	public IModelState getItemModelState() {
		return ModelRotation.X0_Y180;
	}

	@Override
	protected IModel getModel(ModelManager modelManager, Material mat, IBlockState state) {
		String modelName;
		switch (state.getValue(BlockStairs.SHAPE)) {
			default:
			case STRAIGHT:
				modelName = "stairs";
				break;
			case INNER_LEFT:
			case INNER_RIGHT:
				modelName = "stairs_inner";
				break;
			case OUTER_LEFT:
			case OUTER_RIGHT:
				modelName = "stairs_outer";
				break;
		}
		return loadModel(modelManager, "block", mat, modelName);
	}

	@Override
	protected IModelState getModelState(IBlockState state) {
		EnumShape shape = state.getValue(BlockStairs.SHAPE);
		int offset = (shape != EnumShape.INNER_LEFT && shape != EnumShape.OUTER_LEFT) ? 0 : -90;
		if (state.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.BOTTOM) {
			int angle = getAngle(state.getValue(BlockStairs.FACING));
			return ModelRotation.getModelRotation(0, angle + offset);
		} else {
			int angle = getAngle(state.getValue(BlockStairs.FACING));
			return ModelRotation.getModelRotation(180, angle - offset);
		}
	}

	private int getAngle(EnumFacing prop) {
		switch (prop) {
			default:
			case NORTH:
				return 270;
			case EAST:
				return 0;
			case SOUTH:
				return 90;
			case WEST:
				return 180;
		}
	}
}
