package com.hea3ven.buildingbricks.core.materials;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
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
		// TODO:
//		block.setStepSound(structMat.getSoundType());
		block.setHardness(structMat.getHardness());
		if (structMat.getResistance() > 0)
			block.setResistance(structMat.getResistance());
		block.slipperiness = structMat.getSlipperiness();

		// TODO: Register color handler
//		BlockColors.instance.register()
//		{
//			if (!structMat.getColor())
//				return 16777215;
//			else
//				return ColorizerGrass.getGrassColor(0.5D, 1.0D);
//		}
	}

	public StructureMaterial getStructureMaterial() {
		return structMat;
	}

	public MaterialBlockType getBlockType() {
		return blockType;
	}

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int tintIndex) {
		if (!structMat.getColor() || pos == null)
			return 16777215;
		else
			return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
	}

	public BlockRenderLayer getBlockLayer() {
		return structMat.getBlockLayer();
	}

	public String getLocalizedName(Material mat) {
		return I18n.translateToLocalFormatted(blockType.getTranslationKey(), mat.getLocalizedName());
	}

	public String getHarvestTool(IBlockState state) {
		return structMat.getTool();
	}

	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World world, RayTraceResult target, EffectRenderer effectRenderer) {
		BlockPos pos = target.getBlockPos();
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		EnumFacing side = target.sideHit;

		float f = 0.1F;

		double x = pos.getX();
		AxisAlignedBB blockBb = block.getBoundingBox(state, (IBlockAccess) world, pos);
		if (side.getAxis() != Axis.X) {
			x += rand.nextDouble() * (blockBb.maxX - blockBb.minX - f * 2.0F) +
					f + blockBb.minX;
		} else {
			if (side == EnumFacing.WEST) {
				x += blockBb.minX - f;
			}

			if (side == EnumFacing.EAST) {
				x += blockBb.maxX + f;
			}
		}

		double y = pos.getY();
		if (side.getAxis() != Axis.Y) {
			y += rand.nextDouble() * (blockBb.maxY - blockBb.minY - f * 2.0F) +
					f + blockBb.minY;
		} else {
			if (side == EnumFacing.DOWN) {
				y += blockBb.minY - f;
			}

			if (side == EnumFacing.UP) {
				y += blockBb.maxY + f;
			}
		}
		double z = pos.getZ();
		if (side.getAxis() != Axis.Z) {
			z += rand.nextDouble() * (blockBb.maxZ - blockBb.minZ - f * 2.0F) +
					f + blockBb.minZ;
		} else {
			if (side == EnumFacing.NORTH) {
				z += blockBb.minZ - f;
			}

			if (side == EnumFacing.SOUTH) {
				z += blockBb.maxZ + f;
			}
		}

		EntityDiggingFX particle = (EntityDiggingFX) effectRenderer.spawnEffectParticle(
				EnumParticleTypes.BLOCK_CRACK.getParticleID(), x, y, z, 0.0D, 0.0D, 0.0D,
				Block.getStateId(state));

		TileMaterial te = TileMaterial.getTile(world, pos);

		particle.setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F);
		particle.setParticleTexture(ModelBakerBlockMaterial.particleTextures.get(te.getMaterial()));
		return true;
	}

	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer) {
		TileMaterial te = TileMaterial.getTile(world, pos);
		TextureAtlasSprite texture = ModelBakerBlockMaterial.particleTextures.get(te.getMaterial());

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

					particle.setBlockPos(pos);
					particle.setParticleTexture(texture);
				}
			}
		}
		return true;
	}
}
