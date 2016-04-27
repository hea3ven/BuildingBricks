package com.hea3ven.buildingbricks.core.block.base;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import com.hea3ven.buildingbricks.core.materials.Material;

public interface BlockMaterial {

	String getLocalizedName(Material mat);

	Material getMaterial(IBlockAccess world, BlockPos pos);
}
