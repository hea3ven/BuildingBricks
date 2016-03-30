package com.hea3ven.buildingbricks.core.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.util.Constants.NBT;

import com.hea3ven.buildingbricks.core.blocks.base.BlockBuildingBricks;
import com.hea3ven.buildingbricks.core.blocks.base.BlockMaterial;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;
import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.buildingbricks.core.materials.MaterialStack.ItemMaterial;
import com.hea3ven.buildingbricks.core.materials.mapping.MaterialIdMapping;
import com.hea3ven.tools.commonutils.util.PlaceParams;

public class ItemMaterialBlock extends ItemBlock implements ItemMaterial {

	public ItemMaterialBlock(Block block) {
		super(block);
		setHasSubtypes(true);
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
		if (stack.getItemDamage() != 0) {
			stack.setItemDamage(0);
		}
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setString("material", mat.getMaterialId());
	}

	@Override
	public Material getMaterial(ItemStack stack) {
		if (stack.getItemDamage() != 0) {
			setMaterial(stack, MaterialIdMapping.get().getMaterialById((short) stack.getItemDamage()));
		}

		if (stack.getTagCompound() != null && stack.getTagCompound().hasKey("material", NBT.TAG_STRING))
			return MaterialRegistry.get(stack.getTagCompound().getString("material"));
		else
			return null;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		PlaceParams params = new PlaceParams(pos, side, hitX, hitY, hitZ);
		if (placeBlock(stack, player, world, params))
			return EnumActionResult.SUCCESS;
		return super.onItemUse(stack, player, world, pos, hand, side, hitX, hitY, hitZ);
	}

	private boolean placeBlock(ItemStack stack, EntityPlayer player, World world, PlaceParams params) {
		IBlockState state = world.getBlockState(params.pos);
		if (getBlock() != state.getBlock()) {
			return false;
		}

		Material stackMat = getMaterial(stack);
		Material mat = ((BlockMaterial) state.getBlock()).getMaterial(world, params.pos);
		if (stackMat != mat) {
			return false;
		}

		MaterialBlockType thisType = ((BlockBuildingBricks) getBlock()).getBlockLogic().getBlockType();
		IBlockState newState;
		if (thisType == MaterialBlockType.SLAB) {
			newState = tryCombineSlabs(mat, state, params);
			if (newState == null)
				return false;
		} else if (thisType == MaterialBlockType.VERTICAL_SLAB) {
			newState = tryCombineVertSlabs(mat, state, params);
			if (newState == null)
				return false;
		} else if (thisType == MaterialBlockType.STEP) {
			newState = tryCombineSteps(mat, state, params);
			if (newState == null)
				return false;
		} else if (thisType == MaterialBlockType.CORNER) {
			newState = tryCombineCorner(mat, state, params);
			if (newState == null)
				return false;
		} else
			return false;

		return convertBlock(stack, player, world, params.pos, stack, newState);
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player,
			ItemStack stack) {
		Minecraft.getMinecraft().setIngameNotInFocus();
		if (canPlaceBlock(world, pos, side, player, stack))
			return true;
		return canPlaceBlock(world, pos.offset(side), side, player, stack) ||
				super.canPlaceBlockOnSide(world, pos, side, player, stack);
	}

	public boolean canPlaceBlock(World world, BlockPos pos, EnumFacing side, EntityPlayer player,
			ItemStack stack) {
		IBlockState state = world.getBlockState(pos);
		if (getBlock() != state.getBlock())
			return false;

		Material stackMat = getMaterial(stack);
		Material mat = ((BlockMaterial) state.getBlock()).getMaterial(world, pos);
		if (stackMat != mat)
			return false;

		MaterialBlockType thisType = ((BlockBuildingBricks) getBlock()).getBlockLogic().getBlockType();
		if (thisType == MaterialBlockType.SLAB || thisType == MaterialBlockType.VERTICAL_SLAB ||
				thisType == MaterialBlockType.STEP || thisType == MaterialBlockType.CORNER)
			return true;

		return false;
	}

	private IBlockState tryCombineSlabs(Material mat, IBlockState state, PlaceParams params) {
		EnumBlockHalf blockHalf = state.getValue(BlockSlab.HALF);
		EnumBlockHalf placeHalf = params.hit.yCoord > 0.5f ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM;
		if (params.hit.yCoord == 0.5f || blockHalf != placeHalf) {
			BlockDescription blockDesc = mat.getBlock(MaterialBlockType.FULL);
			IBlockState newState = getStateFromStack(blockDesc);
			return newState;
		}
		return null;
	}

