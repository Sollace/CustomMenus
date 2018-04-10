package com.sollace.custommenus.reflection;

/**
 * Instantiators are a functional factory interface to make reflexive object instantiation easier.
 * They wrap either one or more constructors, and implement a fallback mechanism for multiple sets of possible parameters.
 * <p>
 * See {@link AbstractInstantiator} and {@link DynamicInstantiator} for actual implementations.
 * 
 * @param <T> The type of objects instantiated with this IInstantiator
 */
@FunctionalInterface
public interface IInstantiator<T> {
	
	/**
	 * Creates a new instance of the underlying object type.
	 * Returns either the resulting object (if it succeeded) or null (if it failed).
	 * 
	 * @param pars The objects to pass down to the constructor
	 */
	public T newInstance(Object... pars);
	
	/**
	 * Creates an instantiator accepting one set of parameters, or the default constructor.
	 * 
	 * 
	 * @param <T> The type
	 * @param type The type.
	 * @param params Parameter types accepted.
	 */
	public static <T> IInstantiator<T> create(Class<T> type, Class<?>... params) {
		return new AbstractInstantiator<T>(type, params, null) {
			public T newInstance(Object... pars) {
				try {
					if (altConstructorOne != null) {
						return altConstructorOne.newInstance(pars);
					}
					return defaultConstructor.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		};
	}
	
	/**
	 * Creates a dynamic instantiator that attempts to match parameters to a constructor by the selection and number of types.
	 * 
	 * @param type The type to construct.
	 * @param params Parameter types accepted.
	 */
	public static <T> IInstantiator<T> createDynamic(Class<T> type, Class<?>... params) {
		return new DynamicInstantiator<T>(type, params);
	}
}
