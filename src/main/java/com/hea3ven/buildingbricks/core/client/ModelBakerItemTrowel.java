package com.hea3ven.buildingbricks.core.client;

import javax.vecmath.Vector3f;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.client.model.ModelTrowel;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

@SideOnly(Side.CLIENT)
public class ModelBakerItemTrowel extends ModelBakerBase {

	public static ModelBakerItemTrowel instance;

	public static void init() {
		instance = new ModelBakerItemTrowel();
		MinecraftForge.EVENT_BUS.register(instance);
	}

	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		bakeItemTrowelModels(event);
	}

	private void bakeItemTrowelModels(ModelBakeEvent event) {
		ResourceLocation trowelModelLoc = new ResourceLocation("buildingbricks:item/trowel");
		for (Material material : MaterialRegistry.getAll()) {

			// TODO: rework this better
			String model = (!material.getTextures().containsKey("overlay"))
					? "block/cube_bottom_top" : "block/grass";
			IModel itemModel = getModel(new ResourceLocation(model));
			itemModel = retexture(material.getTextures(), itemModel);
			Vector3f translation = new Vector3f(0.3f, 0.5f, 0.3f);
			Vector3f scale = new Vector3f(0.4f, 0.4f, 0.4f);
			IModelState modelState = new TRSRTransformation(translation, null, scale, null);
			IFlexibleBakedModel bakedItemModel = bake(itemModel, modelState, DefaultVertexFormats.ITEM);

			itemModel = getModel(trowelModelLoc);
			IFlexibleBakedModel baseBakedItemModel = bake(itemModel, DefaultVertexFormats.ITEM);

			ModelTrowel.models.put(material, new ModelTrowel(baseBakedItemModel, bakedItemModel));
		}
		IModel baseItemModel = getModel(trowelModelLoc);
		IFlexibleBakedModel baseBakedItemModel = bake(baseItemModel, DefaultVertexFormats.ITEM);
		event.modelRegistry.putObject(new ModelResourceLocation("buildingbricks:trowel#inventory"),
				new ModelTrowel(baseBakedItemModel));
	}
}
