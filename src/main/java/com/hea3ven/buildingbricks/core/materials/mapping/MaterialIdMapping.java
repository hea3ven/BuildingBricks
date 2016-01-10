package com.hea3ven.buildingbricks.core.materials.mapping;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.RegistryNamespaced;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialRegistry;

public class MaterialIdMapping {

	static final Logger logger = LogManager.getLogger("BuildingBricks.MaterialIdMapping");

	static MaterialIdMapping instance;

	private RegistryNamespaced<String, Material> registry = new RegistryNamespaced<>();
	private Map<Short, String> missingRegistry = Maps.newHashMap();
	private Map<String, Short> notFoundMaterials = Maps.newHashMap();
	private short nextId = 1;

	public MaterialIdMapping() {
	}

	public static MaterialIdMapping get() {
		return instance;
	}

	public Material getMaterialById(short matId) {
		return registry.getObjectById(matId);
	}

	public short getIdForMaterial(Material mat) {
		if (mat == null)
			return 0;

		short id = (short) registry.getIDForObject(mat);
		if (id != -1)
			return id;

		if (notFoundMaterials.containsKey(mat.getMaterialId())) {
			id = notFoundMaterials.get(mat.getMaterialId());
			registry.register(id, mat.getMaterialId(), mat);
			notFoundMaterials.remove(mat.getMaterialId());
			return id;
		}

		return 0;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		nextId = nbt.getShort("nextId");

		NBTTagCompound mappingNbt = nbt.getCompoundTag("mapping");
		for (String name : mappingNbt.getKeySet()) {
			Material mat = MaterialRegistry.get(name);
			if (mat == null)
				notFoundMaterials.put(name, mappingNbt.getShort(name));
			else
				registry.register(mappingNbt.getShort(name), name, mat);
		}

		NBTTagCompound missingNbt = nbt.getCompoundTag("missing");
		for (String name : missingNbt.getKeySet()) {
			missingRegistry.put(missingNbt.getShort(name), name);
		}
	}

	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setShort("nextId", nextId);

		NBTTagCompound mappingNbt = new NBTTagCompound();
		for (Material mat : registry) {
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
		for (Material mat : registry) {
			loadedMaterials.remove(mat);
		}

		for (Entry<String, Short> entry : notFoundMaterials.entrySet()) {
			Material mat = MaterialRegistry.get(entry.getKey());
			if (mat != null) {
				loadedMaterials.remove(mat);
				registry.register(entry.getValue(), entry.getKey(), mat);
			} else {
				logger.warn("Missing material {} which had id {}", entry.getValue(), entry.getKey());
				missingRegistry.put(entry.getValue(), entry.getKey());
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
			add(mat);
		}
	}

	public void add(Material mat) {
		short newId = nextId++;
		logger.info("Registering material {} with id {}", mat.getMaterialId(), newId);
		registry.register(newId, mat.getMaterialId(), mat);
	}
}
