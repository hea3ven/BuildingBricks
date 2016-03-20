package com.hea3ven.buildingbricks.compat.vanilla.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

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
		IResource iresource = null;
		try {
			PngSizeInfo pngsizeinfo = PngSizeInfo.makeFromResource(manager.getResource(location));
			IResource resource = manager.getResource(location);
			loadSprite(pngsizeinfo, false);

			BufferedImage bufferedimage = TextureUtil.readBufferedImage(resource.getInputStream());

			location = new ResourceLocation(topSpriteName);
			location = new ResourceLocation(location.getResourceDomain(),
					"textures/" + location.getResourcePath() + ".png");
			resource = manager.getResource(location);
			BufferedImage topImage = TextureUtil.readBufferedImage(resource.getInputStream());
			editImage(bufferedimage, topImage);

			int mipmaps = Minecraft.getMinecraft().gameSettings.mipmapLevels;
			int[][] aint = new int[1 + mipmaps][];
			aint[0] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
			bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[0], 0,
					bufferedimage.getWidth());

			this.framesTextureData.add(aint);

			return false;
		} catch (IOException e) {
			FMLClientHandler.instance().trackMissingTexture(location);
			return true;
		} finally {
			IOUtils.closeQuietly(iresource);
		}
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
