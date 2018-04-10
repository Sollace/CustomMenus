package com.sollace.custommenus.gui.element;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.gui.geometry.UiBounded;
import com.sollace.custommenus.locale.Locales;
import com.sollace.custommenus.utils.JsonUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

public class Button extends AbstractElement implements UiElement, UiBounded {
	
	protected ResourceLocation texture = new ResourceLocation("textures/gui/widgets.png");
	
	protected Image image = null;
	
	private UiContainer container;
	
	protected boolean hovered;
	
	protected String displayString = "";
	
	protected int color = 14737632, disabledColor = 10526880, hoveredColor = 16777120;
	
	public Button(UiContainer container) {
		this.container = container;
		this.sound = SoundEvents.UI_BUTTON_CLICK;
	}
	
	@Override
	public Button init(JsonObject json) {
		if (json.has("image")) setImage(json.get("image").getAsJsonObject());
		color = JsonUtils.get(json, "color", color);
		disabledColor = JsonUtils.get(json, "disabledColor", disabledColor);
		hoveredColor = JsonUtils.get(json, "hoveredColor", hoveredColor);
		return this;
	}
	
	@Override
	public Button setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}
	
	@Override
	public Button setLabel(String label) {
		displayString = Locales.translateUnformatted(label);
		return this;
	}
	
	public UiContainer getContainer() {
		return container;
	}
	
	@Override
	public void setTexture(String resource) {
		texture = new ResourceLocation(resource);
	}
	
	private void setImage(JsonObject json) {
		image = createEmbeddedImage(json);
	}
	
	protected Image createEmbeddedImage(JsonObject json) {
		Image image = new Image(container);
		image.setPos(x, y);
		image.setSize(width, height);
		image.setTexture(json.get("texture").getAsString());
		image.init(json);
		return image;
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
        if (!visible) return;
        
        hovered = isFocused(mouseX, mouseY);
        
        if (image != null) {
        	if (hovered) image.tex.texY += height;
        	image.render(sender, mouseX, mouseY, partialTicks);
        	if (hovered) image.tex.texY -= height;
        	return;
        }
        
        Minecraft mc = sender.getMinecraft();
        mc.getTextureManager().bindTexture(texture);
        
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        
        renderGuiElement();
    	
        drawCenteredString(mc.fontRenderer, getLabelForRender(), x + width / 2, y + (height - 8) / 2, getColor());
    }
	
	protected String getLabelForRender() {
		return displayString;
	}
	
	protected void renderGuiElement() {
		int i = getHoverState();
        drawTexturedModalRect(x, y, 0, 46 + i * 20, width / 2, height);
        drawTexturedModalRect(x + width / 2, y, 200 - width / 2, 46 + i * 20, width / 2, height);
        
	}
	
	protected int getColor() {
		return !enabled ? disabledColor : hovered ? hoveredColor : color;
	}
	
	protected int getHoverState() {
		return !enabled ? 0 : hovered ? 2 : 1;
	}
	
	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		return enabled && super.isFocused(mouseX, mouseY);
	}
	
	@Override
	public void reposition(UiView container) {
		super.reposition(container);
		if (image != null) {
			image.x = x;
			image.y = y;
		}
	}
}
