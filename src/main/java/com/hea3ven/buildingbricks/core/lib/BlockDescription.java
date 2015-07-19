package com.hea3ven.buildingbricks.core.lib;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;

import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;

public class BlockDescription {

	private Block block;
	private ItemStack stack;
	private MaterialBlockType type;

	public BlockDescription(MaterialBlockType type, Block block, int metadata, String tagName, NBTBase tagValue) {
		this.type = type;
		this.block = block;
		stack = new ItemStack(block, 1, metadata);
		stack.setTagInfo(tagName, tagValue);
	}

	public BlockDescription(MaterialBlockType type, Block block, int metadata) {
		this.type = type;
		this.block = block;
		stack = new ItemStack(block, 1, metadata);
	}

	public BlockDescription(MaterialBlockType type, Block block) {
		this.type = type;
		this.block = block;
		stack = new ItemStack(block);
	}

	public Item getItem() {
		return stack.getItem();
	}

	public ItemStack getStack() {
		return stack;
	}

	public Block getBlock() {
		return block;
	}

	public MaterialBlockType getType() {
		return type;
	}

}
