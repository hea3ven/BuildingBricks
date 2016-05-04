package com.hea3ven.buildingbricks.core.materials.rendering;

import com.google.common.base.Predicate;

import net.minecraft.block.BlockWall;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.MultipartBakedModel;
import net.minecraft.client.renderer.block.model.multipart.ICondition;

import net.minecraftforge.client.model.IModel;

import com.hea3ven.buildingbricks.core.block.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.tools.commonutils.client.BakerUtil;

public class RenderDefinitionConnectible extends RenderDefinition {

	protected String postModelLocation;
	private String shortPostModel;
	protected String connModelLocation;
	protected String itemModelLocation;

	public RenderDefinitionConnectible(String postModel, String shortPostModel, String connectionModel,
			String itemModel) {
		postModelLocation = postModel;
		this.shortPostModel = shortPostModel;
		connModelLocation = connectionModel;
		itemModelLocation = itemModel;
	}

	@Override
	public IBakedModel bakeItem(ModelManager modelManager, Material mat) {
		IModel model = loadModel(modelManager, "item", mat, itemModelLocation);
		model = BakerUtil.retexture(mat.getTextures(), model);
		return BakerUtil.bake(model);
	}

	@Override
	public IBakedModel bake(ModelManager modelManager, Material mat, IBlockState state) {
		MultipartBakedModel.Builder builder = new MultipartBakedModel.Builder();
		IModel model;
		if (postModelLocation != null) {
			if (state.getPropertyNames().contains(BlockWall.UP)) {
				model = loadModel(modelManager, "block", mat, postModelLocation);
				model = BakerUtil.retexture(mat.getTextures(), model);
				model = BakerUtil.uvlock(model, mat.getUvlock());
				builder.putModel(new BoolPropertyPredicate(BlockWall.UP), BakerUtil.bake(model));
				model = loadModel(modelManager, "block", mat, shortPostModel);
				model = BakerUtil.retexture(mat.getTextures(), model);
				model = BakerUtil.uvlock(model, mat.getUvlock());
				builder.putModel(new NegBoolPropertyPredicate(BlockWall.UP), BakerUtil.bake(model));
			} else {
				model = loadModel(modelManager, "block", mat, postModelLocation);
				model = BakerUtil.retexture(mat.getTextures(), model);
				model = BakerUtil.uvlock(model, mat.getUvlock());
				builder.putModel(ICondition.TRUE.getPredicate(state.getBlock().getBlockState()),
						BakerUtil.bake(model));
			}
		}

		model = loadModel(modelManager, "block", mat, connModelLocation);
		model = BakerUtil.retexture(mat.getTextures(), model);
		model = BakerUtil.uvlock(model, mat.getUvlock());
		builder.putModel(new BoolPropertyPredicate(BlockProperties.CONNECT_NORTH), BakerUtil.bake(model));

		model = loadModel(modelManager, "block", mat, connModelLocation);
		model = BakerUtil.retexture(mat.getTextures(), model);
		model = BakerUtil.uvlock(model, mat.getUvlock());
		builder.putModel(new BoolPropertyPredicate(BlockProperties.CONNECT_EAST),
				BakerUtil.bake(model, ModelRotation.X0_Y90));

		model = loadModel(modelManager, "block", mat, connModelLocation);
		model = BakerUtil.retexture(mat.getTextures(), model);
		model = BakerUtil.uvlock(model, mat.getUvlock());
		builder.putModel(new BoolPropertyPredicate(BlockProperties.CONNECT_SOUTH),
				BakerUtil.bake(model, ModelRotation.X0_Y180));

		model = loadModel(modelManager, "block", mat, connModelLocation);
		model = BakerUtil.retexture(mat.getTextures(), model);
		model = BakerUtil.uvlock(model, mat.getUvlock());
		builder.putModel(new BoolPropertyPredicate(BlockProperties.CONNECT_WEST),
				BakerUtil.bake(model, ModelRotation.X0_Y270));

		return builder.makeMultipartModel();
	}

	class BoolPropertyPredicate implements Predicate<IBlockState> {

		private final IProperty<Boolean> prop;

		public BoolPropertyPredicate(IProperty<Boolean> prop) {

			this.prop = prop;
		}

		@Override
		public boolean apply(IBlockState input) {
			return input.getValue(prop);
		}
	}

	class NegBoolPropertyPredicate implements Predicate<IBlockState> {

		private final IProperty<Boolean> prop;

		public NegBoolPropertyPredicate(IProperty<Boolean> prop) {

			this.prop = prop;
		}

		@Override
		public boolean apply(IBlockState input) {
			return !input.getValue(prop);
		}
	}
}
