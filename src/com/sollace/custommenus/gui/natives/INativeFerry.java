package com.sollace.custommenus.gui.natives;

import com.sollace.custommenus.GuiMenus;
import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.action.IGuiDecider;
import com.sollace.custommenus.gui.container.UiRoot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.text.ITextComponent;

/**
 * Representative of the native game, and any possible callback that a Gui might want to respond with.
 */
public interface INativeFerry {
	
	public static INativeFerry empty(UiRoot parent) {
		return new NativeFerry(parent.getMinecraft());
	}
	
	
	Minecraft game();
	
	/**
	 * Gets the decider provided to this ferry.
	 */
	IGuiDecider decider();
	
	/**
	 * Assigns a decider to be used for this ferry's callback.
	 */
	INativeFerry decider(IGuiDecider decider);
	
	/**
	 * Gets the value of a previously defined key. Returns the key if none was found.
	 */
	String param(String key);
	
	/**
	 * Defines a key on this ferry with the given value.
	 */
	INativeFerry param(String key, String value);
	
	/**
	 * Checks if the given key has been defined for this ferry.
	 */
	boolean has(String key);
	
	/**
	 * Opens a new gui using the parameters provided to this ferry.
	 */
	default INativeFerry open(String gui) {
		GuiMenus.INSTANCE.openMenu(game(), gui, this);
		return this;
	}
	
	/**
	 * Gets a native callback object compatible with most of the native game's callback mechanisms.
	 * Based on the response, it will either delegate to the 'yes' or 'no' callback specified higher above.
	 */
	default NativeYesNoCallback callback() {
		return new NativeYesNoCallback(this, GuiNull.currentScreen(), decider());
	}
	
	/**
	 * Gets the native callback object. Unlike callback() this one will return other objects if they are of compatible types.
	 * @return
	 */
	default GuiYesNoCallback yesno() {
		if (decider() instanceof GuiYesNoCallback) {
			return (GuiYesNoCallback)decider();
		}
		if (callback() == null) return currentScreen();
		return callback();
	}
	
	/**
	 * If this is a multiplayer selection screen, grabs the active server entry. Otherwise returns a new one.
	 * It's the responsibility of callback holder to decide what happens in the latter case.
	 */
	ServerData getActiveServerData();
	
	/**
	 * Stores a serverdata on this ferry. If one is present it is returned by getActiveServerData instead.
	 */
	INativeFerry setActiveServerData(ServerData data);
	
	/**
	 * Gets the player's current score.
	 */
	default int getPlayerScore() {
		return game().player.getScore();
	}
	
	/**
	 * Gets the player's last death message.
	 */
	ITextComponent getPlayerDeathMessage();
	/**
	 * Gets the current active screen.
	 */
	default GuiScreen currentScreen() {
		return game().currentScreen;
	}
	
}
