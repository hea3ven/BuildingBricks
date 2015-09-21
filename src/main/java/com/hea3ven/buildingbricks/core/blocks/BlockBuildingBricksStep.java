package com.hea3ven.buildingbricks.core.blocks;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.blocks.base.BlockBuildingBricksNonSolid;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.util.BlockPlacingUtil;
import com.hea3ven.transition.helpers.WorldHelper;
import com.hea3ven.transition.m.block.properties.IProperty;
import com.hea3ven.transition.m.block.state.BlockState;
import com.hea3ven.transition.m.block.state.IBlockState;
import com.hea3ven.transition.m.util.BlockPos;
import com.hea3ven.transition.m.util.EnumFacing;
import com.hea3ven.transition.m.util.EnumFacing.Axis;

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
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] {BlockProperties.VERTICAL, BlockProperties.HALF,
				BlockProperties.ROTATION});
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
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX,
			float hitY, float hitZ, int meta, EntityLivingBase placer) {
		IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);

		if (facing.getAxis() == Axis.Y) {
			if (BlockPlacingUtil.isInnerRing(facing, hitX, hitY, hitZ)) {
				state = BlockProperties.setVertical(state, false);
				state = BlockProperties.setHalf(state,
						facing == EnumFacing.UP ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
				state = BlockProperties.setRotation(state, EnumRotation
						.getRotation(BlockPlacingUtil.getClosestFace(facing, hitX, hitY, hitZ)));
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
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		matrix.rotY(-rot.getAngle());

		Point3f min = new Point3f(-0.5f, (vertical || half == EnumBlockHalf.BOTTOM) ? -0.5f : 0.0f,
				-0.5f);
		Point3f max = vertical ? new Point3f(0.0f, 0.5f, 0.0f)
				: new Point3f(0.5f, (half == EnumBlockHalf.BOTTOM) ? 0.0f : 0.5f, 0.0f);
		matrix.transform(min);
		matrix.transform(max);
		AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(min.x + 0.5f, min.y + 0.5f, min.z + 0.5f,
				max.x + 0.5f, max.y + 0.5f, max.z + 0.5f);
		return bb;
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {
		BlockPos selfPos = pos.offset(side.getOpposite());
		EnumBlockHalf half = BlockProperties.getHalf(WorldHelper.get(world).getBlockState(selfPos));
		if (side == half.getSide() && !super.shouldSideBeRendered(world, pos, side))
			return false;
		// if (side == facing && !super.shouldSideBeRendered(world, pos, side))
		// return false;
		return true;
	}
}