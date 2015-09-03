package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStairs.EnumShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.Material;

public class RenderDefinitionStairs extends RenderDefinitionSimple {

	public RenderDefinitionStairs() {
		super("minecraft:block/stairs");
	}

	@Override
	public IModel getItemModel(Material mat) {
		return getModelOrDefault("buildingbricks:block/stairs", mat);
	}

	@Override
	public IModelState getItemModelState(IModelState modelState) {
		return super.getItemModelState(ModelRotation.X0_Y180);
	}

	@Override
	public IModel getModel(IBlockState state, Material mat) {
		switch (BlockProperties.<BlockStairs.EnumShape> getProp(state, BlockStairs.SHAPE)) {
		default:
		case STRAIGHT:
			return getModelOrDefault("buildingbricks:block/stairs", mat);
		case INNER_LEFT:
		case INNER_RIGHT:
			return getModelOrDefault("buildingbricks:block/stairs_inner", mat);
		case OUTER_LEFT:
		case OUTER_RIGHT:
			return getModelOrDefault("buildingbricks:block/stairs_outer", mat);
		}
	}

	@Override
	public IModelState getModelState(IModelState modelState, IBlockState state) {
		EnumShape shape = BlockProperties.<BlockStairs.EnumShape> getProp(state, BlockStairs.SHAPE);
		int offset = (shape != EnumShape.INNER_LEFT && shape != EnumShape.OUTER_LEFT) ? 0 : -90;
		if (BlockProperties.getProp(state, BlockStairs.HALF) == BlockStairs.EnumHalf.BOTTOM) {
			int angle = getAngle(BlockProperties.<EnumFacing> getProp(state, BlockStairs.FACING));
			modelState = ModelRotation.getModelRotation(0, angle + offset);
		} else {
			int angle = getAngle(BlockProperties.<EnumFacing> getProp(state, BlockStairs.FACING));
			modelState = ModelRotation.getModelRotation(180, angle - offset);
		}
		return super.getModelState(modelState, state);
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
