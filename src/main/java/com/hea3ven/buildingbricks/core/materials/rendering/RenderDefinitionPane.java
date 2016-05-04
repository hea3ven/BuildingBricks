package com.hea3ven.buildingbricks.core.materials.rendering;

import net.minecraft.block.BlockPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.MultipartBakedModel;

import net.minecraftforge.client.model.IModel;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.tools.commonutils.client.BakerUtil;

public class RenderDefinitionPane extends RenderDefinition {
	public RenderDefinitionPane() {
	}

	@Override
	public IBakedModel bakeItem(ModelManager modelManager, Material mat) {
		IModel model = loadModel(modelManager, "item", mat, "pane");
		model = BakerUtil.retexture(mat.getTextures(), model);
		return BakerUtil.bake(model);
	}

	@Override
	public IBakedModel bake(ModelManager modelManager, Material mat, IBlockState state) {
		MultipartBakedModel.Builder builder = new MultipartBakedModel.Builder();
		IModel model;
		model = loadModel(modelManager, "block", mat, "pane_post");
		model = BakerUtil.retexture(mat.getTextures(), model);
		model = BakerUtil.uvlock(model, mat.getUvlock());
		builder.putModel(input -> true, BakerUtil.bake(model));

		model = loadModel(modelManager, "block", mat, "pane_side");
		model = BakerUtil.retexture(mat.getTextures(), model);
		model = BakerUtil.uvlock(model, mat.getUvlock());
		builder.putModel(state1 -> state1.getValue(BlockPane.NORTH), BakerUtil.bake(model));

		model = loadModel(modelManager, "block", mat, "pane_side");
		model = BakerUtil.retexture(mat.getTextures(), model);
		model = BakerUtil.uvlock(model, mat.getUvlock());
		builder.putModel(state1 -> state1.getValue(BlockPane.EAST),
				BakerUtil.bake(model, ModelRotation.X0_Y90));

		model = loadModel(modelManager, "block", mat, "pane_side");
		model = BakerUtil.retexture(mat.getTextures(), model);
		model = BakerUtil.uvlock(model, mat.getUvlock());
		builder.putModel(state1 -> state1.getValue(BlockPane.SOUTH),
				BakerUtil.bake(model, ModelRotation.X0_Y180));

		model = loadModel(modelManager, "block", mat, "pane_side");
		model = BakerUtil.retexture(mat.getTextures(), model);
		model = BakerUtil.uvlock(model, mat.getUvlock());
		builder.putModel(state1 -> state1.getValue(BlockPane.WEST),
				BakerUtil.bake(model, ModelRotation.X0_Y270));

		model = loadModel(modelManager, "block", mat, "pane_noside");
		model = BakerUtil.retexture(mat.getTextures(), model);
		model = BakerUtil.uvlock(model, mat.getUvlock());
		builder.putModel(state1 -> !state1.getValue(BlockPane.NORTH), BakerUtil.bake(model));

		model = loadModel(modelManager, "block", mat, "pane_noside");
		model = BakerUtil.retexture(mat.getTextures(), model);
		model = BakerUtil.uvlock(model, mat.getUvlock());
		builder.putModel(state1 -> !state1.getValue(BlockPane.EAST),
				BakerUtil.bake(model, ModelRotation.X0_Y90));

		model = loadModel(modelManager, "block", mat, "pane_noside");
		model = BakerUtil.retexture(mat.getTextures(), model);
		model = BakerUtil.uvlock(model, mat.getUvlock());
		builder.putModel(state1 -> !state1.getValue(BlockPane.SOUTH),
				BakerUtil.bake(model, ModelRotation.X0_Y180));

		model = loadModel(modelManager, "block", mat, "pane_noside");
		model = BakerUtil.retexture(mat.getTextures(), model);
		model = BakerUtil.uvlock(model, mat.getUvlock());
		builder.putModel(state1 -> !state1.getValue(BlockPane.WEST),
				BakerUtil.bake(model, ModelRotation.X0_Y270));
		return builder.makeMultipartModel();
	}
}
