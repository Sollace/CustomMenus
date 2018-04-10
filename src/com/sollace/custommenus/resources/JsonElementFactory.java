package com.sollace.custommenus.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sollace.custommenus.GuiCustomMenu;
import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.geometry.EnumAlignment;
import com.sollace.custommenus.gui.geometry.UiAligned;
import com.sollace.custommenus.gui.geometry.UiBounded;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.gui.natives.INativeFerry;
import com.sollace.custommenus.gui.packing.UiPacker;
import com.sollace.custommenus.registry.UiActions;
import com.sollace.custommenus.registry.UiElements;
import com.sollace.custommenus.registry.UiFilters;
import com.sollace.custommenus.registry.UiPackers;

public class JsonElementFactory {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public void loadElement(UiContainer container, String key, JsonObject json) {
		UiElement element = UiElements.createForKey(container, key);
		if (element == null) return;
		try {
			populateElement(element, container, json);
			container.addChild(element);
		} catch (Throwable e) {
			LOGGER.error("Exception whilst creating element of type \"" + key + "\"", e);
		}
	}
	
	public void populateElement(UiElement element, UiContainer container, JsonObject json) {
		if (element instanceof UiAligned) {
			if (json.has("alignment")) ((UiAligned)element).setAlignment(EnumAlignment.getByName(json.get("alignment").getAsString()));
		}
		
		if (element instanceof UiBounded) {
			if (json.has("width") || json.has("height")) {
				int w = 0, h = 0;
				
				if (json.has("width")) w = json.get("width").getAsInt();
				if (json.has("height")) h = json.get("height").getAsInt();
				((UiBounded)element).setSize(w, h);
			}
			
			if (json.has("posX") || json.has("posY")) {
				int x = 0, y = 0;
				if (json.has("posX")) x = json.get("posX").getAsInt();
				if (json.has("posY")) y = json.get("posY").getAsInt();
				((UiBounded)element).setPos(x, y);
			}
		}
		
		if (json.has("text")) element.setLabel(json.get("text").getAsString());
		
		if (json.has("enabled")) element.setEnabled(json.get("enabled").getAsBoolean());
		
		if (json.has("texture")) element.setTexture(json.get("texture").getAsString());
		
		if (json.has("visible")) element.setVisible(json.get("visible").getAsBoolean());
		
		if (json.has("tooltip")) element.setToolTip(FormattedString.create(json.get("tooltip")));
		
		if (json.has("action")) element.setAction(UiActions.create(json.getAsJsonObject("action"), element));
		
		element.init(json);
		element.reposition(container);
		
		if (element instanceof UiField) {
			if (json.has("name")) ((UiField<?>)element).setName(json.get("name").getAsString());
		}
		
		if (element instanceof UiContainer) {
			if (json.has("pack")) {
				UiPacker packer = UiPackers.create(json.get("pack").getAsJsonObject());
				if (packer != null) ((UiContainer)element).setPacker(packer);
			}
			if (json.has("elements")) {
				JsonElement childs = json.get("elements");
				if (childs.isJsonArray()) {
					loadElements((UiContainer)element, childs.getAsJsonArray());
				}
			}
		}
		
		if (json.has("visibility")) UiFilters.testCondition(json.get("visibility").getAsJsonObject(), container, element, this);
	}
	
	public void loadElements(UiContainer container, JsonArray elements) {
		for (int i = 0; i < elements.size(); i++) {
			JsonObject child = elements.get(i).getAsJsonObject();
			if (!child.has("type")) {
				LOGGER.warn("WARN: Skipping child element with missing type. Missing type for child element");
				continue;
			}
			String type = child.get("type").getAsString();
			loadElement(container, type, child);
		}
	}
	
	public UiRoot loadGui(UiRoot parent, INativeFerry ferry, JsonObject json, String id) {
		UiRoot gui = new GuiCustomMenu(id, ferry, GuiNull.nonNull(parent), json);
		loadElements(gui, json.getAsJsonArray("elements"));
		return gui;
	}
}
