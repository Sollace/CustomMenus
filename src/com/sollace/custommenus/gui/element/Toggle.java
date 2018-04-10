package com.sollace.custommenus.gui.element;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.locale.Locales;
import com.sollace.custommenus.utils.IJsonReadable;
import com.sollace.custommenus.utils.JsonUtils;

public class Toggle extends Button implements UiField<Boolean> {
	
	private boolean on = false;
	
	private String baseString = "";
	
	private String name = null;
	
	private ToggleState onState = new ToggleState(),
						offState = new ToggleState();
	
	public Toggle(UiContainer container) {
		super(container);
	}
	
	@Override
	public Toggle init(JsonObject json) {
		super.init(json);
		if (json.has("state")) on = json.get("state").getAsString().toLowerCase().contentEquals("on");
		name = JsonUtils.get(json, "name", name);
		JsonUtils.get(json, "on", onState);
		JsonUtils.get(json, "off", offState);
		
		updateLabel();
		return this;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public Boolean getValue() {
		return on;
	}
	
	@Override
	public String getDisplayString() {
		String stateString = getState().text;
		if (stateString != null) {
			return stateString;
		}
		stateString = baseString;
		if (baseString.length() > 0) {
			stateString += ": ";
		}
		return stateString + Locales.translateUnformatted(on ? "options.on" : "options.off");
	}
	
	@Override
	public void setValue(Boolean value) {
		on = value;
		updateLabel();
	}
	
	@Override
	public Button setLabel(String label) {
		baseString = Locales.translateUnformatted(label);
		return this;
	}
	
	protected ToggleState getState() {
		return on ? onState : offState;
	}
	
	private void updateLabel() {
		image = getState().image;
		if (image != null) {
			image.x = x;
			image.y = y;
		}
		
		displayString = getDisplayString();
	}
	
	@Override
	public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
		setValue(!on);
		return super.performAction(mouseX, mouseY, sender);
	}
	
	protected class ToggleState implements IJsonReadable {
		private String text;
		private Image image;
		
		public ToggleState init(JsonObject json) {
			if (json.has("text")) {
				text = json.get("text").getAsString();
			} else {
				image = createEmbeddedImage(json);
			}
			return this;
		}
	}
}
