package com.hea3ven.buildingbricks.core.materials;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.client.ModelBakerBlockMaterial;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;

public class MaterialBlockLogic {

	private static Random rand = new Random();

	private StructureMaterial structMat;
	private MaterialBlockType blockType;

	public MaterialBlockLogic(StructureMaterial structMat, MaterialBlockType blockType) {
		this.structMat = structMat;
		this.blockType = blockType;
	}

	public void initBlock(Block block) {
		block.setStepSound(structMat.getSoundType());
		block.setHardness(structMat.getHardness());
		if (structMat.getResistance() > 0)
			block.setResistance(structMat.getResistance());
		block.slipperiness = structMat.getSlipperiness();
	}

	public StructureMaterial getStructureMaterial() {
		return structMat;
	}

	public MaterialBlockType getBlockType() {
		return blockType;
	}

	@SideOnly(Side.CLIENT)
	public int getBlockColor() {
		if (!structMat.getColor())
			return 16777215;
		else
			return ColorizerGrass.getGrassColor(0.5D, 1.0D);
	}

	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state) {
		if (!structMat.getColor())
			return 16777215;
		else
			return this.getBlockColor();
	}

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		if (!structMat.getColor())
			return 16777215;
		else
			return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
	}

	public EnumWorldBlockLayer getBlockLayer() {
		return structMat.getBlockLayer();
	}

	public String getLocalizedName(Material mat) {
		return StatCollector.translateToLocalFormatted(blockType.getTranslationKey(), mat.getLocalizedName());
	}

	public String getHarvestTool(IBlockState state) {
		return structMat.getTool();
	}

	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer) {
		BlockPos pos = target.getBlockPos();
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		EnumFacing side = target.sideHit;

		float f = 0.1F;

		double x = pos.getX();
		if (side.getAxis() != Axis.X) {
			x += rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - f * 2.0F) +
					f + block.getBlockBoundsMinX();
		} else {
			if (side == EnumFacing.WEST) {
				x += block.getBlockBoundsMinX() - f;
			}

			if (side == EnumFacing.EAST) {
				x += block.getBlockBoundsMaxX() + f;
			}
		}

		double y = pos.getY();
		if (side.getAxis() != Axis.Y) {
			y += rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - f * 2.0F) +
					f + block.getBlockBoundsMinY();
		} else {
			if (side == EnumFacing.DOWN) {
				y += block.getBlockBoundsMinY() - f;
			}

			if (side == EnumFacing.UP) {
				y += block.getBlockBoundsMaxY() + f;
			}
		}
		double z = pos.getZ();
		if (side.getAxis() != Axis.Z) {
			z += rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - f * 2.0F) +
					f + block.getBlockBoundsMinZ();
		} else {
			if (side == EnumFacing.NORTH) {
				z += block.getBlockBoundsMinZ() - f;
			}

			if (side == EnumFacing.SOUTH) {
				z += block.getBlockBoundsMaxZ() + f;
			}
		}

		EntityDiggingFX particle = (EntityDiggingFX) effectRenderer.spawnEffectParticle(
				EnumParticleTypes.BLOCK_CRACK.getParticleID(), x, y, z, 0.0D, 0.0D, 0.0D,
				Block.getStateId(state));

		TileMaterial te = TileMaterial.getTile(world, pos);

		particle.func_174846_a(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F);
		particle.func_180435_a(ModelBakerBlockMaterial.instance.particleTextures.get(te.getMaterial()));
		return true;
	}

	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer) {
		TileMaterial te = TileMaterial.getTile(world, pos);
		TextureAtlasSprite texture = ModelBakerBlockMaterial.instance.particleTextures.get(te.getMaterial());

		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				for (int k = 0; k < 4; ++k) {
					double x = pos.getX() + (i + 0.5d) / 4;
					double y = pos.getY() + (j + 0.5d) / 4;
					double z = pos.getZ() + (k + 0.5d) / 4;
					EntityDiggingFX particle = (EntityDiggingFX) effectRenderer.spawnEffectParticle(
							EnumParticleTypes.BLOCK_CRACK.getParticleID(), x, y, z, x - pos.getX() - 0.5d,
							y - pos.getY() - 0.5d, z - pos.getZ() - 0.5d,
							Block.getStateId(world.getBlockState(pos)));

					particle.func_174846_a(pos);
					particle.func_180435_a(texture);
				}
			}
		}
		return true;
	}
}
