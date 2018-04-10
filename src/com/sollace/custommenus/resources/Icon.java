package com.sollace.custommenus.resources;

import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.gui.geometry.BoundingBox;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Icon implements IIcon {
	
	private int x, y, width = 64, height = 64;
	
	private final ResourceLocation icon;
	
	public Icon(String location) {
		icon = new ResourceLocation(location);
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		GlStateManager.color(1, 1, 1, 1);
		sender.getMinecraft().getTextureManager().bindTexture(icon);
		
		GlStateManager.enableBlend();
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width / 2, height / 2, width / 2, height / 2);
        GlStateManager.disableBlend();
	}

	@Override
	public boolean canLoad() {
		return false;
	}

	@Override
	public boolean valid() {
		return true;
	}

	@Override
	public void load() {
		
	}

	@Override
	public Icon setPos(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	@Override
	public Icon setSize(int w, int h) {
		this.width = w;
		this.height = h;
		return this;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(x, y, width, height);
	}

	@Override
	public void reposition(UiView container) {
		
	}
}
