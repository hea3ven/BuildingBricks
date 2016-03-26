package com.hea3ven.buildingbricks.core.eventhandlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialBlockType;
import com.hea3ven.tools.commonutils.util.PlayerUtil;
import com.hea3ven.tools.commonutils.util.PlayerUtil.HeldEquipment;

@SideOnly(Side.CLIENT)
public class EventHandlerTrowelOverlay {

	private ResourceLocation widgetsTexture = new ResourceLocation("textures/gui/widgets.png");

	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.HOTBAR) {
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			HeldEquipment equipment = PlayerUtil.getHeldEquipment(player, ModBuildingBricks.trowel);
			if (equipment != null && equipment.stack.getItem() == ModBuildingBricks.trowel) {
				Material mat = ModBuildingBricks.trowel.getMaterial(equipment.stack);
				if (mat != null) {
					MaterialBlockType type = ModBuildingBricks.trowel.getCurrentBlockType(equipment.stack);
					renderTrowelOverlay(event.getResolution(), event.getPartialTicks(), mat, type);
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

		GlStateManager.enableRescaleNormal();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		RenderHelper.enableGUIStandardItemLighting();

		float f1 = stack.animationsToGo - partialTicks;

		if (f1 > 0.0F) {
			GlStateManager.pushMatrix();
			float f2 = 1.0F + f1 / 5.0F;
			GlStateManager.translate(xPos + 8, yPos + 12, 0.0F);
			GlStateManager.scale(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
			GlStateManager.translate(-(xPos + 8), -(yPos + 12), 0.0F);
		}

		mc.getRenderItem().renderItemAndEffectIntoGUI(stack, xPos, yPos);
		if (f1 > 0.0F) {
			GlStateManager.popMatrix();
		}

		mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, xPos, yPos);

		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
	}
}
