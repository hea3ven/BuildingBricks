package com.hea3ven.buildingbricks.core.util;

import org.apache.commons.lang3.builder.EqualsBuilder;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;

public class BlockPlacingUtil {
	public static class StepPlacement {
		public EnumRotation rot;
		public EnumBlockHalf half;
		public boolean vert;

		public StepPlacement(EnumRotation rot, EnumBlockHalf half, boolean vert) {
			this.rot = rot;
			this.half = half;
			this.vert = vert;
		}

		@Override
		public String toString() {
			return String.format("(%s, %s, %s)", rot, half, vert);
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof StepPlacement)) {
				return false;
			}
			if (obj == this) {
				return true;
			}

			StepPlacement rhs = (StepPlacement) obj;
			return new EqualsBuilder()
					.append(rot, rhs.rot)
					.append(half, rhs.half)
					.append(vert, rhs.vert)
					.isEquals();
		}
	}

	public static class CornerPlacement {
		public EnumRotation rot;
		public EnumBlockHalf half;

		public CornerPlacement(EnumRotation rot, EnumBlockHalf half) {
			this.rot = rot;
			this.half = half;
		}

		@Override
		public String toString() {
			return String.format("(%s, %s)", rot, half);
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof CornerPlacement)) {
				return false;
			}
			if (obj == this) {
				return true;
			}

			CornerPlacement rhs = (CornerPlacement) obj;
			return new EqualsBuilder()
					.append(rot, rhs.rot)
					.append(half, rhs.half)
					.isEquals();
		}
	}

	public static StepPlacement getStepPlacement(EnumFacing facing, float hitX, float hitY,
			float hitZ) {
		if (facing.getAxis() == Axis.Y) {
			return getStepPlacementHorizontal(hitX, hitZ,
					(facing == EnumFacing.UP) ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
		} else {
			return getStepPlacementSide(facing, getFaceX(facing, hitX, hitY, hitZ),
					getFaceY(facing, hitX, hitY, hitZ));
		}
	}

	private static StepPlacement getStepPlacementHorizontal(float faceX, float faceY,
			EnumBlockHalf half) {
		if (0.22f <= faceX && faceX <= (1 - 0.22f) && 0.22f <= faceY && faceY <= (1 - 0.22f)) {
			// Inner ring
			if (faceX >= faceY) {
				if (faceX < (1 - faceY)) {
					return new StepPlacement(EnumRotation.ROT0, half, false);
				} else {
					return new StepPlacement(EnumRotation.ROT90, half, false);
				}
			} else {
				if (faceX >= (1 - faceY)) {
					return new StepPlacement(EnumRotation.ROT180, half, false);
				} else {
					return new StepPlacement(EnumRotation.ROT270, half, false);
				}
			}
		} else {
			// Outer ring
			if (faceX < 0.5f && faceY <= 0.5f) {
				return new StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, true);
			} else if (faceX >= 0.5f && faceY < 0.5f) {
				return new StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, true);
			}
			else if (faceX >= 0.5f && faceY >= 0.5f) {
				return new StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, true);
			} else {
				return new StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, true);
			}
		}
	}

	private static StepPlacement getStepPlacementSide(EnumFacing facing, float faceX, float faceY) {
		if (0.22f <= faceX && faceX <= (1 - 0.22f) && 0.22f <= faceY && faceY <= (1 - 0.22f)) {
			// Inner ring
			if (faceX >= faceY) {
				if (faceX < (1 - faceY)) {
					return new StepPlacement(getRotation(facing), EnumBlockHalf.BOTTOM, false);
				} else {
					return new StepPlacement(getRotation(facing, 1), EnumBlockHalf.BOTTOM, true);
				}
			} else {
				if (faceX >= (1 - faceY)) {
					return new StepPlacement(getRotation(facing), EnumBlockHalf.TOP, false);
				} else {
					return new StepPlacement(getRotation(facing), EnumBlockHalf.BOTTOM, true);
				}
			}
		} else {
			// Outer ring
			if (faceX <= 0.5f && faceY < 0.5f) {
				return new StepPlacement(getRotation(facing, 3), EnumBlockHalf.BOTTOM, false);
			} else if (faceX >= 0.5f && faceY <= 0.5f) {
				return new StepPlacement(getRotation(facing, 1), EnumBlockHalf.BOTTOM, false);
			}
			else if (faceX >= 0.5f && faceY > 0.5f) {
				return new StepPlacement(getRotation(facing, 1), EnumBlockHalf.TOP, false);
			} else {
				return new StepPlacement(getRotation(facing, 3), EnumBlockHalf.TOP, false);
			}
		}
	}

	public static CornerPlacement getCornerPlacement(EnumFacing facing, float hitX, float hitY,
			float hitZ) {
		EnumBlockHalf half= (facing == EnumFacing.UP) ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM;
		if (facing.getAxis() == Axis.Y) {
			if (hitX < 0.5f && hitZ <= 0.5f) {
				return new CornerPlacement(EnumRotation.ROT0, half);
			} else if (hitX >= 0.5f && hitZ < 0.5f) {
				return new CornerPlacement(EnumRotation.ROT90, half);
			} else if (hitX > 0.5f && hitZ >= 0.5f) {
				return new CornerPlacement(EnumRotation.ROT180, half);
			} else {
				return new CornerPlacement(EnumRotation.ROT270, half);
			}
		} else {
			return getCornerPlacementSide(facing, getFaceX(facing, hitX, hitY, hitZ),
					getFaceY(facing, hitX, hitY, hitZ));
		}
	}

	private static CornerPlacement getCornerPlacementSide(EnumFacing facing, float faceX, float faceY) {
		if (faceX < 0.5f && faceY >= 0.5f) {
			return new CornerPlacement(getRotation(facing), EnumBlockHalf.TOP);
		} else if (faceX >= 0.5f && faceY > 0.5f) {
			return new CornerPlacement(getRotation(facing, 1), EnumBlockHalf.TOP);
		}
		else if (faceX > 0.5f && faceY <= 0.5f) {
			return new CornerPlacement(getRotation(facing, 1), EnumBlockHalf.BOTTOM);
		} else {
			return new CornerPlacement(getRotation(facing), EnumBlockHalf.BOTTOM);
		}
	}

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

	private static EnumRotation getRotation(EnumFacing facing) {
		return EnumRotation.getRotation(getRotationIndex(facing));
	}

	private static EnumRotation getRotation(EnumFacing facing, int offset) {
		return EnumRotation.getRotation((getRotationIndex(facing) + offset) % 4);
	}

	private static float getFaceX(EnumFacing facing, float hitX, float hitY, float hitZ) {
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

	private static float getFaceY(EnumFacing facing, float hitX, float hitY, float hitZ) {
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

}
