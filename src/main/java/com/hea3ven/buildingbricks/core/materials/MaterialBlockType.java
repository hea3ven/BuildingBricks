package com.hea3ven.buildingbricks.core.materials;

import java.util.Collection;

import javax.vecmath.Vector3f;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.TRSRTransformation;

import com.hea3ven.buildingbricks.core.blocks.BlockCorner;
import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;

public enum MaterialBlockType {
	FULL, SLAB, STEP, CORNER;

	public static MaterialBlockType getBlockType(int id) {
		return values()[id];
	}

	private Block block;

	public void setBlock(Block block) {
		this.block = block;
	}

	public ResourceLocation baseModel() {
		return new ResourceLocation("buildingbricks:block/corner_bottom");
	}

	public Collection<IBlockState> getValidBlockStates() {
		return block.getBlockState().getValidStates();
	}

	public IModelState getModelStateFromBlockState(IBlockState state) {
		return getModelRotationFromFacing(BlockCorner.getStateRotation(state),
				BlockCorner.getStateHalf(state));
	}

	private IModelState getModelRotationFromFacing(EnumRotation rot, EnumBlockHalf half) {

		TRSRTransformation translate = new TRSRTransformation((half == EnumBlockHalf.BOTTOM) ? null
				: new Vector3f(0.0f, 0.5f, 0.0f), null, null, null);
		return translate.compose(new TRSRTransformation(ModelRotation.getModelRotation(0,
				rot.getAngleDeg())));
	}

}
