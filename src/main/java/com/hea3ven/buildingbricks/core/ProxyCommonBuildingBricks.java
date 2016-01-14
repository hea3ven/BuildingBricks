package com.hea3ven.buildingbricks.core;

import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.function.Supplier;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.config.Property.Type;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import com.hea3ven.buildingbricks.compat.vanilla.GrassSlabWorldGen;
import com.hea3ven.buildingbricks.core.blocks.BlockPortableLadder;
import com.hea3ven.buildingbricks.core.client.gui.GuiTrowel;
import com.hea3ven.buildingbricks.core.client.settings.TrowelKeyBindings;
import com.hea3ven.buildingbricks.core.items.ItemTrowel;
import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.buildingbricks.core.materials.loader.MaterialResourceLoader;
import com.hea3ven.buildingbricks.core.materials.mapping.IdMappingLoader;
import com.hea3ven.buildingbricks.core.network.TrowelRotateBlockTypeMessage;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;
import com.hea3ven.tools.commonutils.inventory.ISimpleGuiHandler;
import com.hea3ven.tools.commonutils.mod.ProxyModBase;
import com.hea3ven.tools.commonutils.mod.config.ConfigManager;
import com.hea3ven.tools.commonutils.mod.config.ConfigManagerBuilder;
import com.hea3ven.tools.commonutils.mod.config.DirectoryConfigManagerBuilder;
import com.hea3ven.tools.commonutils.mod.config.FileConfigManagerBuilder;

public class ProxyCommonBuildingBricks extends ProxyModBase {

	public ProxyCommonBuildingBricks() {
		super(Properties.MODID);

		ModBuildingBricks.trowel = new ItemTrowel();
		ModBuildingBricks.portableLadder =
				(BlockPortableLadder) new BlockPortableLadder().setUnlocalizedName("portableLadder");
	}

	@Override
	public void onInitEvent(FMLInitializationEvent event) {
		MaterialBlockRegistry.instance.logStats();
		MaterialRegistry.logStats();

		super.onInitEvent(event);
	}

	public <T extends Block> void addMaterialBlock(T block, Class<? extends ItemBlock> itemCls, String name) {
		addBlock(block, name, itemCls);
	}

	@Override
	public void registerConfig() {
		addConfigManager(new DirectoryConfigManagerBuilder().setDirName("BuildingBricks")
				.addFile(new FileConfigManagerBuilder().setFileName("general.cfg")
						.setDesc("Building Bricks Configuration")
						.addCategory("world")
						.addValue("generateGrassSlabs", "true", Type.BOOLEAN,
								"Enable to generate grass slabs in the world to smooth out the surface",
								GrassSlabWorldGen.get())
						.endCategory())
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
		MaterialResourceLoader.loadResources(ModBuildingBricks.resScanner, Properties.MODID);

		addBlock(ModBuildingBricks.portableLadder, "portable_ladder",
				BlockPortableLadder.ItemPortableLadder.class);
	}

	@Override
	protected void registerTileEntities() {
		addTileEntity(TileMaterial.class, "tile.material");
	}

	@Override
	protected void registerItems() {
		addItem(ModBuildingBricks.trowel, "trowel");
	}

	@Override
	protected void registerCreativeTabs() {
		addCreativeTab("buildingBricks", new Supplier<Item>() {
			@Override
			public Item get() {
				return ModBuildingBricks.trowel;
			}
		});
		ModBuildingBricks.trowel.setCreativeTab(getCreativeTab("buildingBricks"));
		ModBuildingBricks.portableLadder.setCreativeTab(getCreativeTab("buildingBricks"));
		for (Block block : MaterialBlockRegistry.instance.getAllBlocks()) {
			block.setCreativeTab(getCreativeTab("buildingBricks"));
		}
	}

	@Override
	public void registerRecipes() {
		addRecipe(ModBuildingBricks.portableLadder, "x x", "xxx", "x x", 'x', "ingotIron");
		addRecipe(ModBuildingBricks.trowel, " is", "ii ", 's', "stickWood", 'i', "ingotIron");

		ModBuildingBricks.logger.info("Registering trowel's recipes");
		for (Material mat : MaterialRegistry.getAll()) {
			for (BlockDescription blockDesc : mat.getBlockRotation().getAll().values()) {
				ItemStack trowelStack = ModBuildingBricks.trowel.createStack();
				trowelStack.setItemDamage(OreDictionary.WILDCARD_VALUE);
				ItemStack bindedTrowelStack = ModBuildingBricks.trowel.createStack(mat);
				// TODO: prevent recipe from consuming the block
				addRecipe(true, bindedTrowelStack, trowelStack, blockDesc.getStack());
			}
		}

		ModBuildingBricks.logger.info("Registering materials recipes");
		for (Material mat : MaterialRegistry.getAll()) {
			for (Entry<MaterialBlockType, BlockDescription> entry : mat.getBlockRotation()
					.getAll()
					.entrySet()) {
				for (IRecipe recipe : entry.getKey().registerRecipes(mat)) {
					addRecipe(recipe);
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
			public Gui createGui(EntityPlayer player, World world, BlockPos pos) {
				return new GuiTrowel(player, player.getCurrentEquippedItem());
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
}
