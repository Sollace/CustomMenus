package com.sollace.custommenus.gui.element;

import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.input.UiField;

public class NamedButton extends Button implements UiField<String> {
	
	private String name;
	
	public NamedButton(UiContainer container) {
		super(container);
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
	public String getValue() {
		return name;
	}

	@Override
	public String getDisplayString() {
		return displayString;
	}

	@Override
	public void setValue(String value) {
		
	}

}
