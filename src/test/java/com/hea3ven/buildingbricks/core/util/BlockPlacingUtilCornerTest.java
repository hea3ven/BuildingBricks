package com.hea3ven.buildingbricks.core.util;

//@RunWith(Parameterized.class)
public class BlockPlacingUtilCornerTest {

//	@Parameters(name = "{index}: {0} {1} {2} {3} -> {4}")
//	public static Iterable<Object[]> data() {
//		return Arrays.asList(new Object[][] {
//				// BOTTOM face
//				{EnumFacing.DOWN, 0.25f, 0, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.DOWN, 0.25f, 0, 0.25f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.DOWN, 0.25f, 0, 0.5f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.DOWN, 0.25f, 0, 0.75f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.DOWN, 0.25f, 0, 1.0f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM)},
//
//				{EnumFacing.DOWN, 0.5f, 0, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.DOWN, 0.5f, 0, 0.25f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.DOWN, 0.5f, 0, 0.5f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.DOWN, 0.5f, 0, 0.75f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.DOWN, 0.5f, 0, 1.0f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM)},
//
//				{EnumFacing.DOWN, 0.75f, 0, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.DOWN, 0.75f, 0, 0.25f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.DOWN, 0.75f, 0, 0.5f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.DOWN, 0.75f, 0, 0.75f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.DOWN, 0.75f, 0, 1.0f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM)},
//
//
//				// TOP Face
//				{EnumFacing.UP, 0.25f, 0, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT0, EnumBlockHalf.TOP)},
//				{EnumFacing.UP, 0.25f, 0, 0.25f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT0, EnumBlockHalf.TOP)},
//				{EnumFacing.UP, 0.25f, 0, 0.5f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT0, EnumBlockHalf.TOP)},
//				{EnumFacing.UP, 0.25f, 0, 0.75f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP)},
//				{EnumFacing.UP, 0.25f, 0, 1.0f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP)},
//
//				{EnumFacing.UP, 0.5f, 0, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP)},
//				{EnumFacing.UP, 0.5f, 0, 0.25f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP)},
//				{EnumFacing.UP, 0.5f, 0, 0.5f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP)},
//				{EnumFacing.UP, 0.5f, 0, 0.75f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP)},
//				{EnumFacing.UP, 0.5f, 0, 1.0f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP)},
//
//				{EnumFacing.UP, 0.75f, 0, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP)},
//				{EnumFacing.UP, 0.75f, 0, 0.25f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP)},
//				{EnumFacing.UP, 0.75f, 0, 0.5f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT180, EnumBlockHalf.TOP)},
//				{EnumFacing.UP, 0.75f, 0, 0.75f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT180, EnumBlockHalf.TOP)},
//				{EnumFacing.UP, 0.75f, 0, 1.0f,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT180, EnumBlockHalf.TOP)},
//
//
//				// NORTH Face
//				{EnumFacing.NORTH, 0.25f, 0, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.NORTH, 0.25f, 0.25f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.NORTH, 0.25f, 0.5f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT0, EnumBlockHalf.TOP)},
//				{EnumFacing.NORTH, 0.25f, 0.75f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT0, EnumBlockHalf.TOP)},
//				{EnumFacing.NORTH, 0.25f, 1.0f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT0, EnumBlockHalf.TOP)},
//
//				{EnumFacing.NORTH, 0.5f, 0, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.NORTH, 0.5f, 0.25f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.NORTH, 0.5f, 0.5f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT0, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.NORTH, 0.5f, 0.75f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP)},
//				{EnumFacing.NORTH, 0.5f, 1.0f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP)},
//
//				{EnumFacing.NORTH, 0.75f, 0, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.NORTH, 0.75f, 0.25f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.NORTH, 0.75f, 0.5f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.NORTH, 0.75f, 0.75f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP)},
//				{EnumFacing.NORTH, 0.75f, 1.0f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT90, EnumBlockHalf.TOP)},
//
//
//				// SOUTH Face
//				{EnumFacing.SOUTH, 0.25f, 0, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.SOUTH, 0.25f, 0.25f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.SOUTH, 0.25f, 0.5f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.SOUTH, 0.25f, 0.75f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP)},
//				{EnumFacing.SOUTH, 0.25f, 1.0f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP)},
//
//				{EnumFacing.SOUTH, 0.5f, 0, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.SOUTH, 0.5f, 0.25f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.SOUTH, 0.5f, 0.5f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.SOUTH, 0.5f, 0.75f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP)},
//				{EnumFacing.SOUTH, 0.5f, 1.0f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT270, EnumBlockHalf.TOP)},
//
//				{EnumFacing.SOUTH, 0.75f, 0, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.SOUTH, 0.75f, 0.25f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT180, EnumBlockHalf.BOTTOM)},
//				{EnumFacing.SOUTH, 0.75f, 0.5f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT180, EnumBlockHalf.TOP)},
//				{EnumFacing.SOUTH, 0.75f, 0.75f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT180, EnumBlockHalf.TOP)},
//				{EnumFacing.SOUTH, 0.75f, 1.0f, 0,
//						new BlockPlacingUtil.CornerPlacement(EnumRotation.ROT180, EnumBlockHalf.TOP)},
//
//		});
//	}
//
//	@Parameter
//	public EnumFacing facing;
//
//	@Parameter(value = 1)
//	public float hitX;
//
//	@Parameter(value = 2)
//	public float hitY;
//
//	@Parameter(value = 3)
//	public float hitZ;
//
//	@Parameter(value = 4)
//	public BlockPlacingUtil.CornerPlacement result;
//
//	@Test
//	public void testStepPlacing() {
//		Assert.assertEquals(result, BlockPlacingUtil.getCornerPlacement(facing, hitX, hitY, hitZ));
//	}

}
