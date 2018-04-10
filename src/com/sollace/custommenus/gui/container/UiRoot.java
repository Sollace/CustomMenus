package com.sollace.custommenus.gui.container;

import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.action.IGuiAction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

/**
 * Root UI element for Minecraft screen interfaces.
 */
public interface UiRoot extends UiContainer, IViewPort {
	/**
	 * Gets the unique id used to identify this screen.
	 */
	public String getViewPortId();
	
	/**
	 * Gets the global Minecraft instance related to this screen.
	 */
	Minecraft getMinecraft();
	
	/**
	 * Reloads the current screen. Useful if all you want to do is reload/revert UI changes without causing a navigation.
	 */
	void refresh();
	
	/**
	 * Displays this screen.
	 */
	void show();
	
	/**
	 * Opens a new screen, passing to it the same ferry used to create this one.
	 * This screen will become the parent of the new page, and both will share the same ferry object for
	 * communication back and forth. This screen will be accessible again by calling close on the resulting screen.
	 * <p>
	 * Calling this is equivalent to calling {@code screen.getFerry().open(gui)}
	 * 
	 * @param gui The name of the screen to open
	 */
	void open(String gui);
	
	/**
	 * Closes this screen and returns to its parent.
	 */
	void close();
	
	/**
	 * Enqueues an action to be performed some time in the future. Use this to save things like game options that would otherwise lock the UI.
	 */
	default void performLater(IGuiAction action) {
		action.performLater();
	}
	
	/**
	 * Enqueues an action to be performed in exactly n ticks.
	 */
	void performIn(IGuiAction action, int ticks);
	
	/**
	 * Adds an action to perform when this gui closes.
	 */
	void addCloseListener(IGuiAction action);
	
	/**
	 * Gets a native GuiScree object for use with native GuiScreen code
	 */
	GuiScreen getNative();
	
	/**
	 * Sets the background texture for this screen
	 */
	void setBackground(String resource);
	
	/**
	 * Sets whether this screen should draw its background [default: true]
	 */
	void setDrawBackground(boolean draw);
	
	/**
	 * Sets whether this screen should always draw an opaque background, even when in game.
	 */
	void setAlwaysDrawBackground(boolean draw);
	
	void setPausesGame(boolean pauses); 
	
	/**
	 * Returns true if this screen supports the new system (and should not be overriden).
	 */
	boolean isNonNative();
	
	/**
	 * Returns true if this screen is Null/blank
	 */
	default boolean isNull() {
		return this == GuiNull.NULL;
	}
}
