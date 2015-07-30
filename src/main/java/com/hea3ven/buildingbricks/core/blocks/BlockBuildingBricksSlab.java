package com.hea3ven.buildingbricks.core.blocks;

import java.util.List;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.util.BlockPlacingUtil;

public class BlockBuildingBricksSlab extends BlockBuildingBricksNonSolid {

	public BlockBuildingBricksSlab(StructureMaterial structureMaterial, String name) {
		super(structureMaterial, name);

		IBlockState state = this.blockState.getBaseState();
		state = BlockProperties.setFacing(state, EnumFacing.DOWN);
		setDefaultState(state);
	}

	@Override
	protected void registerProperties(List<IProperty> props) {
		super.registerProperties(props);
		props.add(BlockProperties.FACING);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta |= BlockProperties.getFacing(state).getIndex();
		return meta;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = this.getDefaultState();
		state = BlockProperties.setFacing(state, EnumFacing.getFront(meta & 0x7));
		return state;
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX,
			float hitY, float hitZ, int meta, EntityLivingBase placer) {
		IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);

		if (BlockPlacingUtil.isInnerRing(facing, hitX, hitY, hitZ)) {
			state = BlockProperties.setFacing(state, facing.getOpposite());
		} else {
			state = BlockProperties.setFacing(state,
					BlockPlacingUtil.getClosestSide(facing, hitX, hitY, hitZ));
		}
		return state;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		EnumFacing facing = BlockProperties.getFacing(world.getBlockState(pos));
		Vec3i dir = facing.getDirectionVec();
		setBlockBounds(
				(dir.getX() != 0)
						? (facing.getAxisDirection() == AxisDirection.NEGATIVE) ? 0f : 0.5f : 0f,
				(dir.getY() != 0)
						? (facing.getAxisDirection() == AxisDirection.NEGATIVE) ? 0f : 0.5f : 0f,
				(dir.getZ() != 0)
						? (facing.getAxisDirection() == AxisDirection.NEGATIVE) ? 0f : 0.5f : 0f,
				(dir.getX() != 0)
						? (facing.getAxisDirection() == AxisDirection.NEGATIVE) ? 0.5f : 1f : 1f,
				(dir.getY() != 0)
						? (facing.getAxisDirection() == AxisDirection.NEGATIVE) ? 0.5f : 1f : 1f,
				(dir.getZ() != 0)
						? (facing.getAxisDirection() == AxisDirection.NEGATIVE) ? 0.5f : 1f : 1f);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == BlockProperties.getFacing(world.getBlockState(pos));
	}

	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {
		BlockPos selfPos = pos.offset(side.getOpposite());
		EnumFacing facing = BlockProperties.getFacing(world.getBlockState(selfPos));
		if (side == facing && !super.shouldSideBeRendered(world, pos, side))
			return false;
		return true;
	}
}
