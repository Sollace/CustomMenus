package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;

import net.minecraft.client.Minecraft;

public class ActionEndGame extends ActionOpenGui {
	
	public ActionEndGame(JsonObject json, UiElement owner) {
		String alt = "menu.disconnect";
		
		if (json.has("alt")) alt = json.get("alt").getAsString();
		
		if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
			owner.setLabel(alt);
		}
	}
	
	@Override
	public void perform(UiRoot screen) {
		Minecraft mc = screen.getMinecraft();
		
		if (mc.world == null) return;
		
		gui = mc.isIntegratedServerRunning() ? "mainmenu" : mc.isConnectedToRealms() ? "realms" : "multiplayer";
		
		mc.world.sendQuittingDisconnectingPacket();
		mc.loadWorld(null);
		
		super.perform(screen);
	}

}
