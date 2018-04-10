package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.locale.Locales;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.SoundCategory;

public class ActionAdjustSound implements IGuiAction {
	
	private final UiField<Float> owner;
	
	private final SoundCategory category;
	
	public ActionAdjustSound(JsonObject json, UiField<Float> owner) {
		this.owner = owner;
		
		category = SoundCategory.getByName(json.get("category").getAsString());
		float f = Minecraft.getMinecraft().gameSettings.getSoundLevel(category);
		this.owner.setValue(f);
		updateLabel(f);
	}
	
	@Override
	public void perform(UiRoot screen) {
		GameSettings gs = screen.getMinecraft().gameSettings;
		float f = owner.getValue();
		gs.setSoundLevel(category, f);
        gs.saveOptions();
        
        updateLabel(f);
	}
	
	private void updateLabel(float f) {
		String label = f == 0 ? Locales.translateUnformatted("options.off") : (int)(f * 100.0F) + "%";
        owner.setLabel(Locales.translateUnformatted("soundCategory." + category.getName()) + ": " + label);
	}

}
