package com.hea3ven.buildingbricks.core.block.behavior;

import com.google.gson.JsonObject;

public abstract class BlockBehaviorFactory {
	private String type;

	public BlockBehaviorFactory(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public abstract BlockBehaviorBase create(JsonObject json);
}
