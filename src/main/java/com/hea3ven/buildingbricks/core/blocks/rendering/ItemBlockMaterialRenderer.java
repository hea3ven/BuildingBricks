package com.hea3ven.buildingbricks.core.blocks.rendering;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

import net.minecraftforge.client.IItemRenderer;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksSlab;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksStairs;
import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksWall;
import com.hea3ven.buildingbricks.core.blocks.base.BlockBuildingBricksBase;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;

public class ItemBlockMaterialRenderer implements IItemRenderer {

	private RenderBlocks renderBlocks = new RenderBlocks();

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		Block block = ((ItemBlock) stack.getItem()).field_150939_a;
		if (block instanceof BlockBuildingBricksBase) {
			BlockBuildingBricksBase baseBlock = (BlockBuildingBricksBase) block;
			baseBlock.setCurrentRenderingMaterial(TileMaterial.getStackMaterial(stack));
			AxisAlignedBB bb = baseBlock.getBoundingBox(baseBlock.getDefaultState());
			block.setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX,
					(float) bb.maxY, (float) bb.maxZ);
		} else if (block instanceof BlockBuildingBricksStairs) {
			BlockBuildingBricksStairs stairsBlock = (BlockBuildingBricksStairs) block;
			stairsBlock.setCurrentRenderingMaterial(TileMaterial.getStackMaterial(stack));
		} else if (block instanceof BlockBuildingBricksSlab) {
			BlockBuildingBricksSlab slabBlock = (BlockBuildingBricksSlab) block;
			slabBlock.setCurrentRenderingMaterial(TileMaterial.getStackMaterial(stack));
		} else if (block instanceof BlockBuildingBricksWall) {
			BlockBuildingBricksWall wallBlock = (BlockBuildingBricksWall) block;
			wallBlock.setCurrentRenderingMaterial(TileMaterial.getStackMaterial(stack));
		}
		renderBlocks.renderBlockAsItem(block, 0, 1.0f);
	}

}
