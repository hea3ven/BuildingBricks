package com.hea3ven.buildingbricks.core.materials;

import java.util.HashMap;

import net.minecraft.item.ItemStack;

import com.hea3ven.buildingbricks.core.lib.BlockDescription;

public class Material {

	private String materialId;
	private HashMap<String, String> textures = new HashMap<String, String>();
	private StructureMaterial structureMaterial;
	private BlockRotation blockRotation;

	public Material(String materialId) {
		this.materialId = materialId;
		blockRotation = new BlockRotation();
	}

	public String materialId() {
		return materialId;
	}

	public void setTexture(String name, String textureLocation) {
		this.textures.put(name, textureLocation);
	}

	public void setTexture(String textureLocation) {
		textures.put("all", textureLocation);
		textures.put("wall", textureLocation);
		textures.put("top", textureLocation);
		textures.put("bottom", textureLocation);
		textures.put("side", textureLocation);
	}

	public void setTexture(String topTextureLocation, String bottomTextureLocation,
			String sideTextureLocation) {
		textures.put("top", topTextureLocation);
		textures.put("bottom", bottomTextureLocation);
		textures.put("side", sideTextureLocation);
		textures.put("all", sideTextureLocation);
		textures.put("wall", sideTextureLocation);
	}

	public HashMap<String, String> getTextures() {
		return textures;
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

	@Override
	public String toString() {
		return "<Material " + materialId() + ">";
	}

}
