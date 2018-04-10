package com.sollace.custommenus.gui.element;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.input.IMouseResponder;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.utils.JsonUtils;

public class Slider extends Button implements UiField<Float>, IMouseResponder {
	
	private String name = null;
	
	private boolean dragging = false;
	
	private float min = 0, max = 1, value = .5f;
	
	public Slider(UiContainer container) {
		super(container);
	}
	
	@Override
	protected void renderGuiElement() {
		super.renderGuiElement();
		int i = hovered ? 2 : 1;
        drawTexturedModalRect(x + (int)(value * (width - 8)), y, 0, 46 + i * 20, 4, 20);
        drawTexturedModalRect(x + (int)(value * (width - 8)) + 4, y, 196, 46 + i * 20, 4, 20);
	}
	
	@Override
	public Slider init(JsonObject json) {
		super.init(json);
		min = JsonUtils.get(json, "min", min);
		max = JsonUtils.get(json, "max", max);
		value = JsonUtils.get(json, "value", value);
		name = JsonUtils.get(json, "name", name);
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
	public Float getValue() {
		return min + (value * (max - min));
	}
	
	@Override
	public String getDisplayString() {
		return getValue().toString();
	}
	
	@Override
	public void setValue(Float value) {
		value = clamp(value, min, max);
		this.value = (value - min) / (max - min);
	}
	
	private void updateValue(float mouseX) {
		value = clamp((mouseX - (x + 4)) / (float)(width - 8), 0, 1);
	}
	
	protected float clamp(float value, float min, float max) {
		return value < min ? min : value > max ? max : value;
	}
	
	@Override
	protected int getHoverState() {
        return 0;
    }
	
	@Override
	public void mouseMove(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		if (!visible || !dragging) return;
		performAction(mouseX, mouseY, sender);
	}

	@Override
	public void mouseUp(int mouseX, int mouseY) {
		dragging = false;
	}
	
	@Override
	public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
        dragging = true;
        updateValue(mouseX);
        return super.performAction(mouseX, mouseY, sender);
	}
}
