package com.sollace.fml;

public final class FML {
	static IFML instance = null;
	
	public static void instantiated(IFML instance) {
		FML.instance = instance;
	}
}
