package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.input.UiField;

import net.minecraft.client.Minecraft;

public class ActionLockDifficulty implements IGuiAction {
	
	private final UiField<Boolean> field;
	
	private String difficulty = null;
	
	public ActionLockDifficulty(JsonObject json, UiField<Boolean> owner) {
		field = owner;
		
		if (json.has("difficultySelectField")) difficulty = json.get("difficultySelectField").getAsString();
		
		Minecraft mc = Minecraft.getMinecraft();
		
		if (mc.world != null) {
			field.setValue(mc.world.getWorldInfo().isDifficultyLocked());
			field.setEnabled(!field.getValue());
		}
	}
	
	@Override
	public void perform(UiRoot screen) {
		Minecraft mc = screen.getMinecraft();
		
		if (mc.world == null) return;
		
		mc.world.getWorldInfo().setDifficultyLocked(true);
		field.setEnabled(false);
		field.setValue(true);
		
		if (difficulty != null) {
			UiField<?> diff = screen.getField(difficulty);
			if (diff != null) diff.setEnabled(false);
		}
	}

}
