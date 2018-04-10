package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.input.UiField;

import net.minecraft.client.Minecraft;
import net.minecraft.world.EnumDifficulty;

public class ActionChangeDifficulty implements IGuiAction {
	
	private UiField<Integer> field = null;
	
	private EnumDifficulty value;
	
	public ActionChangeDifficulty(JsonObject json, UiField<Integer> owner) {
		field = owner;
		
		Minecraft mc = Minecraft.getMinecraft();
		
		if (mc.world != null) field.setValue(mc.world.getWorldInfo().getDifficulty().getDifficultyId());
		
		field.setEnabled(mc.world != null && mc.isSingleplayer() && !mc.world.getWorldInfo().isHardcoreModeEnabled() && !mc.world.getWorldInfo().isDifficultyLocked());
	}
	
	public ActionChangeDifficulty(JsonObject json, UiElement owner) {
		if (json.has("difficulty")) value = EnumDifficulty.valueOf(json.get("difficulty").getAsString());
	}
	
	@Override
	public void perform(UiRoot screen) {
		Minecraft mc = screen.getMinecraft();
		if (mc.world != null && mc.isSingleplayer() && !mc.world.getWorldInfo().isHardcoreModeEnabled()) {
			if (field != null) value = EnumDifficulty.getDifficultyEnum(field.getValue());
			mc.world.getWorldInfo().setDifficulty(value);
		}
	}

}
