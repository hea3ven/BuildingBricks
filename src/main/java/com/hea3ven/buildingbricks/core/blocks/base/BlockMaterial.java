package com.hea3ven.buildingbricks.core.blocks.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.tools.commonutils.util.PlaceParams;

public interface BlockMaterial {

	String getLocalizedName(Material mat);

	Material getMaterial(IBlockAccess world, BlockPos pos);

//	IBlockState getPlaceState(World world, PlaceParams params);
}
