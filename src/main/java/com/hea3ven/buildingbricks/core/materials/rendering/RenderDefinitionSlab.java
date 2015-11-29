package com.hea3ven.buildingbricks.core.materials.rendering;

import javax.vecmath.Vector3f;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelRotation;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksSlab;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.Material;

@SideOnly(Side.CLIENT)
public class RenderDefinitionSlab extends RenderDefinitionSimple {

	private boolean vertical;

	public RenderDefinitionSlab(boolean vertical) {
		super(null);
		this.vertical = vertical;
	}

	public RenderDefinitionSlab() {
		this(false);
	}

	@Override
	public IModel getItemModel(Material mat) {
		if (vertical)
			return getModelOrDefault("buildingbricks:block/slab_vertical", mat);
		return getModelOrDefault("buildingbricks:block/slab", mat);
	}

	@Override
	public IModelState getItemModelState(IModelState modelState) {
		if (vertical)
			return new ModelLoader.UVLock(ModelRotation.X0_Y270);
		return super.getItemModelState(modelState);
	}

	@Override
	public IModel getModel(IBlockState state, Material mat) {
		return getItemModel(mat);
	}

	@Override
	public IModelState getModelState(IModelState modelState, IBlockState state) {
		if (vertical) {
			switch (BlockProperties.getSide(state)) {
				default:
				case NORTH:
					modelState = ModelRotation.X0_Y0;
					break;
				case EAST:
					modelState = ModelRotation.X0_Y90;
					break;
				case SOUTH:
					modelState = ModelRotation.X0_Y180;
					break;
				case WEST:
					modelState = ModelRotation.X0_Y270;
					break;
			}
		} else {
			modelState = BlockBuildingBricksSlab.getHalf(state) == BlockSlab.EnumBlockHalf.BOTTOM ?
					ModelRotation.X0_Y0 : new TRSRTransformation(new Vector3f(0, 0.5f, 0), null, null, null);
		}
		return super.getModelState(modelState, state);
	}
}
