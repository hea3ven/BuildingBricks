package com.hea3ven.buildingbricks.core.items.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;

public class CreativeTabBuildingBricks extends CreativeTabs {

	private static final CreativeTabBuildingBricks instance = new CreativeTabBuildingBricks();

	public static CreativeTabs get() {
		return instance;
	}

	public CreativeTabBuildingBricks() {
		super("buildingBricks");
	}

	@Override
	public Item getTabIconItem() {
		return ModBuildingBricks.trowel;
	}

}
