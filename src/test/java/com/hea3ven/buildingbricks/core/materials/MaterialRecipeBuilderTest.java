package com.hea3ven.buildingbricks.core.materials;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

@Ignore
public class MaterialRecipeBuilderTest {

	@Test()
	public void testValid() {
		MaterialRecipeBuilder.create().pattern("a").map('a', MaterialBlockType.FULL).validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testErrorWhenNoPattern() {
		MaterialRecipeBuilder.create().validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testErrorWhenMissingMapping() {
		MaterialRecipeBuilder.create().pattern("a").validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testErrorWhenExtraMapping() {
		MaterialRecipeBuilder
				.create()
				.pattern("a")
				.map('a', MaterialBlockType.FULL)
				.map('b', MaterialBlockType.FULL)
				.validate();
	}

	@Test
	public void testBuild() {
		Block stubBlock = new Block(net.minecraft.block.material.Material.air) {
		};
		Item stubItem = new ItemBlock(stubBlock);
		final ItemStack stack = new ItemStack(stubItem);
		Material stubMat = new Material("test");
		stubMat.addBlock(new BlockDescription(MaterialBlockType.FULL, stubBlock) {
			@Override
			public ItemStack getStack() {
				return stack;
			}
		});
		Object[] result = MaterialRecipeBuilder
				.create()
				.pattern("a")
				.map('a', MaterialBlockType.FULL)
				.build(stubMat);

		assertArrayEquals(new Object[] {"a", 'a', stack}, result);
	}

	@Test
	public void testBuildOutputDefaultsToOne() {
		Block stubBlock = new Block(net.minecraft.block.material.Material.air) {
		};
		Item stubItem = new ItemBlock(stubBlock);
		final ItemStack stack = new ItemStack(stubItem);
		Material stubMat = new Material("test");
		stubMat.addBlock(new BlockDescription(MaterialBlockType.FULL, stubBlock) {
			@Override
			public ItemStack getStack() {
				return stack;
			}
		});
		ItemStack result = MaterialRecipeBuilder.create().buildOutput(stubMat,
				MaterialBlockType.FULL);

		assertEquals(1, result.stackSize);
	}
}
