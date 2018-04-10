package com.sollace.custommenus.gui.overlays;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;

public class LiteLoaderPane extends AbstractOverlay {
	
	public LiteLoaderPane(UiContainer container) {
		super(container);
	}
	
	@Override
	public LiteLoaderPane init(JsonObject json) {
		if (json.has("enabled")) setValue(json.get("enabled").getAsBoolean());
		return this;
	}

	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		return false;
	}
	
	@Override
	public LiteLoaderPane setAction(IGuiAction action) {
		return this;
	}
	
	@Override
	public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
		return false;
	}
	
	@Override
	public void reposition(UiView container) {
		
	}
	
	@Override
	public void destroy() {
	}
	
	@Override
	public void update(UiRoot sender) {
		
	}
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		
	}
	
	@Override
	public String getName() {
		return "liteloaderpanel";
	}
	
	@Override
	public String getDisplayString() {
		return "Liteloader-Panel";
	}
}
