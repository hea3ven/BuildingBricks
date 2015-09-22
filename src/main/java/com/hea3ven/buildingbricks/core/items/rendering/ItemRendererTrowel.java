package com.hea3ven.buildingbricks.core.items.rendering;

import java.util.Map;

import com.google.common.collect.Maps;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import net.minecraftforge.client.IItemRenderer;

import com.hea3ven.buildingbricks.core.materials.Material;

public class ItemRendererTrowel implements IItemRenderer {

	public static Map<Material, IIcon> materialIcons = Maps.newHashMap();

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		//		Minecraft.getMinecraft().rend

		TextureManager texturemanager = Minecraft.getMinecraft().renderEngine;

		IIcon iicon = stack.getItem().getIcon(stack, 0);
		if (iicon == null) {
			GL11.glPopMatrix();
			return;
		}

		texturemanager.bindTexture(texturemanager.getResourceLocation(stack.getItemSpriteNumber()));
		TextureUtil.func_152777_a(false, false, 1.0F);
		Tessellator tessellator = Tessellator.instance;
		float f = iicon.getMinU();
		float f1 = iicon.getMaxU();
		float f2 = iicon.getMinV();
		float f3 = iicon.getMaxV();
		float f4 = 0.0F;
		float f5 = 0.3F;
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glTranslatef(-f4, -f5, 0.0F);
		float f6 = 1.5F;
		GL11.glScalef(f6, f6, f6);
		GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
		GL11.glTranslatef(-1.25f, 0.2F, 0.0F);
		ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, iicon.getIconWidth(),
				iicon.getIconHeight(), 0.0625F);

		iicon = stack.getItem().getIcon(stack, 1);
		if (iicon == null) {
			GL11.glPopMatrix();
			return;
		}

		f = iicon.getMinU();
		f1 = iicon.getMaxU();
		f2 = iicon.getMinV();
		f3 = iicon.getMaxV();
		f4 = 0.0F;
		f5 = 0.3F;
		//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		//        GL11.glTranslatef(-f4, -f5, 0.0F);
		//        f6 = 1.5F;
		//      GL11.glScalef(f6, f6, f6);
		//        GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
		//        GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
		//        GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);

		texturemanager.bindTexture(texturemanager.getResourceLocation(0));
		GL11.glTranslatef(0.45f, 0.55f, 0.1F);
		GL11.glScalef(0.3f, 0.3f, 0.3f);
		ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, iicon.getIconWidth(),
				iicon.getIconHeight(), 16 * 0.0625F);

		//        if (p_78443_2_.hasEffect(p_78443_3_))
		//        {
		//            GL11.glDepthFunc(GL11.GL_EQUAL);
		//            GL11.glDisable(GL11.GL_LIGHTING);
		//            texturemanager.bindTexture(RES_ITEM_GLINT);
		//            GL11.glEnable(GL11.GL_BLEND);
		//            OpenGlHelper.glBlendFunc(768, 1, 1, 0);
		//            float f7 = 0.76F;
		//            GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
		//            GL11.glMatrixMode(GL11.GL_TEXTURE);
		//            GL11.glPushMatrix();
		//            float f8 = 0.125F;
		//            GL11.glScalef(f8, f8, f8);
		//            float f9 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
		//            GL11.glTranslatef(f9, 0.0F, 0.0F);
		//            GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
		//            ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
		//            GL11.glPopMatrix();
		//            GL11.glPushMatrix();
		//            GL11.glScalef(f8, f8, f8);
		//            f9 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
		//            GL11.glTranslatef(-f9, 0.0F, 0.0F);
		//            GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
		//            ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
		//            GL11.glPopMatrix();
		//            GL11.glMatrixMode(GL11.GL_MODELVIEW);
		//            GL11.glDisable(GL11.GL_BLEND);
		//            GL11.glEnable(GL11.GL_LIGHTING);
		//            GL11.glDepthFunc(GL11.GL_LEQUAL);
		//        }

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		texturemanager.bindTexture(texturemanager.getResourceLocation(stack.getItemSpriteNumber()));
		TextureUtil.func_147945_b();
	}

}
