package com.hea3ven.buildingbricks.core.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.block.base.BlockBuildingBricksNonSolid;
import com.hea3ven.buildingbricks.core.block.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.util.BlockPlacingUtil;
import com.hea3ven.tools.commonutils.util.PlaceParams;

public class BlockBuildingBricksVerticalSlab extends BlockBuildingBricksNonSolid {

	public BlockBuildingBricksVerticalSlab(StructureMaterial structMat) {
		super(structMat, MaterialBlockType.VERTICAL_SLAB);

		IBlockState state = this.blockState.getBaseState();
		state = BlockProperties.setSide(state, EnumFacing.NORTH);
		setDefaultState(state);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockProperties.SIDE);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta |= BlockProperties.getSide(state).getHorizontalIndex();
		return meta;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = this.getDefaultState();
		state = BlockProperties.setSide(state, EnumFacing.getHorizontal(meta & 0x3));
		return state;
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		PlaceParams params = new PlaceParams(pos, facing, new Vec3d(hitX, hitY, hitZ), 1.0d);
		IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);
		if (facing.getAxis() == Axis.Y) {
			return state.withProperty(BlockProperties.SIDE, BlockPlacingUtil.getClosestSide(params));
		} else {
			if (BlockPlacingUtil.isInnerRing(params)) {
				return state.withProperty(BlockProperties.SIDE, facing.getOpposite());
			} else {
				return state.withProperty(BlockProperties.SIDE, BlockPlacingUtil.getClosestSide(params));
			}
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		EnumFacing facing = BlockProperties.getSide(state);
		Vec3i dir = facing.getDirectionVec();
		return new AxisAlignedBB(
				(dir.getX() != 0 && facing.getAxisDirection() != AxisDirection.NEGATIVE) ? 0.5f : 0.0f, 0.0f,
				(dir.getZ() != 0 && facing.getAxisDirection() != AxisDirection.NEGATIVE) ? 0.5f : 0.0f,
				(dir.getX() != 0 && facing.getAxisDirection() == AxisDirection.NEGATIVE) ? 0.5f : 1.0f, 1.0f,
				(dir.getZ() != 0 && facing.getAxisDirection() == AxisDirection.NEGATIVE) ? 0.5f : 1.0f);
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos,
			EnumFacing side) {
		return state.getMaterial().isOpaque() && isSideSolid(state, world, pos, side);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos,
			EnumFacing side) {
		BlockPos otherPos = pos.offset(side);
		IBlockState otherState = world.getBlockState(otherPos);
		if (!(otherState.getBlock() instanceof BlockBuildingBricksVerticalSlab)) {
			return !otherState.getBlock().doesSideBlockRendering(otherState, world, otherPos, side);
		}

		if (side.getAxis() == Axis.Y) {
			return BlockProperties.getSide(state) != BlockProperties.getSide(otherState);
		}

		EnumFacing facing = BlockProperties.getSide(otherState);
		EnumFacing ownFacing = BlockProperties.getSide(state);
		if (ownFacing == side) {
			return facing != ownFacing.getOpposite();
		} else if (ownFacing == side.getOpposite()) {
			return true;
		} else {
			return ownFacing != facing && facing != side.getOpposite();
		}
	}
}
