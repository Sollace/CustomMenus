package com.sollace.custommenus.gui.element;

import java.util.regex.Pattern;

import com.google.common.base.Predicate;
import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.gui.input.IKeyResponder;
import com.sollace.custommenus.gui.input.IMouseResponder;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.utils.JsonUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

public class TextBox extends AbstractElement implements UiField<String>, Predicate<String>, IMouseResponder, IKeyResponder {
	
	private GuiTextField nativeObject;
	
	protected String name = null,
			placeholder = "",
			value = "";
	
	protected int enabledColor = 14737632, disabledColor = 7368816, maxLength = 32;
	
	protected final UiContainer container;
	
	protected Pattern matcher = null;
	
	public TextBox(UiContainer container) {
		this.container = container;
		nativeObject = createNative();
	}
	
	@Override
	public TextBox init(JsonObject json) {
		enabledColor = JsonUtils.get(json, "enabledColor", enabledColor);
		disabledColor = JsonUtils.get(json, "disabledColor", disabledColor);
		placeholder = JsonUtils.get(json, "placeholder", placeholder).trim();
		maxLength = Math.max(32, JsonUtils.get(json, "maxLength", maxLength));
		value = JsonUtils.get(json, "value", value);
		matcher = JsonUtils.get(json, "validate", matcher, Pattern.class);
		nativeObject = createNative();
		return this;
	}
	
	@Override
	public TextBox setLabel(String label) {
		placeholder = label;
		return this;
	}
	
	@Override
	public void setTexture(String resource) {
		
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
		return nativeObject == null ? value : nativeObject.getText();
	}
	
	@Override
	public String getDisplayString() {
		value = getValue();
		if (value.isEmpty()) {
			return placeholder;
		}
		return value;
	}
	
	@Override
	public void setValue(String value) {
		this.value = value == null ? "" : value.trim();
		if (nativeObject != null) nativeObject.setText(getDisplayString());
	}
	
	public boolean isFocused(int mouseX, int mouseY) {
		return super.isFocused(mouseX, mouseY) || nativeObject.isFocused();
	}
	
	@Override
	public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
		boolean was = nativeObject.isFocused();
		nativeObject.mouseClicked(mouseX, mouseY, 0);
		if (was && !nativeObject.isFocused()) {
			super.performAction(mouseX, mouseY, sender);
			return true;
		}
		return false;
	}
	
	@Override
	public void reposition(UiView container) {
		super.reposition(container);
		nativeObject = createNative();
	}
	
	protected GuiTextField createNative() {
		GuiTextField nativeObject = new GuiTextField(0,
				Minecraft.getMinecraft().fontRenderer, x, y, width, height);
		nativeObject.setText(getDisplayString());
		nativeObject.setTextColor(enabledColor);
		nativeObject.setDisabledTextColour(disabledColor);
		nativeObject.setValidator(this);
		nativeObject.setMaxStringLength(maxLength);
		return nativeObject;
	}
	
	private int timer = 0;
	
	@Override
	public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		if (!visible) return;
		timer = (timer + 1) % 4;
		if (timer == 0) nativeObject.updateCursorCounter();
		nativeObject.drawTextBox();
	}
	
	@Override
	public UiContainer getContainer() {
		return container;
	}
	
	@Override
	public boolean keyPressed(UiRoot sender, char key, int keyCode) {
		return nativeObject.textboxKeyTyped(key, keyCode);
	}
	
	@Override
	public void mouseMove(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		
	}
	
	@Override
	public void mouseUp(int mouseX, int mouseY) {
		
	}
	
	@Override
	public boolean apply(String input) {
		return matcher == null || matcher.matcher(input).find();
	}
}
