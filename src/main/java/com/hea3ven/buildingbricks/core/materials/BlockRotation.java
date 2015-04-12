package com.hea3ven.buildingbricks.core.materials;

import java.util.HashMap;

import net.minecraft.item.Item;

public class BlockRotation {

	private HashMap<MaterialBlockType, Item> items;

	public BlockRotation() {
		items = new HashMap<MaterialBlockType, Item>();
	}

	public void add(MaterialBlockType blockType, Item item) {
		items.put(blockType, item);
	}

	public MaterialBlockType getFirst() {
		// TODO: Rewrite this
		for (MaterialBlockType blockType : MaterialBlockType.values()) {
			if(items.containsKey(blockType))
				return blockType;
		}
		throw new IllegalStateException("no items defined");
	}

	public MaterialBlockType getNext(MaterialBlockType blockType) {
		// TODO: Rewrite this
		for (int i = 1; i < MaterialBlockType.values().length; i++) {
			MaterialBlockType nextBlockType = MaterialBlockType.getBlockType((blockType.ordinal() + i) % MaterialBlockType.values().length);
			if (items.containsKey(nextBlockType))
				return nextBlockType;
		}
		return blockType;
	}

	public MaterialBlockType getPrev(MaterialBlockType blockType) {
		// TODO: Rewrite this
		for (int i = 1; i < MaterialBlockType.values().length; i++) {
			MaterialBlockType nextBlockType = MaterialBlockType.getBlockType((blockType.ordinal() + MaterialBlockType.values().length - i) % MaterialBlockType.values().length);
			if (items.containsKey(nextBlockType))
				return nextBlockType;
		}
		return blockType;
	}

	public Item getItem(MaterialBlockType blockType) {
		return items.get(blockType);
	}

}
