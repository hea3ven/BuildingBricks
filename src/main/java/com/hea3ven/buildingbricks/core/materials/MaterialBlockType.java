package com.hea3ven.buildingbricks.core.materials;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.vecmath.Vector3f;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.TRSRTransformation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.hea3ven.buildingbricks.core.blocks.BlockBuildingBricksBase;
import com.hea3ven.buildingbricks.core.blocks.properties.BlockProperties;
import com.hea3ven.buildingbricks.core.blockstate.EnumBlockHalf;
import com.hea3ven.buildingbricks.core.blockstate.EnumRotation;

public enum MaterialBlockType {
	FULL("block", "minecraft:block/cube_bottom_top", 1000),
	STAIRS_FIXED_INNER_CORNER("stair_inner_corner", "minecraft:block/inner_stairs", 875),
	STAIRS_FIXED_SIDE("stair_side", "minecraft:block/stairs", 750),
	STAIRS_FIXED_CORNER("stair_corner", "minecraft:block/outer_stairs", 625),
	SLAB("slab", "minecraft:block/half_slab", 500),
	STEP("step", "buildingbricks:block/step_bottom", 250),
	CORNER("corner", "buildingbricks:block/corner_bottom", 125),
	WALL("wall", "minecraft:block/wall_post", 1000);

	static {
		FULL.addRecipe(true, MaterialRecipeBuilder
				.create()
				.outputAmount(2)
				.pattern("xx", "xx")
				.map('x', SLAB)
				.validate());
		SLAB.addRecipe(false, MaterialRecipeBuilder
				.create()
				.outputAmount(6)
				.pattern("xxx")
				.map('x', FULL)
				.validate());
		SLAB.addRecipe(true, MaterialRecipeBuilder
				.create()
				.outputAmount(1)
				.pattern("xx")
				.map('x', STEP)
				.validate());
		STEP.addRecipe(false, MaterialRecipeBuilder
				.create()
				.outputAmount(6)
				.pattern("xxx")
				.map('x', SLAB)
				.validate());
		STEP.addRecipe(true, MaterialRecipeBuilder
				.create()
				.outputAmount(1)
				.pattern("xx")
				.map('x', CORNER)
				.validate());
		CORNER.addRecipe(false, MaterialRecipeBuilder
				.create()
				.outputAmount(6)
				.pattern("xxx")
				.map('x', STEP)
				.validate());
	}

	public static MaterialBlockType getBlockType(int id) {
		return values()[id];
	}

	public static MaterialBlockType getForVolume(int volume) {
		for (MaterialBlockType blockType : values()) {
			if (blockType.getVolume() == volume)
				return blockType;
		}
		return null;
	}

	public static MaterialBlockType getBestForVolume(int volume) {
		for (MaterialBlockType blockType : values()) {
			if (blockType.getVolume() <= volume)
				return blockType;
		}
		return null;
	}

	private String name;
	private ResourceLocation baseModel;
	private List<MaterialRecipeBuilder> allwaysRecipes;
	private List<MaterialRecipeBuilder> materialRecipes;
	private int volume = 0;

	private MaterialBlockType(String name, String modelLocation, int volume) {
		this.name = name;
		baseModel = new ResourceLocation(modelLocation);
		allwaysRecipes = Lists.newArrayList();
		materialRecipes = Lists.newArrayList();
		this.volume = volume;
	}

	public ResourceLocation baseModel() {
		return baseModel;
	}

	public ResourceLocation baseModel(IBlockState state) {
		if (this == STEP) {
			if (BlockProperties.getVertical(state)) {
				return new ResourceLocation("buildingbricks:block/step_vertical");
			}
		} else if (this == WALL) {
			if (BlockProperties.getConnectionNorth(state)) {
				if (BlockProperties.getConnectionEast(state)) {
					if (BlockProperties.getConnectionSouth(state)) {
						if (BlockProperties.getConnectionWest(state)) {
							return new ResourceLocation("minecraft:block/wall_nsew");
						} else {
							return new ResourceLocation("minecraft:block/wall_nse");
						}
					} else {
						if (BlockProperties.getConnectionWest(state)) {
							return new ResourceLocation("minecraft:block/wall_nse");
						} else {
							return new ResourceLocation("minecraft:block/wall_ne");
						}
					}
				} else {
					if (BlockProperties.getConnectionSouth(state)) {
						if (BlockProperties.getConnectionWest(state)) {
							return new ResourceLocation("minecraft:block/wall_nse");
						} else {
							return new ResourceLocation("minecraft:block/wall_ns");
						}
					} else {
						if (BlockProperties.getConnectionWest(state)) {
							return new ResourceLocation("minecraft:block/wall_ne");
						} else {
							return new ResourceLocation("minecraft:block/wall_n");
						}
					}
				}
			} else {
				if (BlockProperties.getConnectionEast(state)) {
					if (BlockProperties.getConnectionSouth(state)) {
						if (BlockProperties.getConnectionWest(state)) {
							return new ResourceLocation("minecraft:block/wall_nse");
						} else {
							return new ResourceLocation("minecraft:block/wall_ne");
						}
					} else {
						if (BlockProperties.getConnectionWest(state)) {
							return new ResourceLocation("minecraft:block/wall_ns");
						} else {
							return new ResourceLocation("minecraft:block/wall_n");
						}
					}
				} else {
					if (BlockProperties.getConnectionSouth(state)) {
						if (BlockProperties.getConnectionWest(state)) {
							return new ResourceLocation("minecraft:block/wall_ne");
						} else {
							return new ResourceLocation("minecraft:block/wall_n");
						}
					} else {
						if (BlockProperties.getConnectionWest(state)) {
							return new ResourceLocation("minecraft:block/wall_n");
						} else {
							return baseModel;
						}
					}
				}
			}
		}
		return baseModel;
	}

