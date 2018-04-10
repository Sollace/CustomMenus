package com.sollace.custommenus.reflection;

import java.lang.reflect.Constructor;

public abstract class AbstractInstantiator<T> implements IInstantiator<T> {
	protected final Constructor<T> defaultConstructor, altConstructorOne, altConstructorTwo;
	
	@SuppressWarnings("unchecked")
	private Constructor<T> getConstructor(Class<? extends T> type, Class<?>... params) {
		try {
			return (Constructor<T>) type.getConstructor(params);
		} catch (NoSuchMethodException | SecurityException e) {}
		return null;
	}
	
	public AbstractInstantiator(Class<? extends T> type, Class<?>[] optionOne, Class<?>[] optionTwo) {
		defaultConstructor = getConstructor(type);
		altConstructorOne = getConstructor(type, optionOne);
		altConstructorTwo = optionTwo == null ? null : getConstructor(type, optionTwo);
		
		if (defaultConstructor == null && altConstructorOne == null && altConstructorTwo == null) {
			String parsOne = "";
			for (Class<?> i : optionTwo != null && altConstructorOne == null ? optionOne : optionTwo) parsOne += (parsOne.length() > 0 ? ", " : "") + i.getName();
			throw new IllegalArgumentException(
				String.format("Class type '%s' does not have a default constructor, or an accepted alternative. Ensure it either has a default constructor, or implements the constructor <init>(%s) before continuing.",
						type.getName(), parsOne));
		}
	}
}