	private IBlockState tryCombineVertSlabs(Material mat, IBlockState state, PlaceParams params) {
		EnumFacing blockSide = state.getValue(BlockProperties.SIDE);
		if ((blockSide.getDirectionVec().getX() > 0 && params.hit.xCoord <= 0.5f) ||
				(blockSide.getDirectionVec().getX() < 0 && params.hit.xCoord >= 0.5f) ||
				(blockSide.getDirectionVec().getZ() > 0 && params.hit.zCoord <= 0.5f) ||
				(blockSide.getDirectionVec().getZ() < 0 && params.hit.zCoord >= 0.5f)) {
			BlockDescription blockDesc = mat.getBlock(MaterialBlockType.FULL);

			IBlockState newState = getStateFromStack(blockDesc);
			return newState;
		}
		return null;
	}

	private IBlockState tryCombineSteps(Material mat, IBlockState state, PlaceParams params) {
		boolean vertical = state.getValue(BlockProperties.VERTICAL);
		EnumFacing blockSide = state.getValue(BlockProperties.ROTATION).getSide();
		if (!vertical) {
			EnumBlockHalf blockHalf = state.getValue(BlockProperties.HALF);
			boolean join;
			if ((((blockHalf == EnumBlockHalf.BOTTOM && params.hit.yCoord >= 0.5f) ||
					(blockHalf == EnumBlockHalf.TOP && params.hit.yCoord <= 0.5f)))) {
				join = (blockSide.getDirectionVec().getX() > 0 && params.hit.xCoord >= 0.5f) ||
						(blockSide.getDirectionVec().getX() < 0 && params.hit.xCoord <= 0.5f) ||
						(blockSide.getDirectionVec().getZ() > 0 && params.hit.zCoord >= 0.5f) ||
						(blockSide.getDirectionVec().getZ() < 0 && params.hit.zCoord <= 0.5f);
			} else {
				join = (blockSide.getDirectionVec().getX() > 0 && params.hit.xCoord <= 0.5f) ||
						(blockSide.getDirectionVec().getX() < 0 && params.hit.xCoord >= 0.5f) ||
						(blockSide.getDirectionVec().getZ() > 0 && params.hit.zCoord <= 0.5f) ||
						(blockSide.getDirectionVec().getZ() < 0 && params.hit.zCoord >= 0.5f);
			}
			if (join) {
				BlockDescription blockDesc = mat.getBlock(MaterialBlockType.SLAB);

				IBlockState newState = getStateFromStack(blockDesc);
				newState = newState.withProperty(BlockSlab.HALF, blockHalf);
				return newState;
			}
		} else {
			EnumFacing newSide = null;
			if (blockSide == EnumFacing.NORTH && params.hit.zCoord <= 0.5f && params.hit.xCoord >= 0.5f)
				newSide = EnumFacing.NORTH;
			else if (blockSide == EnumFacing.EAST && params.hit.zCoord <= 0.5f && params.hit.xCoord <= 0.5f)
				newSide = EnumFacing.NORTH;
			else if (blockSide == EnumFacing.EAST && params.hit.xCoord >= 0.5 && params.hit.zCoord >= 0.5f)
				newSide = EnumFacing.EAST;
			else if (blockSide == EnumFacing.SOUTH && params.hit.xCoord >= 0.5 && params.hit.zCoord <= 0.5f)
				newSide = EnumFacing.EAST;
			else if (blockSide == EnumFacing.SOUTH && params.hit.xCoord <= 0.5f && params.hit.zCoord >= 0.5)
				newSide = EnumFacing.SOUTH;
			else if (blockSide == EnumFacing.WEST && params.hit.xCoord >= 0.5 && params.hit.zCoord >= 0.5)
				newSide = EnumFacing.SOUTH;
			else if (blockSide == EnumFacing.WEST && params.hit.xCoord <= 0.5 && params.hit.zCoord <= 0.5)
				newSide = EnumFacing.WEST;
			else if (blockSide == EnumFacing.NORTH && params.hit.xCoord <= 0.5 && params.hit.zCoord >= 0.5)
				newSide = EnumFacing.WEST;
			if (newSide != null) {
				BlockDescription blockDesc = mat.getBlock(MaterialBlockType.VERTICAL_SLAB);

				IBlockState newState = getStateFromStack(blockDesc);

				newState = newState.withProperty(BlockProperties.SIDE, newSide);
				return newState;
			}
		}
		return null;
	}

