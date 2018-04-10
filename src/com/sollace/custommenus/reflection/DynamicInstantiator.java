package com.sollace.custommenus.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import com.google.common.collect.Lists;

/**
 * Attempts to instantiate an object by parameter types.
 * 
 * TODO: (WIP - Probably broken - needs rewrite)
 */
public class DynamicInstantiator<T> implements IInstantiator<T> {
	
	private Constructor<T> constructor;
	private List<Class<?>> parameterTypes;
	
	public DynamicInstantiator(Class<T> type, Class<?>... params) {
		@SuppressWarnings("unchecked")
		final Constructor<T>[] constructors = (Constructor<T>[]) type.getConstructors();
		
		for (int i = 0; i < constructors.length; i++) {
			final List<Class<?>> given = Lists.newArrayList(params);
			final List<Class<?>> accepted = Lists.newArrayList(constructors[i].getParameterTypes());
			
			if (accepted.size() == 0) {
				constructor = constructors[i];
				continue;
			}
			
			for (int j = 0; j < accepted.size(); i++) {
				for (int k = 0; k < given.size(); k++) {
					if (accepted.get(j) == given.get(k)) {
						accepted.remove(j);
						given.remove(k);
						break;
					}
				}
			}
			
			if (given.size() == 0 && accepted.size() == 0) {
				constructor = constructors[i];
				parameterTypes = Lists.newArrayList(constructors[i].getParameterTypes());
			}
		}
	}
	
	@Override
	public T newInstance(Object... pars) {
		if (constructor == null) return null;
		
		if (parameterTypes == null) {
			try {
				return constructor.newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		final Object[] passed = new Object[parameterTypes.size()];
		final List<?> unmatched = Lists.newArrayList(pars);
		int finding = 0;
		while (unmatched.size() > 0 && finding <= parameterTypes.size()) {
			for (int i = 0; i < unmatched.size(); i++) {
				if (pars[i].getClass().equals(parameterTypes.get(finding))) {
					passed[finding++] = unmatched.remove(i);
					break;
				}
			}
		}
		
		try {
			return constructor.newInstance(passed);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

}
