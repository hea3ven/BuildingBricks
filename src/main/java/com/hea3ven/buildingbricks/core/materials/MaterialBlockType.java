package com.hea3ven.buildingbricks.core.materials;

public enum MaterialBlockType {
	SLAB, STEP, CORNER;

	public static MaterialBlockType getBlockType(int id) {
		return values()[id];
	}

}
