/**
 * 
 */
package com.sollace.custommenus.mixin;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that a method declaration is intended to override a method
 * previously defined in a Mixin super interface.
 */
@Documented
@Retention(SOURCE)
@Target(METHOD)
public @interface MixinOverride {
	public Class<?>[] value();
}
