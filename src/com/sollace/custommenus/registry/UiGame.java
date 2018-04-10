package com.sollace.custommenus.registry;

import java.util.Map;

import com.google.common.collect.Maps;
import com.sollace.custommenus.GuiProgressor;
import com.sollace.custommenus.gui.natives.INativeFerry;
import com.sollace.custommenus.gui.natives.INativeVariable;
import com.sollace.fml.IFML;

/**
 * Registry of all game values that need to accessible from within GUIs.
 * <p>
 * Currently only used translation strings for dynamic parameters.
 * <p>
 * <b>Usage:</b>
 * 
 * {@code UiGame.register(<variable_name>, ferry -> "my value");}
 *
 */
public class UiGame {
	
	private static final Map<String, INativeVariable> variables = Maps.newHashMap();
	
	static {
		register("minecraft:version", ferry -> ferry.game().getVersion());
		register("minecraft:player:score", ferry -> Integer.toString(ferry.getPlayerScore()));
		register("minecraft:player:cause_of_death", ferry -> ferry.getPlayerDeathMessage().getFormattedText());
		register("minecraft:player:name", ferry -> ferry.game().player.getDisplayName().getFormattedText());
		register("minecraft:player:username", ferry -> ferry.game().player.getName());
		register("progress:title", ferry -> GuiProgressor.INSTANCE.title);
		register("progress:stage", ferry -> GuiProgressor.INSTANCE.stage);
		register("progress:progress", ferry -> Integer.toString(GuiProgressor.INSTANCE.progress));
		register("progress:percentage", ferry -> GuiProgressor.INSTANCE.progress > 0 ? GuiProgressor.INSTANCE.progress + "%" : "");
		register("progress:working", ferry -> Boolean.toString(!GuiProgressor.INSTANCE.doneWorking));
		register("fml:branding", ferry -> {
			if (!IFML.isForge()) return "";
			String s = "";
			for (String i : IFML.instance().getBrandings(true)) s += i + "\n";
			return s;
		});
	}
	
	public static void register(String key, INativeVariable supplier) {
		variables.put(key, supplier);
	}
	
	public static boolean has(String key, INativeFerry ferry) {
		return variables.containsKey(key) || ferry.has(key);
	}
	
	public static String lookupValue(String key, INativeFerry ferry) {
		if (variables.containsKey(key)) {
			return variables.get(key).get(ferry);
		}
		if (ferry.has(key)) return ferry.param(key);
		return key;
	}
}
