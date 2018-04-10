package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.gui.natives.INativeFerry;

import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSummary;

public class ActionAlterWorld implements IGuiAction {
	
	private String saveFile = null;
	
	private String worldListField = null;
	
	private final String action;
	
	public ActionAlterWorld(JsonObject json, UiElement owner) {
		if (json.has("saveFile")) saveFile = json.get("saveFile").getAsString();
		if (json.has("worldListField")) worldListField = json.get("worldListField").getAsString();
		action = json.get("action").getAsString();
	}
	
	public ActionAlterWorld(String type, WorldSummary summary) {
		action = type;
		saveFile = summary.getFileName();
	}
	
	@Override
	public void perform(UiRoot screen) {
		if (worldListField != null) {
			UiField<Integer> list = screen.getField(worldListField);
			saveFile = list.getDisplayString();
		}
		
		if (saveFile != null) {
			switch (action) {
				case "delete": deleteWorld(screen); break;
				case "edit": editWorld(screen); break;
				case "recreate": recreateWorld(screen); break;
			}
		}
	}
	
	private void deleteWorld(UiRoot screen) {
		screen.open("working");
		
		ISaveFormat loader = screen.getMinecraft().getSaveLoader();
		loader.flushCache();
		loader.deleteWorldDirectory(saveFile);
		
		screen.refresh();
	}
	
	private void editWorld(UiRoot screen) {
		INativeFerry.empty(screen).param("worldId", saveFile).open("editworld");
	}
	
	private void recreateWorld(UiRoot screen) {
		screen.open("working");
		
        ISaveHandler saver = screen.getMinecraft().getSaveLoader().getSaveLoader(saveFile, false);
        WorldInfo info = saver.loadWorldInfo();
        saver.flush();

        if (info != null) {
        	GuiCreateWorld guicreateworld = new GuiCreateWorld(screen.getNative());
            guicreateworld.recreateFromExistingWorld(info);
            screen.getMinecraft().displayGuiScreen(guicreateworld);
        }
	}
}
