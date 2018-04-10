package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class ActionResetControls implements IGuiAction {
	
	private final GameSettings gs;
	private final UiElement owner;
	
	public ActionResetControls(JsonObject json, UiElement owner) {
		this.owner = owner;
		gs = Minecraft.getMinecraft().gameSettings;
		owner.setEnabled(isAnyChanged());
	}
	
	@Override
	public void perform(UiRoot screen) {
		for (KeyBinding keybinding : gs.keyBindings) {
            keybinding.setKeyCode(keybinding.getKeyCodeDefault());
        }

        KeyBinding.resetKeyBindingArrayAndHash();
        
        owner.setEnabled(false);
	}
	
	private boolean isAnyChanged() {
		for (KeyBinding i : gs.keyBindings) if (i.getKeyCode() != i.getKeyCodeDefault()) return true;
		return false;
	}
}
