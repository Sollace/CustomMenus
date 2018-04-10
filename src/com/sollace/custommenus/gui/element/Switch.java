package com.sollace.custommenus.gui.element;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.locale.Locales;
import com.sollace.custommenus.utils.JsonUtils;

public class Switch extends Button implements UiField<Integer> {
	
	private int selectedIndex = 0;
	
	private String[] values = new String[0], labels = new String[0];
	
	private String format = "%s: %s";
	
	private String name = null, displayValue;
	
	public Switch(UiContainer container) {
		super(container);
	}
	
	@Override
	public Switch init(JsonObject json) {
		format = JsonUtils.get(json, "format", format);
		name = JsonUtils.get(json, "name", name);
		
		labels = values = JsonUtils.get(json, "values", values);
		labels = JsonUtils.get(json, "labels", values, values.length);
		setValue(JsonUtils.get(json, "selectedIndex", selectedIndex));
		
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
	public Integer getValue() {
		return selectedIndex;
	}
	
	@Override
	public String getDisplayString() {
		return Locales.translateUnformatted(labels[selectedIndex]);
	}
	
	@Override
	public void setValue(Integer value) {
		if (values != null && values.length > 0) {
			selectedIndex = value % values.length;
			displayValue = getDisplayString();
		}
	}
	
	protected String getLabelForRender() {
		return String.format(format, super.getLabelForRender(), displayValue);
	}
	
	@Override
	public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
		setValue(selectedIndex + 1);
		return super.performAction(mouseX, mouseY, sender);
	}
}
