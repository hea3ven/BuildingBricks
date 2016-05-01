package com.hea3ven.buildingbricks.core.block.behavior;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class BlockBehaviorRegistry {

	private static Map<String, BlockBehaviorFactory> factories = new HashMap<>();

	static {
		register(new BlockBehaviorFactory("emit-light") {
			@Override
			public BlockBehaviorBase create(JsonObject json) {
				if (!json.has("light-level"))
					throw new JsonParseException("missing 'light-level' property");
				int lightLevel = json.get("light-level").getAsInt();
				if (lightLevel < 0 || 15 < lightLevel)
					throw new JsonParseException("'light-level' property must be between 0 and 15");
				return new BlockBehaviorEmitLight(lightLevel);
			}
		});
		register(new BlockBehaviorFactory("redstone-light") {
			@Override
			public BlockBehaviorBase create(JsonObject json) {
				if (!json.has("light-level"))
					throw new JsonParseException("missing 'light-level' property");
				int lightLevel = json.get("light-level").getAsInt();
				if (lightLevel < 0 || 15 < lightLevel)
					throw new JsonParseException("'light-level' property must be between 0 and 15");
				return new BlockBehaviorRedstoneLight(lightLevel);
			}
		});
	}

	public static void register(BlockBehaviorFactory behaviourFactory) {
		factories.put(behaviourFactory.getType(), behaviourFactory);
	}

	public static BlockBehaviorBase create(String type, JsonObject json) {
		if (!factories.containsKey(type))
			throw new JsonParseException("unknown behavior " + type);
		return factories.get(type).create(json);
	}
}
