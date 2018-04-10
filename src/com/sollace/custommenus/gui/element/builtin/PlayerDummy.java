package com.sollace.custommenus.gui.element.builtin;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.AbstractElement;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.utils.IJsonReadable;
import com.sollace.custommenus.utils.JsonUtils;
import com.sollace.dummy.DPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

public class PlayerDummy extends AbstractElement {
	
	protected int scale = 30;
	
	protected EntityLivingBase entity;
	
	public PlayerDummy(UiContainer container) {
		Minecraft mc = Minecraft.getMinecraft();
		entity = mc.player;
		if (entity == null) {
			entity = new DPlayer(mc.getSession().getProfile());
		}
	}

	@Override
	public UiElement setLabel(String label) {
		return this;
	}

	@Override
	public void setTexture(String resource) {
		
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		GlStateManager.color(1, 1, 1, 1);
		GuiInventory.drawEntityOnScreen(x, y, scale, x - mouseX, y - mouseY, entity);
	}

	@Override
	public IJsonReadable init(JsonObject json) {
		scale = JsonUtils.get(json, "scale", scale);
		return this;
	}
	
	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		return false;
	}
}
