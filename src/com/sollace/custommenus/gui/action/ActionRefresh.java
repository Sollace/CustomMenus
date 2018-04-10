package com.sollace.custommenus.gui.action;

import com.sollace.custommenus.GuiMenus;
import com.sollace.custommenus.gui.container.UiRoot;

public class ActionRefresh implements IGuiAction {
	
	@Override
	public void perform(UiRoot screen) {
		GuiMenus.INSTANCE.onResourceManagerReload(screen.getMinecraft().getResourceManager());
	}
}
