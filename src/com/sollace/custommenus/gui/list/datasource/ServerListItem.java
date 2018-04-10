package com.sollace.custommenus.gui.list.datasource;

import java.net.UnknownHostException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.JsonObject;
import com.sollace.custommenus.GuiMenus;
import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.gui.element.SignalBar;
import com.sollace.custommenus.gui.element.SignalBar.EnumPingType;
import com.sollace.custommenus.gui.geometry.EnumEdge;
import com.sollace.custommenus.gui.list.UiList;
import com.sollace.custommenus.gui.list.datasource.ServerSource.ListItem;
import com.sollace.custommenus.locale.Locales;
import com.sollace.custommenus.resources.BufferedIcon;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.text.TextFormatting;

public class ServerListItem extends ServerSource.ListItem implements Runnable {
	private static final ThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
	
	private static final int SERVER_VERSION = (new ServerData("", "", false)).version;
	
	private final ServerData server;
	
	private final SignalBar signal = new SignalBar();
	
	private int serverIndex;
	
	protected final IGuiAction moveDown = screen -> {
		owner.moveItem(this, EnumEdge.BOTTOM);
		int from = serverIndex;
		if (from < source.servers.countServers() - 1) source.servers.swapServers(from, serverIndex++);
	};
	protected final IGuiAction moveUp = screen -> {
		owner.moveItem(this, EnumEdge.TOP);
		int from = serverIndex;
		if (from > 0) source.servers.swapServers(from, serverIndex--);
	};
	
	public ServerListItem(ServerSource source, UiList<ListItem> owner, ServerData server, int serverIndex) {
		super(source, owner, new BufferedIcon(source.mc, "servers/" + server.serverIP + "/icon", "textures/misc/unknown_server.png", () -> server.getBase64EncodedIconData()));
		this.server = server;
		this.serverIndex = serverIndex;
		
		signal.setToolTip((sender, mouseX, mouseY, partialTicks) -> {
			sender.getNative().drawHoveringText(Locales.translateUnformatted(getPingStatus()), mouseX, mouseY);
		});
		extra.setToolTip((sender, mouseX, mouseY, partialTicks) -> {
			String text = getPlayerList();
			if (text != null) sender.getNative().drawHoveringText(Locales.translateUnformatted(text), mouseX, mouseY);
		});
		
		updateLabels();
	}
	
	@Override
	public ListItem init(JsonObject json) {
		super.init(json);
		if (json.has("signal")) GuiMenus.jsonFactory().populateElement(signal, owner, json.get("signal").getAsJsonObject());
		return this;
	}
	
	private void updateLabels() {
		title.setLabel(server.serverName);
		detail.setLabel(server.serverMOTD);
		extra.setLabel(getExtra());
	}
	
	@Override
	public void itemMoved(int newPosition) {
		super.itemMoved(newPosition);
		arrowBox.setDownAction(owner.canMove(this, EnumEdge.BOTTOM) ? moveDown : null);
		arrowBox.setUpAction(owner.canMove(this, EnumEdge.TOP) ? moveUp : null);
	}
	
	@Override
	public boolean isMoveable() {
		return true;
	}
	
	@Override
	public void reposition(UiView container) {
		super.reposition(container);
		signal.reposition(this);
	}
	
	@Override
	public void renderTranslated(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		if (!server.pinged) {
            server.pinged = true;
            server.pingToServer = -2L;
            server.serverMOTD = "";
            server.populationInfo = "";
            
            updateLabels();
    		
            EXECUTOR.submit(this);
        }
		super.renderTranslated(sender, mouseX, mouseY, partialTicks);
		
		signal.setSignalStrength(getPingStrength());
		signal.setPingType(server.version != SERVER_VERSION || (server.pinged && server.pingToServer != -2L) ? EnumPingType.PINGED : EnumPingType.PINGING);
		signal.render(sender, mouseX, mouseY, partialTicks);
	}
	
	protected String getPingStatus() {
		if (server.version > SERVER_VERSION) {
			return "multiplayer.status.client_out_of_date";
		}
		if (server.version < SERVER_VERSION) {
			return "multiplayer.status.server_out_of_date";
		}
		if (server.pinged && server.pingToServer != -2) {
			if (server.pingToServer < 0) return "multiplayer.status.no_connection";
			return server.pingToServer + "ms";
		}
		return "multiplayer.status.pinging";
	}
	
	public ServerData getServer() {
		return server;
	}
	
	protected String getPlayerList() {
		if (server.version != SERVER_VERSION || server.pingToServer > 0) {
			return server.playerList;
		}
		return null;
	}
	
	protected int getPingStrength() {
		if (server.pinged && server.pingToServer != -2) {
			if (server.version != SERVER_VERSION || server.pingToServer < 0) return 5;
			if (server.pingToServer < 150) return 0;
			if (server.pingToServer < 300) return 1;
			if (server.pingToServer < 600) return 2;
			if (server.pingToServer < 1000) return 3;
			return 4;
		}
		
		int ping = (int)(GuiNull.systemTime() / 100 + index * 2 & 7);
		if (ping > 4) return 8 - ping;
		return ping;
	}
	
	protected String getExtra() {
		if (server.version != SERVER_VERSION) {
			return TextFormatting.DARK_RED + server.gameVersion;
		}
		return server.populationInfo;
	}
	
	@Override
	public void run() {
		try {
            source.pinger.ping(server);
        } catch (UnknownHostException e) {
            server.pingToServer = -1L;
            server.serverMOTD = TextFormatting.DARK_RED + Locales.translateUnformatted("multiplayer.status.cannot_resolve");
        } catch (Exception e) {
            server.pingToServer = -1L;
            server.serverMOTD = TextFormatting.DARK_RED + Locales.translateUnformatted("multiplayer.status.cannot_connect");
        }
		
		updateLabels();
	}
	
	@Override
	public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
		return super.performAction(mouseX, mouseY, sender);
	}
	
	@Override
	public String getValue() {
		return server.serverIP;
	}
}
