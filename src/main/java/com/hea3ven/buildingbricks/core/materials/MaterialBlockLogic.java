package com.hea3ven.buildingbricks.core.materials;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MaterialBlockLogic {

	private StructureMaterial structMat;

	public MaterialBlockLogic(StructureMaterial structMat) {
		this.structMat = structMat;
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

}
