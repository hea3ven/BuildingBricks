package com.hea3ven.buildingbricks.core.materials;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;

public class BlockDescription {

	private String blockName;
	private Block block;
	private ItemStack stack;
	private MaterialBlockType type;
	private int meta;
	private String tagName;
	private NBTBase tagValue;

	public BlockDescription(MaterialBlockType type, int metadata, String tagName, NBTBase tagValue) {
		this.type = type;
		this.meta = metadata;
		this.tagName = tagName;
		this.tagValue = tagValue;
	}

	public BlockDescription(MaterialBlockType type, Block block, int metadata, String tagName,
			NBTBase tagValue) {
		this(type, metadata, tagName, tagValue);
		this.block = block;
	}

	public BlockDescription(MaterialBlockType type, Block block, int metadata) {
		this(type, block, metadata, null, null);
	}

	public BlockDescription(MaterialBlockType type, Block block) {
		this(type, block, 0);
	}

	public BlockDescription(MaterialBlockType type, String blockName, int metadata, String tagName,
			NBTBase tagValue) {
		this(type, metadata, tagName, tagValue);
		this.blockName = blockName;
	}

	public BlockDescription(MaterialBlockType type, String blockName, int metadata) {
		this(type, blockName, metadata, null, null);
	}

	public BlockDescription(MaterialBlockType type, String blockName) {
		this(type, blockName, 0);
	}

	public ItemBlock getItem() {
		return (ItemBlock) getStack().getItem();
	}

	public ItemStack getStack() {
		if (stack == null) {
			stack = new ItemStack(getBlock(), 1, meta);
			if (tagName != null)
				stack.setTagInfo(tagName, tagValue);
		}
		return stack;
	}

	public Block getBlock() {
		if (block == null) {
			block = Block.getBlockFromName(blockName);
		}
		return block;
	}

	public MaterialBlockType getType() {
		return type;
	}
}
