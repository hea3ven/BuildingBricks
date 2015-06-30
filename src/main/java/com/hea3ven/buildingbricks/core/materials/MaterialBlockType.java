package com.hea3ven.buildingbricks.core.materials;

import java.util.Collection;

import javax.vecmath.Vector3f;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.TRSRTransformation;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;

public enum MaterialBlockType {
	FULL("minecraft:block/cube_bottom_top"),
	SLAB("minecraft:block/half_slab"),
	STEP("buildingbricks:block/step_bottom"),
	CORNER("buildingbricks:block/corner_bottom");

	public static MaterialBlockType getBlockType(int id) {
		return values()[id];
	}

	private Block block;
	private ResourceLocation baseModel;

	private MaterialBlockType(String modelLocation) {
		baseModel = new ResourceLocation(modelLocation);
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public ResourceLocation baseModel() {
		return baseModel;
	}

	public ResourceLocation baseModel(IBlockState state) {
		if (this == STEP) {
			if (BlockProperties.getVertical(state)) {
				return new ResourceLocation("buildingbricks:block/step_vertical");
			}
		}
		return baseModel;
	}

	public Collection<IBlockState> getValidBlockStates() {
		return block.getBlockState().getValidStates();
	}

	public IModelState getModelStateFromBlockState(IBlockState state) {
		if (this == SLAB) {
			return getModelRotationFromFacing(BlockProperties.getFacing(state));
		} else if (this == STEP) {
			if (BlockProperties.getVertical(state)) {
				return getModelRotationVertical(BlockProperties.getRotation(state));
			} else {
				return getModelRotationFromFacing(BlockProperties.getRotation(state),
						BlockProperties.getHalf(state));
			}

		} else if (this == CORNER) {
			return getModelRotationFromFacing(BlockProperties.getRotation(state),
					BlockProperties.getHalf(state));
		} else {
			return null;
		}
	}

	private IModelState getModelRotationFromFacing(EnumFacing facing) {
		switch (facing) {
		default:
		case DOWN:
			return ModelRotation.X0_Y0;
		case UP:
			return ModelRotation.X180_Y0;
		case SOUTH:
			return ModelRotation.X90_Y0;
		case WEST:
			return ModelRotation.X90_Y90;
		case NORTH:
			return ModelRotation.X90_Y180;
		case EAST:
			return ModelRotation.X90_Y270;
		}
	}

	private IModelState getModelRotationVertical(EnumRotation rot) {
		return ModelRotation.getModelRotation(0, rot.getAngleDeg());
	}

	private IModelState getModelRotationFromFacing(EnumRotation rot, EnumBlockHalf half) {

		TRSRTransformation translate = new TRSRTransformation((half == EnumBlockHalf.BOTTOM) ? null
				: new Vector3f(0.0f, 0.5f, 0.0f), null, null, null);
		return translate.compose(new TRSRTransformation(ModelRotation.getModelRotation(0,
				rot.getAngleDeg())));
	}

}
