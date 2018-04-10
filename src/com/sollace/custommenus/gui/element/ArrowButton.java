package com.sollace.custommenus.gui.element;

import com.google.gson.JsonObject;
import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.gui.geometry.EnumEdge;
import com.sollace.custommenus.resources.IIcon;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ArrowButton extends AbstractElement {
	
	private ResourceLocation texture = new ResourceLocation("textures/gui/world_selection.png");
	private ResourceLocation vertical_buttons = new ResourceLocation("textures/gui/server_selection.png");
	
	protected int warningLevel = 0;
	
	protected IGuiAction up = null, down = null;
	
	protected IIcon background;
	
	private long lastClick;
	
	public ArrowButton(UiContainer container) {
		setSize(32, 32);
	}
	
	public ArrowButton setBackground(IIcon icon) {
		background = icon;
		return this;
	}
	
	@Override
	public ArrowButton init(JsonObject json) {
		return this;
	}
	
	@Override
	public void reposition(UiView container) {
		super.reposition(container);
		if (background != null) {
			background.setPos(x, y).setSize(width, height);
		}
	}
	
	@Override
	public ArrowButton setLabel(String label) {
		return this;
	}
	
	public void setWarningLevel(int level) {
		warningLevel = level;
	}
	
	@Override
	public void setTexture(String resource) {
		texture = new ResourceLocation(resource);
	}
	
	public ArrowButton setUpAction(IGuiAction action) {
		up = action;
		return this;
	}
	
	public ArrowButton setDownAction(IGuiAction action) {
		down = action;
		return this;
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		if (!visible) return;
		
		if (background != null) background.render(sender, mouseX, mouseY, partialTicks);
		
		sender.getMinecraft().getTextureManager().bindTexture(texture);
		
		drawRect(x, y, x + width, y + height, -1601138544);
		GlStateManager.color(1, 1, 1, 1);
		
		EnumEdge focusDirection = getFocusDirection(mouseX, mouseY);
		
		int focus = focusDirection == EnumEdge.RIGHT ? 32 : 0;
		int rightX = up != null || down != null ? 6 : 0;
		
		drawModalRectWithCustomSizedTexture(rightX + x, y, warningLevel > 0 ? 32 : 0, focus, 32, 32, 256.0F, 256);
		
		if (warningLevel > 0) {
			drawModalRectWithCustomSizedTexture(x, y, (warningLevel + 1) * 32, focus, 32, 32, 256, 256);
		}
		
		if (up != null || down != null) {
			sender.getMinecraft().getTextureManager().bindTexture(vertical_buttons);
		}
		
		if (up != null) {
			drawModalRectWithCustomSizedTexture(x, y, 96, focusDirection == EnumEdge.TOP ? 32 : 0, 32, 32, 256, 256);
		}
		
		if (down != null) {
			drawModalRectWithCustomSizedTexture(x, y, 64, focusDirection == EnumEdge.BOTTOM ? 32 : 0, 32, 32, 256, 256);
		}
	}
	
	protected EnumEdge getFocusDirection(int mouseX, int mouseY) {
		if (!isFocused(mouseX, mouseY)) return null;
		
		if (up == null && down == null) {
			return EnumEdge.RIGHT;
		}
		
		if (mouseX > width/2) return EnumEdge.RIGHT;
		
		if (mouseY < height / 2) return EnumEdge.TOP;
		
		return EnumEdge.BOTTOM;
	}
	
	@Override
	public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
		if (isFocused(mouseX, mouseY) || GuiNull.systemTime() - lastClick < 250l) {
			EnumEdge focus = getFocusDirection(mouseX, mouseY);
			
			if (up != null && focus == EnumEdge.TOP) {
				up.perform(sender);
				return true;
			}
			
			if (down != null && focus == EnumEdge.BOTTOM) {
				down.perform(sender);
				return true;
			}
			
			return super.performAction(mouseX, mouseY, sender);
		}
		return false;
	}
}
