package com.hea3ven.transition.m.util;

public class BlockPos extends Vec3i {

	public BlockPos(int x, int y, int z) {
		super(x, y, z);
	}

	public BlockPos offset(EnumFacing facing) {
		return add(facing.getFaceDirection());
	}

	public BlockPos add(Vec3i off) {
		return add(off.getX(), off.getY(), off.getZ());
	}

	public BlockPos add(int xOff, int yOff, int zOff) {
		return new BlockPos(getX() + xOff, getY() + yOff, getZ() + zOff);
	}

	public BlockPos up() {
		return add(0, 1, 0);
	}

	public BlockPos down() {
		return add(0, -1, 0);
	}

	public BlockPos north() {
		return add(0, 0, -1);
	}

	public BlockPos east() {
		return add(-1, 0, 0);
	}

	public BlockPos south() {
		return add(0, 0, 1);
	}

	public BlockPos west() {
		return add(1, 0, 0);
	}

}
