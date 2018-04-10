package com.sollace.custommenus.reflection;

import java.lang.reflect.Array;

/**
 * Utility class for working with classes.
 */
public final class Classes {
	/**
	 * Checks if a class is or contains the named class in its type hierarchy.
	 */
	public static boolean descendsFrom(Class<?> type, String canonicalName) {
		return descendsFrom(type, get(canonicalName));
	}
	
	/**
	 * Checks if a class is or contains the named class in its type hierarchy.
	 */
	public static boolean descendsFrom(Class<?> type, Class<?> parent) {
		return parent != null && parent.isAssignableFrom(type);
	}
	
	/**
	 * Finds a class object corresponding to the given canonical name, otherwise returns null if one could not be found.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> get(String canonicalName) {
		try {
			return (Class<T>)Class.forName(canonicalName);
		} catch (Throwable e) {}
		
		return null;
	}
	
	/**
	 * Gets the runtime type of an object. Unlike getClass(), this one actually remembers the source type.
	 * @throws NullPointerException when called with null values.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> typeOf(T obj) {
		return (Class<T>)obj.getClass();
	}
	
	/**
	 * Gets the runtime type of an array. (the component type) Unlike getClass(), this one actually remembers the source type.
	 * @throws NullPointerException when called with null values.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> typeOf(T[] obj) {
		return (Class<T>)obj.getClass().getComponentType();
	}
	
	/**
	 * Create a new array of a given type and length. Unlike Array, this one actually remembers what type it is.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(Class<T> type, int length) {
		return (T[])Array.newInstance(type, length);
	}
}
