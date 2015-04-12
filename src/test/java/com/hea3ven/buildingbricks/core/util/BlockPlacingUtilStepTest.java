package com.hea3ven.buildingbricks.core.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import scala.actors.threadpool.Arrays;

import net.minecraft.util.EnumFacing;

import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;

@RunWith(Parameterized.class)
public class BlockPlacingUtilStepTest {

	private static final float OUTER_RING_MIN_1 = 0.08f;
	private static final float OUTER_RING_MIN_2 = 0.15f;
	private static final float OUTER_RING_MAX_2 = 0.85f;
	private static final float OUTER_RING_MAX_1 = 0.92f;
	private static final float INNER_RING_MIN_1 = 0.30f;
	private static final float INNER_RING_MIN_2 = 0.45f;
	private static final float INNER_RING_MAX_2 = 0.55f;
	private static final float INNER_RING_MAX_1 = 0.70f;

	@Parameters(name = "{index}: {0} {1} {2} {3} -> {4}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				// BOTTOM face
				{EnumFacing.DOWN, OUTER_RING_MIN_1, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, OUTER_RING_MIN_1, 0, OUTER_RING_MIN_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, OUTER_RING_MIN_1, 0, OUTER_RING_MIN_2,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, OUTER_RING_MIN_1, 0, 0.5f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, OUTER_RING_MIN_1, 0, OUTER_RING_MAX_2,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, OUTER_RING_MIN_1, 0, OUTER_RING_MAX_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, OUTER_RING_MIN_1, 0, 1.0f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, true)},

