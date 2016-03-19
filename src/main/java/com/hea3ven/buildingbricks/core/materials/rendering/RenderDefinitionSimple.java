package com.hea3ven.buildingbricks.core.materials.rendering;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;

import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.tools.commonutils.client.BakerUtil;

@SideOnly(Side.CLIENT)
public class RenderDefinitionSimple extends RenderDefinition {

	protected static final Logger logger = LogManager.getLogger("BuildingBricks.RenderDefinition");
	protected final MaterialBlockType blockType;

	public RenderDefinitionSimple(MaterialBlockType blockType) {
		this.blockType = blockType;
	}

	@Override
	public IBakedModel bakeItem(ModelManager modelManager, Material mat) {
		IModel model = loadModel(modelManager, "item", mat, blockType.getName());
		model = BakerUtil.retexture(mat.getTextures(), model);
		IModelState modelState = getItemModelState();
		return BakerUtil.bake(model, modelState != null ? modelState : model.getDefaultState());
	}

	@Override
	public IBakedModel bake(ModelManager modelManager, Material mat, IBlockState state) {
		IModel model = getModel(modelManager, mat, state);
		model = BakerUtil.retexture(mat.getTextures(), model);
		IModelState modelState = getModelState(state);
		return BakerUtil.bake(model, modelState != null ? modelState : model.getDefaultState());
	}

	protected IModel getModel(ModelManager modelManager, Material mat, IBlockState state) {
		return loadModel(modelManager, "block", mat, blockType.getName());
	}

	protected IModelState getItemModelState() {
		return null;
	}

	protected IModelState getModelState(IBlockState state) {
		return null;
	}
}
