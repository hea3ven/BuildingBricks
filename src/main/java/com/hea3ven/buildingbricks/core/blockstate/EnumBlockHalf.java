package com.hea3ven.buildingbricks.core.blockstate;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

public enum EnumBlockHalf implements IStringSerializable {
	TOP(0, 180, EnumFacing.UP),
	BOTTOM(1, 0, EnumFacing.DOWN);
	
	public static EnumBlockHalf getHalf(int index) {
		return (index == 0)? TOP : BOTTOM;
	}
	
	private int angleDeg;
	private int meta;
	private EnumFacing side;
	
	EnumBlockHalf(int meta, int angleDeg, EnumFacing side) {
		this.angleDeg = angleDeg;
		this.meta = meta;
		this.side = side;
	}

	public String getName() {
		return name();
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

}
