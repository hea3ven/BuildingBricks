package com.hea3ven.buildingbricks.core.materials;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import com.hea3ven.buildingbricks.core.blocks.base.BlockMaterial;

public class Material {

	private String materialId;
	private Map<String, String> textures = new HashMap<>();
	private StructureMaterial structureMaterial;
	private BlockRotation blockRotation;
	private float hardness = 1.0f;
	private float resistance = 5.0f;
	private String normalHarvestMaterial;
	private String silkHarvestMaterial = null;

	public Material(String materialId) {
		this.materialId = materialId;
		blockRotation = new BlockRotation();
		normalHarvestMaterial = this.materialId;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setTexture(String name, String textureLocation) {
		textures.put(name, textureLocation);
	}

	public Map<String, String> getTextures() {
		return textures;
	}

	public void setStructureMaterial(StructureMaterial structureMaterial) {
		this.structureMaterial = structureMaterial;
	}

	public StructureMaterial getStructureMaterial() {
		return structureMaterial;
	}

	public void setHardness(float hardness) {
		this.hardness = hardness;
	}

	public float getHardness() {
		return hardness;
	}

	public void setResistance(float resistance) {
		this.resistance = resistance;
	}

	public float getResistance() {
		return resistance;
	}

	public void setNormalHarvestMaterial(String mat) {
		normalHarvestMaterial = mat;
	}

	public String getNormalHarvestMaterial() {
		return normalHarvestMaterial;
	}

	public void setSilkHarvestMaterial(String mat) {
		silkHarvestMaterial = mat;
	}

	public String getSilkHarvestMaterial() {
		return silkHarvestMaterial;
	}

	public BlockRotation getBlockRotation() {
		return blockRotation;
	}

	public void addBlock(BlockDescription blockDesc) {
		getBlockRotation().add(blockDesc.getType(), blockDesc);
	}

	public void removeBlock(BlockDescription blockDesc) {
		getBlockRotation().remove(blockDesc);
	}

	public BlockDescription getBlock(MaterialBlockType blockType) {
		return getBlockRotation().get(blockType);
	}

	public BlockDescription getBlock(ItemStack stack) {
		for (BlockDescription blockDesc : getBlockRotation().getAll().values()) {
			if (ItemStack.areItemsEqual(stack, blockDesc.getStack()) &&
					ItemStack.areItemStackTagsEqual(stack, blockDesc.getStack())) {
				return blockDesc;
			}
		}
		return null;
	}

	public BlockDescription getFirstBlock() {
		return getBlock(blockRotation.getFirst());
	}

	@Override
	public String toString() {
		return "<Material " + getMaterialId() + ">";
	}

	public String getTranslationKey() {
		return "material." + getMaterialId().replace(':', '.');
	}

	public String getLocalizedName() {
		if (I18n.canTranslate(getTranslationKey()))
			return I18n.translateToLocal(getTranslationKey());

		BlockDescription block = getFirstBlock();
		if (block.getBlock() instanceof BlockMaterial)
			return materialId;

		return block.getStack().getDisplayName();
	}
}
