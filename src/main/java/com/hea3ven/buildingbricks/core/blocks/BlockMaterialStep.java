package com.hea3ven.buildingbricks.core.blocks;

import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;
import com.hea3ven.buildingbricks.core.util.BlockPlacingUtil;

public class BlockMaterialStep extends BlockBuildingBricksNonSolid {
	private static final PropertyBool VERTICAL = PropertyBool.create("vertical");
	private static final PropertyEnum HALF = PropertyEnum.create("half", EnumBlockHalf.class);
	private static final PropertyEnum ROTATION = PropertyEnum.create("rotation", EnumRotation.class);

	public BlockMaterialStep(String name) {
		super(Material.rock);
		setUnlocalizedName(name);
		
		IBlockState state = this.blockState.getBaseState();
		state = setStateVertical(state, false);
		state = setStateHalf(state, EnumBlockHalf.BOTTOM);
		state = setStateRotation(state, EnumRotation.ROT0);
		setDefaultState(state);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta |= ((getStateVertical(state))? 1 : 0) << 3;
		meta |= getStateHalf(state).getMetaValue() << 2;
		meta |= getStateRotation(state).getMetaValue();
		return meta;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = this.getDefaultState();
		state = setStateVertical(state, (meta & 0x8) > 0);
		state = setStateHalf(state, EnumBlockHalf.getHalf((meta & 0x4) >> 2));
		state = setStateRotation(state, EnumRotation.getRotation(meta & 0x3));
		return state;
	}
	
	@Override
	protected void registerProperties(List<IProperty> props) {
		super.registerProperties(props);
		props.add(VERTICAL);
		props.add(HALF);
		props.add(ROTATION);
	}

	private Boolean getStateVertical(IBlockState state) {
		return (Boolean)state.getValue(VERTICAL);
	}

	protected IBlockState setStateVertical(IBlockState state, Boolean vert) {
		return state.withProperty(VERTICAL, vert);
	}

	private EnumBlockHalf getStateHalf(IBlockState state) {
		return (EnumBlockHalf)state.getValue(HALF);
	}

	protected IBlockState setStateHalf(IBlockState state, EnumBlockHalf half) {
		return state.withProperty(HALF, half);
	}

	private EnumRotation getStateRotation(IBlockState state) {
		return (EnumRotation)state.getValue(ROTATION);
	}

	protected IBlockState setStateRotation(IBlockState state, EnumRotation rotation) {
		return state.withProperty(ROTATION, rotation);
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX,
			float hitY, float hitZ, int meta, EntityLivingBase placer) {
		if (!world.isRemote)
			System.out.println("a");
		
		
		BlockPlacingUtil.StepPlacement place = BlockPlacingUtil.getStepPlacement(facing.getOpposite(), hitX, hitY, hitZ);
//		EnumBlockHalf half = BlockPlacingUtil.getHalf(facing, hitX, hitY, hitZ);
//		boolean vertical = BlockPlacingUtil.getIsVertical(facing.getOpposite(), hitX, hitY, hitZ);
		IBlockState state = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);
		state = setStateVertical(state, place.vert);
		state = setStateHalf(state, place.half);
		state = setStateRotation(state, place.rot);
		return state;
	}

	private AxisAlignedBB getBoundingBox(IBlockState state) {
		EnumBlockHalf half = getStateHalf(state);
		EnumRotation rot = getStateRotation(state);
		Boolean vertical = getStateVertical(state);
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		matrix.rotY(-rot.getAngle());

		Point3f min = new Point3f(-0.5f,
				(vertical || half == EnumBlockHalf.BOTTOM) ? -0.5f : 0.0f, -0.5f);
		Point3f max = vertical ? new Point3f(0.0f, 0.5f, 0.0f) : new Point3f(0.5f,
				(half == EnumBlockHalf.BOTTOM) ? 0.0f : 0.5f, 0.0f);
		matrix.transform(min);
		matrix.transform(max);
		AxisAlignedBB bb = new AxisAlignedBB(min.x + 0.5f, min.y + 0.5f, min.z + 0.5f,
				max.x + 0.5f, max.y + 0.5f, max.z + 0.5f);
		return bb;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		IBlockState state = getStateFromWorld(world, pos);
		AxisAlignedBB bb = getBoundingBox(state);
		
		setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX,
				(float) bb.maxY, (float) bb.maxZ);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return getBoundingBox(state).offset(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {
        BlockPos selfPos = pos.offset(side.getOpposite());
        EnumBlockHalf half = getStateHalf(getStateFromWorld(world, selfPos));
        if (side == half.getSide() && !super.shouldSideBeRendered(world, pos, side))
        	return false;
//		if (side == facing && !super.shouldSideBeRendered(world, pos, side))
//			return false;
		return true;
	}
	
//	@Override
//	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
//		list.add(new ItemStack(itemIn, 1, 0));
//		list.add(new ItemStack(itemIn, 1, 1));
//	}

}
