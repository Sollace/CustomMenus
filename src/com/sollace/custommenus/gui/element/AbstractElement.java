package com.sollace.custommenus.gui.element;

import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.gui.geometry.BoundingBox;
import com.sollace.custommenus.gui.geometry.EnumAlignment;
import com.sollace.custommenus.gui.geometry.UiAligned;
import com.sollace.custommenus.gui.geometry.UiBounded;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public abstract class AbstractElement extends Gui implements UiElement, UiBounded, UiAligned {
	
	private int offsetX, offsetY;
	protected int x, y;
	
	protected int width, height;
	
	protected boolean enabled = true, visible = true;
	
	protected SoundEvent sound = null;
	
	protected IGuiAction action;
	
	protected EnumAlignment alignment = EnumAlignment.TOP_LEFT;
	
	protected IRenderable tooltip = null;
	
	@Override
	public AbstractElement setPos(int x, int y) {
		offsetX = x;
		offsetY = y;
		return this;
	}
	
	@Override
	public AbstractElement setSize(int w, int h) {
		this.width = w;
		this.height = h;
		return this;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(x, y, width, height);
	}

	@Override
	public AbstractElement setEnabled(boolean enable) {
		this.enabled = enable;
		return this;
	}
	
	@Override
	public AbstractElement setToolTip(IRenderable tooltip) {
		this.tooltip = tooltip;
		return this;
	}
	
	public IRenderable getToolTipAt(int mouseX, int mouseY) {
		return tooltip;
	}
	
	@Override
	public void setSound(String sound) {
		this.sound = SoundEvent.REGISTRY.getObject(new ResourceLocation(sound));
	}
	
	@Override
	public AbstractElement setAction(IGuiAction action) {
		this.action = action;
		return this;
	}
	
	@Override
	public void setAlignment(EnumAlignment alignment) {
		this.alignment = alignment;
	}
	
	@Override
	public AbstractElement setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}
	
	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		return visible && mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
	}
	
	@Override
	public void playSound(SoundHandler sounds) {
		if (sound != null) sounds.playSound(PositionedSoundRecord.getMasterRecord(sound, 1));
	}
	
	@Override
	public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
		if (action != null) {
			action.perform(sender);
			return true;
		}
		return false;
	}
	
	@Override
	public void reposition(UiView container) {
		this.x = alignment.computeX(offsetX, width, container.getViewPort().getWidth());
		this.y = alignment.computeY(offsetY, height, container.getViewPort().getHeight());
	}
}