	private IBlockState tryCombineCorner(Material mat, IBlockState state, PlaceParams params) {
		EnumFacing blockSide = state.getValue(BlockProperties.ROTATION).getSide();
		EnumBlockHalf blockHalf = state.getValue(BlockProperties.HALF);
		if ((((blockHalf == EnumBlockHalf.BOTTOM && params.hit.yCoord >= 0.5f) ||
				(blockHalf == EnumBlockHalf.TOP && params.hit.yCoord <= 0.5f)))) {
			boolean join = false;
			if (blockSide == EnumFacing.NORTH && params.hit.xCoord <= 0.5f && params.hit.zCoord <= 0.5f) {
				join = true;
			} else if (blockSide == EnumFacing.EAST && params.hit.xCoord >= 0.5f &&
					params.hit.zCoord <= 0.5f) {
				join = true;
			} else if (blockSide == EnumFacing.SOUTH && params.hit.xCoord >= 0.5f &&
					params.hit.zCoord >= 0.5f) {
				join = true;
			} else if (blockSide == EnumFacing.WEST && params.hit.xCoord <= 0.5f &&
					params.hit.zCoord >= 0.5f) {
				join = true;
			}
			if (join) {
				BlockDescription blockDesc = mat.getBlock(MaterialBlockType.STEP);

				IBlockState newState = getStateFromStack(blockDesc);
				newState =
						newState.withProperty(BlockProperties.ROTATION, EnumRotation.getRotation(blockSide));
				newState = newState.withProperty(BlockProperties.HALF, EnumBlockHalf.BOTTOM);
				newState = newState.withProperty(BlockProperties.VERTICAL, true);
				return newState;
			}
		} else {
			EnumFacing newSide = null;
			if (blockSide == EnumFacing.NORTH && params.hit.xCoord >= 0.5f && params.hit.zCoord <= 0.5f)
				newSide = EnumFacing.NORTH;
			else if (blockSide == EnumFacing.NORTH && params.hit.xCoord <= 0.5f && params.hit.zCoord >= 0.5f)
				newSide = EnumFacing.WEST;
			else if (blockSide == EnumFacing.EAST && params.hit.xCoord >= 0.5f && params.hit.zCoord >= 0.5f)
				newSide = EnumFacing.EAST;
			else if (blockSide == EnumFacing.EAST && params.hit.xCoord <= 0.5f && params.hit.zCoord <= 0.5f)
				newSide = EnumFacing.NORTH;
			else if (blockSide == EnumFacing.SOUTH && params.hit.xCoord <= 0.5f && params.hit.zCoord >= 0.5f)
				newSide = EnumFacing.SOUTH;
			else if (blockSide == EnumFacing.SOUTH && params.hit.xCoord >= 0.5f && params.hit.zCoord <= 0.5f)
				newSide = EnumFacing.EAST;
			else if (blockSide == EnumFacing.WEST && params.hit.xCoord <= 0.5f && params.hit.zCoord <= 0.5f)
				newSide = EnumFacing.WEST;
			else if (blockSide == EnumFacing.WEST && params.hit.xCoord >= 0.5f && params.hit.zCoord >= 0.5f)
				newSide = EnumFacing.SOUTH;
			if (newSide != null) {
				BlockDescription blockDesc = mat.getBlock(MaterialBlockType.STEP);

				IBlockState newState = getStateFromStack(blockDesc);
				newState = newState.withProperty(BlockProperties.ROTATION, EnumRotation.getRotation(newSide));
				newState = newState.withProperty(BlockProperties.HALF, blockHalf);
				return newState;
			}
		}
		return null;
	}

	private IBlockState getStateFromStack(BlockDescription blockDesc) {
		ItemStack stack = blockDesc.getStack();
		return blockDesc.getBlock().getStateFromMeta(stack.getItem().getMetadata(stack.getMetadata()));
	}

	private boolean convertBlock(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
			ItemStack newStack, IBlockState newState) {
		AxisAlignedBB box = newState.getBlock().getCollisionBoundingBox(newState, world, pos).offset(pos);
		if (world.checkNoEntityCollision(box)) {

			if (!world.setBlockState(pos, newState, 3))
				return false;

			IBlockState state = world.getBlockState(pos);
			state.getBlock().onBlockPlacedBy(world, pos, state, player, stack);

			SoundType soundtype = this.block.getSoundType();
			world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS,
					(soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
			--stack.stackSize;
			return true;
		}
		return false;
	}
}
