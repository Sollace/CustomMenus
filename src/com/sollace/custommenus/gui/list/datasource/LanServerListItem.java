package com.sollace.custommenus.gui.list.datasource;

import com.sollace.custommenus.gui.list.UiList;
import com.sollace.custommenus.gui.list.datasource.ServerSource.ListItem;
import com.sollace.custommenus.resources.Icon;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.LanServerInfo;

public class LanServerListItem extends ServerSource.ListItem {
	private final LanServerInfo server;
	
	public LanServerListItem(ServerSource source, UiList<ListItem> owner, LanServerInfo server) {
		super(source, owner, new Icon("textures/misc/unknown_server.png"));
		
		title.setLabel(getDisplayString());
		detail.setLabel(server.getServerMotd());
		extra.setLabel(source.mc.settings().hideServerAddress ? "selectServer.hiddenAddress" : server.getServerIpPort());
		
		this.server = server;
	}
	
	@Override
	public String getValue() {
		return server.getServerIpPort();
	}

	@Override
	public String getDisplayString() {
		return "lanServer.title";
	}
	
	public ServerData getServer() {
		return new ServerData(server.getServerMotd(), server.getServerIpPort(), true);
	}
}
