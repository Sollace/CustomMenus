package com.sollace.custommenus.utils;

import com.google.gson.JsonObject;

public interface IJsonReadable {
	/**
	 * Initialises this element. Called on json load to set any extra properties this element accepts.
	 */
	public IJsonReadable init(JsonObject json);
}
