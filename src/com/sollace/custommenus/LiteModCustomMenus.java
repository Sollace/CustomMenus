package com.sollace.custommenus;

import java.io.File;
import com.mumfrey.liteloader.LiteMod;
import com.sollace.custommenus.privileged.IGame;

/**
 * It's a litemod class. It doesn't do much.
 * <p>
 * 
 * If you're a modder, you can probably test for this mod's presence by doing this:
 * 
 * <p>
 * try {<br>
 *   Class.forName("com.sollace.custommenus.LiteModCustomMenus");<br>
 *   return true;<br>
 * }<br>
 * return false;
 * <p>
 * More interesting parts can be found in {@link com.sollace.custommenus.registry}.
 * 
 */
public class LiteModCustomMenus implements LiteMod {
	
	@Override
	public String getName() {
		return "Custom Menus";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
	
	@Override
	public void init(File configPath) {
		IGame.current().resources().registerReloadListener(GuiMenus.INSTANCE);
	}
	
	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {
		
	}
}
