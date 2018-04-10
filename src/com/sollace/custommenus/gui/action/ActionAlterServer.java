package com.sollace.custommenus.gui.action;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.gui.list.UiList;
import com.sollace.custommenus.gui.list.datasource.ServerSource;
import com.sollace.custommenus.gui.natives.INativeFerry;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;

public class ActionAlterServer extends ActionYesNo {
	
	private ServerData server = null;
	
	private String serverListField = null;
	
	private final String action;
	
	private ServerList servers;
	
	public ActionAlterServer(JsonObject json, UiElement owner) {
		super(json, owner);
		if (json.has("serverListField")) serverListField = json.get("serverListField").getAsString();
		action = json.get("action").getAsString();
	}
	
	@Override
	public void perform(UiRoot screen) {
		servers = new ServerList(screen.getMinecraft());
		servers.loadServerList();
		
		
		switch (action) {
			case "add": addServer(screen); return;
			case "direct": directJoinServer(screen); return;
		}
		
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
		
		if (server != null) {
			server = makeLive(servers, server);
			
			switch (action) {
				case "edit": editServer(screen); break;
				case "delete": deleteServer(screen); break;
			}
		}
	}
	
	private void directJoinServer(UiRoot parent) {
		INativeFerry ferry = INativeFerry.empty(parent);
		ferry.decider(new IGuiDecider() {
			@Override
			public void yes(UiRoot screen) {
				servers.addServerData(ferry.getActiveServerData());
				servers.saveServerList();
				screen.open("connecting");
			}
			@Override
			public void no(UiRoot screen) {
				screen.refresh();
			}
		}).open("connectserver");
	}
	
	private void editServer(UiRoot parent) {
		ServerData temp = new ServerData(server.serverName, server.serverIP, server.isOnLAN());
		temp.copyFrom(server);
		INativeFerry ferry = INativeFerry.empty(parent).setActiveServerData(temp);
		ferry.decider(new IGuiDecider() {
			@Override
			public void yes(UiRoot screen) {
				server.serverIP = temp.serverIP;
				server.serverName = temp.serverName;
				server.copyFrom(temp);
				
				servers.saveServerList();
				screen.refresh();
			}
			@Override
			public void no(UiRoot screen) {
				screen.refresh();
			}
		}).open("addserver");
	}
	
	private void addServer(UiRoot parent) {
		INativeFerry ferry = INativeFerry.empty(parent);
		ferry.decider(new IGuiDecider() {
			@Override
			public void yes(UiRoot screen) {
				servers.addServerData(ferry.getActiveServerData());
				servers.saveServerList();
				screen.refresh();
			}
			@Override
			public void no(UiRoot screen) {
				screen.refresh();
			}
		}).open("addserver");
	}
	
	private void deleteServer(UiRoot screen) {
		super.perform(screen);
	}
	
	@Override
	public void yes(UiRoot screen) {
		if (server != null) {
			for (int i = 0; i < servers.countServers(); i++) {
				if (serversAreEqual(server, servers.getServerData(i))) {
					servers.removeServerData(i);
					servers.saveServerList();
					break;
				}
			}
		}
		screen.refresh();
	}
	
	private static ServerData makeLive(ServerList servers, ServerData data) {
		for (int i = 0; i < servers.countServers(); i++) {
			if (serversAreEqual(data, servers.getServerData(i))) {
				return servers.getServerData(i);
			}
		}
		return data;
	}
	
	private static boolean serversAreEqual(ServerData server, ServerData other) {
		return server != null && (server == other || (server.serverIP.contentEquals(other.serverIP) && server.serverName.contentEquals(other.serverName))); 
	}
}
