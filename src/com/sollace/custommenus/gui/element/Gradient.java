package com.sollace.custommenus.gui.element;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.IViewPort;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.gui.geometry.PaddingBox;
import com.sollace.custommenus.utils.JsonUtils;

public class Gradient extends AbstractElement {
	
	protected int startColor = -1, endColor = -1;
	
	protected IViewPort view;
	
	protected PaddingBox padding;
	
	public Gradient(UiContainer container) {
		view = container.getViewPort();
		padding = new PaddingBox();
	}
	
	@Override
	public Gradient init(JsonObject json) {
		startColor = JsonUtils.get(json, "start", startColor);
		endColor = JsonUtils.get(json, "end", endColor);
		return this;
	}

	@Override
	public Gradient setLabel(String label) {
		return this;
	}

	@Override
	public void setTexture(String resource) {
		
	}
	
	@Override
	public void reposition(UiView container) {
		super.reposition(container);
		padding.reposition(container);
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		drawGradientRect(x + padding.getLeft(), y + padding.getTop(), x + view.getWidth() - padding.getRight(), y + view.getHeight() - padding.getBottom(), startColor, endColor);
	}

}
