package com.hea3ven.tools.commonutils.util;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class ModifiableBlockPos extends BlockPos {

	private int x;
	private int y;
	private int z;

	public ModifiableBlockPos(int x, int y, int z) {
		super(0, 0, 0);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public ModifiableBlockPos(BlockPos blockPos) {
		this(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getZ() {
		return z;
	}

	@Override
	public BlockPos offset(EnumFacing facing, int n) {
		x = x + facing.getFrontOffsetX() * n;
		y = y + facing.getFrontOffsetY() * n;
		z = z + facing.getFrontOffsetZ() * n;
		return this;
	}

	public ModifiableBlockPos set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
}
