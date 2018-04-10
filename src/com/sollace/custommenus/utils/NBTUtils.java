package com.sollace.custommenus.utils;

import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.nbt.*;

public class NBTUtils {
	
	public static NBTBase jsonToNBT(JsonElement json) {
		if (json.isJsonObject()) return getObject(json.getAsJsonObject());
		if (json.isJsonArray()) return getArray(json.getAsJsonArray());
		if (json.isJsonPrimitive()) return getPrimitive(json.getAsJsonPrimitive());
		return null;
	}
	
	public static NBTTagCompound getObject(JsonObject json) {
		NBTTagCompound result = new NBTTagCompound();
		for (Map.Entry<String, JsonElement> i : json.entrySet()) {
			if (i.getValue().isJsonNull()) continue;
			NBTBase value = jsonToNBT(i.getValue());
			if (value != null) result.setTag(i.getKey(), value);
		}
		
		return result;
	}
	
	public static NBTTagList getArray(JsonArray json) {
		NBTTagList result = new NBTTagList();
		for (int i = 0; i < json.size(); i++) {
			NBTBase value = jsonToNBT(json.get(i));
			if (value != null) result.appendTag(value);
		}
		return result;
	}
	
	public static NBTBase getPrimitive(JsonPrimitive json) {
		if (json.isString()) {
			return new NBTTagString(json.getAsString());
		}
		if (json.isNumber()) {
			return new NBTTagDouble(json.getAsDouble());
		}
		return null;
	}
}
