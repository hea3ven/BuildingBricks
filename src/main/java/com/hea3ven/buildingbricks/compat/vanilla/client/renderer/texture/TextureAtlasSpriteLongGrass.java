package com.hea3ven.buildingbricks.compat.vanilla.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.ResourceLocation;

public class TextureAtlasSpriteLongGrass extends TextureAtlasSprite {
	private final String topSpriteName;

	public TextureAtlasSpriteLongGrass(String spriteName, String topSpriteName) {
		super(spriteName);
		this.topSpriteName = topSpriteName;
	}

	@Override
	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
		return true;
	}

	@Override
	public boolean load(IResourceManager manager, ResourceLocation location) {
		location = new ResourceLocation(getIconName());
		location = new ResourceLocation(location.getResourceDomain(),
				"textures/" + location.getResourcePath() + ".png");

		int mipmaps = Minecraft.getMinecraft().gameSettings.mipmapLevels;
		BufferedImage[] image = new BufferedImage[1 + mipmaps];
		BufferedImage topImage;
		AnimationMetadataSection animation;
		try {
			IResource resource = manager.getResource(location);
			image[0] = TextureUtil.readBufferedImage(resource.getInputStream());

			animation = resource.getMetadata("animation");

			location = new ResourceLocation(topSpriteName);
			location = new ResourceLocation(location.getResourceDomain(),
					"textures/" + location.getResourcePath() + ".png");
			resource = manager.getResource(location);
			topImage = TextureUtil.readBufferedImage(resource.getInputStream());
		} catch (RuntimeException e) {
			return true;
		} catch (IOException e) {
			return true;
		}

		editImage(image[0], topImage);

		try {
			loadSprite(image, animation);
		} catch (IOException e) {
			return true;
		}
		return false;
	}

	private void editImage(BufferedImage sideImage, BufferedImage topImage) {
		int height = sideImage.getHeight();
		int width = sideImage.getWidth();
		int[] imgData = new int[width * height];
		sideImage.getRGB(0, 0, width, height, imgData, 0, width);
		int[] topImgData = new int[width * height];
		topImage.getRGB(0, 0, width, height, topImgData, 0, width);

		// Copy the top half to the bottom half
		for (int y = 0; y < height / 2; y++) {
			for (int x = 0; x < width; x++) {
				imgData[(height / 2 + y) * width + x] = imgData[y * width + x];
			}
		}
		// Copy half of the top image to the top half of the side image
		for (int y = 0; y < height / 2; y++) {
			for (int x = 0; x < width; x++) {
				imgData[y * width + x] = topImgData[y * width + x];
			}
		}

		sideImage.setRGB(0, 0, width, height, imgData, 0, width);
	}
}
