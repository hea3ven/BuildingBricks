package com.hea3ven.buildingbricks.core.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.materials.MaterialBlockLogic;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public class BlockBuildingBricksBase extends Block {

	protected MaterialBlockLogic blockLogic;

	public BlockBuildingBricksBase(StructureMaterial structMat, MaterialBlockType blockType) {
		super(structMat.getMcMaterial());

		blockLogic = new MaterialBlockLogic(structMat, blockType);
		blockLogic.initBlock(this);
	}

	public boolean requiresUpdates() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		return blockLogic.getBlockColor();
	}

	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state) {
		return blockLogic.getRenderColor(state);
	}

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		return blockLogic.colorMultiplier(worldIn, pos, renderPass);
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return blockLogic.getBlockLayer();
	}

	public int getHarvestLevel(IBlockState state) {
		return 0;
	}

	public String getHarvestTool(IBlockState state) {
		return blockLogic.getHarvestTool(state);
	}
}