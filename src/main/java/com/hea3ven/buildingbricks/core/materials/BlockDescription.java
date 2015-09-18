package com.hea3ven.buildingbricks.core.materials;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;

public class BlockDescription {

	private Block block;
	private ItemStack stack;
	private MaterialBlockType type;
	private int meta;
	private String tagName;
	private NBTBase tagValue;

	public BlockDescription(MaterialBlockType type, Block block, int metadata, String tagName,
			NBTBase tagValue) {
		this.type = type;
		this.block = block;
		this.meta = metadata;
		this.tagName = tagName;
		this.tagValue = tagValue;
	}

	public BlockDescription(MaterialBlockType type, Block block, int metadata) {
		this(type, block, metadata, null, null);
	}

	public BlockDescription(MaterialBlockType type, Block block) {
		this(type, block, 0);
	}

	public Item getItem() {
		return getStack().getItem();
	}

	public ItemStack getStack() {
		if (stack == null) {
			stack = new ItemStack(block, 1, meta);
			if (tagName != null)
				stack.setTagInfo(tagName, tagValue);
		}
		return stack;
	}

	public Block getBlock() {
		return block;
	}

	public MaterialBlockType getType() {
		return type;
	}

}
