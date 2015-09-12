package com.hea3ven.buildingbricks.core.blocks.base;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
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

	protected void registerProperties(List<IProperty> props) {
	}

	@Override
	protected BlockState createBlockState() {
		List<IProperty> props = new ArrayList<IProperty>();
		registerProperties(props);
		return new BlockState(this, props.toArray(new IProperty[0]));
	}

	protected IBlockState getStateFromWorld(IBlockAccess world, BlockPos pos) {
		return world.getBlockState(pos);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		return blockLogic.getBlockColor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state) {
		return blockLogic.getRenderColor(state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		return blockLogic.colorMultiplier(worldIn, pos, renderPass);
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return blockLogic.getBlockLayer();
	}

	@Override
	public int getHarvestLevel(IBlockState state) {
		return 0;
	}

	@Override
	public String getHarvestTool(IBlockState state) {
		return blockLogic.getHarvestTool(state);
	}
}