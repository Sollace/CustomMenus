package com.sollace.custommenus.gui.list.datasource;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.element.KeyBind;
import com.sollace.custommenus.gui.element.Label;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.gui.list.UiList;
import com.sollace.custommenus.privileged.IGame;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class ControlsSource implements IDataSource<KeyBind> {
	
	protected GameSettings gs;
	
	protected String reset = null;
	
	private UiList<KeyBind> owner;
	
	public ControlsSource(IGame mc) {
		gs = mc.settings();
	}
	
	@Override
	public void populateList(JsonObject json, UiList<KeyBind> list) {
		owner = list;
		
		if (json.has("resetButton")) reset = json.get("resetButton").getAsString();
		
		KeyBinding[] bindings = ArrayUtils.clone(gs.keyBindings);
		Arrays.sort(bindings);
		
		String currentGroup = "";
		
		for (KeyBinding i : bindings) {
			if (!i.getKeyCategory().contentEquals(currentGroup)) {
				currentGroup = i.getKeyCategory();
				list.addChild(new Label(list).setLabel(currentGroup));
			}
			
			list.addChild(new KeyBind(list).setBinding(i));
		}
	}
	
	public void selectionChanged() {
		if (reset != null) {
			UiField<?> field = owner.getField(reset);
			if (field != null) field.setEnabled(isAnyChanged());
		}
	}
	
	private boolean isAnyChanged() {
		for (KeyBinding i : gs.keyBindings) if (i.getKeyCode() != i.getKeyCodeDefault()) return true;
		return false;
	}

	
}
