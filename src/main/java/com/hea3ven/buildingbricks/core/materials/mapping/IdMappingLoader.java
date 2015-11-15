package com.hea3ven.buildingbricks.core.materials.mapping;

import java.util.Set;

import com.google.common.collect.Sets;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.items.crafting.RecipeBindTrowel;
import com.hea3ven.buildingbricks.core.materials.Material;

public class IdMappingLoader {

	public static Set<Pair<ItemStack, Material>> dynamicStacks = Sets.newHashSet();

	@SubscribeEvent
	public void onWorldLoadEvent(WorldEvent.Load event) {
		if (!event.world.isRemote && event.world.provider.getDimensionId() == 0) {
			MaterialIdMapping.logger.info("Loading the material mapping for the world");
			MapStorage storage = event.world.getPerWorldStorage();
			MaterialIdMapping mapping =
					(MaterialIdMapping) storage.loadData(MaterialIdMapping.class, "materialsIdMapping");
			if (mapping == null) {
				MaterialIdMapping.logger.info("Material mapping not found, generating a new one");
				mapping = new MaterialIdMapping("materialsIdMapping");
				storage.setData("materialsIdMapping", mapping);
			}
			MaterialIdMapping.logger.info("Verifying the material mapping");
			mapping.validate();
			MaterialIdMapping.instance = mapping;
			for (Pair<ItemStack, Material> stack : dynamicStacks) {
				ModBuildingBricks.trowel.setBindedMaterial(stack.getLeft(), stack.getRight());
			}
		}
	}

	@SubscribeEvent
	public void onWorldUnloadEvent(WorldEvent.Unload event) {
		if (event.world.provider.getDimensionId() == 0) {
			MaterialIdMapping.logger.info("Unloading the material mapping for the world");
			MaterialIdMapping.instance = null;
		}
	}
}
