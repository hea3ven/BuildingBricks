package com.hea3ven.buildingbricks.compat.mcmultipart.microblock;

import mcmultipart.microblock.IMicroMaterial;
import mcmultipart.microblock.Microblock;
import mcmultipart.microblock.MicroblockClass;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.PartSlot;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;

import com.hea3ven.buildingbricks.compat.mcmultipart.ProxyModBuildingBricksCompatMcMultipart;
import com.hea3ven.buildingbricks.core.ModBuildingBricks;

public class MicroblockBlockMaterial extends Microblock {
	public static Map<Block, MicroblockClass> microblockClasses = new HashMap<>();

	private IBlockState state;

	public MicroblockBlockMaterial(IMicroMaterial microMat, PartSlot part, int size, boolean isRemote,
			IBlockState state) {
		super(microMat, part, size, isRemote);
		this.state = state;
	}

	@Override
	public MicroblockClass getMicroClass() {
		return microblockClasses.get(state.getBlock());
	}

	@Override
	public AxisAlignedBB getBounds() {
		return state.getBlock().getBoundingBox(state, null, null);
	}
}
