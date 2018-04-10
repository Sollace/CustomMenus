package com.sollace.custommenus.gui.input;

import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.element.UiElement;

/**
 * Basic field element. Supports setting and getting a value.
 * 
 * @param <T>	The type of value
 */
public interface UiField<T> extends UiElement, INamed {

	/**
	 * Gets this field's current value.
	 */
	T getValue();
	
	/**
	 * Gets the human readable string for this field's current value.
	 */
	String getDisplayString();
	
	/**
	 * Sets this field's value.
	 */
	void setValue(T value);
	
	/**
	 * Gets the owning container for this field.
	 */
	UiContainer getContainer();
}
