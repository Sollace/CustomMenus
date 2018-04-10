package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.gui.list.UiList;
import com.sollace.custommenus.gui.list.datasource.ServerSource;
import com.sollace.custommenus.gui.natives.INativeFerry;
import com.sollace.custommenus.locale.Locales;

import net.minecraft.client.multiplayer.ServerData;

public class ActionJoinServer implements IGuiAction, IGuiDecider {
	
	private ServerData server = null;
	
	private String serverListField = null;
	
	public ActionJoinServer(JsonObject json, UiElement owner) {
		if (json.has("serverName") && json.has("serverIp")) {
			boolean isLan = json.has("isLan") ? json.get("isLan").getAsBoolean() : false;
			server = new ServerData(json.get("serverName").getAsString(), json.get("serverIp").getAsString(), isLan);
		}
		if (json.has("serverListField")) serverListField = json.get("serverListField").getAsString();
	}
	
	public ActionJoinServer(JsonObject json, UiField<String> owner) {
		this(json, (UiElement)owner);
		if (owner instanceof ServerSource.ListItem) {
			server = ((ServerSource.ListItem)owner).getServer();
		}
	}
	
	@Override
	public void perform(UiRoot screen) {
		if (server == null && serverListField != null) {
			UiField<Integer> list = screen.getField(serverListField);
			
			if (list instanceof UiList && ((UiList<?>)list).getSelectedItem() instanceof UiField) {
				@SuppressWarnings("unchecked")
				UiField<String> field = (UiField<String>)((UiList<?>)list).getSelectedItem();
				
				if (field instanceof ServerSource.ListItem) {
					server = ((ServerSource.ListItem)field).getServer();
				}
			}
		}
		
		if (server == null) {
			server = new ServerData(Locales.translateUnformatted("selectServer.defaultName"), "", false);
			INativeFerry.empty(screen).setActiveServerData(server).decider(this).open("connectserver");
		} else {
			screen.getFerry().setActiveServerData(server).open("connecting");
		}
	}

	@Override
	public void yes(UiRoot screen) {
		screen.open("connecting");
	}

	@Override
	public void no(UiRoot screen) {
		screen.refresh();
	}
}
