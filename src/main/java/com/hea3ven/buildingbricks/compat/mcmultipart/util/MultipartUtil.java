package com.hea3ven.buildingbricks.compat.mcmultipart.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.hea3ven.buildingbricks.compat.mcmultipart.multipart.MultipartBlockWrapper;

public class MultipartUtil {
	public static boolean canPartFit(World world, MultipartBlockWrapper part, BlockPos pos) {
		List<AxisAlignedBB> boxes = new ArrayList<>();
		AxisAlignedBB partBox = part.getBoundingBox().offset(pos);
		world.getBlockState(pos).addCollisionBoxToList(world,pos, partBox, boxes, null);
		if (boxes.size() > 0)
			return false;
		return true;
	}

}
