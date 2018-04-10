package com.sollace.custommenus.locale;

import java.util.IllegalFormatException;
import java.util.UnknownFormatConversionException;

import net.minecraft.client.resources.I18n;

/**
 * Wraps I18n to prevent Format Error: Format Error: this from happening every time someone uses a '%' sign. 
 */
public class Locales {
	
	private static String format(String key, Object...args) {
		if (key == null || key.isEmpty()) return "";
		try {
			return String.format(key,  args);
		} catch (UnknownFormatConversionException e) {
			return key;
		} catch (IllegalFormatException e) {
			return key;
		}
	}
	
	public static String translateFormatted(String key, Object...args) {
		return format(translateUnformatted(key), args);
	}
	
	public static String translateUnformatted(String key) {
		if (key == null || key.isEmpty()) return "";
		if (!canTranslate(key)) return key;
		if (key.indexOf("%") > -1) return i18n(key.replaceAll("%", "////:")).replaceAll("////:", "%");
		
		return i18n(key);
	}
	
	public static String translate(String key, String format) {
		key = translateUnformatted(key);
		return format != null ? format(format, key) : key;
	}
	
	private static String i18n(String key) {
		return I18n.format(key);
	}
	
	public static boolean canTranslate(String key) {
		return key != null && I18n.hasKey(key);
	}
}
