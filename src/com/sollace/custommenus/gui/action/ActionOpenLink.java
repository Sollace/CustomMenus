package com.sollace.custommenus.gui.action;

import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;

import java.awt.Desktop;

public class ActionOpenLink implements IGuiAction {
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final String url;
	
	public ActionOpenLink(JsonObject json, UiElement owner) {
		url = json.get("link").getAsString();
	}
	
	@Override
	public void perform(UiRoot screen) {
		try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Throwable throwable) {
            LOGGER.error("Couldn't open link", throwable);
        }
	}

}
