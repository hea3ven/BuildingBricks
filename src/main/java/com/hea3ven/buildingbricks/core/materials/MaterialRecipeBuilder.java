package com.hea3ven.buildingbricks.core.materials;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.common.primitives.Chars;

import net.minecraft.item.ItemStack;

public class MaterialRecipeBuilder {

	private interface Ingredient {

		Object getStack(Material mat);
	}

	private class MaterialIngredient implements Ingredient {

		private MaterialBlockType blockType;

		public MaterialIngredient(MaterialBlockType blockType) {
			this.blockType = blockType;
		}

		@Override
		public Object getStack(Material mat) {
			BlockDescription block = mat.getBlock(blockType);
			return block != null ? block.getStack() : null;
		}

	}

	public static MaterialRecipeBuilder create() {
		return new MaterialRecipeBuilder();
	}

	private int output = 1;
	private String[] pattern;
	private Map<Character, Ingredient> mapping = Maps.newHashMap();

	public MaterialRecipeBuilder outputAmount(int output) {
		this.output = output;
		return this;
	}

	public MaterialRecipeBuilder pattern(String... pattern) {
		this.pattern = pattern;
		return this;
	}

	public MaterialRecipeBuilder map(char key, MaterialBlockType blockType) {
		mapping.put(key, new MaterialIngredient(blockType));
		return this;
	}

	public MaterialRecipeBuilder validate() {
		if (pattern == null)
			throw new IllegalStateException("missing pattern");
		HashSet<Character> keys = new HashSet<Character>();
		for (String pat : pattern) {
			keys.addAll(Chars.asList(pat.toCharArray()));
		}

		for (Character c : keys) {
			if (!mapping.containsKey(c) && c != ' ')
				throw new IllegalStateException("missing mapping for " + c);
		}
		for (Character c : mapping.keySet()) {
			if (!keys.contains(c))
				throw new IllegalStateException("extra mapping for " + c);
		}
		return this;
	}

	public ItemStack buildOutput(Material mat, MaterialBlockType blockType) {
		ItemStack output = mat.getBlock(blockType).getStack().copy();
		output.stackSize = this.output;
		return output;
	}

	public Object[] build(Material mat) {
		ArrayList<Object> recipe = new ArrayList<Object>();
		for (String pat : pattern) {
			recipe.add(pat);
		}
		for (Entry<Character, Ingredient> entry : mapping.entrySet()) {
			Object stack = entry.getValue().getStack(mat);
			if (stack == null)
				return null;

			recipe.add(entry.getKey());
			recipe.add(stack);
		}
		return recipe.toArray();
	}

}
