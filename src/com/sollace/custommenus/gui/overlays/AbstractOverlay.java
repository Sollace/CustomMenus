package com.sollace.custommenus.gui.overlays;

import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.element.IRenderable;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.input.UiField;

import net.minecraft.client.audio.SoundHandler;

public abstract class AbstractOverlay implements UiOverlay, UiField<Boolean> {
	private boolean enabled = true;
	
	protected UiContainer container;
	
	public AbstractOverlay(UiContainer container) {
		this.container = container;
	}
	
	@Override
	public void setName(String name) {
		
	}

	@Override
	public Boolean getValue() {
		return enabled;
	}

	@Override
	public void setValue(Boolean value) {
		enabled = value;
	}
	
	@Override
	public UiContainer getContainer() {
		return container;
	}
	
	@Override
	public AbstractOverlay setEnabled(boolean enable) {
		setValue(enable);
		return this;
	}
	
	@Override
	public LiteLoaderPane setVisible(boolean visible) {
		return null;
	}
	
	@Override
	public UiElement setLabel(String label) {
		return this;
	}

	@Override
	public void setTexture(String resource) {
		
	}

	@Override
	public void setSound(String sound) {
		
	}

	@Override
	public void playSound(SoundHandler sounds) {
		
	}
	
	@Override
	public AbstractOverlay setToolTip(IRenderable tooltip) {
		return this;
	}

	@Override
	public IRenderable getToolTipAt(int mouseX, int mouseY) {
		return null;
	}
}
