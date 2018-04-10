package com.sollace.custommenus.gui.element;

import com.sollace.custommenus.gui.action.IActionable;
import com.sollace.custommenus.gui.element.builtin.ToolTip;
import com.sollace.custommenus.gui.geometry.IPositioned;
import com.sollace.custommenus.resources.FormattedString;
import com.sollace.custommenus.utils.IJsonReadable;

import net.minecraft.client.audio.SoundHandler;

/**
 * A rendered UI element
 */
public interface UiElement extends IRenderable, IActionable, IPositioned, IJsonReadable {
	
	/**
	 * Sets the label for this element.
	 */
	UiElement setLabel(String label);
	
	/**
	 * Sets a tooltip to render when this element receives focus.
	 */
	UiElement setToolTip(IRenderable tooltip);
	
	/**
	 * Sets a tooltip to render when this element receives focus.
	 */
	default UiElement setToolTip(FormattedString tooltip) {
		setToolTip(new ToolTip(tooltip));
		return this;
	}
	
	/**
	 * Sets a tooltip to render when this element receives focus.
	 */
	default UiElement setToolTip(String tooltip) {
		return setToolTip(FormattedString.create(tooltip));
	}
	
	/**
	 * Sets whether this element is enabled and will accept user interactions.
	 */
	UiElement setEnabled(boolean enable);
	
	/**
	 * Sets a texture to be used when rendering this element.
	 */
	void setTexture(String resource);
	
	/**
	 * Sets a sound to play when this element is clicked.
	 */
	void setSound(String sound);
	
	/**
	 * Plays this element's click sound. Called before performAction.
	 */
	void playSound(SoundHandler sounds);
	
	/**
	 * Gets a tooltip to render for this element. May be null.
	 */
	IRenderable getToolTipAt(int mouseX, int mouseY);
}
