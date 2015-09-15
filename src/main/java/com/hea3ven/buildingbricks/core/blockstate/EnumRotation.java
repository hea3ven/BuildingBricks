package com.hea3ven.buildingbricks.core.blockstate;

import com.hea3ven.transition.m.util.EnumFacing;
import com.hea3ven.transition.m.util.IStringSerializable;

public enum EnumRotation implements IStringSerializable {
	ROT0(0, 0f, 0),
	ROT90(90, (float)Math.PI / 2, 1),
	ROT180(180, (float)Math.PI, 2),
	ROT270(270, (float)Math.PI * 3 / 2, 3);
	
	public static EnumRotation[] HALF_ROTATION = new EnumRotation[2];
	
	static {
		HALF_ROTATION[0] = ROT0;
		HALF_ROTATION[1] = ROT180;
	}
	
	public static EnumRotation getRotation(int rot) {
		return EnumRotation.values()[rot];
	}

	public static EnumRotation getRotation(EnumFacing side) {
		switch (side) {
		default:
		case NORTH:
			return ROT0;
		case EAST:
			return ROT90;
		case SOUTH:
			return ROT180;
		case WEST:
			return ROT270;
		}
	}

	public static EnumRotation getHalfRotation(int rot) {
		return rot == 0 ? ROT0 : ROT180;
	}
	
	private int angleDeg;
	private float angle;
	private int meta;

	EnumRotation(int angleDeg, float angle, int meta) {
		this.angleDeg = angleDeg;
		this.angle = angle;
		this.meta = meta;
	}

	public String getName() {
		return name();
	}

	public int getAngleDeg() {
		return angleDeg;
	}

	public float getAngle() {
		return angle;
	}

	public int getMetaValue() {
		return meta;
	}

}
