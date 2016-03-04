package com.hea3ven.buildingbricks.core.blocks;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.blocks.base.BlockBuildingBricksNonSolid;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.util.BlockPlacingUtil;

public class BlockBuildingBricksStep extends BlockBuildingBricksNonSolid {

	public BlockBuildingBricksStep(StructureMaterial structMat) {
		super(structMat, MaterialBlockType.STEP);

		IBlockState state = this.blockState.getBaseState();
		state = BlockProperties.setVertical(state, false);
		state = BlockProperties.setHalf(state, EnumBlockHalf.BOTTOM);
		state = BlockProperties.setRotation(state, EnumRotation.ROT0);
		setDefaultState(state);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockProperties.VERTICAL, BlockProperties.HALF, BlockProperties.ROTATION);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta |= ((BlockProperties.getVertical(state)) ? 1 : 0) << 3;
		meta |= BlockProperties.getHalf(state).getMetaValue() << 2;
		meta |= BlockProperties.getRotation(state).getMetaValue();
		return meta;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = this.getDefaultState();
		state = BlockProperties.setVertical(state, (meta & 0x8) > 0);
		state = BlockProperties.setHalf(state, EnumBlockHalf.getHalf((meta & 0x4) >> 2));
		state = BlockProperties.setRotation(state, EnumRotation.getRotation(meta & 0x3));
		return state;
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);

		if (facing.getAxis() == Axis.Y) {
			if (BlockPlacingUtil.isInnerRing(facing, hitX, hitY, hitZ)) {
				state = BlockProperties.setVertical(state, false);
				state = BlockProperties.setHalf(state,
						facing == EnumFacing.UP ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
				state = BlockProperties.setRotation(state,
						EnumRotation.getRotation(BlockPlacingUtil.getClosestFace(facing, hitX, hitY, hitZ)));
			} else {
				state = BlockProperties.setVertical(state, true);
				state = BlockProperties.setHalf(state, EnumBlockHalf.BOTTOM);
				state = BlockProperties.setRotation(state,
						BlockPlacingUtil.getClosestCorner(facing, hitX, hitY, hitZ));
			}
		} else {
			if (BlockPlacingUtil.isInnerRing(facing, hitX, hitY, hitZ)) {
				EnumFacing closeFace = BlockPlacingUtil.getClosestFace(facing, hitX, hitY, hitZ);
				if (closeFace.getAxis() == Axis.Y) {
					state = BlockProperties.setVertical(state, false);
					state = BlockProperties.setHalf(state,
							closeFace == EnumFacing.UP ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
					state = BlockProperties.setRotation(state,
							EnumRotation.getRotation(facing.getOpposite()));
				} else {
					state = BlockProperties.setVertical(state, true);
					state = BlockProperties.setHalf(state, EnumBlockHalf.BOTTOM);
					state = BlockProperties.setRotation(state,
							BlockPlacingUtil.getRotation(facing.getOpposite(), closeFace));
				}
			} else {
				state = BlockProperties.setVertical(state, false);
				state = BlockProperties.setHalf(state,
						hitY >= 0.5f ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
				state = BlockProperties.setRotation(state,
						BlockPlacingUtil.getRotation(facing, hitX, hitY, hitZ));
			}
		}
		return state;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state) {
		EnumBlockHalf half = BlockProperties.getHalf(state);
		EnumRotation rot = BlockProperties.getRotation(state);
		Boolean vertical = BlockProperties.getVertical(state);
		if (!vertical) {
			double minX, maxX;
			if (rot == EnumRotation.ROT0 || rot == EnumRotation.ROT180) {
				minX = 0.0d;
				maxX = 1.0d;
			} else if (rot == EnumRotation.ROT90) {
				minX = 0.5d;
				maxX = 1.0d;
			} else {
				minX = 0.0d;
				maxX = 0.5d;
			}
			double minZ, maxZ;
			if (rot == EnumRotation.ROT90 || rot == EnumRotation.ROT270) {
				minZ = 0.0d;
				maxZ = 1.0d;
			} else if (rot == EnumRotation.ROT0) {
				minZ = 0.0d;
				maxZ = 0.5d;
			} else {
				minZ = 0.5d;
				maxZ = 1.0d;
			}
			return new AxisAlignedBB(minX, half == EnumBlockHalf.BOTTOM ? 0.0d : 0.5d, minZ, maxX,
					half == EnumBlockHalf.BOTTOM ? 0.5d : 1.0d, maxZ);
		} else {
			double minX, maxX;
			if (rot == EnumRotation.ROT0 || rot == EnumRotation.ROT270) {
				minX = 0.0d;
				maxX = 0.5d;
			} else {
				minX = 0.5d;
				maxX = 1.0d;
			}
			double minZ, maxZ;
			if (rot == EnumRotation.ROT0 || rot == EnumRotation.ROT90) {
				minZ = 0.0d;
				maxZ = 0.5d;
			} else {
				minZ = 0.5d;
				maxZ = 1.0d;
			}
			return new AxisAlignedBB(minX, 0.0d, minZ, maxX, 1.0d, maxZ);
		}
	}
}
