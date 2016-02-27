package com.hea3ven.buildingbricks.compat.vanilla.client;

import java.util.Map;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.compat.vanilla.GrassSlabWorldGen;
import com.hea3ven.buildingbricks.compat.vanilla.client.renderer.texture.TextureAtlasSpriteLongGrass;

@SideOnly(Side.CLIENT)
public class LongGrassTextureGenerator {
	public static boolean enabled = true;
	public static boolean forceEnabled = false;

	@SubscribeEvent
	public void onTextureStichPreEvent(final TextureStitchEvent.Pre event) {
		if (forceEnabled || (enabled && GrassSlabWorldGen.enabled)) {
			Map<String, TextureAtlasSprite> map =
					ReflectionHelper.getPrivateValue(TextureMap.class, event.map, 5);
			map.put("minecraft:blocks/grass_side_overlay",
					new TextureAtlasSpriteLongGrass("minecraft:blocks/grass_side_overlay",
							"minecraft:blocks/grass_top"));
			map.put("minecraft:blocks/grass_side_snowed",
					new TextureAtlasSpriteLongGrass("minecraft:blocks/grass_side_snowed",
							"minecraft:blocks/snow"));
		}
	}
}
