package com.sollace.custommenus.registry;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.list.UiListItem;
import com.sollace.custommenus.gui.list.datasource.BasicSource;
import com.sollace.custommenus.gui.list.datasource.ControlsSource;
import com.sollace.custommenus.gui.list.datasource.IDataSource;
import com.sollace.custommenus.gui.list.datasource.LanguageSource;
import com.sollace.custommenus.gui.list.datasource.ServerSource;
import com.sollace.custommenus.gui.list.datasource.SnooperSource;
import com.sollace.custommenus.gui.list.datasource.WorldSource;
import com.sollace.custommenus.privileged.IGame;
import com.sollace.custommenus.reflection.AbstractInstantiator;
import com.sollace.custommenus.reflection.IInstantiator;

/**
 * Registry of IDataSource used for populating lists with items.
 * <p>
 * <b>Usage:</b>
 * 
 * {@code UiDataSources.register(<name>, <class>);}
 */
public class UiDataSources {
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Map<String, IInstantiator<IDataSource<?>>> source_types = Maps.newHashMap();
	
	private static final Class<?>[]
			PARS_1 = new Class<?>[] {IGame.class},
			PARS_2 = new Class<?>[] {JsonObject.class};
	
	static {
		register("values", BasicSource.class);
		register("saves", WorldSource.class);
		register("languages", LanguageSource.class);
		register("snooper", SnooperSource.class);
		register("controls", ControlsSource.class);
		register("servers", ServerSource.class);
	}
	
	public static <T extends IDataSource<?>> void register(String name, Class<T> type) {
		source_types.put(name, new Instantiator(type));
	}
	
	public static <T extends UiListItem<T>> IDataSource<T> create(JsonElement json) {
		if (json.isJsonArray()) return new BasicSource<T>(json.getAsJsonArray());
		if (json.isJsonObject()) {
			JsonObject o = json.getAsJsonObject();
			return create(o.get("key").getAsString(), o);
		}
		
		return create(json.getAsString(), IGame.current());
	}
	
	@SuppressWarnings("unchecked")
	protected static <T extends UiListItem<T>> IDataSource<T> create(String key, Object...pars) {
		if (source_types.containsKey(key)) {
			return (IDataSource<T>)source_types.get(key).newInstance(pars);
		}
		LOGGER.warn(String.format("Invalid itemsource '%s'", key));
		return null;
	}
	
	protected static class Instantiator extends AbstractInstantiator<IDataSource<?>> {
		
		public Instantiator(Class<? extends IDataSource<?>> type) {
			super(type, PARS_1, PARS_2);
		}
		
		@Override
		public IDataSource<?> newInstance(Object... pars) {
			try {
				if (altConstructorTwo != null && pars[0] instanceof JsonObject) {
					return altConstructorTwo.newInstance(pars);
				}
				if (altConstructorOne != null) {
					return altConstructorOne.newInstance(pars);
				}
				return defaultConstructor.newInstance();
				
			} catch (Exception e) {
				LOGGER.error("Unknown exception whilst creating datasource", e);
			}
			return null;
		}
	}
}
