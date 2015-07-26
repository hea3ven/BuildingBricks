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
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;
import com.hea3ven.buildingbricks.core.util.BlockPlacingUtil;

public class BlockBuildingBricksStairsFixedInnerCorner extends BlockBuildingBricksNonSolid {

	public BlockBuildingBricksStairsFixedInnerCorner(StructureMaterial structureMaterial,
			String name) {
		super(structureMaterial, name);

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
		BlockPlacingUtil.StepPlacement place = BlockPlacingUtil
				.getStepPlacement(facing.getOpposite(), hitX, hitY, hitZ);
		IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);
		state = BlockProperties.setVertical(state, place.vert);
		state = BlockProperties.setHalf(state, place.half);
		state = BlockProperties.setRotation(state, place.rot);
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
		Point3f max = new Point3f(0.5f, (half == EnumBlockHalf.BOTTOM) ? 0.5f : 0.0f, 0.0f);
		Point3f min2 = new Point3f(0.0f, (half == EnumBlockHalf.BOTTOM) ? 0.0f : -0.5f, 0.0f);
		Point3f max2 = new Point3f(0.5f, (half == EnumBlockHalf.BOTTOM) ? 0.5f : 0.0f, 0.5f);
		matrix.transform(min);
		matrix.transform(max);
		matrix.transform(min2);
		matrix.transform(max2);
		this.setBlockBounds(min.x + 0.5F, min.y + 0.5F, min.z + 0.5F, max.x + 0.5F, max.y + 0.5F,
				max.z + 0.5F);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		this.setBlockBounds(min2.x + 0.5F, min2.y + 0.5F, min2.z + 0.5F, max2.x + 0.5F,
				max2.y + 0.5F, max2.z + 0.5F);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
}