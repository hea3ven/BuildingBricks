package com.hea3ven.buildingbricks.core;

import javax.vecmath.Vector3f;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

import com.hea3ven.buildingbricks.core.blocks.BlockPortableLadder;
import com.hea3ven.buildingbricks.core.blocks.base.BlockBuildingBricks;
import com.hea3ven.buildingbricks.core.blocks.placement.BlockPlacementManager;
import com.hea3ven.buildingbricks.core.client.ModelBakerBlockMaterial;
import com.hea3ven.buildingbricks.core.client.ModelBakerItemMaterial;
import com.hea3ven.buildingbricks.core.client.gui.GuiMaterialBag;
import com.hea3ven.buildingbricks.core.client.gui.GuiTrowel;
import com.hea3ven.buildingbricks.core.client.settings.TrowelKeyBindings;
import com.hea3ven.buildingbricks.core.command.CommandCreateMaterial;
import com.hea3ven.buildingbricks.core.eventhandlers.EventHandlerShowMaterialTooltip;
import com.hea3ven.buildingbricks.core.eventhandlers.EventHandlerTrowelOverlay;
import com.hea3ven.buildingbricks.core.items.ItemMaterialBag;
import com.hea3ven.buildingbricks.core.items.ItemTrowel;
import com.hea3ven.buildingbricks.core.items.crafting.RecipeBindTrowel;
import com.hea3ven.buildingbricks.core.items.crafting.RecipeBlockMaterial;
import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockRecipes.MaterialBlockRecipeBuilder;
import com.hea3ven.buildingbricks.core.materials.loader.MaterialResourceLoader;
import com.hea3ven.buildingbricks.core.materials.mapping.IdMappingLoader;
import com.hea3ven.buildingbricks.core.network.TrowelRotateBlockTypeMessage;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;
import com.hea3ven.tools.commonutils.client.renderer.color.IColorHandler;
import com.hea3ven.tools.commonutils.inventory.ISimpleGuiHandler;
import com.hea3ven.tools.commonutils.mod.ProxyModComposite;
import com.hea3ven.tools.commonutils.mod.config.ConfigManager;
import com.hea3ven.tools.commonutils.mod.config.ConfigManagerBuilder;
import com.hea3ven.tools.commonutils.mod.config.DirectoryConfigManagerBuilder;
import com.hea3ven.tools.commonutils.mod.config.FileConfigManagerBuilder;
import com.hea3ven.tools.commonutils.util.ConfigurationUtil;
import com.hea3ven.tools.commonutils.util.SidedCall;
import com.hea3ven.tools.commonutils.util.SidedCall.SidedRunnable;

public class ProxyCommonBuildingBricks extends ProxyModComposite {

	public ProxyCommonBuildingBricks() {
		super(ModBuildingBricks.MODID);

		addModule("compatvanilla",
				"com.hea3ven.buildingbricks.compat.vanilla.ProxyModBuildingBricksCompatVanilla");

		ModBuildingBricks.trowel = (ItemTrowel) new ItemTrowel().
				setUnlocalizedName("trowel");
		ModBuildingBricks.materialBag =
				(ItemMaterialBag) new ItemMaterialBag().setUnlocalizedName("materialBag");
		ModBuildingBricks.portableLadder =
				(BlockPortableLadder) new BlockPortableLadder().setUnlocalizedName("portableLadder");
	}

	@Override
	public void onPreInitEvent(FMLPreInitializationEvent event) {
		super.onPreInitEvent(event);

		MaterialBlockRegistry.instance.generateBlocks(this);
	}

	@Override
	public void onInitEvent(FMLInitializationEvent event) {
		MaterialBlockRegistry.instance.logStats();
		MaterialRegistry.logStats();

		super.onInitEvent(event);

		RecipeSorter.register("buildingbricks:trowelbind", RecipeBindTrowel.class, Category.SHAPELESS,
				"after:minecraft:shapeless");
		RecipeSorter.register("buildingbricks:blockmaterial", RecipeBlockMaterial.class, Category.SHAPED,
				"after:minecraft:shaped");
		MinecraftForge.EVENT_BUS.register(ModBuildingBricks.materialBag);
		MinecraftForge.EVENT_BUS.register(BlockPlacementManager.getInstance());
		SidedCall.run(Side.CLIENT, new SidedRunnable() {
			@Override
			@SideOnly(Side.CLIENT)
			public void run() {
				MinecraftForge.EVENT_BUS.register(new EventHandlerTrowelOverlay());
			}
		});
	}

