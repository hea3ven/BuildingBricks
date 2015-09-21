package com.hea3ven.buildingbricks.core.blocks;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.blocks.base.BlockBuildingBricksNonSolid;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.util.BlockPlacingUtil;
import com.hea3ven.transition.helpers.WorldHelper;
import com.hea3ven.transition.m.block.properties.IProperty;
import com.hea3ven.transition.m.block.state.BlockState;
import com.hea3ven.transition.m.block.state.IBlockState;
import com.hea3ven.transition.m.util.BlockPos;
import com.hea3ven.transition.m.util.EnumFacing;
import com.hea3ven.transition.m.util.EnumFacing.AxisDirection;
import com.hea3ven.transition.m.util.Vec3i;

public class BlockBuildingBricksVerticalSlab extends BlockBuildingBricksNonSolid {

	public BlockBuildingBricksVerticalSlab(StructureMaterial structMat) {
		super(structMat, MaterialBlockType.VERTICAL_SLAB);

		IBlockState state = this.blockState.getBaseState();
		state = BlockProperties.setSide(state, EnumFacing.NORTH);
		setDefaultState(state);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] {BlockProperties.SIDE});
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
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX,
			float hitY, float hitZ, int meta, EntityLivingBase placer) {
		IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);

		if (facing.getHorizontalIndex() != -1
				&& BlockPlacingUtil.isInnerRing(facing, hitX, hitY, hitZ)) {
			state = BlockProperties.setSide(state, facing.getOpposite());
		} else {
			state = BlockProperties.setSide(state,
					BlockPlacingUtil.getClosestSide(facing, hitX, hitY, hitZ));
		}

		//		if (BlockPlacingUtil.isInnerRing(facing, hitX, hitY, hitZ)) {
		//			state = BlockProperties.setFacingHoriz(state, facing.getOpposite());
		//		} else {
		//			state = BlockProperties.setFacingHoriz(state,
		//					BlockPlacingUtil.getClosestSide(facing, hitX, hitY, hitZ));
		//		}
		return state;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state) {
		EnumFacing facing = BlockProperties.getSide(state);
		Vec3i dir = facing.getDirectionVec();
		AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(
				(dir.getX() != 0 && facing.getAxisDirection() != AxisDirection.NEGATIVE) ? 0.5f
						: 0.0f,
				0.0f,
				(dir.getZ() != 0 && facing.getAxisDirection() != AxisDirection.NEGATIVE) ? 0.5f
						: 0.0f,
				(dir.getX() != 0 && facing.getAxisDirection() == AxisDirection.NEGATIVE) ? 0.5f
						: 1.0f,
				1.0f, (dir.getZ() != 0 && facing.getAxisDirection() == AxisDirection.NEGATIVE)
						? 0.5f : 1.0f);
		return bb;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == BlockProperties.getSide(WorldHelper.get(world).getBlockState(pos));
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {
		BlockPos selfPos = pos.offset(side.getOpposite());
		EnumFacing facing = BlockProperties.getSide(WorldHelper.get(world).getBlockState(selfPos));
		if (side == facing && !super.shouldSideBeRendered(world, pos, side))
			return false;
		return true;
	}
}
