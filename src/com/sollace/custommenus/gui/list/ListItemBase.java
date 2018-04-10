package com.sollace.custommenus.gui.list;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.IViewPort;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.gui.element.AbstractElement;
import com.sollace.custommenus.gui.geometry.EnumAlignment;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class ListItemBase<V extends UiListItem<V>> extends AbstractElement implements UiListItem<V>, UiView, IViewPort {
	protected int index;
	
	protected String displayString = "";
	
	protected UiList<V> owner;
	
	public ListItemBase(UiList<V> owner) {
		this.owner = owner;
		index = owner.size();
		setAlignment(EnumAlignment.TOP_CENTER);
	}
	
	@Override
	public String getDisplayString() {
		return displayString;
	}
	
	@Override
	public ListItemBase<V> setLabel(String label) {
		displayString = label;
		return this;
	}
	
	@Override
	public ListItemBase<V> init(JsonObject json) {
		return this;
	}
	
	@Override
	public void setTexture(String resource) {
		
	}
	
	@Override
	public void itemMoved(int newPosition) {
		index = newPosition;
	}
	
	@Override
	public boolean isMoveable() {
		return false;
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		if (isSelected()) drawSelectionBox();
		drawContents(sender, mouseX, mouseY, partialTicks);
	}
	
	public void drawContents(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		drawCenteredString(owner.getFonts(), displayString, x + width / 2, y + height / 3, owner.getItemColour());
	}
	
	@Override
	public void drawSelectionBox() {
		Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableTexture2D();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        
        bufferbuilder.pos(x, y + height, 0).tex(0, 1).color(128, 128, 128, 255).endVertex();
        bufferbuilder.pos(x + width, y + height, 0).tex(1, 1).color(128, 128, 128, 255).endVertex();
        bufferbuilder.pos(x + width, y, 0).tex(1, 0).color(128, 128, 128, 255).endVertex();
        bufferbuilder.pos(x, y, 0).tex(0, 0).color(128, 128, 128, 255).endVertex();
        
        bufferbuilder.pos(x + 1, y + height - 1, 0).tex(0, 1).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(x + width - 1, y + height - 1, 0).tex(1, 1).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(x + width - 1, y + 1, 0).tex(1, 0).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(x + 1, y + 1, 0).tex(0, 0).color(0, 0, 0, 255).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
	}
	
	@Override
	public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
		if (owner.setValue(index, sender)) {
			return true;
		}
		return super.performAction(mouseX, mouseY, sender);
	}
	
	@Override
	public boolean isSelected() {
		return owner.getSelectedItem() == this;
	}
	

	@Override
	public UiContainer getContainer() {
		return owner;
	}
	
	@Override
	public IViewPort getViewPort() {
		return this;
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public void setGlViewport() {
		owner.getViewPort().setGlViewport();
	}

	@Override
	public int getMouseX() {
		return owner.getViewPort().getMouseX() - x;
	}

	@Override
	public int getMouseY() {
		return owner.getViewPort().getMouseY() - y;
	}
}