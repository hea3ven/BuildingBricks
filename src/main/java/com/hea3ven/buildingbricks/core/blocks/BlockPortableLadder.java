package com.hea3ven.buildingbricks.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;

public class BlockPortableLadder extends Block {

	public BlockPortableLadder() {
		super(Material.iron);
		setHardness(0.05f);

		IBlockState state = blockState.getBaseState();
		state = BlockProperties.setSide(state, EnumFacing.NORTH);
		state = BlockProperties.setHalf(state, EnumBlockHalf.BOTTOM);
		setDefaultState(state);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockProperties.SIDE, BlockProperties.HALF);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = getDefaultState();
		state = BlockProperties.setSide(state, EnumFacing.getHorizontal(meta & 0x3));
		state = BlockProperties.setHalf(state, EnumBlockHalf.values()[(meta >> 2) & 0x1]);
		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		meta |= BlockProperties.getSide(state).getHorizontalIndex();
		meta |= BlockProperties.getHalf(state).ordinal() << 2;
		return meta;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		if (state.getBlock() == this) {
			boolean bottom = BlockProperties.getHalf(state) == EnumBlockHalf.BOTTOM;
			switch (BlockProperties.getSide(state)) {
				default:
				case NORTH:
					return new AxisAlignedBB(0.0f, 0.0f, bottom ? 0.25f : 0.0f, 1.0f, 1.0f, 0.5f);
				case EAST:
					return new AxisAlignedBB(0.5f, 0.0f, 0.0f, bottom ? 0.75f : 1.0f, 1.0f, 1.0f);
				case SOUTH:
					return new AxisAlignedBB(0.0f, 0.0f, 0.5f, 1.0f, 1.0f, bottom ? 0.75f : 1.0f);
				case WEST:
					return new AxisAlignedBB(bottom ? 0.25f : 0.0f, 0.0f, 0.0f, 0.5f, 1.0f, 1.0f);
			}
		}
		return null;
	}

	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		BlockPos startPos = pos;
		if (BlockProperties.getHalf(state) == EnumBlockHalf.TOP)
			startPos = pos.down(2);
		else {
			IBlockState upState = world.getBlockState(pos.up());
			if (upState.getBlock() == this && BlockProperties.getHalf(upState) == EnumBlockHalf.TOP)
				startPos = pos.down();
		}

		EnumFacing face = BlockProperties.getSide(state);
		for (int i = 0; i < 3; i++) {
			BlockPos curPos = startPos.up(i);
			if (!curPos.equals(pos))
				world.setBlockToAir(curPos);
			world.setBlockToAir(curPos.offset(face));
		}
	}

	public static class ItemPortableLadder extends ItemBlock {

		public ItemPortableLadder(Block block) {
			super(block);
		}

		@Override
		public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World world, BlockPos pos,
				EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
			if (world.isRemote) {
				return EnumActionResult.SUCCESS;
			} else if (side != EnumFacing.UP) {
				return EnumActionResult.PASS;
			} else {
				if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
					pos = pos.up();
				}

				int i = MathHelper.floor_double((double) (playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
				EnumFacing placeFacing = EnumFacing.getHorizontal(i);

				for (int height = 0; height < 3; height++) {
					BlockPos placePos1 = pos.up(height);
					IBlockState state1 = world.getBlockState(placePos1);
					if (!playerIn.canPlayerEdit(placePos1, side, stack) || !state1.getBlock()
							.isReplaceable(world, placePos1))
						return EnumActionResult.FAIL;
					BlockPos placePos2 = placePos1.offset(placeFacing);
					IBlockState state2 = world.getBlockState(placePos2);
					if (!playerIn.canPlayerEdit(placePos2, side, stack) || !state2.getBlock()
							.isReplaceable(world, placePos2))
						return EnumActionResult.FAIL;
				}

				for (int height = 0; height < 3; height++) {
					IBlockState state = ModBuildingBricks.portableLadder.getDefaultState();
					state = BlockProperties.setSide(state, placeFacing);
					if (height != 2)
						state = BlockProperties.setHalf(state, EnumBlockHalf.BOTTOM);
					else
						state = BlockProperties.setHalf(state, EnumBlockHalf.TOP);
					BlockPos placePos = pos.up(height);
					world.setBlockState(placePos, state, 3);
					state = BlockProperties.setSide(state, placeFacing.getOpposite());
					world.setBlockState(placePos.offset(placeFacing), state, 3);
				}
				--stack.stackSize;
				return EnumActionResult.SUCCESS;
			}
		}
	}
}
