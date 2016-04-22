package com.hea3ven.buildingbricks.core.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;

import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;
import com.hea3ven.tools.commonutils.util.PlaceParams;

public class BlockPlacingUtil {
	private static final double RING_SIZE = 0.22f;

	private static int getRotationIndex(EnumFacing facing) {
		if (facing == EnumFacing.NORTH) {
			return 0;
		} else if (facing == EnumFacing.EAST) {
			return 1;
		} else if (facing == EnumFacing.SOUTH) {
			return 2;
		} else if (facing == EnumFacing.WEST) {
			return 3;
		}
		throw new IllegalArgumentException(facing.getName());
	}

	public static EnumRotation getRotation(EnumFacing facing) {
		return EnumRotation.getRotation(getRotationIndex(facing));
	}

	private static EnumRotation getRotation(EnumFacing facing, int offset) {
		return EnumRotation.getRotation((getRotationIndex(facing) + offset) % 4);
	}

	public static double getFaceX(EnumFacing facing, double hitX, double hitY, double hitZ) {
		switch (facing.getAxis()) {
			default:
			case X:
				if (facing.getAxisDirection() == AxisDirection.POSITIVE) {
					return hitZ;
				} else {
					return (1 - hitZ);
				}
			case Y:
				if (facing.getAxisDirection() == AxisDirection.POSITIVE) {
					return (1 - hitX);
				} else {
					return hitX;
				}
			case Z:
				if (facing.getAxisDirection() == AxisDirection.POSITIVE) {
					return (1 - hitX);
				} else {
					return hitX;
				}
		}
	}

	public static double getFaceY(EnumFacing facing, double hitX, double hitY, double hitZ) {
		switch (facing.getAxis()) {
			default:
			case X:
				return hitY;
			case Y:
				return hitZ;
			case Z:
				return hitY;
		}
	}

	public static boolean isInnerRing(PlaceParams params) {
		return isInnerRing(params.side, params.hit.xCoord, params.hit.yCoord, params.hit.zCoord);
	}

	public static boolean isInnerRing(EnumFacing facing, double hitX, double hitY, double hitZ) {
		double faceX = getFaceX(facing, hitX, hitY, hitZ);
		double faceY = getFaceY(facing, hitX, hitY, hitZ);
		return RING_SIZE <= faceX && faceX <= (1 - RING_SIZE) && RING_SIZE <= faceY
				&& faceY <= (1 - RING_SIZE);
	}

	public static EnumFacing getClosestFace(PlaceParams params) {
		return getClosestFace(params.side, params.hit.xCoord, params.hit.yCoord, params.hit.zCoord);
	}

	public static EnumFacing getClosestFace(EnumFacing facing, double hitX, double hitY, double hitZ) {
		double faceX = getFaceX(facing, hitX, hitY, hitZ);
		double faceY = getFaceY(facing, hitX, hitY, hitZ);
		if (facing.getAxis() == Axis.Y) {
			if (hitX >= hitZ) {
				if (hitX < (1 - hitZ)) {
					return EnumFacing.NORTH;
				} else {
					return EnumFacing.EAST;
				}
			} else {
				if (hitX >= (1 - hitZ)) {
					return EnumFacing.SOUTH;
				} else {
					return EnumFacing.WEST;
				}
			}
		} else {
			if (faceX >= faceY) {
				if (faceX < (1 - faceY)) {
					return EnumFacing.DOWN;
				} else {
					return facing.rotateY();
				}
			} else {
				if (faceX >= (1 - faceY)) {
					return EnumFacing.UP;
				} else {
					return facing.rotateYCCW();
				}
			}
		}
	}

	public static EnumFacing getClosestSide(PlaceParams params) {
		return getClosestSide(params.side, params.hit.xCoord, params.hit.yCoord, params.hit.zCoord);
	}

	public static EnumFacing getClosestSide(EnumFacing facing, double hitX, double hitY, double hitZ) {
		if (facing.getAxis() == Axis.Y) {
			return getClosestFace(facing, hitX, hitY, hitZ);
		} else {
			double faceX = getFaceX(facing, hitX, hitY, hitZ);
			double faceY = getFaceY(facing, hitX, hitY, hitZ);
			if (faceX >= faceY) {
				return facing.rotateY();
			} else {
				return facing.rotateYCCW();
			}
		}
	}

	public static EnumRotation getClosestCorner(PlaceParams params) {
		return getClosestCorner(params.side, params.hit.xCoord, params.hit.yCoord, params.hit.zCoord);
	}

	public static EnumRotation getClosestCorner(EnumFacing facing, double hitX, double hitY,
			double hitZ) {
		if (hitX < 0.5f && hitZ <= 0.5f) {
			return EnumRotation.ROT0;
		} else if (hitX >= 0.5f && hitZ < 0.5f) {
			return EnumRotation.ROT90;
		} else if (hitX >= 0.5f && hitZ >= 0.5f) {
			return EnumRotation.ROT180;
		} else {
			return EnumRotation.ROT270;
		}
	}

	public static EnumRotation getRotation(EnumFacing facing, EnumFacing closeFace) {
		if (facing.rotateY() == closeFace) {
			return getRotation(facing, 1);
		} else {
			return getRotation(facing);
		}
	}

	public static EnumRotation getRotation(EnumFacing facing, double hitX, double hitY, double hitZ) {
		double faceX = getFaceX(facing, hitX, hitY, hitZ);
		if (faceX <= 0.5f) {
			return getRotation(facing, 3);
		} else {
			return getRotation(facing, 1);
		}
	}

	public static EnumRotation getRotationHalf(EnumFacing facing, double hitX, double hitY,
			double hitZ) {
		double faceX = getFaceX(facing, hitX, hitY, hitZ);
		if (faceX <= 0.5f) {
			return getRotation(facing, 3);
		} else {
			return getRotation(facing, 2);
		}
	}
}
