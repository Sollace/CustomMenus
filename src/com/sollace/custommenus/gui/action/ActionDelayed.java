package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.registry.UiActions;

public class ActionDelayed implements IGuiAction {
	IGuiAction action;
	
	int delay = 0;
	
	public ActionDelayed(JsonObject json, UiElement owner) {
		delay = json.get("delay").getAsInt();
		action = UiActions.create(json.get("action").getAsJsonObject(), owner);
	}
	
	@Override
	public void perform(UiRoot screen) {
		if (delay <= 0) {
			action.perform(screen);
		} else {
			screen.performIn(action, delay);
		}
	}

}
