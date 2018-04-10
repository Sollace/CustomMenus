package com.sollace.custommenus.gui.action;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sollace.custommenus.GuiMenus;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.input.UiField;

public class ActionModifyElements implements IGuiAction {
	
	private final String[] targets;
	
	private final JsonObject apply;
	
	public ActionModifyElements(JsonObject json, UiElement owner) {
		JsonArray ts = json.get("targets").getAsJsonArray();
		targets = new String[ts.size()];
		for (int i = 0; i < targets.length; i++) targets[i] = ts.get(i).getAsString();
		apply = json.get("apply").getAsJsonObject();
		
	}
	
	@Override
	public void perform(UiRoot screen) {
		for (int i = 0; i < targets.length; i++) {
			UiField<?> field = screen.getField(targets[i]);
			if (field != null) {
				GuiMenus.jsonFactory().populateElement(field, field.getContainer(), apply);
			}
		}
	}

}
