package com.hea3ven.buildingbricks.core.materials;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Material {

	public String topTextureLocation;
	public String bottomTextureLocation;
	public String sideTextureLocation;
	public int globalId;
	private Block fullBlock;
	private int fullBlockMeta;
	private Block slabBlock;
	private Block stepBlock;
	private Block cornerBlock;
	private BlockRotation blockRotation;

	public Material(String materialId) {
		blockRotation = new BlockRotation();
	}

	public void setTextureName(String textureLocation) {
		this.topTextureLocation = textureLocation;
		this.bottomTextureLocation = textureLocation;
		this.sideTextureLocation = textureLocation;
	}

	public void setFullBlock(Block block, int meta) {
		fullBlock = block;
		fullBlockMeta = meta;
		getBlockRotation().add(MaterialBlockType.FULL, Item.getItemFromBlock(block));
	}

	public Block getFullBlock() {
		return fullBlock;
	}

	public ItemStack getFullBlockItem() {
		return new ItemStack(fullBlock, 1, fullBlockMeta);
	}

	public void setSlabBlock(Block block) {
		slabBlock = block;
		getBlockRotation().add(MaterialBlockType.SLAB, Item.getItemFromBlock(block));
	}

	public Block getSlabBlock() {
		return slabBlock;
	}

	public void setStepBlock(Block block) {
		stepBlock = block;
		getBlockRotation().add(MaterialBlockType.STEP, Item.getItemFromBlock(block));
	}

	public Block getStepBlock() {
		return stepBlock;
	}

	public void setCornerBlock(Block block) {
		cornerBlock = block;
		getBlockRotation().add(MaterialBlockType.CORNER, Item.getItemFromBlock(block));
	}

	public Block getCornerBlock() {
		return cornerBlock;
	}

	public BlockRotation getBlockRotation() {
		return blockRotation;
	}

	public Item getBlockItem(MaterialBlockType blockType) {
		return getBlockRotation().getItem(blockType);
	}

}

