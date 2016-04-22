package com.hea3ven.buildingbricks.core.materials;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;

public class BlockRotation {

	private HashMap<MaterialBlockType, BlockDescription> items;

	public BlockRotation() {
		items = new HashMap<>();
	}

	public void add(MaterialBlockType blockType, BlockDescription blockDesc) {
		items.put(blockType, blockDesc);
	}

	public void remove(BlockDescription blockDesc) {
		items.remove(blockDesc.getType());
	}

	public MaterialBlockType getFirst() {
		// TODO: Rewrite this
		for (MaterialBlockType blockType : MaterialBlockType.values()) {
			if (items.containsKey(blockType) && blockType.isStackType())
				return blockType;
		}
		throw new IllegalStateException("no items defined");
	}

	public MaterialBlockType getNext(MaterialBlockType blockType) {
		// TODO: Rewrite this
		for (int i = 1; i < MaterialBlockType.values().length; i++) {
			MaterialBlockType nextBlockType = MaterialBlockType.getBlockType(
					(blockType.ordinal() + i) % MaterialBlockType.values().length);
			if (items.containsKey(nextBlockType) && nextBlockType.getStackType() == nextBlockType)
				return nextBlockType;
		}
		return blockType;
	}

	public MaterialBlockType getPrev(MaterialBlockType blockType) {
		// TODO: Rewrite this
		for (int i = 1; i < MaterialBlockType.values().length; i++) {
			MaterialBlockType nextBlockType = MaterialBlockType.getBlockType(
					(blockType.ordinal() + MaterialBlockType.values().length - i) %
							MaterialBlockType.values().length);
			if (items.containsKey(nextBlockType) && nextBlockType.isStackType())
				return nextBlockType;
		}
		return blockType;
	}

	public Item getItem(MaterialBlockType blockType) {
		return items.get(blockType).getItem();
	}

	public Map<MaterialBlockType, BlockDescription> getAll() {
		return items;
	}

	public BlockDescription get(MaterialBlockType blockType) {
		return items.get(blockType);
	}

	public BlockDescription get(int slotIndex) {
		for (MaterialBlockType blockType : MaterialBlockType.values()) {
			if (items.containsKey(blockType) && blockType.isStackType()) {
				if (slotIndex-- <= 0) {
					return items.get(blockType);
				}
			}
		}
		return null;
	}

	public int getIndex(MaterialBlockType targetBlockType) {
		int i = 0;
		for (MaterialBlockType blockType : MaterialBlockType.values()) {
			if (items.containsKey(blockType) && blockType.isStackType()) {
				if (blockType == targetBlockType)
					return i;
				i++;
			}
		}
		throw new IllegalArgumentException("This block rotation does not have the target block type");
	}
}
