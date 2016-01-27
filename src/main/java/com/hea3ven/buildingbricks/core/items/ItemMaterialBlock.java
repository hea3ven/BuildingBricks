package com.hea3ven.buildingbricks.core.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.blocks.base.BlockBuildingBricks;
import com.hea3ven.buildingbricks.core.blocks.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;
import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.buildingbricks.core.materials.MaterialStack.ItemMaterial;
import com.hea3ven.buildingbricks.core.materials.mapping.MaterialIdMapping;

public class ItemMaterialBlock extends ItemBlock implements ItemMaterial {

	public ItemMaterialBlock(Block block) {
		super(block);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		Material mat = MaterialStack.get(stack);
		if (mat == null)
			return "tile.invalidMaterialBlock.name";
		return ((BlockMaterial) block).getLocalizedName(mat);
	}

	@Override
	public void setMaterial(ItemStack stack, Material mat) {
		if (stack.hasTagCompound()) // update from 1.0.x
			stack.setTagCompound(null);

		stack.setItemDamage(MaterialIdMapping.get().getIdForMaterial(mat));
	}

	@Override
	public Material getMaterial(ItemStack stack) {
		if (stack.hasTagCompound()) { // update from 1.0.x
			String matId = stack.getTagCompound().getString("material");
			stack.setTagCompound(null);
			setMaterial(stack, MaterialRegistry.get(matId));
		}

		short matId = (short) stack.getItemDamage();
		if (matId == 0)
			return null;
		return MaterialIdMapping.get().getMaterialById(matId);
	}

	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int tintIndex) {
		return getBlock().getRenderColor(getBlock().getStateFromMeta(stack.getMetadata()));
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		return onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ, true);
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, boolean firstTry) {
		IBlockState state = world.getBlockState(pos);
		if (getBlock() != state.getBlock()) {
			if (firstTry)
				return onItemUse(stack, player, world, pos.offset(side), side, hitX - side.getFrontOffsetX(),
						hitY - side.getFrontOffsetY(), hitZ - side.getFrontOffsetZ(), false);
			else
				return super.onItemUse(stack, player, world, pos.offset(side.getOpposite()), side,
						hitX + side.getFrontOffsetX(), hitY + side.getFrontOffsetY(),
						hitZ + side.getFrontOffsetZ());
		}

		Material stackMat = getMaterial(stack);
		Material mat = ((BlockMaterial) state.getBlock()).getMaterial(world, pos);
		if (stackMat != mat) {
			if (firstTry)
				return onItemUse(stack, player, world, pos.offset(side), side, hitX - side.getFrontOffsetX(),
						hitY - side.getFrontOffsetY(), hitZ - side.getFrontOffsetZ(), false);
			else
				return super.onItemUse(stack, player, world, pos.offset(side.getOpposite()), side,
						hitX + side.getFrontOffsetX(), hitY + side.getFrontOffsetY(),
						hitZ + side.getFrontOffsetZ());
		}

		MaterialBlockType thisType = ((BlockBuildingBricks) getBlock()).getBlockLogic().getBlockType();
		if (thisType == MaterialBlockType.SLAB &&
				tryCombineSlabs(stack, player, world, pos, state, side, hitX, hitY, hitZ))
			return true;
		else if (thisType == MaterialBlockType.VERTICAL_SLAB &&
				tryCombineVertSlabs(stack, player, world, pos, state, side, hitX, hitY, hitZ))
			return true;
		else if (thisType == MaterialBlockType.STEP &&
				tryCombineSteps(stack, player, world, pos, state, side, hitX, hitY, hitZ))
			return true;
		else if (thisType == MaterialBlockType.CORNER &&
				tryCombineCorner(stack, player, world, pos, state, side, hitX, hitY, hitZ))
			return true;

		if (firstTry) {
			if (onItemUse(stack, player, world, pos.offset(side), side, hitX - side.getFrontOffsetX(),
					hitY - side.getFrontOffsetY(), hitZ - side.getFrontOffsetZ(), false))
				return true;
			else
				return super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
		} else {
			return false;
		}
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player,
			ItemStack stack) {
		return canPlaceBlockOnSide(world, pos, side, player, stack, true);
	}

	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player,
			ItemStack stack, boolean firstTry) {
		IBlockState state = world.getBlockState(pos);
		if (getBlock() != state.getBlock()) {
			if (firstTry)
				return canPlaceBlockOnSide(world, pos.offset(side), side, player, stack, false);
			else
				return super.canPlaceBlockOnSide(world, pos.offset(side.getOpposite()), side, player, stack);
		}

		Material stackMat = getMaterial(stack);
		Material mat = ((BlockMaterial) state.getBlock()).getMaterial(world, pos);
		if (stackMat != mat) {
			if (firstTry)
				return canPlaceBlockOnSide(world, pos.offset(side), side, player, stack, false);
			else
				return super.canPlaceBlockOnSide(world, pos.offset(side.getOpposite()), side, player, stack);
		}

		MaterialBlockType thisType = ((BlockBuildingBricks) getBlock()).getBlockLogic().getBlockType();
		if (thisType == MaterialBlockType.SLAB || thisType == MaterialBlockType.VERTICAL_SLAB ||
				thisType == MaterialBlockType.STEP || thisType == MaterialBlockType.CORNER) {
			return true;
		}

		if (firstTry) {
			if (canPlaceBlockOnSide(world, pos.offset(side), side, player, stack, false))
				return true;
			else
				return super.canPlaceBlockOnSide(world, pos, side, player, stack);
		} else {
			return false;
		}
	}

	private boolean tryCombineSlabs(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			IBlockState state, EnumFacing side, float hitX, float hitY, float hitZ) {
		EnumBlockHalf blockHalf = state.getValue(BlockSlab.HALF);
		if ((blockHalf == EnumBlockHalf.BOTTOM && hitY >= 0.5f) ||
				(blockHalf == EnumBlockHalf.TOP && hitY <= 0.5f)) {
			Material mat = ((BlockMaterial) state.getBlock()).getMaterial(world, pos);
			BlockDescription blockDesc = mat.getBlock(MaterialBlockType.FULL);

			IBlockState newState = getStateFromStack(blockDesc);
			return convertBlock(stack, player, world, pos, blockDesc.getStack(), newState);
		}
		return false;
	}

	private boolean tryCombineVertSlabs(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			IBlockState state, EnumFacing side, float hitX, float hitY, float hitZ) {
		EnumFacing blockSide = state.getValue(BlockProperties.SIDE);
		if ((blockSide.getDirectionVec().getX() > 0 && hitX <= 0.5f) ||
				(blockSide.getDirectionVec().getX() < 0 && hitX >= 0.5f) ||
				(blockSide.getDirectionVec().getZ() > 0 && hitZ <= 0.5f) ||
				(blockSide.getDirectionVec().getZ() < 0 && hitZ >= 0.5f)) {
			Material mat = ((BlockMaterial) state.getBlock()).getMaterial(world, pos);
			BlockDescription blockDesc = mat.getBlock(MaterialBlockType.FULL);

			IBlockState newState = getStateFromStack(blockDesc);
			return convertBlock(stack, player, world, pos, blockDesc.getStack(), newState);
		}
		return false;
	}

	private boolean tryCombineSteps(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			IBlockState state, EnumFacing side, float hitX, float hitY, float hitZ) {
		boolean vertical = state.getValue(BlockProperties.VERTICAL);
		EnumFacing blockSide = state.getValue(BlockProperties.ROTATION).getSide();
		if (!vertical) {
			com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf blockHalf =
					state.getValue(BlockProperties.HALF);
			boolean join;
			if ((((blockHalf == com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf.BOTTOM &&
					hitY >= 0.5f) ||
					(blockHalf == com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf.TOP &&
							hitY <= 0.5f)))) {
				join = (blockSide.getDirectionVec().getX() > 0 && hitX >= 0.5f) ||
						(blockSide.getDirectionVec().getX() < 0 && hitX <= 0.5f) ||
						(blockSide.getDirectionVec().getZ() > 0 && hitZ >= 0.5f) ||
						(blockSide.getDirectionVec().getZ() < 0 && hitZ <= 0.5f);
			} else {
				join = (blockSide.getDirectionVec().getX() > 0 && hitX <= 0.5f) ||
						(blockSide.getDirectionVec().getX() < 0 && hitX >= 0.5f) ||
						(blockSide.getDirectionVec().getZ() > 0 && hitZ <= 0.5f) ||
						(blockSide.getDirectionVec().getZ() < 0 && hitZ >= 0.5f);
			}
			if (join) {
				Material mat = ((BlockMaterial) state.getBlock()).getMaterial(world, pos);
				BlockDescription blockDesc = mat.getBlock(MaterialBlockType.SLAB);

				IBlockState newState = getStateFromStack(blockDesc);
				newState = newState.withProperty(BlockSlab.HALF, blockHalf.toSlabEnum());
				return convertBlock(stack, player, world, pos, blockDesc.getStack(), newState);
			}
		} else {
			EnumFacing newSide = null;
			if (blockSide == EnumFacing.NORTH && hitZ <= 0.5f && hitX >= 0.5f)
				newSide = EnumFacing.NORTH;
			else if (blockSide == EnumFacing.EAST && hitZ <= 0.5f && hitX <= 0.5f)
				newSide = EnumFacing.NORTH;
			else if (blockSide == EnumFacing.EAST && hitX >= 0.5 && hitZ >= 0.5f)
				newSide = EnumFacing.EAST;
			else if (blockSide == EnumFacing.SOUTH && hitX >= 0.5 && hitZ <= 0.5f)
				newSide = EnumFacing.EAST;
			else if (blockSide == EnumFacing.SOUTH && hitX <= 0.5f && hitZ >= 0.5)
				newSide = EnumFacing.SOUTH;
			else if (blockSide == EnumFacing.WEST && hitX >= 0.5 && hitZ >= 0.5)
				newSide = EnumFacing.SOUTH;
			else if (blockSide == EnumFacing.WEST && hitX <= 0.5 && hitZ <= 0.5)
				newSide = EnumFacing.WEST;
			else if (blockSide == EnumFacing.NORTH && hitX <= 0.5 && hitZ >= 0.5)
				newSide = EnumFacing.WEST;
			if (newSide != null) {
				Material mat = ((BlockMaterial) state.getBlock()).getMaterial(world, pos);
				BlockDescription blockDesc = mat.getBlock(MaterialBlockType.VERTICAL_SLAB);

				IBlockState newState = getStateFromStack(blockDesc);

				newState = newState.withProperty(BlockProperties.SIDE, newSide);
				convertBlock(stack, player, world, pos, blockDesc.getStack(), newState);
			}
		}
		return false;
	}

	private boolean tryCombineCorner(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			IBlockState state, EnumFacing side, float hitX, float hitY, float hitZ) {
		EnumFacing blockSide = state.getValue(BlockProperties.ROTATION).getSide();
		com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf blockHalf =
				state.getValue(BlockProperties.HALF);
		if ((((blockHalf == com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf.BOTTOM &&
				hitY >= 0.5f) || (blockHalf == com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf.TOP &&
				hitY <= 0.5f)))) {
			boolean join = false;
			if (blockSide == EnumFacing.NORTH && hitX <= 0.5f && hitZ <= 0.5f) {
				join = true;
			} else if (blockSide == EnumFacing.EAST && hitX >= 0.5f && hitZ <= 0.5f) {
				join = true;
			} else if (blockSide == EnumFacing.SOUTH && hitX >= 0.5f && hitZ >= 0.5f) {
				join = true;
			} else if (blockSide == EnumFacing.WEST && hitX <= 0.5f && hitZ >= 0.5f) {
				join = true;
			}
			if (join) {
				Material mat = ((BlockMaterial) state.getBlock()).getMaterial(world, pos);
				BlockDescription blockDesc = mat.getBlock(MaterialBlockType.STEP);

				IBlockState newState = getStateFromStack(blockDesc);
				newState =
						newState.withProperty(BlockProperties.ROTATION, EnumRotation.getRotation(blockSide));
				newState = newState.withProperty(BlockProperties.HALF,
						com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf.BOTTOM);
				newState = newState.withProperty(BlockProperties.VERTICAL, true);
				return convertBlock(stack, player, world, pos, blockDesc.getStack(), newState);
			}
		} else {
			EnumFacing newSide = null;
			if (blockSide == EnumFacing.NORTH && hitX >= 0.5f && hitZ <= 0.5f)
				newSide = EnumFacing.NORTH;
			else if (blockSide == EnumFacing.NORTH && hitX <= 0.5f && hitZ >= 0.5f)
				newSide = EnumFacing.WEST;
			else if (blockSide == EnumFacing.EAST && hitX >= 0.5f && hitZ >= 0.5f)
				newSide = EnumFacing.EAST;
			else if (blockSide == EnumFacing.EAST && hitX <= 0.5f && hitZ <= 0.5f)
				newSide = EnumFacing.NORTH;
			else if (blockSide == EnumFacing.SOUTH && hitX <= 0.5f && hitZ >= 0.5f)
				newSide = EnumFacing.SOUTH;
			else if (blockSide == EnumFacing.SOUTH && hitX >= 0.5f && hitZ <= 0.5f)
				newSide = EnumFacing.EAST;
			else if (blockSide == EnumFacing.WEST && hitX <= 0.5f && hitZ <= 0.5f)
				newSide = EnumFacing.WEST;
			else if (blockSide == EnumFacing.WEST && hitX >= 0.5f && hitZ >= 0.5f)
				newSide = EnumFacing.SOUTH;
			if (newSide != null) {
				Material mat = ((BlockMaterial) state.getBlock()).getMaterial(world, pos);
				BlockDescription blockDesc = mat.getBlock(MaterialBlockType.STEP);

				IBlockState newState = getStateFromStack(blockDesc);
				newState = newState.withProperty(BlockProperties.ROTATION, EnumRotation.getRotation(newSide));
				newState = newState.withProperty(BlockProperties.HALF, blockHalf);
				return convertBlock(stack, player, world, pos, blockDesc.getStack(), newState);
			}
		}
		return false;
	}

	private IBlockState getStateFromStack(BlockDescription blockDesc) {
		ItemStack stack = blockDesc.getStack();
		return blockDesc.getBlock().getStateFromMeta(stack.getItem().getMetadata(stack.getMetadata()));
	}

	private boolean convertBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			ItemStack newStack, IBlockState newState) {
		if (world.checkNoEntityCollision(newState.getBlock().getCollisionBoundingBox(world, pos, newState))) {
			if (((ItemBlock) newStack.getItem()).placeBlockAt(stack, player, world, pos, null, 0.0f, 0.0f,
					0.0f, newState)) {
				world.playSoundEffect((double) ((float) pos.getX() + 0.5F),
						(double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F),
						newState.getBlock().stepSound.getPlaceSound(),
						(newState.getBlock().stepSound.getVolume() + 1.0F) / 2.0F,
						newState.getBlock().stepSound.getFrequency() * 0.8F);
				--stack.stackSize;
				return true;
			}
		}
		return false;
	}
}
