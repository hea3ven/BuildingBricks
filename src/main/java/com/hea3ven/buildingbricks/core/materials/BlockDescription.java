package com.hea3ven.buildingbricks.core.materials;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;

import com.hea3ven.buildingbricks.core.materials.MaterialBlockRecipes.MaterialBlockRecipeBuilder;

public class BlockDescription {

	private String blockName;
	private Block block;
	private ItemStack stack;
	private MaterialBlockType type;
	private int meta;
	@Nonnull
	private Map<String, NBTBase> tags = new HashMap<>();
	@Nonnull
	private List<MaterialBlockRecipeBuilder> recipes = new ArrayList<>();

	private boolean stackInit = false;

	public BlockDescription(MaterialBlockType type, Block block, int metadata,
			@Nonnull Map<String, NBTBase> tags, @Nonnull List<MaterialBlockRecipeBuilder> recipes) {
		this.type = type;
		this.block = block;
		this.meta = metadata;
		if (tags != null)
			this.tags = tags;
		if (recipes != null)
			this.recipes = recipes;
	}

	public BlockDescription(MaterialBlockType type, String blockName, int metadata,
			@Nonnull Map<String, NBTBase> tags, @Nonnull List<MaterialBlockRecipeBuilder> recipes) {
		this(type, (Block) null, metadata, tags, recipes);
		this.blockName = blockName;
	}

	public BlockDescription(MaterialBlockType type, List<MaterialBlockRecipeBuilder> recipes) {
		this(type, (String) null, 0, null, recipes);
	}

	public ItemBlock getItem() {
		return (ItemBlock) getStack().getItem();
	}

	public ItemStack getStack() {
		if (!stackInit) {
			stackInit = true;
			Item item = Item.getItemFromBlock(getBlock());
			if (item != null) {
				stack = new ItemStack(item, 1, meta);
				for (Entry<String, NBTBase> entry : tags.entrySet()) {
					stack.setTagInfo(entry.getKey(), entry.getValue());
				}
			}
		}
		return stack;
	}

	boolean isBlockTemplate() {
		return block == null && blockName == null;
	}

	void setBlock(Block block, int meta, Map<String, NBTBase> tags) {
		this.block = block;
		this.meta = meta;
		if (tags != null)
			this.tags = tags;
	}

	public Block getBlock() {
		if (block == null) {
			block = Block.getBlockFromName(blockName);
		}
		return block;
	}

	public MaterialBlockType getType() {
		return type;
	}

	public List<MaterialBlockRecipeBuilder> getRecipes() {
		return recipes;
	}
}
