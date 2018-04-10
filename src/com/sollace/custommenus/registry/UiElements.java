package com.sollace.custommenus.registry;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.sollace.custommenus.gui.container.Container;
import com.sollace.custommenus.gui.container.ScrollContainer;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.element.Button;
import com.sollace.custommenus.gui.element.Gradient;
import com.sollace.custommenus.gui.element.Image;
import com.sollace.custommenus.gui.element.KeyBind;
import com.sollace.custommenus.gui.element.Label;
import com.sollace.custommenus.gui.element.NamedButton;
import com.sollace.custommenus.gui.element.ProgressBar;
import com.sollace.custommenus.gui.element.Slider;
import com.sollace.custommenus.gui.element.Switch;
import com.sollace.custommenus.gui.element.TextBox;
import com.sollace.custommenus.gui.element.Throbber;
import com.sollace.custommenus.gui.element.Toggle;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.element.builtin.Item;
import com.sollace.custommenus.gui.element.builtin.Panorama;
import com.sollace.custommenus.gui.element.builtin.PlayerDummy;
import com.sollace.custommenus.gui.element.builtin.Splash;
import com.sollace.custommenus.gui.list.List;
import com.sollace.custommenus.gui.overlays.LiteLoaderPane;
import com.sollace.custommenus.gui.overlays.RealmsNotifications;
import com.sollace.custommenus.reflection.IInstantiator;

/**
 * Registry of all the different element types.
 * <p>
 * <b>Usage:</b>
 * 
 * {@code UiElements.register(<name>, <class>);}
 */
public final class UiElements {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Map<String, IInstantiator<? extends UiElement>> element_types = Maps.newHashMap();
	
	private static final Class<?>[] PARS_1 = new Class<?>[] {UiContainer.class};
	
	static {
		register("button", Button.class);
		register("named-button", NamedButton.class);
		register("slider", Slider.class);
		register("label", Label.class);
		register("image", Image.class);
		register("gradient", Gradient.class);
		register("switch", Switch.class);
		register("toggle", Toggle.class);
		register("splash-text", Splash.class);
		register("realms-notifications", RealmsNotifications.class);
		register("panorama", Panorama.class);
		register("container", Container.class);
		register("scroll", ScrollContainer.class);
		register("litepanel", LiteLoaderPane.class);
		register("keybind", KeyBind.class);
		register("textbox", TextBox.class);
		register("list", List.class);
		register("throbber", Throbber.class);
		register("progressbar", ProgressBar.class);
		register("itemstack", Item.class);
		register("dummy", PlayerDummy.class);
	}
	
	public static <T extends UiElement> void register(String name, Class<T> type) {
		element_types.put(name, IInstantiator.<T>create(type, PARS_1));
	}
	
	public static UiElement createForKey(UiContainer container, String key) {
		
		if (element_types.containsKey(key)) {
			return element_types.get(key).newInstance(container);
		}
		
		LOGGER.warn(String.format("Invalid element type '%s", key));
		return null;
	}
}
