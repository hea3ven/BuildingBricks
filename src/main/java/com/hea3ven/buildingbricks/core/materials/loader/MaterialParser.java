package com.hea3ven.buildingbricks.core.materials.loader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;

import net.minecraft.util.JsonUtils;

import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRecipes.MaterialBlockRecipeBuilder;

public class MaterialParser {

	static final Gson GSON =
			(new GsonBuilder()).registerTypeAdapter(MaterialDefinition.class, new MaterialDeserializer())
					.registerTypeAdapter(StructureMaterial.class, new StructureMaterialDeserializer())
					.create();

	public static void loadMaterialFromStream(InputStream matStream) {
		MaterialDefinition matDef =
				GSON.fromJson(new InputStreamReader(matStream, Charsets.UTF_8), MaterialDefinition.class);

		for (Material mat : matDef.getMaterials()) {
			for (MaterialBlockType blockType : mat.getStructureMaterial().getBlockTypes()) {
				if (mat.getBlock(blockType) == null) {
					mat.addBlock(new BlockDescription(blockType,
							MaterialBlockRecipes.getForType(mat.getStructureMaterial(), blockType)));
				}
			}
			MaterialRegistry.registerMaterial(mat);
		}
	}

	public static class MaterialDeserializer implements JsonDeserializer<MaterialDefinition> {

		@Override
		public MaterialDefinition deserialize(JsonElement element, Type typeOfT,
				JsonDeserializationContext context)
				throws JsonParseException {
			JsonObject json = element.getAsJsonObject();

			if (!json.has("id"))
				throw new JsonParseException("material does not have an id");

			MaterialBuilder matBuilder;
			if (!json.has("meta"))
				matBuilder = new MaterialBuilderSimple(json.get("id").getAsString());
			else if (json.get("meta").getAsString().equals("dye")) {
				matBuilder = new MaterialBuilderDyeMeta(json.get("id").getAsString());
			} else {
				throw new JsonParseException("invalid meta specified");
			}

			if (!json.has("type"))
				throw new JsonParseException("material does not have a type");
			StructureMaterial structMat = context.deserialize(json.get("type"), StructureMaterial.class);
			matBuilder.setStructureMaterial(structMat);

			if (json.has("hardness")) {
				matBuilder.setHardness(json.get("hardness").getAsFloat());
			}

			if (json.has("resistance")) {
				matBuilder.setResistance(json.get("resistance").getAsFloat());
			}

			if (json.has("normalHarvest")) {
				matBuilder.setNormalHarvestMaterial(json.get("normalHarvest").getAsString());
			}

			if (json.has("silkHarvest")) {
				matBuilder.setSilkHarvestMaterial(json.get("silkHarvest").getAsString());
			}

			if (!json.has("textures"))
				throw new JsonParseException("material does not have textures");
			if (json.get("textures").isJsonPrimitive())
				matBuilder.setTextures(ImmutableMap.<String, String>builder()
						.put("all", json.get("textures").getAsString())
						.build());
			else {
				JsonObject textures = json.get("textures").getAsJsonObject();
				ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
				for (Map.Entry<String, JsonElement> entry : textures.entrySet()) {
					builder.put(entry.getKey(), entry.getValue().getAsString());
				}
				matBuilder.setTextures(builder.build());
			}

			for (Map.Entry<String, JsonElement> blockEntry : json.get("blocks")
					.getAsJsonObject()
					.entrySet()) {
				MaterialBlockType type = MaterialBlockType.valueOf(blockEntry.getKey().toUpperCase());
				if (blockEntry.getValue().isJsonPrimitive()) {
					String blockName = blockEntry.getValue().getAsString();
					matBuilder.addBlock(type, blockName, 0, null, null);
				} else {
					JsonObject blockJson = blockEntry.getValue().getAsJsonObject();

					List<MaterialBlockRecipeBuilder> recipes = null;
					if (blockJson.has("recipes")) {
						recipes = parseRecipes(blockJson.getAsJsonArray("recipes"));
					}

					if (blockJson.has("id")) {
						String blockName = blockJson.get("id").getAsString();
						int meta = blockJson.get("meta").getAsInt();
						matBuilder.addBlock(type, blockName, meta, null, recipes);
					} else {
						if (recipes == null)
							recipes = MaterialBlockRecipes.getForType(structMat, type);
						if (blockJson.has("recipes_add")) {
							recipes.addAll(parseRecipes(blockJson.getAsJsonArray("recipes_add")));
						}
						matBuilder.addBlock(type, recipes);
					}
				}
			}

			return new MaterialDefinition(matBuilder.build());
		}

		private List<MaterialBlockRecipeBuilder> parseRecipes(JsonArray recipesData) {
			List<MaterialBlockRecipeBuilder> recipes = new ArrayList<>();
			for (JsonElement recipeData : recipesData) {
				JsonObject recipe = recipeData.getAsJsonObject();
				MaterialBlockRecipeBuilder builder = new MaterialBlockRecipeBuilder();

				if (recipe.has("output"))
					builder.outputAmount(recipe.get("output").getAsInt());

				String[] ingredients = new String[recipe.getAsJsonArray("ingredients").size()];
				int i = 0;
				for (JsonElement ingredient : recipe.getAsJsonArray("ingredients"))
					ingredients[i++] = ingredient.getAsString();
				recipes.add(builder.ingredients(ingredients));
			}
			return recipes;
		}
	}

	public static class StructureMaterialDeserializer implements JsonDeserializer<StructureMaterial> {

		@Override
		public StructureMaterial deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			String strucMatName = JsonUtils.getString(json, "type");
			return StructureMaterial.valueOf(strucMatName.toUpperCase());
		}
	}

	private static class MaterialDefinition {
		private final Material[] materials;

		public MaterialDefinition(Material[] materials) {

			this.materials = materials;
		}

		public Material[] getMaterials() {
			return materials;
		}
	}
}
