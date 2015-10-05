package com.hea3ven.buildingbricks.core.blocks;

import org.junit.Test;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public class BlockBuildingBricksStepTest extends BlockTestBase {

	@Test
	public void testGetBoundingBoxRot0() {
		Block corner = new BlockBuildingBricksStep(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT0);

		assertAABBEquals(0, 0, 0, 1.0d, 0.5d, 0.5d,
				corner.getCollisionBoundingBox(null, new BlockPos(0, 0, 0), state));
	}

	@Test
	public void testGetBoundingBoxRot90() {
		Block corner = new BlockBuildingBricksStep(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT90);

		assertAABBEquals(0.5d, 0, 0, 1.0d, 0.5d, 1.0d,
				corner.getCollisionBoundingBox(null, new BlockPos(0, 0, 0), state));
	}

	@Test
	public void testGetBoundingBoxRot180() {
		Block corner = new BlockBuildingBricksStep(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT180);

		assertAABBEquals(0.0d, 0, 0.5d, 1.0d, 0.5d, 1.0d,
				corner.getCollisionBoundingBox(null, new BlockPos(0, 0, 0), state));
	}

	@Test
	public void testGetBoundingBoxRot270() {
		Block corner = new BlockBuildingBricksStep(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT270);

		assertAABBEquals(0.0d, 0, 0.0d, 0.5d, 0.5d, 1.0d,
				corner.getCollisionBoundingBox(null, new BlockPos(0, 0, 0), state));
	}

	@Test
	public void testGetBoundingBoxUpperRot0() {
		Block corner = new BlockBuildingBricksStep(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT0);
		state = BlockProperties.setHalf(state, EnumBlockHalf.TOP);

		assertAABBEquals(0, 0.5d, 0, 1.0d, 1.0d, 0.5d,
				corner.getCollisionBoundingBox(null, new BlockPos(0, 0, 0), state));
	}

	@Test
	public void testGetBoundingBoxUpperRot90() {
		Block corner = new BlockBuildingBricksStep(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT90);
		state = BlockProperties.setHalf(state, EnumBlockHalf.TOP);

		assertAABBEquals(0.5d, 0.5d, 0, 1.0d, 1.0d, 1.0d,
				corner.getCollisionBoundingBox(null, new BlockPos(0, 0, 0), state));
	}

	@Test
	public void testGetBoundingBoxUpperRot180() {
		Block corner = new BlockBuildingBricksStep(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT180);
		state = BlockProperties.setHalf(state, EnumBlockHalf.TOP);

		assertAABBEquals(0.0d, 0.5d, 0.5d, 1.0d, 1.0d, 1.0d,
				corner.getCollisionBoundingBox(null, new BlockPos(0, 0, 0), state));
	}

	@Test
	public void testGetBoundingBoxUpperRot270() {
		Block corner = new BlockBuildingBricksStep(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT270);
		state = BlockProperties.setHalf(state, EnumBlockHalf.TOP);

		assertAABBEquals(0.0d, 0.5d, 0.0d, 0.5d, 1.0d, 1.0d,
				corner.getCollisionBoundingBox(null, new BlockPos(0, 0, 0), state));
	}

	@Test
	public void testGetBoundingBoxVerticalRot0() {
		Block corner = new BlockBuildingBricksStep(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT0);
		state = BlockProperties.setVertical(state, true);

		assertAABBEquals(0, 0, 0, 0.5d, 1.0d, 0.5d,
				corner.getCollisionBoundingBox(null, new BlockPos(0, 0, 0), state));
	}

	@Test
	public void testGetBoundingBoxVerticalRot90() {
		Block corner = new BlockBuildingBricksStep(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT90);
		state = BlockProperties.setVertical(state, true);

		assertAABBEquals(0.5d, 0, 0, 1.0d, 1.0d, 0.5d,
				corner.getCollisionBoundingBox(null, new BlockPos(0, 0, 0), state));
	}

	@Test
	public void testGetBoundingBoxVerticalRot180() {
		Block corner = new BlockBuildingBricksStep(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT180);
		state = BlockProperties.setVertical(state, true);

		assertAABBEquals(0.5d, 0, 0.5d, 1.0d, 1.0d, 1.0d,
				corner.getCollisionBoundingBox(null, new BlockPos(0, 0, 0), state));
	}

	@Test
	public void testGetBoundingBoxVerticalRot270() {
		Block corner = new BlockBuildingBricksStep(StructureMaterial.ROCK);

		IBlockState state = corner.getDefaultState();
		state = BlockProperties.setRotation(state, EnumRotation.ROT270);
		state = BlockProperties.setVertical(state, true);

		assertAABBEquals(0.0d, 0, 0.5d, 0.5d, 1.0d, 1.0d,
				corner.getCollisionBoundingBox(null, new BlockPos(0, 0, 0), state));
	}

}
