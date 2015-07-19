package com.hea3ven.buildingbricks.core.materials;

import net.minecraft.item.ItemStack;

import com.hea3ven.buildingbricks.core.lib.BlockDescription;

public class Material {

	private String materialId;
	private String topTextureLocation;
	private String bottomTextureLocation;
	private String sideTextureLocation;
	private StructureMaterial structureMaterial;
	private BlockRotation blockRotation;

	public Material(String materialId) {
		this.materialId = materialId;
		blockRotation = new BlockRotation();
	}

	public String materialId() {
		return materialId;
	}

	public void setTexture(String textureLocation) {
		this.topTextureLocation = textureLocation;
		this.bottomTextureLocation = textureLocation;
		this.sideTextureLocation = textureLocation;
	}

	public void setTexture(String topTextureLocation, String bottomTextureLocation,
			String sideTextureLocation) {
		this.topTextureLocation = topTextureLocation;
		this.bottomTextureLocation = bottomTextureLocation;
		this.sideTextureLocation = sideTextureLocation;
	}

	public void setStructureMaterial(StructureMaterial structureMaterial) {
		this.structureMaterial = structureMaterial;
	}

	public StructureMaterial getStructureMaterial() {
		return structureMaterial;
	}

	public BlockRotation getBlockRotation() {
		return blockRotation;
	}

	public void addBlock(BlockDescription blockDesc) {
		getBlockRotation().add(blockDesc.getType(), blockDesc);

	}

	public void addBlock(MaterialBlockType blockType) {
		addBlock(MaterialBlockRegistry.instance.addBlock(blockType, this));
	}

	public BlockDescription getBlock(MaterialBlockType blockType) {
		return getBlockRotation().get(blockType);
	}

	public BlockDescription getBlock(ItemStack stack) {
		for (BlockDescription blockDesc : getBlockRotation().getAll().values()) {
			if (ItemStack.areItemsEqual(stack, blockDesc.getStack())
					&& ItemStack.areItemStackTagsEqual(stack, blockDesc.getStack())) {
				return blockDesc;
			}
		}
		return null;
	}

	public String topTextureLocation() {
		return topTextureLocation;
	}

	public String bottomTextureLocation() {
		return bottomTextureLocation;
	}

	public String sideTextureLocation() {
		return sideTextureLocation;
	}

	@Override
	public String toString() {
		return "<Material " + materialId() + ">";
	}

}
