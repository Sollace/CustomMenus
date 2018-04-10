package com.sollace.custommenus.resources;

import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.locale.Locales;
import com.sollace.custommenus.registry.UiGame;

public class FormattedString {
	
	public static FormattedString create(JsonElement json) {
		if (json.isJsonObject()) return new FormattedString(null, json.getAsJsonObject());
		return new FormattedString(null, json.getAsString());
	}
	
	public static FormattedString create(String text, Object... pars) {
		return new FormattedString(null, text, pars);
	}
	
	private final String string, from;
	
	private final FormattedString parent;
	
	private Object[] parameters;
	
	private UiContainer root;
	
	protected FormattedString(FormattedString parent, String text, Object... pars) {
		this.parent = parent;
		string = text;
		from = null;
		parameters = pars;
	}
	
	protected FormattedString(FormattedString parent, JsonObject json) {
		this.parent = parent;
		string = json.has("text") ? json.get("text").getAsString() : null;
		from = json.has("from") ? json.get("from").getAsString() : null;
		parameters = loadParams(json);
	}
	
	private Object[] loadParams(JsonObject json) {
		if (!json.has("parameters")) return new Object[0];
		JsonArray arr = json.get("parameters").getAsJsonArray();
		Object[] pars = new Object[arr.size()];
		for (int i = 0; i < arr.size(); i++) {
			JsonElement item = arr.get(i);
			if (item.isJsonObject()) {
				pars[i] = new FormattedString(this, item.getAsJsonObject());
			} else {
				pars[i] = new FormattedString(this, item.getAsString());
			}
		}
		return pars;
	}
	
	public int addParam(Object value) {
		parameters = Arrays.copyOf(parameters, parameters.length + 1);
		parameters[parameters.length - 1] = value;
		return parameters.length - 1;
	}
	
	public void setParam(int index, Object value) {
		parameters[index] = value;
	}
	
	public UiContainer root() {
		return parent == null ? root : parent.root();
	}
	
	public String toString() {
		root = root();
		if (from != null && root != null) {
			if (UiGame.has(from, root.getFerry())) {
				return Locales.translateFormatted(UiGame.lookupValue(from, root.getFerry()), parameters);
			}
			UiField<?> field = root.getField(from);
			if (field != null) {
				return Locales.translateFormatted(field.getDisplayString(), parameters);
			}
		}
		return Locales.translateFormatted(string, parameters);
	}
	
	public String toString(UiContainer container) {
		root = container;
		return toString();
	}
}
