package com.sollace.custommenus.gui.element;

import java.util.function.Supplier;

import com.google.gson.JsonObject;
import com.sollace.custommenus.GuiMenus;
import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.container.Container;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.gui.geometry.EnumAlignment;
import com.sollace.custommenus.gui.geometry.UiAligned;
import com.sollace.custommenus.gui.input.IKeyResponder;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.gui.list.UiListItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;

public class KeyBind extends Container implements UiField<Integer>, IKeyResponder, UiListItem<KeyBind>, UiAligned {
	
	private String name = null;
	
	protected Label label;
	protected Button bindButton, resetButton;
	
	protected KeyBinding binding;
	
	protected Supplier<String> displayValue = () -> "";
	
	private final GameSettings gs;
	
	protected boolean isSelecting = false, valid = true;
	
	private IGuiAction action = null;
	
	protected EnumAlignment alignment = EnumAlignment.TOP_CENTER;
	
	protected int offsetX = 0, offsetY = 0;
	
	private KeyBinding findBinding(String key) {
		for (KeyBinding i : gs.keyBindings) {
			if (i.getKeyDescription().contentEquals(key)) return i;
		}
		return null;
	}
	
	private boolean isBindingValid() {
		if (binding.getKeyCode() != 0) {
            for (KeyBinding i : gs.keyBindings) {
                if (i != binding && i.getKeyCode() == binding.getKeyCode()) {
                    return false;
                }
            }
        }
		return true;
	}
	
	public KeyBind(UiContainer container) {
		super(container);
		
		gs = Minecraft.getMinecraft().gameSettings;
		
		label = new Label(this);
		bindButton = new Button(this);
		resetButton = new Button(this);
		addChild(label);
		addChild(bindButton);
		addChild(resetButton);
		
		bindButton.setAction(screen -> {
			isSelecting = true;
		});
		resetButton.setAction(screen -> {
			isSelecting = false;
			if (binding != null) setValue(binding.getKeyCodeDefault());
			if (action != null) action.perform(screen);
		});
	}
	
	@Override
	public KeyBind setSize(int w, int h) {
		super.setSize(w, h);
		
		label.setSize(w - bindButton.width - resetButton.width, h);
		bindButton.setPos(-resetButton.width - bindButton.width - 30, -bindButton.height/2);
		resetButton.setPos(-resetButton.width - 20, -resetButton.height/2);
		label.setPos(0, -label.height);
		return this;
	}
	
	@Override
	public KeyBind setPos(int x, int y) {
		super.setPos(x, y);
		offsetX = x;
		offsetY = y;
		return this;
	}
	
	@Override
	public KeyBind setLabel(String label) {
		this.label.setLabel(label);
		return this;
	}
	
	public KeyBind setBinding(KeyBinding binding) {
		this.binding = binding;
		setLabel(binding.getKeyDescription());
		displayValue = KeyBinding.getDisplayString(binding.getKeyDescription());
		valid = isBindingValid();
		
		resetButton.enabled = binding.getKeyCode() != binding.getKeyCodeDefault();
		
		return this;
	}
	
	public void setBinding(String key) {
		binding = findBinding(key);
		setLabel(key);
		displayValue = KeyBinding.getDisplayString(key);
		valid = isBindingValid();
		
		resetButton.enabled = binding.getKeyCode() != binding.getKeyCodeDefault();
	}
	

	@Override
	public Integer getValue() {
		return binding == null ? 0 : binding.getKeyCode();
	}

	@Override
	public void setValue(Integer value) {
		if (binding != null) {
			gs.setOptionKeyBinding(binding, value);
			valid = isBindingValid();
			resetButton.enabled = binding.getKeyCode() != binding.getKeyCodeDefault();
		}
	}
	
	@Override
	public KeyBind init(JsonObject json) {
		super.init(json);
		
		if (json.has("binding")) setBinding(json.get("binding").getAsString());
		
		if (json.has("label")) GuiMenus.jsonFactory().populateElement(label, this, json.get("label").getAsJsonObject());
		if (json.has("bind")) GuiMenus.jsonFactory().populateElement(bindButton, this, json.get("bind").getAsJsonObject());
		if (json.has("reset")) GuiMenus.jsonFactory().populateElement(resetButton, this, json.get("reset").getAsJsonObject());
		
		return this;
	}
	
	@Override
	public void setTexture(String resource) {
		bindButton.setTexture(resource);
		resetButton.setTexture(resource);
	}
	

	@Override
	public void setSound(String sound) {
		bindButton.setSound(sound);
		resetButton.setSound(sound);
	}
	
	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		return bindButton.enabled && (isSelecting || super.isFocused(mouseX, mouseY));
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
	public UiElement setAction(IGuiAction action) {
		this.action = action;
		return this;
	}
	
	@Override
	public KeyBind setEnabled(boolean enable) {
		bindButton.enabled = enable;
		return this;
	}
	
	@Override
	public UiContainer getContainer() {
		return container;
	}
	
	@Override
	public String getDisplayString() {
		if (isSelecting) return ">" + TextFormatting.YELLOW + displayValue.get() + TextFormatting.WHITE + "<";
		if (!valid) return TextFormatting.RED + displayValue.get();
		
		return displayValue.get();
	}
	
	@Override
	public void reposition(UiView container) {
		x = alignment.computeX(offsetX, getWidth(), container.getViewPort().getWidth());
		y = alignment.computeY(offsetY, getHeight(), container.getViewPort().getHeight());
		super.reposition(container);
	}
	
	@Override
	public void renderTranslated(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		bindButton.setLabel(getDisplayString());
		resetButton.enabled = bindButton.enabled && binding != null && binding.getKeyCode() != binding.getKeyCodeDefault();
		
		label.render(sender, mouseX, mouseY, partialTicks);
		
		bindButton.render(sender, mouseX, mouseY, partialTicks);
		resetButton.render(sender, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean keyPressed(UiRoot sender, char key, int keyCode) {
		if (isSelecting) {
			setValue(keyCode);
			isSelecting = false;
			if (action != null) action.perform(sender);
			return true;
		}
		return false;
	}

	@Override
	public void drawSelectionBox() {
		
	}

	@Override
	public boolean isSelected() {
		return false;
	}

	@Override
	public void setAlignment(EnumAlignment alignment) {
		this.alignment = alignment;
	}

	@Override
	public void itemMoved(int newPosition) {
		
	}
	
	@Override
	public boolean isMoveable() {
		return false;
	}
}
