package com.sollace.custommenus.gui.action;

import com.sollace.custommenus.gui.container.UiRoot;

public class ActionQuit implements IGuiAction {
	@Override
	public void perform(UiRoot screen) {
		screen.getMinecraft().shutdown();
	}

}
