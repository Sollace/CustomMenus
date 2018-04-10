package com.sollace.custommenus.gui.packing;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;

@FunctionalInterface
public interface UiPacker {
	/**
	 * Arranges (packs) the provided container's contents.
	 */
	void pack(UiContainer container);
	
	/**
	 * Initializes this packer with any extra json values it may take.
	 */
	default void init(JsonObject json) {
		
	}
}
