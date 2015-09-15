package com.hea3ven.buildingbricks.core.blocks.properties;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.transition.f.common.property.IUnlistedProperty;

public class PropertyMaterial implements IUnlistedProperty<Material> {

	@Override
	public String getName() {
		return "material";
	}

	@Override
	public boolean isValid(Material value) {
		return value != null;
	}

	@Override
	public Class<Material> getType() {
		return Material.class;
	}

	@Override
	public String valueToString(Material value) {
		return value.materialId();
	}

}
