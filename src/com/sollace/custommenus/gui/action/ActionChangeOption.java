package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class ActionChangeOption implements IGuiAction {
	private GameSettings.Options option;
	
	private final UiElement owner;
	
	private static boolean saveNeeded = false;
	
	public ActionChangeOption(JsonObject json, UiElement owner) {
		this.owner = owner;
		
		option = GameSettings.Options.valueOf(json.get("option").getAsString().toUpperCase());
		
		owner.setLabel(Minecraft.getMinecraft().gameSettings.getKeyBinding(option));
	}
	
	@Override
	public void perform(UiRoot screen) {
		GameSettings gs = screen.getMinecraft().gameSettings;
		
		//3D anaglyph locks the entire client, so we have to guard against that
		if (option == GameSettings.Options.ANAGLYPH) {
			boolean from = gs.anaglyph;
			boolean to = !gs.anaglyph;
			
			gs.anaglyph = to;
			owner.setLabel(gs.getKeyBinding(option));
			gs.anaglyph = from;
			
			saveNeeded = true;
			screen.performLater(currentScreen -> {
				if (!saveNeeded) return;
				saveNeeded = false;
				if (gs.anaglyph == to) return;
				
				gs.setOptionValue(option, 1); 
			});
			
			return;
		}
		
		gs.setOptionValue(option, 1); // should actually be cycleValue but oh well
		owner.setLabel(gs.getKeyBinding(option));
	}
	
}
