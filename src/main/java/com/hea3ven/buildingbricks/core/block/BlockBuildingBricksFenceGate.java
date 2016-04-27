package com.hea3ven.buildingbricks.core.block;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.block.base.BlockBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockLogic;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.buildingbricks.core.materials.StructureMaterial;

public class BlockBuildingBricksFenceGate extends BlockFenceGate implements BlockBuildingBricks {

	protected MaterialBlockLogic blockLogic;

	public BlockBuildingBricksFenceGate(StructureMaterial structMat) {
		super(EnumType.OAK);

		blockLogic = new MaterialBlockLogic(structMat, MaterialBlockType.FENCE_GATE);
		blockLogic.initBlock(this);
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