	@Override
	public void registerConfig() {
		addConfigManager(new DirectoryConfigManagerBuilder().setDirName("BuildingBricks")
				.addFile(new FileConfigManagerBuilder().setFileName("general.cfg")
						.setDesc("Building Bricks Configuration")
						.addCategory("general")
						.addValue("generateBlocks", "true", Type.BOOLEAN,
								"Enable to generate the missing blocks for the materials",
								new Consumer<Property>() {
									@Override
									public void accept(Property property) {
										MaterialBlockRegistry.instance.enableGenerateBlocks =
												property.getBoolean();
									}
								}, true, true)
						.addValue("blocksInCreative", "true", Type.BOOLEAN,
								"Enable to add all the generated blocks to the creative menu",
								new Consumer<Property>() {
									@Override
									public void accept(Property property) {
										TileMaterial.blocksInCreative = property.getBoolean();
									}
								}, false, false)
						.addValue("trowelsInCreative", "true", Type.BOOLEAN,
								"Enable to add binded trowels for each material to the creative menu",
								new Consumer<Property>() {
									@Override
									public void accept(Property property) {
										ItemTrowel.trowelsInCreative = property.getBoolean();
									}
								}, false, false)
						.addValue("showMaterialInTooltip", "true", Type.BOOLEAN,
								"Display the material on the blocks' tooltips", new Consumer<Property>() {
									@Override
									public void accept(Property property) {
										if(property.getBoolean())
											MinecraftForge.EVENT_BUS.register(EventHandlerShowMaterialTooltip.getInstance());
										else
											MinecraftForge.EVENT_BUS.unregister(EventHandlerShowMaterialTooltip.getInstance());
									}
								})
						.endCategory()
						.addCategory("compat")
						.add(this.getModule("compatvanilla").getConfig())
						.endCategory()
						.Update(new Consumer<Configuration>() {
							@Override
							public void accept(Configuration cfg) {
								ConfigCategory worldCat = cfg.getCategory("world");
								ConfigCategory generalCat = cfg.getCategory("general");

								ConfigCategory vanillaCompatCat =
										ConfigurationUtil.getSubCategory(cfg.getCategory("compat"),
												"vanilla");
								if (worldCat.containsKey("generateGrassSlabs"))
									vanillaCompatCat.get("generateGrassSlabs")
											.set(worldCat.get("generateGrassSlabs").getBoolean());
								if (generalCat.containsKey("replaceGrassTexture")) {
									vanillaCompatCat.get("replaceGrassTexture")
											.set(generalCat.get("replaceGrassTexture").getBoolean());
									vanillaCompatCat.get("replaceGrassTextureForce")
											.set(generalCat.get("replaceGrassTexture").getBoolean());
								}

								cfg.removeCategory(worldCat);
								generalCat.remove("replaceGrassTexture");
							}
						}))
				.addFile(new ConfigManagerBuilder() {
					@Override
					public ConfigManager build(String modId, Path path) {
						IdMappingLoader.init(path.resolve("material_ids.nbt"));
						return null;
					}
				}));
	}

	@Override
	protected void registerBlocks() {
		MaterialResourceLoader.loadResources(ModBuildingBricks.resScanner);

		addBlock(ModBuildingBricks.portableLadder, "portable_ladder",
				new BlockPortableLadder.ItemPortableLadder(ModBuildingBricks.portableLadder));
	}

	@Override
	protected void registerTileEntities() {
		addTileEntity(TileMaterial.class, "tile.material");
	}

	@Override
	protected void registerItems() {
		addItem(ModBuildingBricks.trowel, "trowel");
		addItem(ModBuildingBricks.materialBag, "material_bag");
	}

