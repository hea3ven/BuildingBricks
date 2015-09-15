package com.hea3ven.buildingbricks.core.eventhandlers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import com.hea3ven.buildingbricks.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;

public class EventHandlerTrowelOverlay {

    protected static final RenderItem itemRenderer = new RenderItem();
	private ResourceLocation widgetsTexture = new ResourceLocation("textures/gui/widgets.png");

	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
		if (event.type == ElementType.HOTBAR) {
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			ItemStack stack = player.getCurrentEquippedItem();
			if (stack != null && stack.getItem() == ModBuildingBricks.trowel) {
				Material mat = ModBuildingBricks.trowel.getBindedMaterial(stack);
				if (mat != null) {
					MaterialBlockType type = ModBuildingBricks.trowel.getCurrentBlockType(stack);
					renderTrowelOverlay(event.resolution, event.partialTicks, mat, type);
				}
			}
		}
	}

	private void renderTrowelOverlay(ScaledResolution sr, float partialTicks, Material mat,
			MaterialBlockType type) {
		int xPos = sr.getScaledWidth() / 2 - 88 + 10 * 20;
		int yPos = sr.getScaledHeight() - 16 - 3;

		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(widgetsTexture);
		mc.ingameGUI.drawTexturedModalRect(xPos - 3, yPos - 3, 0, 0, 21, 22);
		mc.ingameGUI.drawTexturedModalRect(xPos + 18, yPos - 3, 181, 0, 1, 22);

		ItemStack stack1 = mat.getBlock(type).getStack();
		if (stack1.getItem() != null)
			renderItem(partialTicks, xPos, yPos, stack1);
	}

	private void renderItem(float partialTicks, int xPos, int yPos, ItemStack stack) {
		Minecraft mc = Minecraft.getMinecraft();

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.enableGUIStandardItemLighting();

		float f1 = stack.animationsToGo - partialTicks;

		if (f1 > 0.0F) {
            GL11.glPushMatrix();
			float f2 = 1.0F + f1 / 5.0F;
			GL11.glTranslatef(xPos + 8, yPos + 12, 0.0F);
			GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
			GL11.glTranslatef(-(xPos + 8), -(yPos + 12), 0.0F);
		}

//		Minecraft.getMinecraft().setIngameNotInFocus();
		itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, xPos, yPos);
		if (f1 > 0.0F) {
			GL11.glPopMatrix();
		}

		itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, xPos, yPos);

		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_BLEND);
	}

}
