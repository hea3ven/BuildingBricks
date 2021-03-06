package com.hea3ven.buildingbricks.core.client;

import javax.vecmath.Vector3f;
import java.util.List;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.client.model.ModelItemMaterial;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;
import com.hea3ven.tools.commonutils.client.BakerUtil;
import com.hea3ven.tools.commonutils.client.ModelBakerBase;

@SideOnly(Side.CLIENT)
public class ModelBakerItemMaterial extends ModelBakerBase {

	public static ModelBakerItemMaterial instance;
	private ResourceLocation modelLoc;
	private ModelResourceLocation targetModelLoc;
	private Vector3f translation;
	private Vector3f scale;

	public ModelBakerItemMaterial(String modelLocName, String targetModelLocName, Vector3f translation,
			Vector3f scale) {
		modelLoc = new ResourceLocation(modelLocName);
		targetModelLoc = new ModelResourceLocation(targetModelLocName);
		this.translation = translation;
		this.scale = scale;
	}

	@Override
	public void onModelBakeEvent(ModelBakeEvent event) {
		IModel baseItemModel = getModel(modelLoc);
		IBakedModel baseBakedItemModel = BakerUtil.bake(baseItemModel, DefaultVertexFormats.ITEM);
		ModelItemMaterial dynModel = new ModelItemMaterial(baseBakedItemModel);
		event.getModelRegistry().putObject(targetModelLoc, dynModel);

		for (Material material : MaterialRegistry.getAll()) {

			ItemStack stack = material.getFirstBlock().getStack();
			Item item = stack.getItem();
			List<String> variantNames;
			try {
				//noinspection unchecked
				variantNames =
						(List<String>) ReflectionHelper.findMethod(ModelBakery.class, event.getModelLoader(),
								new String[] {"getVariantNames", "func_177596_a"}, Item.class)
								.invoke(event.getModelLoader(), item);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			ResourceLocation itemName = new ResourceLocation(
					variantNames.size() > 1 ? variantNames.get(stack.getMetadata()) : variantNames.get(0));
			ResourceLocation model =
					new ResourceLocation(itemName.getResourceDomain(), "item/" + itemName.getResourcePath());
			IModel itemModel = getModel(model, new ResourceLocation("minecraft", "block/cube_bottom_top"));
			itemModel = BakerUtil.retexture(material.getTextures(), itemModel);
			IModelState modelState = new TRSRTransformation(translation, null, scale, null);
			IBakedModel bakedItemModel = BakerUtil.bake(itemModel, modelState, DefaultVertexFormats.ITEM);

			itemModel = getModel(modelLoc);
			baseBakedItemModel = BakerUtil.bake(itemModel, DefaultVertexFormats.ITEM);

			dynModel.put(material, new ModelItemMaterial(baseBakedItemModel, bakedItemModel));
		}
	}
}