	@Override
	protected void registerCreativeTabs() {
		addCreativeTab("buildingBricks", ModBuildingBricks.trowel);
		CreativeTabs tab = getCreativeTab("buildingBricks");
		ModBuildingBricks.trowel.setCreativeTab(tab);
		ModBuildingBricks.materialBag.setCreativeTab(tab);
		ModBuildingBricks.portableLadder.setCreativeTab(tab);
		for (Block block : MaterialBlockRegistry.instance.getAllBlocks()) {
			block.setCreativeTab(tab);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void registerModelBakers() {
		addModelBaker(new ModelBakerBlockMaterial());
		addModelBaker(
				new ModelBakerItemMaterial("buildingbricks:item/trowel", "buildingbricks:trowel#inventory",
						new Vector3f(0.3f, 0.0625f, 0.125f), new Vector3f(0.4f, 0.4f, 0.4f)));
		addModelBaker(new ModelBakerItemMaterial("buildingbricks:item/material_bag",
				"buildingbricks:material_bag#inventory", new Vector3f(0.25f, 0.3f, 0.4375f /* 0.125f*/),
				new Vector3f(0.5f, 0.5f, 0.125001f)));
	}

	@SideOnly(Side.CLIENT)
	protected void registerColors() {
		List<Block> blocksWithItems = new ArrayList<>();
		List<Block> blocksWithoutItems = new ArrayList<>();
		for (Block block : MaterialBlockRegistry.instance.getAllBlocks()) {
			if (Item.getItemFromBlock(block) != null)
				blocksWithItems.add(block);
			else
				blocksWithoutItems.add(block);
		}
		addColors(new IColorHandler() {
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
				return ((BlockBuildingBricks) state.getBlock()).getBlockLogic()
						.colorMultiplier(world, pos, tintIndex);
			}

			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex) {
				Material mat = MaterialStack.get(stack);
				if (mat == null)
					return 0;
				else
					return Minecraft.getMinecraft()
							.getItemColors()
							.getColorFromItemstack(mat.getFirstBlock().getStack(), tintIndex);
			}
		}, blocksWithItems);
		addBlockColors(new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
				return ((BlockBuildingBricks) state.getBlock()).getBlockLogic()
						.colorMultiplier(world, pos, tintIndex);
			}
		}, blocksWithoutItems);
	}

	@Override
	public void registerRecipes() {
		addRecipe(ModBuildingBricks.portableLadder, "x x", "xxx", "x x", 'x', "ingotIron");
		addRecipe(ModBuildingBricks.trowel, " is", "ii ", 's', "stickWood", 'i', "ingotIron");

		ModBuildingBricks.logger.info("Registering trowel's recipes");
		for (Material mat : MaterialRegistry.getAll()) {
			for (BlockDescription blockDesc : mat.getBlockRotation().getAll().values()) {
				if (blockDesc.getStack() != null)
					addRecipe(new RecipeBindTrowel(mat, blockDesc.getStack()));
			}
		}

		ModBuildingBricks.logger.info("Registering material blocks recipes");
		for (Material mat : MaterialRegistry.getAll()) {
			for (Entry<MaterialBlockType, BlockDescription> entry : mat.getBlockRotation()
					.getAll()
					.entrySet()) {
				for (MaterialBlockRecipeBuilder builder : entry.getValue().getRecipes()) {
					addRecipe(builder.output(entry.getKey()).bind(mat).build());
				}
			}
		}
	}

	@Override
	public void registerGuis() {
		addGui(GuiTrowel.ID, new ISimpleGuiHandler() {
			@Override
			public Container createContainer(EntityPlayer player, World world, BlockPos pos) {
				return ModBuildingBricks.trowel.getContainer(player);
			}

			@Override
			@SideOnly(Side.CLIENT)
			public Gui createGui(EntityPlayer player, World world, BlockPos pos) {
				for (ItemStack stack : player.getHeldEquipment()) {
					if (stack.getItem() == ModBuildingBricks.trowel)
						return new GuiTrowel(player, stack);
				}
				throw new RuntimeException();
			}
		});
		addGui(GuiMaterialBag.ID, new ISimpleGuiHandler() {
			@Override
			public Container createContainer(EntityPlayer player, World world, BlockPos pos) {
				return ModBuildingBricks.materialBag.getContainer(player);
			}

			@Override
			@SideOnly(Side.CLIENT)
			public Gui createGui(EntityPlayer player, World world, BlockPos pos) {
				return new GuiMaterialBag(ModBuildingBricks.materialBag.getContainer(player));
			}
		});
	}

	@Override
	protected void registerNetworkPackets() {
		addNetworkPacket(TrowelRotateBlockTypeMessage.Handler.class, TrowelRotateBlockTypeMessage.class, 0,
				Side.SERVER);
	}

	@Override
	protected void registerKeyBindings() {
		addKeyBinding("key.trowel.prev", Keyboard.KEY_J, "key.trowel", TrowelKeyBindings.getOnTrowelPrev());
		addKeyBinding("key.trowel.next", Keyboard.KEY_K, "key.trowel", TrowelKeyBindings.getOnTrowelNext());
		addItemScrollWheelBinding(ModBuildingBricks.trowel, TrowelKeyBindings.getOnTrowelScroll());
		addItemKeyBinding(ModBuildingBricks.trowel, "key.trowel.selectFull", Keyboard.KEY_NUMPAD0,
				"key.trowel", TrowelKeyBindings.getOnTrowelSelect(MaterialBlockType.FULL));
		addItemKeyBinding(ModBuildingBricks.trowel, "key.trowel.selectStairs", Keyboard.KEY_NUMPAD1,
				"key.trowel", TrowelKeyBindings.getOnTrowelSelect(MaterialBlockType.STAIRS));
		addItemKeyBinding(ModBuildingBricks.trowel, "key.trowel.selectSlab", Keyboard.KEY_NUMPAD2,
				"key.trowel", TrowelKeyBindings.getOnTrowelSelect(MaterialBlockType.SLAB));
		addItemKeyBinding(ModBuildingBricks.trowel, "key.trowel.selectVertSlab", Keyboard.KEY_NUMPAD3,
				"key.trowel", TrowelKeyBindings.getOnTrowelSelect(MaterialBlockType.VERTICAL_SLAB));
		addItemKeyBinding(ModBuildingBricks.trowel, "key.trowel.selectStep", Keyboard.KEY_NUMPAD4,
				"key.trowel", TrowelKeyBindings.getOnTrowelSelect(MaterialBlockType.STEP));
		addItemKeyBinding(ModBuildingBricks.trowel, "key.trowel.selectCorner", Keyboard.KEY_NUMPAD5,
				"key.trowel", TrowelKeyBindings.getOnTrowelSelect(MaterialBlockType.CORNER));
	}

	@Override
	protected void registerCommands() {
		addCommand(new CommandCreateMaterial());
	}
}
