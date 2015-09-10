package com.hea3ven.buildingbricks.core.materials;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MaterialBlockLogic {

	private Material mat;
	private MaterialBlockType blockType;

	public MaterialBlockLogic(Material mat, MaterialBlockType blockType) {
		this.mat = mat;
		this.blockType = blockType;
	}

	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		if (!mat.getStructureMaterial().getColor())
			return 16777215;
		else
			return ColorizerGrass.getGrassColor(0.5D, 1.0D);
	}

	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state) {
		if (!mat.getStructureMaterial().getColor())
			return 16777215;
		else
			return this.getBlockColor();
	}

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		if (!mat.getStructureMaterial().getColor())
			return 16777215;
		else
			return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return mat.getStructureMaterial().getBlockLayer();
	}

	public String getLocalizedName() {

		String matName = StatCollector.canTranslate(mat.getTranslationKey())
				? StatCollector.translateToLocal(mat.getTranslationKey())
				: mat.getBlock(MaterialBlockType.FULL).getStack().getDisplayName();
		return StatCollector.translateToLocalFormatted(blockType.getTranslationKey(), matName);
	}

}
