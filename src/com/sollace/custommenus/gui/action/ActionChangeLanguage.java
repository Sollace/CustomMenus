package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;

public class ActionChangeLanguage implements IGuiAction {
	
	private final Minecraft mc;
	
	private final LanguageManager langs;
	
	private final Language lang;
	
	public ActionChangeLanguage(JsonObject json, UiElement owner) {
		mc = Minecraft.getMinecraft();
		langs = mc.getLanguageManager();
		lang = langs.getLanguage(json.get("lang").getAsString());
	}
	
	public ActionChangeLanguage(Language lang) {
		mc = Minecraft.getMinecraft();
		langs = mc.getLanguageManager();
		this.lang = lang;
	}
	
	@Override
	public void perform(UiRoot screen) {
		if (!langs.getCurrentLanguage().equals(lang)) {
			langs.setCurrentLanguage(lang);
			mc.gameSettings.language = lang.getLanguageCode();
			mc.gameSettings.saveOptions();
			
			mc.refreshResources();
			
			mc.fontRenderer.setUnicodeFlag(langs.isCurrentLocaleUnicode());
            mc.fontRenderer.setBidiFlag(lang.isBidirectional());
            
    		screen.refresh();
		}
	}

}
