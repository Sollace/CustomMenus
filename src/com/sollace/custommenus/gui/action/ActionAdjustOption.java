package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.input.UiField;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class ActionAdjustOption implements IGuiAction {
	
	private final UiField<Float> owner;
	
	private final GameSettings.Options option;
	
	private static boolean saveNeeded = false;
	
	@SuppressWarnings("unchecked")
	public ActionAdjustOption(JsonObject json, UiElement owner) {
		GameSettings gs = Minecraft.getMinecraft().gameSettings;
		option = GameSettings.Options.valueOf(json.get("option").getAsString().toUpperCase());
		
		this.owner = (UiField<Float>)owner;
		this.owner.setValue(option.normalizeValue(gs.getOptionFloatValue(option)));
		owner.setLabel(gs.getKeyBinding(option));
	}
	
	@Override
	public void perform(UiRoot screen) {
		GameSettings gs = screen.getMinecraft().gameSettings;
		float newValue = option.denormalizeValue(owner.getValue());
		float oldValue = gs.getOptionFloatValue(option);
		
		owner.setValue(option.normalizeValue(newValue));
		
		if (oldValue == newValue) return;
		
		//Mipmapping locks the entire client, so we have to guard against that
		if (option == GameSettings.Options.MIPMAP_LEVELS) {
			gs.mipmapLevels = (int)newValue;
			owner.setLabel(gs.getKeyBinding(option));
			gs.mipmapLevels = (int)oldValue;
			
			saveNeeded = true;
			screen.performLater(currentScreen -> {
				if (!saveNeeded) return;
				saveNeeded = false;
				float to = option.denormalizeValue(owner.getValue());
				float from = gs.getOptionFloatValue(option);
				
				if (from == to) return;
				
				gs.setOptionFloatValue(option, to);
				gs.saveOptions();
			});
			
			return;
		}
		
		gs.setOptionFloatValue(option, newValue);
		gs.saveOptions();
		owner.setLabel(gs.getKeyBinding(option));
	}
}
