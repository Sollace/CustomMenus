package com.sollace.custommenus.utils;

import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sollace.custommenus.reflection.Classes;
import com.sollace.custommenus.resources.FormattedString;

/**
 * Various utilities for reading json to known types. Supports some object primitives (Integer/Float), strings, arrays, and Enums.
 * <p>
 * It's all just really basic serialisation that can probably be done via Gson, but I don't really want to be messing around with that.
 */
public class JsonUtils {
	
	private static final Map<Class<?>, JsonType<?>> types = Maps.newHashMap();
	
	static {
		types.put(Integer.class, json -> json.getAsInt());
		types.put(String.class, json -> json.getAsString());
		types.put(Float.class, json -> json.getAsFloat());
		types.put(Pattern.class, json -> {
			try {
				return Pattern.compile(json.getAsString());
			} catch (Throwable e) {return null;}
		});
		types.put(FormattedString.class, json -> FormattedString.create(json));
	}
	
	public static <T> boolean has(T obj, Class<T> type) {
		return types.containsKey(type) || (obj instanceof JsonType);
	}
	
	public static <T> boolean has(T[] obj, Class<T> type) {
		return types.containsKey(type) || (obj.length > 0 && obj[0] instanceof JsonType);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> JsonType<T> typeOf(T obj, Class<T> type) {
		if (obj instanceof JsonType<?>) return (JsonType<T>)obj;
		return (JsonType<T>)types.get(type);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> JsonType<T> typeOf(T[] obj, Class<T> type) {
		if (obj.length > 0) return typeOf(obj[0], type);
		return (JsonType<T>)types.get(type);
	}
	
	public static int get(JsonObject json, String key, int def) {
		return json.has(key) ? json.get(key).getAsInt() : def;
	}
	
	public static float get(JsonObject json, String key, float def) {
		return json.has(key) ? json.get(key).getAsFloat() : def;
	}
	
	public static String get(JsonObject json, String key, String def) {
		return json.has(key) ? json.get(key).getAsString() : def;
	}
	
	public static boolean get(JsonObject json, String key, boolean def) {
		return json.has(key) ? json.get(key).getAsBoolean() : def;
	}
	
	public static <T extends Enum<T>> T get(JsonObject json, String key, @Nonnull T def) {
		return get(json, key, def, Classes.typeOf(def));
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> T get(JsonObject json, String key, T def, Class<T> type) {
		if (json.has(key)) {
			try {
				if (def instanceof JsonType) return ((JsonType<T>)def).get(json.get(key));
				return Enum.valueOf(type, json.get(key).getAsString().toUpperCase());
			} catch (IllegalArgumentException e) {}
		}
		return def;
	}
	
	public static <T> T get(JsonObject json, String key, T def, Class<T> type) {
		if (json.has(key) && has(def, type)) {
			return typeOf(def, type).get(json.get(key));
		}
		return def;
	}
	
	public static <T> T[] get(JsonObject json, String key, @Nonnull T[] def) {
		return get(json, key, def, 0);
	}
	
	public static <T> T[] get(JsonObject json, String key, @Nonnull T[] def, int minL) {
		return get(json, key, def, Classes.typeOf(def), minL);
	}
	
	public static void get(JsonObject json, String key, IJsonReadable readable) {
		if (readable != null && json.has(key)) readable.init(json.get(key).getAsJsonObject());
	}
	
	public static <T> T[] get(JsonObject json, String key, T[] def, Class<T> type, int minL) {
		if (json.has(key) && has(def, type)) {
			JsonElement k = json.get(key);
			if (!k.isJsonArray()) return def;
			
			JsonArray arr = k.getAsJsonArray();
			if (arr.size() < minL) return def;
			
			JsonType<T> jtype = typeOf(def, type);
			T[] values = Classes.newArray(type, arr.size());
			for (int i = 0; i < arr.size(); i++) {
				values[i] = jtype.get(arr.get(i));
			}
			return values;
		}
		return def;
	}
	
	/**
	 * Type converter used by JsonUtils.
	 *
	 * @param <T>	The type to convert to.
	 */
	@FunctionalInterface
	public static interface JsonType<T> {
		/**
		 * Converts a json element to an object value of this type.
		 */
		T get(JsonElement json);
	}
}
