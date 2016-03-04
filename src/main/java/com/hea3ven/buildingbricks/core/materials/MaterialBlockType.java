package com.hea3ven.buildingbricks.core.materials;

public enum MaterialBlockType {
	FULL("block", 1000),
	STAIRS("stairs", 750),
	SLAB("slab", 500),
	VERTICAL_SLAB("vertical_slab", 500),
	STEP("step", 250),
	CORNER("corner", 125),
	WALL("wall", 1000),
	FENCE("fence", 1500),
	FENCE_GATE("fence_gate", 3000),
	PANE("pane", 100);

	public static MaterialBlockType getBlockType(int id) {
		if (id >= values().length)
			return FULL;
		return values()[id];
	}

	public static MaterialBlockType getForVolume(int volume) {
		for (MaterialBlockType blockType : values()) {
			if (blockType.getVolume() == volume)
				return blockType;
		}
		return null;
	}

	public static MaterialBlockType getBestForVolume(int volume) {
		for (MaterialBlockType blockType : values()) {
			if (blockType.getVolume() <= volume)
				return blockType;
		}
		return null;
	}

	public static MaterialBlockType getBestForVolume(Material mat, int volume) {
		for (MaterialBlockType blockType : values()) {
			if (mat.getBlock(blockType) != null && blockType.getVolume() <= volume)
				return blockType;
		}
		return null;
	}

	private String name;
	private int volume = 0;

	MaterialBlockType(String name, int volume) {
		this.name = name;
		this.volume = volume;
	}

	public String getName() {
		return name;
	}

	public int getVolume() {
		return volume;
	}

	public String getTranslationKey() {
		String name = getName().toLowerCase();
		while (name.indexOf('_') != -1) {
			int i = name.indexOf('_');
			name = name.substring(0, i) + Character.toUpperCase(name.charAt(i + 1)) + name.substring(i + 2);
		}
		return "blockType." + name;
	}

}
