package com.hea3ven.buildingbricks.core.materials;

import org.junit.Before;
import org.junit.Test;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.hea3ven.buildingbricks.core.materials.MaterialBlockRecipes.MaterialBlockRecipeBuilder;
import static org.junit.Assert.*;

public class MaterialBlockRecipesTest {

	private Block stubBlock;
	private ItemBlock stubItem;
	private Material stubMat;

	class MaterialBlockRecipeBuilderWrapper extends MaterialBlockRecipeBuilder {
		@Override
		protected boolean isOreDict(String ingredient) {
			return ingredient.equals("logWood");
		}
	}

	@Before
	public void setUp() {
		stubBlock = new Block(net.minecraft.block.material.Material.air) {
		};
		stubItem = new ItemBlock(stubBlock);
		final ItemStack stack = new ItemStack(stubItem);
		stubMat = new Material("test");
		stubMat.addBlock(new BlockDescription(MaterialBlockType.FULL, stubBlock, 0, null, null) {
			@Override
			public ItemStack getStack() {
				return stack;
			}
		});
	}

	@Test()
	public void testValid() {
		new MaterialBlockRecipeBuilderWrapper().ingredients("a", "a", "FULL")
				.output(MaterialBlockType.FULL)
				.bind(stubMat)
				.build();
	}

	@Test(expected = IllegalStateException.class)
	public void testErrorWhenNoPattern() {
		new MaterialBlockRecipeBuilderWrapper().output(MaterialBlockType.FULL)
				.bind(stubMat).build();
	}

	@Test(expected = IllegalStateException.class)
	public void testErrorWhenMissingMapping() {
		new MaterialBlockRecipeBuilderWrapper().ingredients("a").output(MaterialBlockType.FULL)
				.bind(stubMat).build();
	}

	@Test(expected = IllegalStateException.class)
	public void testErrorWhenExtraMapping() {
		new MaterialBlockRecipeBuilderWrapper()
				.ingredients("a", "a", "FULL", "b", "FULL").output(MaterialBlockType.FULL)
				.bind(stubMat)
				.build();
	}

	@Test(expected = IllegalStateException.class)
	public void testErrorWhenMappingIsNotOneCharacter() {
		new MaterialBlockRecipeBuilderWrapper()
				.ingredients("a", "a", "FULL", "bb", "FULL").output(MaterialBlockType.FULL)
				.bind(stubMat)
				.build();
	}

	@Test
	public void testBuild() {

		IRecipe result =
				new MaterialBlockRecipeBuilderWrapper().ingredients("a", "a", "FULL")
						.output(MaterialBlockType.FULL)
						.bind(stubMat)
						.build();

		assertTrue(result instanceof ShapedOreRecipe);
		assertEquals(stubItem, result.getRecipeOutput().getItem());
		assertEquals(1, result.getRecipeOutput().stackSize);
		assertItemArrayEquals(new Object[] {new ItemStack(stubItem)}, ((ShapedOreRecipe) result).getInput());
	}

	@Test
	public void testBuildShape() {
		IRecipe result =
				new MaterialBlockRecipeBuilderWrapper().ingredients("aa", "a ", "a", "FULL")
						.output(MaterialBlockType.FULL)
						.bind(stubMat)
						.build();

		assertTrue(result instanceof ShapedOreRecipe);
		assertEquals(stubItem, result.getRecipeOutput().getItem());
		assertEquals(1, result.getRecipeOutput().stackSize);
		ItemStack stack = new ItemStack(stubItem);
		assertItemArrayEquals(new Object[] {stack, stack, stack, null},
				((ShapedOreRecipe) result).getInput());
	}

	@Test
	public void testOreDictIngredient() {

		try {
			new MaterialBlockRecipeBuilderWrapper().ingredients("a", "a", "logWood")
					.output(MaterialBlockType.FULL)
					.bind(stubMat)
					.build();
		} catch (Throwable e) {
			if (!e.getCause().getMessage().equals("Accessed Blocks before Bootstrap!"))
				fail();
		}
	}

	@Test(expected = IllegalStateException.class)
	public void testNotOreDictIngredient() {
		new MaterialBlockRecipeBuilderWrapper().ingredients("a", "a", "logWoodBad")
				.output(MaterialBlockType.FULL)
				.bind(stubMat)
				.build();
	}

	@Test()
	public void testShapeless() {
		IRecipe result = new MaterialBlockRecipeBuilderWrapper().ingredients("FULL")
				.shaped(false)
				.output(MaterialBlockType.FULL)
				.bind(stubMat)
				.build();

		assertTrue(result instanceof ShapelessOreRecipe);
		assertEquals(stubItem, result.getRecipeOutput().getItem());
		assertEquals(1, result.getRecipeOutput().stackSize);
		assertItemArrayEquals(new Object[] {new ItemStack(stubItem)},
				((ShapelessOreRecipe) result).getInput().toArray());
	}

	private void assertItemArrayEquals(Object[] expected, Object[] actual) {
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) {
			if (expected[i] == null)
				assertNull(actual[i]);
			else {
				assertEquals(expected[i].getClass(), actual[i].getClass());
				if (expected[i] instanceof ItemStack)
					assertTrue(ItemStack.areItemStacksEqual((ItemStack) expected[i], (ItemStack) actual[i]) &&
							ItemStack.areItemStackTagsEqual((ItemStack) expected[i], (ItemStack) actual[i]));
			}
		}
	}
}
