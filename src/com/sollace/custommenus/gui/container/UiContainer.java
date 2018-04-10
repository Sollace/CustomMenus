package com.sollace.custommenus.gui.container;

import java.util.Iterator;

import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.gui.natives.INativeFerry;
import com.sollace.custommenus.gui.packing.UiPacker;

/**
 * Base interface for containers - any element that can contain other elements.
 */
public interface UiContainer extends UiView {
	
	/**
	 * Gets the native ferry used for creating this screen. May be used to reflect values back to the caller,
	 * or to pass those on to child screens via {@link open()}
	 */
	INativeFerry getFerry();
	
	/**
	 * Adds a child element to this container. If the element is an instance of UiField and has a name,
	 * it will also be added as a field as per addField(child)
	 */
	void addChild(UiElement child);

	/**
	 * Adds a named field to this container. If the field does not have a name defined (getName returns null) does nothing.
	 */
	void addField(UiField<?> field);
	
	/**
	 * Gets a child from this container by its unique index / position inside the children list.
	 */
	UiElement getChild(int id);
	
	/**
	 * Gets an iterator of this container's child elements.
	 */
	Iterator<UiElement> getElementsIterator();
	
	/**
	 * Gets a named field if one exists, otherwise returns null.
	 */
	<T> UiField<T> getField(String name);
	
	/**
	 * Sets whether this container should draw its background
	 */
	void setDrawBackground(boolean draw);
	
	/**
	 * Sets a background to draw behind thiss container.
	 */
	void setBackground(String resource);
	
	/**
	 * Sets a layout packer for arranging elements inside this container. Packing is done before UiElement.reposition
	 */
	void setPacker(UiPacker packer);
}
