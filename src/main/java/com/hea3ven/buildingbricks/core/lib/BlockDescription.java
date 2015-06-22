package com.hea3ven.buildingbricks.core.lib;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockDescription {

	private Block block;
	private ItemStack stack;

	public BlockDescription(Block block, int metadata) {
		this.block = block;
		stack = new ItemStack(block, 1, metadata);
	}

	public BlockDescription(Block block) {
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

}
