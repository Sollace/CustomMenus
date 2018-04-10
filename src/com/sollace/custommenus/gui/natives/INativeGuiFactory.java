package com.sollace.custommenus.gui.natives;

import net.minecraft.client.gui.GuiScreen;

/**
 * Factory Interface for generating native GuiScreen instances. Used in conjunction with UiNatives.
 * 
 * <p>
 * {@code UiNatives.register("mainmenu", GuiMainMenu.class, ferry -> new GuiMainMenu());}
 * </p>
 */
@FunctionalInterface
public interface INativeGuiFactory<T extends GuiScreen> {
	/**
	 * Creates a new GuiScreen instance.
	 * 
	 * @param ferry		A ferry for moving data back and forth between the native client and new client systems
	 */
	T newInstance(INativeFerry ferry);
}
