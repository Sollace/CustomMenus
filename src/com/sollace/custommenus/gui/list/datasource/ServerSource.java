package com.sollace.custommenus.gui.list.datasource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.sollace.custommenus.GuiMenus;
import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.gui.element.ArrowButton;
import com.sollace.custommenus.gui.element.Label;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.gui.list.ListItemBase;
import com.sollace.custommenus.gui.list.UiList;
import com.sollace.custommenus.privileged.IGame;
import com.sollace.custommenus.resources.IIcon;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerInfo;
import net.minecraft.client.network.ServerPinger;
import net.minecraft.client.network.LanServerDetector.LanServerList;
import net.minecraft.client.network.LanServerDetector.ThreadLanServerFind;
import net.minecraft.client.renderer.GlStateManager;

public class ServerSource implements IDataSource<ServerSource.ListItem>, IGuiAction {
	private static final Logger LOGGER = LogManager.getLogger();
	
	protected final IGame mc;
	
	protected final ServerList servers;
	
	private final LanServerList lan = new LanServerList();
	
	protected final ServerPinger pinger = new ServerPinger();
	
	private ThreadLanServerFind detector;
	
	public ServerSource(IGame mc) {
		this.mc = mc;
		
		servers = new ServerList(mc.getNative());
        servers.loadServerList();
        
        try {
            detector = new ThreadLanServerFind(lan);
            detector.start();
        } catch (Exception e) {
            LOGGER.warn("Unable to start LAN server detection: {}", e.getMessage());
        }
	}
	
	@Override
	public void populateList(JsonObject json, UiList<ListItem> list) {
		refreshList(list);
	}
	
	private void refreshList(UiList<ListItem> list) {
		for (LanServerInfo i : lan.getLanServers()) {
			list.addChild(new LanServerListItem(this, list, i));
		}
		for (int i = 0; i < servers.countServers(); ++i) {
			list.addChild(new ServerListItem(this, list, servers.getServerData(i), i));
		}
	}
	
	@Override
	public void updateContents(UiList<ListItem> list) {
		if (lan.getWasUpdated()) {
            lan.setWasNotUpdated();
            list.clear();
            refreshList(list);
        }
		
        pinger.pingPendingNetworks();
	}
	

	@Override
	public void perform(UiRoot screen) {
		if (detector != null) {
            detector.interrupt();
            detector = null;
        }

        pinger.clearPendingNetworks();
	}
	
	@Override
	public void selectionChanged() {
        
	}
	
	public abstract static class ListItem extends ListItemBase<ListItem> implements UiField<String> {
		private final IIcon icon;
		
		protected final ArrowButton arrowBox;
		protected final Label title, detail, extra;
		
		protected final ServerSource source;
		
		public ListItem(ServerSource source, UiList<ListItem> owner, IIcon icon) {
			super(owner);
			this.source = source;
			this.icon = icon;
			
			arrowBox = new ArrowButton(owner).setBackground(icon);
			title = new Label(owner);
			detail = new Label(owner);
			extra = new Label(owner);
		}
		
		@Override
		public ListItem init(JsonObject json) {
			super.init(json);
			if (json.has("icon")) GuiMenus.jsonFactory().populateElement(arrowBox, owner, json.get("icon").getAsJsonObject());
			if (json.has("title")) GuiMenus.jsonFactory().populateElement(title, owner, json.get("title").getAsJsonObject());
			if (json.has("detail")) GuiMenus.jsonFactory().populateElement(detail, owner, json.get("detail").getAsJsonObject());
			if (json.has("extra")) GuiMenus.jsonFactory().populateElement(extra, owner, json.get("extra").getAsJsonObject());
			return this;
		}
		
		@Override
		public String getName() {
			return null;
		}

		@Override
		public void setName(String name) {
			
		}
		
		@Override
		public void setValue(String value) {
			
		}
		
		@Override
		public ListItem setAction(IGuiAction action) {
			arrowBox.setAction(action);
			return this;
		}
		
		@Override
		public void reposition(UiView container) {
			super.reposition(container);
			title.reposition(container);
			detail.reposition(container);
			extra.reposition(container);
			arrowBox.reposition(container);
		}
		
		@Override
		public void drawContents(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
	        GlStateManager.pushMatrix();
	        GlStateManager.translate(x, y, 0);
        	GlStateManager.color(1, 1, 1, 1);
			
        	renderTranslated(sender, mouseX - x, mouseY - y, partialTicks);
			
			GlStateManager.popMatrix();
		}
		
		public void renderTranslated(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
			title.render(sender, mouseX, mouseY, partialTicks);
			detail.render(sender, mouseX, mouseY, partialTicks);
			extra.render(sender, mouseX, mouseY, partialTicks);
			
			if (source.mc.settings().touchscreen || isFocused(x + mouseX, y + mouseY)) {
				arrowBox.render(sender, mouseX, mouseY, partialTicks);
			} else {
				icon.render(sender, mouseX, mouseY, partialTicks);
			}
		}
		
		@Override
		public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
			if (arrowBox.performAction(mouseX - x, mouseY - y, sender)) {
				return true;
			}
			return super.performAction(mouseX, mouseY, sender);
		}
		
		public abstract ServerData getServer();
	}
}
