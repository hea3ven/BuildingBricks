package com.hea3ven.buildingbricks.compat.vanilla.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.PngSizeInfo;
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
//		location = new ResourceLocation(getIconName());
//		location = new ResourceLocation(location.getResourceDomain(),
//				"textures/" + location.getResourcePath() + ".png");

//		int mipmaps = Minecraft.getMinecraft().gameSettings.mipmapLevels;
//		PngSizeInfo[] image = new PngSizeInfo[1 + mipmaps];
//		PngSizeInfo topImage;
//		boolean animation;
//		try {
//			IResource resource = manager.getResource(location);
//			image[0] = PngSizeInfo.makeFromResource(resource);

//			animation = resource.getMetadata("animation") != null;

//			location = new ResourceLocation(topSpriteName);
//			location = new ResourceLocation(location.getResourceDomain(),
//					"textures/" + location.getResourcePath() + ".png");
//			resource = manager.getResource(location);
//			topImage = PngSizeInfo.makeFromResource(resource);
//		} catch (RuntimeException e) {
//			return true;
//		} catch (IOException e) {
//			return true;
//		}

//		editImage(image[0], topImage);

//		try {
//			loadSprite(image[0], animation);
//		} catch (IOException e) {
//			return true;
//		}
		return false;
	}

//	private void editImage(PngSizeInfo sideImage, PngSizeInfo topImage) {
//		int height = sideImage.pngHeight;
//		int width = sideImage.pngWidth;
//		int[] imgData = new int[width * height];
//		sideImage.getRGB(0, 0, width, height, imgData, 0, width);
//		int[] topImgData = new int[width * height];
//		topImage.getRGB(0, 0, width, height, topImgData, 0, width);

//		 Copy the top half to the bottom half
//		for (int y = 0; y < height / 2; y++) {
//			for (int x = 0; x < width; x++) {
//				imgData[(height / 2 + y) * width + x] = imgData[y * width + x];
//			}
//		}
//		 Copy half of the top image to the top half of the side image
//		for (int y = 0; y < height / 2; y++) {
//			for (int x = 0; x < width; x++) {
//				imgData[y * width + x] = topImgData[y * width + x];
//			}
//		}

//		sideImage.setRGB(0, 0, width, height, imgData, 0, width);
//	}
}
