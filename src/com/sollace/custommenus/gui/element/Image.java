package com.sollace.custommenus.gui.element;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.resources.TextureLocation;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Image extends AbstractElement {
	
	protected ResourceLocation image;
	
	protected TextureLocation tex = new TextureLocation();
	
	public Image(UiContainer container) {
		
	}
	
	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		return (tooltip != null || action != null) && super.isFocused(mouseX, mouseY);
	}
	
	@Override
	public Image setLabel(String label) {
		return this;
	}
	
	@Override
	public Image init(JsonObject json) {
		tex.init(json);
		return this;
	}

	@Override
	public void setTexture(String resource) {
		image = new ResourceLocation(resource);
	}

	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		sender.getMinecraft().getTextureManager().bindTexture(image);
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.disableDepth();
		tex.drawTextureAt(x, y, width, height);
		GlStateManager.enableDepth();
	}

}
