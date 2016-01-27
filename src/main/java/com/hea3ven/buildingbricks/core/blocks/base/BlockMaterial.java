package com.hea3ven.buildingbricks.core.blocks.base;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;

public interface BlockMaterial {

	String getLocalizedName(Material mat);

	Material getMaterial(IBlockAccess world, BlockPos pos);
}
