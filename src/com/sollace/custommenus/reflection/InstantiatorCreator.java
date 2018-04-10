package com.sollace.custommenus.reflection;

/**
 * An object generator using instantiators.
 * 
 * Woah, reflection inception.
 *
 * @param <T>	The type parameter used for the instantiator.
 */
@FunctionalInterface
public interface InstantiatorCreator<T> {
	/**
	 * Called with an instantiator to call.
	 * @param instantiator The instantiator
	 * @return The result from calling {@link IInstantiator.newInstance}
	 */
	public T call(IInstantiator<T> instantiator);
}
