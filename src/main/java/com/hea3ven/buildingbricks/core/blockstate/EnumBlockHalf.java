package com.hea3ven.buildingbricks.core.blockstate;

import net.minecraft.block.BlockSlab;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

public enum EnumBlockHalf implements IStringSerializable {
	TOP(0, 180, EnumFacing.UP, BlockSlab.EnumBlockHalf.TOP),
	BOTTOM(1, 0, EnumFacing.DOWN, BlockSlab.EnumBlockHalf.BOTTOM);

	public static EnumBlockHalf getHalf(int index) {
		return (index == 0) ? TOP : BOTTOM;
	}

	private int angleDeg;
	private int meta;
	private EnumFacing side;
	private BlockSlab.EnumBlockHalf slabEnumVal;

	EnumBlockHalf(int meta, int angleDeg, EnumFacing side, BlockSlab.EnumBlockHalf slabEnumVal) {
		this.angleDeg = angleDeg;
		this.meta = meta;
		this.side = side;
		this.slabEnumVal = slabEnumVal;
	}

	public String getName() {
		return name().toLowerCase();
	}

	public int getAngleDeg() {
		return angleDeg;
	}

	public int getMetaValue() {
		return meta;
	}

	public EnumFacing getSide() {
		return side;
	}

	public BlockSlab.EnumBlockHalf toSlabEnum() {
		return slabEnumVal;
	}
}
