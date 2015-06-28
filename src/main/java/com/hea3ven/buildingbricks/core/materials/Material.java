package com.hea3ven.buildingbricks.core.materials;

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

	public void addBlock(MaterialBlockType blockType, BlockDescription blockDesc) {
		getBlockRotation().add(blockType, blockDesc);

	}

	public void addBlock(MaterialBlockType blockType) {
		addBlock(blockType, MaterialBlockRegistry.instance.addBlock(blockType, this));
	}

	public BlockDescription getBlock(MaterialBlockType blockType) {
		return getBlockRotation().get(blockType);
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
