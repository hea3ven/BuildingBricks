package com.hea3ven.buildingbricks.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import com.hea3ven.buildingbricks.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class TexturedModelLoader implements ICustomModelLoader {

	public ModelLoader loader;
	public ModelBakery bakery;

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return modelLocation.getResourceDomain().equals(ModBuildingBricks.MODID)
				&& modelLocation.getResourcePath().startsWith("models/material/");
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) {
		int materialId = getMaterialId(modelLocation);
		Material mat = MaterialRegistry.get(materialId);
		Map<String, String> textures = new HashMap<String, String>();
		textures.put("top", mat.topTextureLocation);
		textures.put("side", mat.sideTextureLocation);
		textures.put("bottom", mat.bottomTextureLocation);
		String modelName = getModelName(modelLocation);
		ModelBlock model = createModelBlock(new ResourceLocation(modelName),
				textures);
		return wrapModel(modelLocation, model);
	}

	private String getModelName(ResourceLocation modelLocation) {
		String model = modelLocation.getResourcePath().substring(16).split("_", 2)[1];
		if (model.equals("half_slab")) {
			return "minecraft:block/half_slab";
		} else if (model.equals("step")) {
			return "buildingbricks:block/step_bottom";
		} else if (model.equals("step_vertical")) {
			return "buildingbricks:block/step_vertical";
		} else if (model.equals("corner")) {
			return "buildingbricks:block/corner_bottom";
		} else {
			return null;
		}
	}

	private int getMaterialId(ResourceLocation modelLocation) {
		return Integer.parseInt(modelLocation.getResourcePath().substring(16).split("_", 2)[0]);
	}

	private IModel wrapModel(ResourceLocation modelLocation, ModelBlock model) {
		Class<?>[] innerClasses = loader.getClass().getDeclaredClasses();
		for (Class<?> innerClass : innerClasses) {
			if (innerClass.getName().equals(
					"net.minecraftforge.client.model.ModelLoader$VanillaModelWrapper")) {
				try {
					Constructor<?> constructor = innerClass.getConstructor(ModelLoader.class,
							ResourceLocation.class,
							ModelBlock.class);
					constructor.setAccessible(true);
					IModel wrappedModel = (IModel) constructor.newInstance(loader, modelLocation,
							model);
					Method method = innerClass.getMethod("getTextures");
					method.setAccessible(true);
					method.invoke(wrappedModel);
					return wrappedModel;
				} catch (InstantiationException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e);
				} catch (SecurityException e) {
					throw new RuntimeException(e);
				}
			}
		}
		throw new RuntimeException();
	}

	private ModelBlock createModelBlock(ResourceLocation parent, Map textures) {
		Constructor[] allConstructors = ModelBlock.class.getDeclaredConstructors();
		Constructor constructor = null;
		for (Constructor ctor : allConstructors) {
			Class<?>[] pType = ctor.getParameterTypes();

			if (pType[0].equals(ResourceLocation.class) && pType[1].equals(List.class)) {
				constructor = ctor;
			}
		}
		try {
			constructor.setAccessible(true);
			return (ModelBlock) constructor.newInstance(parent, Collections.emptyList(), textures,
					true, true, ItemCameraTransforms.DEFAULT);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