				{EnumFacing.DOWN, INNER_RING_MIN_2, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, INNER_RING_MIN_2, 0, OUTER_RING_MIN_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, INNER_RING_MIN_2, 0, INNER_RING_MIN_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.DOWN, INNER_RING_MIN_2, 0, 0.5f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.DOWN, INNER_RING_MIN_2, 0, INNER_RING_MAX_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.DOWN, INNER_RING_MIN_2, 0, OUTER_RING_MAX_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, INNER_RING_MIN_2, 0, 1.0f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, true)},

				{EnumFacing.DOWN, INNER_RING_MAX_2, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, INNER_RING_MAX_2, 0, OUTER_RING_MIN_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, INNER_RING_MAX_2, 0, INNER_RING_MIN_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.DOWN, INNER_RING_MAX_2, 0, 0.5f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.DOWN, INNER_RING_MAX_2, 0, INNER_RING_MAX_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.DOWN, INNER_RING_MAX_2, 0, OUTER_RING_MAX_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, INNER_RING_MAX_2, 0, 1.0f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, true)},

				{EnumFacing.DOWN, OUTER_RING_MAX_1, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, OUTER_RING_MAX_1, 0, OUTER_RING_MIN_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, OUTER_RING_MAX_1, 0, OUTER_RING_MIN_2,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, OUTER_RING_MAX_1, 0, 0.5f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, OUTER_RING_MAX_1, 0, OUTER_RING_MAX_2,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, OUTER_RING_MAX_1, 0, OUTER_RING_MAX_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.DOWN, OUTER_RING_MAX_1, 0, 1.0f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, true)},

				// TOP Face
				{EnumFacing.UP, OUTER_RING_MIN_1, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, OUTER_RING_MIN_1, 0, OUTER_RING_MIN_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, OUTER_RING_MIN_1, 0, OUTER_RING_MIN_2,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, OUTER_RING_MIN_1, 0, 0.5f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, OUTER_RING_MIN_1, 0, OUTER_RING_MAX_2,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, OUTER_RING_MIN_1, 0, OUTER_RING_MAX_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, OUTER_RING_MIN_1, 0, 1.0f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, true)},

				{EnumFacing.UP, INNER_RING_MIN_2, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, INNER_RING_MIN_2, 0, OUTER_RING_MIN_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, INNER_RING_MIN_2, 0, INNER_RING_MIN_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.TOP, false)},
				{EnumFacing.UP, INNER_RING_MIN_2, 0, 0.5f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP, false)},
				{EnumFacing.UP, INNER_RING_MIN_2, 0, INNER_RING_MAX_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.TOP, false)},
				{EnumFacing.UP, INNER_RING_MIN_2, 0, OUTER_RING_MAX_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, INNER_RING_MIN_2, 0, 1.0f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, true)},

				{EnumFacing.UP, INNER_RING_MAX_2, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, INNER_RING_MAX_2, 0, OUTER_RING_MIN_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, INNER_RING_MAX_2, 0, INNER_RING_MIN_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.TOP, false)},
				{EnumFacing.UP, INNER_RING_MAX_2, 0, 0.5f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP, false)},
				{EnumFacing.UP, INNER_RING_MAX_2, 0, INNER_RING_MAX_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.TOP, false)},
				{EnumFacing.UP, INNER_RING_MAX_2, 0, OUTER_RING_MAX_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, INNER_RING_MAX_2, 0, 1.0f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, true)},

				{EnumFacing.UP, OUTER_RING_MAX_1, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, OUTER_RING_MAX_1, 0, OUTER_RING_MIN_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, OUTER_RING_MAX_1, 0, OUTER_RING_MIN_2,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, OUTER_RING_MAX_1, 0, 0.5f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, OUTER_RING_MAX_1, 0, OUTER_RING_MAX_2,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, OUTER_RING_MAX_1, 0, OUTER_RING_MAX_1,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.UP, OUTER_RING_MAX_1, 0, 1.0f,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, true)},

				// NORTH Face
				{EnumFacing.NORTH, OUTER_RING_MIN_1, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.NORTH, OUTER_RING_MIN_1, OUTER_RING_MIN_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.NORTH, OUTER_RING_MIN_1, OUTER_RING_MIN_2, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.NORTH, OUTER_RING_MIN_1, 0.5f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP, false)},
				{EnumFacing.NORTH, OUTER_RING_MIN_1, OUTER_RING_MAX_2, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP, false)},
				{EnumFacing.NORTH, OUTER_RING_MIN_1, OUTER_RING_MAX_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP, false)},
				{EnumFacing.NORTH, OUTER_RING_MIN_1, 1.0f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP, false)},

				{EnumFacing.NORTH, INNER_RING_MIN_2, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.NORTH, INNER_RING_MIN_2, OUTER_RING_MIN_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.NORTH, INNER_RING_MIN_2, INNER_RING_MIN_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.NORTH, INNER_RING_MIN_2, 0.5f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.NORTH, INNER_RING_MIN_2, INNER_RING_MAX_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.TOP, false)},
				{EnumFacing.NORTH, INNER_RING_MIN_2, OUTER_RING_MAX_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP, false)},
				{EnumFacing.NORTH, INNER_RING_MIN_2, 1.0f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP, false)},

				{EnumFacing.NORTH, INNER_RING_MAX_2, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.NORTH, INNER_RING_MAX_2, OUTER_RING_MIN_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.NORTH, INNER_RING_MAX_2, INNER_RING_MIN_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.NORTH, INNER_RING_MAX_2, 0.5f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.NORTH, INNER_RING_MAX_2, INNER_RING_MAX_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT0, EnumBlockHalf.TOP, false)},
				{EnumFacing.NORTH, INNER_RING_MAX_2, OUTER_RING_MAX_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP, false)},
				{EnumFacing.NORTH, INNER_RING_MAX_2, 1.0f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP, false)},

				{EnumFacing.NORTH, OUTER_RING_MAX_1, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.NORTH, OUTER_RING_MAX_1, OUTER_RING_MIN_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.NORTH, OUTER_RING_MAX_1, OUTER_RING_MIN_2, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.NORTH, OUTER_RING_MAX_1, 0.5f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.NORTH, OUTER_RING_MAX_1, OUTER_RING_MAX_2, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP, false)},
				{EnumFacing.NORTH, OUTER_RING_MAX_1, OUTER_RING_MAX_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP, false)},
				{EnumFacing.NORTH, OUTER_RING_MAX_1, 1.0f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP, false)},

				// SOUTH Face
				{EnumFacing.SOUTH, OUTER_RING_MIN_1, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.SOUTH, OUTER_RING_MIN_1, OUTER_RING_MIN_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.SOUTH, OUTER_RING_MIN_1, OUTER_RING_MIN_2, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.SOUTH, OUTER_RING_MIN_1, 0.5f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.SOUTH, OUTER_RING_MIN_1, OUTER_RING_MAX_2, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP, false)},
				{EnumFacing.SOUTH, OUTER_RING_MIN_1, OUTER_RING_MAX_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP, false)},
				{EnumFacing.SOUTH, OUTER_RING_MIN_1, 1.0f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP, false)},

				{EnumFacing.SOUTH, INNER_RING_MIN_2, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.SOUTH, INNER_RING_MIN_2, OUTER_RING_MIN_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.SOUTH, INNER_RING_MIN_2, INNER_RING_MIN_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.SOUTH, INNER_RING_MIN_2, 0.5f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.SOUTH, INNER_RING_MIN_2, INNER_RING_MAX_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.TOP, false)},
				{EnumFacing.SOUTH, INNER_RING_MIN_2, OUTER_RING_MAX_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP, false)},
				{EnumFacing.SOUTH, INNER_RING_MIN_2, 1.0f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP, false)},

				{EnumFacing.SOUTH, INNER_RING_MAX_2, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.SOUTH, INNER_RING_MAX_2, OUTER_RING_MIN_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.SOUTH, INNER_RING_MAX_2, INNER_RING_MIN_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.SOUTH, INNER_RING_MAX_2, 0.5f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM, true)},
				{EnumFacing.SOUTH, INNER_RING_MAX_2, INNER_RING_MAX_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT180, EnumBlockHalf.TOP, false)},
				{EnumFacing.SOUTH, INNER_RING_MAX_2, OUTER_RING_MAX_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP, false)},
				{EnumFacing.SOUTH, INNER_RING_MAX_2, 1.0f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP, false)},

				{EnumFacing.SOUTH, OUTER_RING_MAX_1, 0, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.SOUTH, OUTER_RING_MAX_1, OUTER_RING_MIN_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.SOUTH, OUTER_RING_MAX_1, OUTER_RING_MIN_2, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM, false)},
				{EnumFacing.SOUTH, OUTER_RING_MAX_1, 0.5f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP, false)},
				{EnumFacing.SOUTH, OUTER_RING_MAX_1, OUTER_RING_MAX_2, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP, false)},
				{EnumFacing.SOUTH, OUTER_RING_MAX_1, OUTER_RING_MAX_1, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP, false)},
				{EnumFacing.SOUTH, OUTER_RING_MAX_1, 1.0f, 0,
						new BlockPlacingUtil.StepPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP, false)},

		});
	}

	@Parameter
	public EnumFacing facing;

	@Parameter(value = 1)
	public float hitX;

	@Parameter(value = 2)
	public float hitY;

	@Parameter(value = 3)
	public float hitZ;

	@Parameter(value = 4)
	public BlockPlacingUtil.StepPlacement result;

	@Test
	public void testStepPlacing() {
		Assert.assertEquals(result, BlockPlacingUtil.getStepPlacement(facing, hitX, hitY, hitZ));
	}

}
