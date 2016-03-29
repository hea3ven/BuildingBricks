package com.hea3ven.buildingbricks.core.blocks;

import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.blocks.base.BlockBuildingBricksNonSolid;
import com.hea3ven.tools.commonutils.util.BlockStateUtil;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.util.BlockPlacingUtil;

public class BlockBuildingBricksCorner extends BlockBuildingBricksNonSolid {

	public BlockBuildingBricksCorner(StructureMaterial structMat) {
		super(structMat, MaterialBlockType.CORNER);

		IBlockState state = this.blockState.getBaseState();
		state = BlockProperties.setHalf(state, EnumBlockHalf.BOTTOM);
		state = BlockProperties.setRotation(state, EnumRotation.ROT0);
		setDefaultState(state);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return BlockStateUtil.addProperties(this, super.createBlockState(),
				new IProperty[] {BlockProperties.HALF, BlockProperties.ROTATION});
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta |= BlockProperties.getHalf(state).ordinal() << 2;
		meta |= BlockProperties.getRotation(state).getMetaValue();
		return meta;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = this.getDefaultState();
		state = BlockProperties.setHalf(state, EnumBlockHalf.values()[(meta & 0x4) >> 2]);
		state = BlockProperties.setRotation(state, EnumRotation.getRotation(meta & 0x3));
		return state;
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);

		if (facing.getAxis() == Axis.Y) {
			EnumBlockHalf half = facing == EnumFacing.UP ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP;
			state = BlockProperties.setHalf(state, half);
			state = BlockProperties.setRotation(state,
					BlockPlacingUtil.getClosestCorner(facing, hitX, hitY, hitZ));
		} else {
			state = BlockProperties.setHalf(state, hitY >= 0.5f ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
			state = BlockProperties.setRotation(state,
					BlockPlacingUtil.getRotationHalf(facing, hitX, hitY, hitZ));
		}

		return state;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumBlockHalf half = BlockProperties.getHalf(state);
		EnumRotation rot = BlockProperties.getRotation(state);
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
		return new AxisAlignedBB(minX, half == EnumBlockHalf.BOTTOM ? 0.0d : 0.5d, minZ, maxX,
				half == EnumBlockHalf.BOTTOM ? 0.5d : 1.0d, maxZ);
	}
}
