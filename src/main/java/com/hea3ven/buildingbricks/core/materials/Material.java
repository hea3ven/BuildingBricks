package com.hea3ven.buildingbricks.core.materials;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class Material {

	public String topTextureLocation;
	public String bottomTextureLocation;
	public String sideTextureLocation;
	public int globalId;
	private Block slabBlock;
	private Block stepBlock;
	private BlockRotation blockRotation;
	private Block cornerBlock;

	public Material(String materialId) {
		blockRotation = new BlockRotation();
	}

	public void setTextureName(String textureLocation) {
		this.topTextureLocation = textureLocation;
		this.bottomTextureLocation = textureLocation;
		this.sideTextureLocation = textureLocation;
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

