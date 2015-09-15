package com.hea3ven.transition.m.block.properties;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import com.hea3ven.transition.m.util.EnumFacing;

public class PropertyDirection implements IProperty {

	public static PropertyDirection create(String name) {
		return create(name, Predicates.<EnumFacing> alwaysTrue());
	}

	public static PropertyDirection create(String string, Predicate<EnumFacing> filter) {
		return new PropertyDirection();
	}

}
