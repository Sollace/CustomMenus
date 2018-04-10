package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.locale.Locales;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class ActionToggleSkin implements IGuiAction {
	private EnumPlayerModelParts part;
	
	private final UiElement owner;
	
	public ActionToggleSkin(JsonObject json, UiElement owner) {
		this.owner = owner;
		
		part = EnumPlayerModelParts.valueOf(json.get("part").getAsString());
		
		updateLabel();
	}
	
	@Override
	public void perform(UiRoot screen) {
		screen.getMinecraft().gameSettings.switchModelPartEnabled(part);
		updateLabel();
	}
	
	private void updateLabel() {
		String s = Locales.translateUnformatted(Minecraft.getMinecraft().gameSettings.getModelParts().contains(part) ? "options.on" : "options.off");
        owner.setLabel(part.getName().getFormattedText() + ": " + s);
	}
}
