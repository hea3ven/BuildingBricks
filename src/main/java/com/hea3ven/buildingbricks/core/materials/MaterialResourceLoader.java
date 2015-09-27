package com.hea3ven.buildingbricks.core.materials;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map.Entry;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.apache.commons.io.Charsets;

import net.minecraft.block.Block;
import net.minecraft.util.JsonUtils;

public class MaterialResourceLoader {

	static final Gson GSON = (new GsonBuilder())
			.registerTypeAdapter(Material.class, new MaterialDeserializer())
			.registerTypeAdapter(StructureMaterial.class, new StructureMaterialDeserializer())
			.create();

	protected static void loadMaterialFromStream(InputStream matStream) {
		Material mat = GSON.fromJson(new InputStreamReader(matStream, Charsets.UTF_8),
				Material.class);
		for (MaterialBlockType blockType : mat.getStructureMaterial().getBlockTypes()) {
			if (mat.getBlock(blockType) == null)
				mat.addBlock(blockType);
		}
		MaterialRegistry.registerMaterial(mat);
	}

	protected static <T> T getField(Object obj, Class<?> cls, String fieldName,
			String deobfFieldName) {
		try {
			Field fld;
			try {
				fld = cls.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				fld = cls.getDeclaredField(deobfFieldName);
			}
			fld.setAccessible(true);
			return (T) fld.get(obj);
		} catch (Exception e) {
			Throwables.propagate(e);
			return null;
		}
	}

	public static class MaterialDeserializer implements JsonDeserializer<Material> {

		@Override
		public Material deserialize(JsonElement element, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			JsonObject json = element.getAsJsonObject();

			if (!json.has("id"))
				throw new JsonParseException("material does not have an id");

			Material mat = new Material(json.get("id").getAsString());

			if (!json.has("type"))
				throw new JsonParseException("material does not have a type");
			mat.setStructureMaterial((StructureMaterial) context.deserialize(json.get("type"),
					StructureMaterial.class));

			if (json.has("hardness")) {
				mat.setHardness(json.get("hardness").getAsFloat());
			}

			if (json.has("resistance")) {
				mat.setResistance(json.get("resistance").getAsFloat());
			}

			if (json.has("normalHarvest")) {
				mat.setNormalHarvestMaterial(json.get("normalHarvest").getAsString());
			}

			if (json.has("silkHarvest")) {
				mat.setSilkHarvestMaterial(json.get("silkHarvest").getAsString());
			}

			if (!json.has("textures"))
				throw new JsonParseException("material does not have textures");
			if (json.get("textures").isJsonPrimitive())
				mat.setTexture(json.get("textures").getAsString());
			else {
				JsonObject textures = json.get("textures").getAsJsonObject();
				for (Entry<String, JsonElement> entry : textures.entrySet()) {
					mat.setTexture(entry.getKey(), entry.getValue().getAsString());
				}
				if (!mat.getTextures().containsKey("all"))
					mat.setTexture("all", mat.getTextures().get("side"));
				if (!mat.getTextures().containsKey("particle"))
					mat.setTexture("particle", mat.getTextures().get("side"));
				if (!mat.getTextures().containsKey("wall"))
					mat.setTexture("wall", mat.getTextures().get("side"));
			}

			for (Entry<String, JsonElement> blockEntry : json
					.get("blocks")
					.getAsJsonObject()
					.entrySet()) {
				MaterialBlockType type = MaterialBlockType
						.valueOf(blockEntry.getKey().toUpperCase());
				if (blockEntry.getValue().isJsonPrimitive()) {
					String blockName = blockEntry.getValue().getAsString();
					mat.addBlock(new BlockDescription(type, Block.getBlockFromName(blockName)));
				} else {
					JsonObject blockJson = blockEntry.getValue().getAsJsonObject();
					String blockName = blockJson.get("id").getAsString();
					int meta = blockJson.get("meta").getAsInt();
					mat.addBlock(
							new BlockDescription(type, Block.getBlockFromName(blockName), meta));
				}
			}

			return mat;
		}

	}

	public static class StructureMaterialDeserializer
			implements JsonDeserializer<StructureMaterial> {

		@Override
		public StructureMaterial deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			String strucMatName = JsonUtils.getJsonElementStringValue(json, "type");
			return StructureMaterial.valueOf(strucMatName.toUpperCase());
		}

	}

}
