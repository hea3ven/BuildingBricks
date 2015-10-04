package com.hea3ven.buildingbricks.core.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.MultiModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBakerBase {

	private static final Logger logger = LogManager.getLogger("BuildingBricks.RenderingManager");

	private DefaultStateMapper stateMap = new DefaultStateMapper();

	protected IModel getModel(ResourceLocation modelLoc) {
		try {
			return ModelLoaderRegistry.getModel(modelLoc);
		} catch (IOException e) {
			logger.warn("Could not find model {}", modelLoc);
			return ModelLoaderRegistry.getMissingModel();
		}
	}

	protected IFlexibleBakedModel bake(IModel model) {
		return bake(model, model.getDefaultState());
	}

	protected IFlexibleBakedModel bake(IModel model, IModelState modelState) {
		Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
			public TextureAtlasSprite apply(ResourceLocation location) {
				return Minecraft
						.getMinecraft()
						.getTextureMapBlocks()
						.getAtlasSprite(location.toString());
			}
		};
		return model.bake(modelState, Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
	}

	protected IModel retexture(HashMap<String, String> textures, IModel blockModel) {
		if (blockModel instanceof IRetexturableModel)
			return ((IRetexturableModel) blockModel).retexture(ImmutableMap.copyOf(textures));
		else if (blockModel instanceof MultiModel) {
			Map<String, Pair<IModel, IModelState>> parts = Maps.newHashMap();
			for (Entry<String, Pair<IModel, IModelState>> entry : ((MultiModel) blockModel)
					.getParts()
					.entrySet()) {
				parts.put(entry.getKey(), Pair.of(retexture(textures, entry.getValue().getLeft()),
						entry.getValue().getRight()));
			}
			return new MultiModel(retexture(textures, ((MultiModel) blockModel).getBaseModel()),
					blockModel.getDefaultState(), parts);
		}
		return blockModel;
	}

	protected String getPropertyString(IBlockState state) {
		return stateMap.getPropertyString(state.getProperties());
	}
}
