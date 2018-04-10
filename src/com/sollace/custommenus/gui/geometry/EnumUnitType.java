package com.sollace.custommenus.gui.geometry;

import com.google.gson.JsonElement;
import com.sollace.custommenus.utils.JsonUtils.JsonType;

/**
 * Unit types for use with generic units.
 */
public enum EnumUnitType implements JsonType<EnumUnitType> {
	/**
	 * Positioning is absolute. The original value is used without modification.
	 */
	ABSOLUTE,
	/**
	 * The original value is taken as percentage value.
	 */
	PERCENTAGE,
	/**
	 * Subtracts a fixed amount from the total available space.
	 */
	OFFSET;
	
	/**
	 * Consumes a unit value and container size, then returns the compute absolute size based on this unit type.
	 */
	public int operate(float absolute, int containerLength) {
		if (this == OFFSET) return (int)Math.floor(containerLength + absolute);
		if (this == ABSOLUTE) return (int)Math.floor(absolute);
		return (int)Math.floor(containerLength * (absolute / 100));
	}
	
	@Override
	public EnumUnitType get(JsonElement json) {
		return getByName(json.getAsString());
	}

	public static EnumUnitType getByName(String name) {
		EnumUnitType a = valueOf(name.toUpperCase());
		return a == null ? ABSOLUTE : a;
	}
}
