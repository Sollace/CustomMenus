package com.sollace.custommenus.registry;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.packing.GridPacker;
import com.sollace.custommenus.gui.packing.ListPacker;
import com.sollace.custommenus.gui.packing.UiPacker;
import com.sollace.custommenus.reflection.IInstantiator;

/**
 * Registry of UiPackers for arranging elements.
 * <p>
 * <b>Usage:</b>
 * 
 * {@code UiPackers.register(<name>, <class>);}
 */
public class UiPackers {
private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Map<String, IInstantiator<? extends UiPacker>> packer_types = Maps.newHashMap();
	
	private static final Class<?>[] PARS_1 = new Class<?>[] {JsonObject.class};
	
	static {
		register("grid", GridPacker.class);
		register("list", ListPacker.class);
	}
	
	public static <T extends UiPacker> void register(String name, Class<T> type) {
		packer_types.put(name, IInstantiator.<T>create(type, PARS_1));
	}
	
	public static UiPacker create(JsonObject json) {
		String key = json.get("type").getAsString();
		
		if (packer_types.containsKey(key)) {
			return packer_types.get(key).newInstance(json);
		}
		
		LOGGER.warn(String.format("Invalid packer type '%s'", key));
		return null;
	}
}
