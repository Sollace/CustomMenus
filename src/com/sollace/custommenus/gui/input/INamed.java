package com.sollace.custommenus.gui.input;

/**
 * Things that can be named.
 */
public interface INamed {
	/**
	 * Gets a name for this field if on was assigned. Used by actions to retrieve a pre-defined field. 
	 */
	String getName();
	
	/**
	 * Sets this field's name
	 */
	void setName(String name);
}
