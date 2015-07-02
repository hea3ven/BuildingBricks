package com.hea3ven.buildingbricks.core.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public abstract class BlockBuildingBricksBase extends Block {

	private String name;

	public BlockBuildingBricksBase(StructureMaterial materialIn, String name) {
		super(materialIn.getMcMaterial());

		this.name = name;
		setUnlocalizedName(name);
	}

	public String getName() {
		return name;
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

}