package com.hea3ven.transition.m.util;

import com.google.common.base.Predicate;

import net.minecraftforge.common.util.ForgeDirection;

public enum EnumFacing {
	DOWN(Axis.Y, AxisDirection.NEGATIVE, new Vec3i(0, -1, 0), -1),
	UP(Axis.Y, AxisDirection.POSITIVE, new Vec3i(0, 1, 0), -1),
	NORTH(Axis.Z, AxisDirection.NEGATIVE, new Vec3i(0, 0, -1), 2),
	SOUTH(Axis.Z, AxisDirection.POSITIVE, new Vec3i(0, 0, 1), 0),
	WEST(Axis.X, AxisDirection.NEGATIVE, new Vec3i(-1, 0, 0), 1),
	EAST(Axis.X, AxisDirection.POSITIVE, new Vec3i(1, 0, 0), 3);

	public static enum Axis {
		X, Y, Z
	}

	public static enum AxisDirection {
		POSITIVE, NEGATIVE
	}

	public static enum Plane implements Predicate<EnumFacing> {
		HORIZONTAL, VERTICAL;

		@Override
		public boolean apply(EnumFacing input) {
			if (this == VERTICAL)
				return input.getAxis() == Axis.Y;
			else
				return input.getAxis() != Axis.Y;
		}
	}

	public static EnumFacing get(int facingIdx) {
		return values()[facingIdx % 6];
	}

	public static EnumFacing get(ForgeDirection side) {
		return get(side.ordinal());
	}

	public static EnumFacing getHorizontal(int i) {
		for (EnumFacing face : values()) {
			if (face.getHorizontalIndex() == i)
				return face;
		}
		return null;
	}

	static {
		DOWN.opposite = UP;
		UP.opposite = DOWN;
		NORTH.opposite = SOUTH;
		SOUTH.opposite = NORTH;
		EAST.opposite = WEST;
		WEST.opposite = EAST;
	}

	private Axis axis;
	private AxisDirection axisDirection;
	private EnumFacing opposite;
	private Vec3i faceDirection;
	private int horizontalIndex;

	private EnumFacing(Axis axis, AxisDirection axisDirection, Vec3i faceDirection,
			int horizontalIndex) {
		this.axis = axis;
		this.axisDirection = axisDirection;
		this.faceDirection = faceDirection;
		this.horizontalIndex = horizontalIndex;
	}

	public String getName() {
		return name();
	}

	public Axis getAxis() {
		return axis;
	}

	public AxisDirection getAxisDirection() {
		return axisDirection;
	}

	public EnumFacing getOpposite() {
		return opposite;
	}

	public EnumFacing rotateY() {
		switch (this) {
		case NORTH:
			return EAST;
		case EAST:
			return SOUTH;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
		default:
			return this;
		}
	}

	public EnumFacing rotateYCCW() {
		switch (this) {
		case NORTH:
			return WEST;
		case EAST:
			return NORTH;
		case SOUTH:
			return EAST;
		case WEST:
			return SOUTH;
		default:
			return this;
		}
	}

	public int getHorizontalIndex() {
		return horizontalIndex;
	}

	public ForgeDirection getForgeDir() {
		return ForgeDirection.getOrientation(ordinal());
	}

	public Vec3i getDirectionVec() {
		return faceDirection;
	}
}
