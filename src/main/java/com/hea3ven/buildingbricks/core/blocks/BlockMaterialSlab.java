package com.hea3ven.buildingbricks.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMaterialSlab extends BlockBuildingBricksBase {
	public static final PropertyInteger MATERIAL = PropertyInteger.create("material", 1, 2);
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	public BlockMaterialSlab(String name) {
		super(Material.rock);
		setUnlocalizedName(name);

		IBlockState blockState = this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.DOWN);
//				.withProperty(MATERIAL, 1);
		setDefaultState(blockState);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] {FACING});
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
//		meta |= ((Integer) state.getValue(MATERIAL)) << 10;
		meta |= ((EnumFacing) state.getValue(FACING)).getIndex();
		return meta;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = this.getDefaultState()
//				.withProperty(MATERIAL, 1);
				.withProperty(FACING, EnumFacing.getFront(meta & 0xf));
		return state;
	}
	
	private int getMaterialFromState(IBlockAccess world, BlockPos pos) {
		return (Integer)world.getBlockState(pos).getValue(MATERIAL);
	}
	
	private EnumFacing getFacingFromState(IBlockAccess world, BlockPos pos) {
		return (EnumFacing)world.getBlockState(pos).getValue(FACING);
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX,
			float hitY, float hitZ, int meta, EntityLivingBase placer) {
		EnumFacing blockFacing = facing.getOpposite();
		IBlockState blockState = super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer)
				.withProperty(FACING, blockFacing);
//				.withProperty(MATERIAL, 1);
		return blockState;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
        EnumFacing facing = getFacingFromState(world, pos);
		Vec3i dir = facing.getDirectionVec();
		setBlockBounds(
				(dir.getX() != 0) ? (facing.getAxisDirection() == AxisDirection.NEGATIVE) ? 0f
						: 0.5f : 0f,
				(dir.getY() != 0) ? (facing.getAxisDirection() == AxisDirection.NEGATIVE) ? 0f
						: 0.5f : 0f,
				(dir.getZ() != 0) ? (facing.getAxisDirection() == AxisDirection.NEGATIVE) ? 0f
						: 0.5f : 0f,
				(dir.getX() != 0) ? (facing.getAxisDirection() == AxisDirection.NEGATIVE) ? 0.5f
						: 1f : 1f,
				(dir.getY() != 0) ? (facing.getAxisDirection() == AxisDirection.NEGATIVE) ? 0.5f
						: 1f : 1f,
				(dir.getZ() != 0) ? (facing.getAxisDirection() == AxisDirection.NEGATIVE)? 0.5f : 1f : 1f);
	}
	
	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == getFacingFromState(world, pos);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side) {
//        else if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(worldIn, pos, side))
//        {
//            return false;
//        }
//        else
//        {
            BlockPos selfPos = pos.offset(side.getOpposite());
		EnumFacing facing = getFacingFromState(world, selfPos);
		if (side == facing && !super.shouldSideBeRendered(world, pos, side))
			return false;
		return true;
	}
	
//	@Override
//	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
//		list.add(new ItemStack(itemIn, 1, 0));
//		list.add(new ItemStack(itemIn, 1, 1));
//	}
	
	@Override
	public int getHarvestLevel(IBlockState state) {
		return 0;
	}
	
	@Override
	public String getHarvestTool(IBlockState state) {
		return "pickaxe";
	}
}
