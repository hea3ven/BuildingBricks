package com.hea3ven.buildingbricks.compat.vanilla.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.PngSizeInfo;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.client.FMLClientHandler;

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
		try (IResource resource = manager.getResource(location)) {
			loadSprite(PngSizeInfo.makeFromResource(resource), false);
		} catch (IOException e) {
			FMLClientHandler.instance().trackMissingTexture(location);
			return true;
		}
		BufferedImage sideImage;
		try (IResource resource = manager.getResource(location)) {

			sideImage = TextureUtil.readBufferedImage(resource.getInputStream());
		} catch (IOException e) {
			FMLClientHandler.instance().trackMissingTexture(location);
			return true;
		}

		location = new ResourceLocation(topSpriteName);
		location = new ResourceLocation(location.getResourceDomain(),
				"textures/" + location.getResourcePath() + ".png");
		BufferedImage topImage;
		try (IResource resource = manager.getResource(location)) {
			topImage = TextureUtil.readBufferedImage(resource.getInputStream());
		} catch (IOException e) {
			FMLClientHandler.instance().trackMissingTexture(location);
			return true;
		}
		editImage(sideImage, topImage);

		int mipmaps = Minecraft.getMinecraft().gameSettings.mipmapLevels;
		int[][] framesData = new int[1 + mipmaps][];
		framesData[0] = new int[sideImage.getWidth() * sideImage.getHeight()];
		sideImage.getRGB(0, 0, sideImage.getWidth(), sideImage.getHeight(), framesData[0], 0,
				sideImage.getWidth());

		this.framesTextureData.add(framesData);

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
		System.arraycopy(imgData, 0, imgData, (height / 2) * width, (height / 2) * width);
		// Copy half of the top image to the top half of the side image
		System.arraycopy(topImgData, 0, imgData, 0, (height / 2) * width);

		sideImage.setRGB(0, 0, width, height, imgData, 0, width);
	}
}
