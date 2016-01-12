package com.hea3ven.buildingbricks.core;

import java.util.Map.Entry;
import java.util.function.Supplier;

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

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import com.hea3ven.buildingbricks.core.blocks.BlockPortableLadder;
import com.hea3ven.buildingbricks.core.client.gui.GuiTrowel;
import com.hea3ven.buildingbricks.core.items.ItemTrowel;
import com.hea3ven.buildingbricks.core.materials.*;
import com.hea3ven.buildingbricks.core.network.TrowelRotateBlockTypeMessage;
import com.hea3ven.buildingbricks.core.tileentity.TileMaterial;
import com.hea3ven.tools.commonutils.inventory.ISimpleGuiHandler;
import com.hea3ven.tools.commonutils.mod.ProxyModBase;

public class ProxyCommonBuildingBricks extends ProxyModBase {

	public ProxyCommonBuildingBricks() {
		super(Properties.MODID);

		ModBuildingBricks.trowel = new ItemTrowel();
		ModBuildingBricks.portableLadder =
				(BlockPortableLadder) new BlockPortableLadder().setUnlocalizedName("portableLadder");
	}

	@Override
	public void onInitEvent() {
		MaterialBlockRegistry.instance.logStats();
		MaterialRegistry.logStats();

		super.onInitEvent();
	}

	public <T extends Block> void addMaterialBlock(T block, Class<? extends ItemBlock> itemCls, String name) {
		addBlock(block, name, itemCls);
	}

	@Override
	protected void registerBlocks() {
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
}
