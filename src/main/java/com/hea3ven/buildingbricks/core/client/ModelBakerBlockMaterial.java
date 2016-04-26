package com.hea3ven.buildingbricks.core.client;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Table.Cell;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.client.model.ModelItemMaterialBlock;
import com.hea3ven.buildingbricks.core.client.model.ModelMaterialBlock;
import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.buildingbricks.core.materials.rendering.*;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;
import com.hea3ven.tools.commonutils.client.ModelBakerBase;
import com.hea3ven.tools.commonutils.util.BlockStateUtil;

@SideOnly(Side.CLIENT)
public class ModelBakerBlockMaterial extends ModelBakerBase {

	public static Map<Material, TextureAtlasSprite> particleTextures = Maps.newHashMap();

	private Map<MaterialBlockType, RenderDefinition> renderDefinitions = Maps.newHashMap();

	public ModelBakerBlockMaterial() {
		super();

		renderDefinitions.put(MaterialBlockType.FULL, new RenderDefinitionSimple(MaterialBlockType.FULL));
		renderDefinitions.put(MaterialBlockType.STAIRS, new RenderDefinitionStairs());
		renderDefinitions.put(MaterialBlockType.SLAB, new RenderDefinitionSlab(MaterialBlockType.SLAB));
		renderDefinitions.put(MaterialBlockType.VERTICAL_SLAB,
				new RenderDefinitionSlab(MaterialBlockType.VERTICAL_SLAB));
		renderDefinitions.put(MaterialBlockType.STEP, new RenderDefinitionStep());
		renderDefinitions.put(MaterialBlockType.CORNER,
				new RenderDefinitionRotHalf(MaterialBlockType.CORNER));
		renderDefinitions.put(MaterialBlockType.WALL,
				new RenderDefinitionConnectable("wall_post", "wall_post_short", "wall_connection", "wall"));
		renderDefinitions.put(MaterialBlockType.FENCE,
				new RenderDefinitionConnectable("fence_post", null, "fence_connection", "fence"));
		renderDefinitions.put(MaterialBlockType.FENCE_GATE,
				new RenderDefinitionFenceGate(MaterialBlockType.FENCE_GATE));
		renderDefinitions.put(MaterialBlockType.PANE, new RenderDefinitionPane());
	}

	@Override
	public void onTextureStichPreEvent(TextureStitchEvent.Pre event) {
		for (Material mat : MaterialRegistry.getAll()) {
			particleTextures.put(mat,
					event.getMap().registerSprite(new ResourceLocation(mat.getTextures().get("particle"))));
		}
	}

	@Override
	public void onModelBakeEvent(ModelBakeEvent event) {
		for (Cell<MaterialBlockType, StructureMaterial, Block> cell : MaterialBlockRegistry.instance.getBlocks()
				.cellSet()) {
			bakeBlockModels(event.getModelManager(), event.getModelRegistry(), cell.getRowKey(),
					cell.getColumnKey(), cell.getValue());
		}
	}

	private void bakeBlockModels(ModelManager modelManager,
			IRegistry<ModelResourceLocation, IBakedModel> modelRegistry,
			MaterialBlockType blockType, StructureMaterial structMat, Block block) {
		RenderDefinition renderDefinition = renderDefinitions.get(blockType);
		if (renderDefinition == null)
			return;

		ResourceLocation blockName = Block.REGISTRY.getNameForObject(block);

		// Register models in the registry
		ModelItemMaterialBlock itemBakedModel = new ModelItemMaterialBlock(modelManager.getMissingModel());
		modelRegistry.putObject(new ModelResourceLocation(blockName + "#inventory"), itemBakedModel);

		ModelMaterialBlock blockBakedModel = new ModelMaterialBlock();
		for (Object stateObj : block.getBlockState().getValidStates()) {
			ModelResourceLocation modelLoc =
					new ModelResourceLocation(blockName, getPropertyString((IBlockState) stateObj));
			modelRegistry.putObject(modelLoc, blockBakedModel);
		}
		blockBakedModel.setDelegate(modelManager.getMissingModel());

		// Generate the actual models
		for (Material mat : MaterialBlockRegistry.instance.getBlockMaterials(block)) {
			// Item model
			itemBakedModel.put(mat.getMaterialId(), renderDefinition.bakeItem(modelManager, mat));

			// Block models
			for (Object stateObj : block.getBlockState().getValidStates()) {
				IBlockState state = (IBlockState) stateObj;
				state = TileMaterial.setStateMaterial((IExtendedBlockState) state, mat);
				blockBakedModel.put(BlockStateUtil.getHashCode(state),
						renderDefinition.bake(modelManager, mat, state));
			}
		}
	}
}
