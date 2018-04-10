package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.gui.list.UiList;
import com.sollace.custommenus.gui.list.datasource.WorldSource;

import net.minecraft.world.storage.WorldSummary;

public class ActionJoinGame extends ActionYesNo {

	private String saveFile = null;
	private String worldName = "World";
	
	private String worldListField = null;
	
	private final int versionParam;
	
	public ActionJoinGame(JsonObject json, UiElement owner) {
		super(json, owner);
		if (json.has("saveFile")) saveFile = json.get("saveFile").getAsString();
		if (json.has("worldName")) worldName = json.get("worldName").getAsString();
		if (json.has("worldListField")) worldListField = json.get("worldListField").getAsString();
		
		versionParam = message2.addParam("%s");
	}
	
	public ActionJoinGame(JsonObject json, UiField<String> owner) {
		super(json, owner);
		saveFile = owner.getValue();
		worldName = owner.getDisplayString();
		if (json.has("worldListField")) worldListField = json.get("worldListField").getAsString();
		
		versionParam = message2.addParam("%s");
	}
	
	@Override
	public void perform(UiRoot screen) {
		if (worldListField != null) {
			UiField<Integer> list = screen.getField(worldListField);
			
			if (list instanceof UiList && ((UiList<?>)list).getSelectedItem() instanceof UiField) {
				@SuppressWarnings("unchecked")
				UiField<String> field = (UiField<String>)((UiList<?>)list).getSelectedItem();
				worldName = field.getDisplayString();
				saveFile = field.getValue();
				
				if (field instanceof WorldSource.ListItem) {
					WorldSummary summary = ((WorldSource.ListItem)field).getSummary();
					if (summary.askToOpenWorld()) {
						message2.setParam(versionParam, summary.getVersionName());
						super.perform(screen);
						return;
					}
				}
			} else {
				saveFile = list.getDisplayString();
			}
		}
		
		yes(screen);
	}
	
	@Override
	public void yes(UiRoot screen) {
		if (saveFile != null && screen.getMinecraft().getSaveLoader().canLoadWorld(saveFile)) {
			screen.getMinecraft().launchIntegratedServer(saveFile, worldName, null);
		}
	}
}
