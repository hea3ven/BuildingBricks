package com.hea3ven.buildingbricks.core.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.hea3ven.buildingbricks.core.ModBuildingBricks;
import com.hea3ven.buildingbricks.core.materials.Material;
import com.hea3ven.buildingbricks.core.materials.MaterialStack;
import com.hea3ven.tools.commonutils.util.PlayerUtil;
import com.hea3ven.tools.commonutils.util.PlayerUtil.HeldEquipment;

public class GuiTrowel extends GuiContainer {

	public static final int ID = 0;
	public static final ResourceLocation BG_RESOURCE =
			new ResourceLocation("buildingbricks:textures/gui/container/trowel.png");

	private EntityPlayer player;

	public GuiTrowel(EntityPlayer player, ItemStack trowel) {
		super(ModBuildingBricks.trowel.getContainer(player));

		this.player = player;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(BG_RESOURCE);

		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

		HeldEquipment equip = PlayerUtil.getHeldEquipment(player, ModBuildingBricks.trowel);
		if(equip == null)
			return;
		ItemStack stack = equip.getStack();
		Material mat = MaterialStack.get(stack);
		if (mat != null) {
			int i = mat.getBlockRotation().getIndex(ModBuildingBricks.trowel.getCurrentBlockType(stack));
			int xCol = i % 4;
			int yCol = i / 4;
			this.drawTexturedModalRect(k + 95 + 18 * xCol, l + 6 + 18 * yCol, 176, 0, 22, 22);
		}
	}
}
