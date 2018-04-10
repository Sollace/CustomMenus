package com.sollace.custommenus.gui.geometry;

import com.google.gson.JsonObject;
import com.sollace.custommenus.utils.JsonUtils;

public class Unit {
	private EnumUnitType type = EnumUnitType.ABSOLUTE;
	
	private float amount = 0;
	
	public int computed = 0;
	
	public void set(int amount) {
		this.amount = this.computed = amount;
	}
	
	public void recalculate(int containerLength) {
		computed = type.operate(amount, containerLength);
	}
	
	public Unit(int initial) {
		amount = initial;
		computed = initial;
	}
	
	public Unit(String name, JsonObject json) {
		loadJson(name, json);
	}
	
	public void loadJson(String name, JsonObject json) {
		amount = JsonUtils.get(json, name, amount);
		if (json.has("unit" + name)) type = EnumUnitType.getByName(json.get("unit" + name).getAsString());
	}
}
