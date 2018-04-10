package com.sollace.custommenus.gui.action;

import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.container.UiRoot;

public class ActionRespawn implements IGuiAction {

	@Override
	public void perform(UiRoot screen) {
		if (screen.getMinecraft().player != null) {
			screen.getMinecraft().player.respawnPlayer();
			GuiNull.NULL.show();
		}
	}
}
