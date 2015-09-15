package com.hea3ven.buildingbricks.core.materials;

import net.minecraft.block.Block;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import com.hea3ven.transition.m.block.state.IBlockState;
import com.hea3ven.transition.m.util.BlockPos;
import com.hea3ven.transition.m.util.EnumWorldBlockLayer;
import com.hea3ven.transition.m.world.biome.BiomeColorHelper;

public class MaterialBlockLogic {

	private StructureMaterial structMat;
	private MaterialBlockType blockType;

	public MaterialBlockLogic(StructureMaterial structMat, MaterialBlockType blockType) {
		this.structMat = structMat;
		this.blockType = blockType;
	}

	public void initBlock(Block block) {
		block.setStepSound(structMat.getSoundType());
		block.setHardness(structMat.getHardness());
		if (structMat.getResistance() > 0)
			block.setResistance(structMat.getResistance());
		block.slipperiness = structMat.getSlipperiness();
	}

	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		if (!structMat.getColor())
			return 16777215;
		else
			return ColorizerGrass.getGrassColor(0.5D, 1.0D);
	}

	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state) {
		if (!structMat.getColor())
			return 16777215;
		else
			return this.getBlockColor();
	}

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		if (!structMat.getColor())
			return 16777215;
		else
			return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return structMat.getBlockLayer();
	}

	public String getLocalizedName(Material mat) {
		return StatCollector.translateToLocalFormatted(blockType.getTranslationKey(),
				mat.getLocalizedName());
	}

	public String getHarvestTool(IBlockState state) {
		return structMat.getTool();
	}
}