	public Collection<IBlockState> getValidBlockStates() {
		Collection<IBlockState> states = new ArrayList<IBlockState>();
		for (BlockBuildingBricksBase block : MaterialBlockRegistry.instance
				.getBlocks()
				.get(this)
				.values()) {
			states.addAll(block.getBlockState().getValidStates());
		}
		return states;
	}

	public IModelState getModelStateFromBlockState(IBlockState state) {
		if (this == SLAB) {
			return getModelRotationFromFacing(BlockProperties.getFacing(state));
		} else if (this == STAIRS_FIXED_SIDE || this == MaterialBlockType.STAIRS_FIXED_CORNER
				|| this == MaterialBlockType.STAIRS_FIXED_INNER_CORNER) {
			return getModelStateStairs(BlockProperties.getRotation(state),
					BlockProperties.getHalf(state));
		} else if (this == STEP) {
			if (BlockProperties.getVertical(state)) {
				return getModelRotationVertical(BlockProperties.getRotation(state));
			} else {
				return getModelRotationFromFacing(BlockProperties.getRotation(state),
						BlockProperties.getHalf(state));
			}

		} else if (this == CORNER) {
			return getModelRotationFromFacing(BlockProperties.getRotation(state),
					BlockProperties.getHalf(state));
		} else if (this == WALL) {
			return getModelRotationFromConnections(state);
		} else {
			return null;
		}
	}

	private IModelState getModelRotationFromFacing(EnumFacing facing) {
		switch (facing) {
		default:
		case DOWN:
			return ModelRotation.X0_Y0;
		case UP:
			return ModelRotation.X180_Y0;
		case SOUTH:
			return ModelRotation.X90_Y0;
		case WEST:
			return ModelRotation.X90_Y90;
		case NORTH:
			return ModelRotation.X90_Y180;
		case EAST:
			return ModelRotation.X90_Y270;
		}
	}

	private IModelState getModelRotationVertical(EnumRotation rot) {
		return ModelRotation.getModelRotation(0, rot.getAngleDeg());
	}

	private IModelState getModelRotationFromFacing(EnumRotation rot, EnumBlockHalf half) {

		TRSRTransformation translate = new TRSRTransformation(
				(half == EnumBlockHalf.BOTTOM) ? null : new Vector3f(0.0f, 0.5f, 0.0f), null, null,
				null);
		return translate.compose(
				new TRSRTransformation(ModelRotation.getModelRotation(0, rot.getAngleDeg())));
	}

	private IModelState getModelStateStairs(EnumRotation rot, EnumBlockHalf half) {

		TRSRTransformation translate = new TRSRTransformation(
				(half == EnumBlockHalf.BOTTOM) ? null : new Vector3f(0.0f, 0.5f, 0.0f), null, null,
				null);
		return translate.compose(
				new TRSRTransformation(ModelRotation.getModelRotation(0, rot.getAngleDeg() - 90)));
	}

	private IModelState getModelRotationFromConnections(IBlockState state) {
		if (BlockProperties.getConnectionNorth(state)) {
			if (BlockProperties.getConnectionEast(state)) {
				if (BlockProperties.getConnectionSouth(state)) {
					if (BlockProperties.getConnectionWest(state)) {
						return ModelRotation.X0_Y0;
					} else {
						return ModelRotation.X0_Y0;
					}
				} else {
					if (BlockProperties.getConnectionWest(state)) {
						return ModelRotation.X0_Y270;
					} else {
						return ModelRotation.X0_Y0;
					}
				}
			} else {
				if (BlockProperties.getConnectionSouth(state)) {
					if (BlockProperties.getConnectionWest(state)) {
						return ModelRotation.X0_Y180;
					} else {
						return ModelRotation.X0_Y0;
					}
				} else {
					if (BlockProperties.getConnectionWest(state)) {
						return ModelRotation.X0_Y270;
					} else {
						return ModelRotation.X0_Y0;
					}
				}
			}
		} else {
			if (BlockProperties.getConnectionEast(state)) {
				if (BlockProperties.getConnectionSouth(state)) {
					if (BlockProperties.getConnectionWest(state)) {
						return ModelRotation.X0_Y90;
					} else {
						return ModelRotation.X0_Y90;
					}
				} else {
					if (BlockProperties.getConnectionWest(state)) {
						return ModelRotation.X0_Y90;

					} else {
						return ModelRotation.X0_Y90;
					}
				}
			} else {
				if (BlockProperties.getConnectionSouth(state)) {
					if (BlockProperties.getConnectionWest(state)) {
						return ModelRotation.X0_Y180;
					} else {
						return ModelRotation.X0_Y180;
					}
				} else {
					if (BlockProperties.getConnectionWest(state)) {
						return ModelRotation.X0_Y270;
					} else {
						return ModelRotation.X0_Y0;
					}
				}
			}
		}
	}

	public String getName() {
		return name;
	}

	private void addRecipe(boolean allways, MaterialRecipeBuilder recipe) {
		if (allways) {
			allwaysRecipes.add(recipe);
		} else {
			materialRecipes.add(recipe);
		}
	}

	public void addRecipes(Material mat) {
		for (MaterialRecipeBuilder recipeDesc : allwaysRecipes) {
			GameRegistry.addShapedRecipe(recipeDesc.buildOutput(mat, this), recipeDesc.build(mat));
		}
		if (MaterialBlockRegistry.instance.getAllBlocks().contains(mat.getBlock(this).getBlock())) {
			for (MaterialRecipeBuilder recipeDesc : materialRecipes) {
				GameRegistry.addShapedRecipe(recipeDesc.buildOutput(mat, this),
						recipeDesc.build(mat));

			}
		}
	}

	public int getVolume() {
		return volume;
	}
}
