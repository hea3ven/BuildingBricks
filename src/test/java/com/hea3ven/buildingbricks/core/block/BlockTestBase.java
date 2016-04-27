package com.hea3ven.buildingbricks.core.block;

import static org.junit.Assert.assertEquals;

import net.minecraft.util.math.AxisAlignedBB;

public class BlockTestBase {

	protected void assertAABBEquals(double minX, double minY, double minZ, double maxX, double maxY,
			double maxZ, AxisAlignedBB aabb2) {
		assertEquals("minX", minX, aabb2.minX, 0.01d);
		assertEquals("minY", minY, aabb2.minY, 0.01d);
		assertEquals("minZ", minZ, aabb2.minZ, 0.01d);
		assertEquals("maxX", maxX, aabb2.maxX, 0.01d);
		assertEquals("maxY", maxY, aabb2.maxY, 0.01d);
		assertEquals("maxZ", maxZ, aabb2.maxZ, 0.01d);
	}

}
