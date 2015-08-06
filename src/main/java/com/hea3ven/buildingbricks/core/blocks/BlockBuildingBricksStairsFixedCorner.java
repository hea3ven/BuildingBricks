package com.hea3ven.buildingbricks.core.blocks;

import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.util.BlockPlacingUtil;

public class BlockBuildingBricksStairsFixedCorner extends BlockBuildingBricksNonSolid {

	public BlockBuildingBricksStairsFixedCorner(StructureMaterial structureMaterial) {
		super(structureMaterial);

		IBlockState state = this.blockState.getBaseState();
		state = BlockProperties.setVertical(state, false);
		state = BlockProperties.setHalf(state, EnumBlockHalf.BOTTOM);
		state = BlockProperties.setRotation(state, EnumRotation.ROT0);
		setDefaultState(state);
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
	protected void registerProperties(List<IProperty> props) {
		super.registerProperties(props);
		props.add(BlockProperties.VERTICAL);
		props.add(BlockProperties.HALF);
		props.add(BlockProperties.ROTATION);
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX,
			float hitY, float hitZ, int meta, EntityLivingBase placer) {
		// TODO: fix
		// BlockPlacingUtil.StepPlacement place = BlockPlacingUtil
		// .getStepPlacement(facing.getOpposite(), hitX, hitY, hitZ);
		IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);
		// state = BlockProperties.setVertical(state, place.vert);
		// state = BlockProperties.setHalf(state, place.half);
		// state = BlockProperties.setRotation(state, place.rot);
		if (facing.getAxis() == Axis.Y) {
			if (BlockPlacingUtil.isInnerRing(facing, hitX, hitY, hitZ)) {
				state = BlockProperties.setVertical(state, false);
				state = BlockProperties.setHalf(state,
						facing == EnumFacing.UP ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
				state = BlockProperties.setRotation(state, EnumRotation
						.getRotation(BlockPlacingUtil.getClosestSide(facing, hitX, hitY, hitZ)));
			} else {
				state = BlockProperties.setVertical(state, true);
				state = BlockProperties.setHalf(state, EnumBlockHalf.BOTTOM);
				state = BlockProperties.setRotation(state,
						BlockPlacingUtil.getClosestCorner(facing, hitX, hitY, hitZ));
			}
		} else {
			if (BlockPlacingUtil.isInnerRing(facing, hitX, hitY, hitZ)) {
				EnumFacing closeFace = BlockPlacingUtil.getClosestSide(facing, hitX, hitY, hitZ);
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
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state,
			AxisAlignedBB mask, List list, Entity collidingEntity) {
		EnumBlockHalf half = BlockProperties.getHalf(state);
		if (half == EnumBlockHalf.TOP) {
			this.setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
		} else {
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		Matrix4f matrix = new Matrix4f();
		matrix.rotY(-BlockProperties.getRotation(state).getAngle());

		Point3f min = new Point3f(-0.5f, (half == EnumBlockHalf.BOTTOM) ? 0.0f : -0.5f, -0.5f);
		Point3f max = new Point3f(0.0f, (half == EnumBlockHalf.BOTTOM) ? 0.5f : 0.0f, 0.0f);
		matrix.transform(min);
		matrix.transform(max);
		this.setBlockBounds(min.x + 0.5F, min.y + 0.5F, min.z + 0.5F, max.x + 0.5F, max.y + 0.5F,
				max.z + 0.5F);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
}