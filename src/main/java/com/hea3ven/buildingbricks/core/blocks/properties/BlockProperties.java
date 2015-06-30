package com.hea3ven.buildingbricks.core.blocks.properties;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;

public class BlockProperties {
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyEnum HALF = PropertyEnum.create("half", EnumBlockHalf.class);
	public static final PropertyEnum ROTATION = PropertyEnum.create("rotation", EnumRotation.class);
	public static final PropertyBool VERTICAL = PropertyBool.create("vertical");

	public static EnumFacing getFacing(IBlockState state) {
		return (EnumFacing) state.getValue(FACING);
	}

	public static IBlockState setFacing(IBlockState state, EnumFacing facing) {
		return state.withProperty(FACING, facing);
	}

	public static Boolean getVertical(IBlockState state) {
		return (Boolean) state.getValue(VERTICAL);
	}

	public static IBlockState setVertical(IBlockState state, Boolean vert) {
		return state.withProperty(VERTICAL, vert);
	}

	public static EnumBlockHalf getHalf(IBlockState state) {
		return (EnumBlockHalf) state.getValue(HALF);
	}

	public static IBlockState setHalf(IBlockState state, EnumBlockHalf half) {
		return state.withProperty(HALF, half);
	}

	public static EnumRotation getRotation(IBlockState state) {
		return (EnumRotation) state.getValue(ROTATION);
	}

	public static IBlockState setRotation(IBlockState state, EnumRotation rotation) {
		return state.withProperty(ROTATION, rotation);
	}
}
