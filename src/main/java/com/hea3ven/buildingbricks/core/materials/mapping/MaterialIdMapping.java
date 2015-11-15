package com.hea3ven.buildingbricks.core.materials.mapping;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.world.WorldSavedData;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class MaterialIdMapping extends WorldSavedData {

	static final Logger logger = LogManager.getLogger("BuildingBricks.MaterialIdMapping");

	static MaterialIdMapping instance;

	private RegistryNamespaced registry = new RegistryNamespaced();
	private Map<Short, String> missingRegistry = Maps.newHashMap();
	private Set<Pair<Short, String>> notFoundMaterials = Sets.newHashSet();
	private short nextId = 1;

	public MaterialIdMapping(String name) {
		super(name);
	}

	public static MaterialIdMapping get() {
		return instance;
	}

	public Material getMaterialById(short matId) {
		return (Material) registry.getObjectById(matId);
	}

	public short getIdForMaterial(Material mat) {
		return (short) registry.getIDForObject(mat);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		nextId = nbt.getShort("nextId");

		NBTTagCompound mappingNbt = nbt.getCompoundTag("mapping");
		for (String name : (Set<String>) mappingNbt.getKeySet()) {
			Material mat = MaterialRegistry.get(name);
			if (mat == null)
				notFoundMaterials.add(Pair.of(mappingNbt.getShort(name), name));
			else
				registry.register(mappingNbt.getShort(name), name, mat);
		}

		NBTTagCompound missingNbt = nbt.getCompoundTag("missing");
		for (String name : (Set<String>) missingNbt.getKeySet()) {
			missingRegistry.put(missingNbt.getShort(name), name);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setShort("nextId", nextId);

		NBTTagCompound mappingNbt = new NBTTagCompound();
		for (Material mat : (Iterable<Material>) registry) {
			mappingNbt.setShort(mat.getMaterialId(), (short) registry.getIDForObject(mat));
		}
		nbt.setTag("mapping", mappingNbt);

		NBTTagCompound missingNbt = new NBTTagCompound();
		for (Map.Entry<Short, String> name : missingRegistry.entrySet()) {
			missingNbt.setShort(name.getValue(), name.getKey());
		}
		nbt.setTag("missing", missingNbt);
	}

	public void validate() {
		Set<Material> loadedMaterials = Sets.newHashSet(MaterialRegistry.getAll());
		for (Material mat : (Iterable<Material>) registry) {
			loadedMaterials.remove(mat);
		}

		for (Pair<Short, String> entry : notFoundMaterials) {
			Material mat = MaterialRegistry.get(entry.getValue());
			if (mat != null) {
				loadedMaterials.remove(mat);
				registry.register(entry.getKey(), entry.getValue(), mat);
			} else {
				logger.warn("Missing material {} which had id {}", entry.getValue(), entry.getKey());
				missingRegistry.put(entry.getKey(), entry.getValue());
			}
		}
		notFoundMaterials.clear();

		for (Map.Entry<Short, String> entry : Maps.newHashMap(missingRegistry).entrySet()) {
			Material mat = MaterialRegistry.get(entry.getValue());
			if (mat != null) {
				logger.info("Previously missing material {} which had id {} found", entry.getValue(),
						entry.getKey());
				loadedMaterials.remove(mat);
				missingRegistry.remove(entry.getKey());
				registry.register(entry.getKey(), mat.getMaterialId(), mat);
			}
		}

		for (Material mat : loadedMaterials) {
			short newId = nextId++;
			logger.info("Registering material {} with id {}", mat.getMaterialId(), newId);
			registry.register(newId, mat.getMaterialId(), mat);
		}
		markDirty();
	}
}
