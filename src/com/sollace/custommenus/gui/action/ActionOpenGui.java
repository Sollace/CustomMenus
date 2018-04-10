package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.natives.INativeFerry;
import com.sollace.custommenus.registry.UiActions;

public class ActionOpenGui implements IGuiAction, IGuiDecider {
	protected String gui;
	
	protected IGuiAction yes, no;
	
	protected ActionOpenGui() {
		
	}
	
	public ActionOpenGui(JsonObject json, UiElement owner) {
		gui = json.get("gui").getAsString();
		
		if (json.has("callback")) yes = UiActions.create(json.get("callback").getAsJsonObject(), owner);
		if (json.has("yes")) yes = UiActions.create(json.get("yes").getAsJsonObject(), owner);
		if (json.has("no")) no = UiActions.create(json.get("no").getAsJsonObject(), owner);
	}
	
	@Override
	public void perform(UiRoot screen) {
		if (screen.getViewPortId().contentEquals(gui)) {
			screen.refresh();
		} else {
			INativeFerry.empty(screen).decider(this).open(gui);
		}
	}

	@Override
	public void yes(UiRoot screen) {
		perform(yes, screen);
	}

	@Override
	public void no(UiRoot screen) {
		perform(no, screen);
	}
	
	private void perform(IGuiAction action, UiRoot screen) {
		if (action != null) {
			action.perform(screen);
			screen.show();
			return;
		}
		screen.refresh();
	}
}
