package com.hea3ven.buildingbricks.core.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.IBlockAccess;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.blocks.base.BlockBuildingBricks;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockLogic;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public class BlockBuildingBricksWall extends BlockWall implements BlockBuildingBricks {

	protected MaterialBlockLogic blockLogic;

	public BlockBuildingBricksWall(StructureMaterial structMat) {
		super(new Block(structMat.getMcMaterial()) {
		});

		blockLogic = new MaterialBlockLogic(structMat, MaterialBlockType.WALL);
		blockLogic.initBlock(this);
	}

	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		list.add(new ItemStack(itemIn));
	}

	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		if (!(state.getBlock() instanceof BlockWall)) {
			return !state.getBlock().isSideSolid(state,world, pos, side);
		}

		BlockPos ownPos = pos.offset(side.getOpposite());
		IBlockState ownState = world.getBlockState(ownPos);
		return side.getAxis() == Axis.Y || BlockProperties.getConnection(ownState, side);
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	//region COMMON BLOCK CODE

	@Override
	public MaterialBlockLogic getBlockLogic() {
		return blockLogic;
	}

	@Override
	public boolean requiresUpdates() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return blockLogic.getBlockLayer();
	}

	@Override
	public int getHarvestLevel(IBlockState state) {
		return 0;
	}

	@Override
	public String getHarvestTool(IBlockState state) {
		return blockLogic.getHarvestTool(state);
	}

	//endregion COMMON BLOCK CODE
}
