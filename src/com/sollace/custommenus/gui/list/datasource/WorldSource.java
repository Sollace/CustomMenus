package com.sollace.custommenus.gui.list.datasource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.sollace.custommenus.GuiMenus;
import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.container.UiView;
import com.sollace.custommenus.gui.element.ArrowButton;
import com.sollace.custommenus.gui.element.Label;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.gui.list.ListItemBase;
import com.sollace.custommenus.gui.list.UiList;
import com.sollace.custommenus.locale.Locales;
import com.sollace.custommenus.privileged.IGame;
import com.sollace.custommenus.resources.IIcon;
import com.sollace.custommenus.resources.LocalIcon;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;

public class WorldSource implements IDataSource<WorldSource.ListItem> {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat();
	
	private final IGame mc;
	
	private final ISaveFormat saves;
	
	public WorldSource(IGame mc) {
		this.mc = mc;
		saves = mc.saves();
	}
	
	@Override
	public void populateList(JsonObject json, UiList<ListItem> list) {
		List<WorldSummary> worlds = null;
		
        try {
        	worlds = saves.getSaveList();
        } catch (Exception e) {
        	LOGGER.error("Couldn't load level list", e);
        	GuiNull.currentScreen().getFerry()
        		.param("title", "selectWorld.unable_to_load")
        		.param("message", e.getMessage())
        		.open("errorscreen");
        	return;
        }
        
        Collections.sort(worlds);
        
        for (WorldSummary i : worlds) {
        	list.addChild(new ListItem(list, i));
        }
	}
	
	@Override
	public void selectionChanged() {
		
	}
	
	public class ListItem extends ListItemBase<ListItem> implements UiField<String> {
		
		private final WorldSummary summary;
		private final ArrowButton arrowBox;
		private final IIcon icon;
		
		private final Label title, detail, extra;
		
		public ListItem(UiList<ListItem> owner, WorldSummary summary) {
			super(owner);
			this.summary = summary;
			
			icon = new LocalIcon(mc,
					"worlds/" + summary.getFileName() + "/icon",
					saves.getFile(summary.getFileName(), "icon.png"),
					"textures/misc/unknown_server.png").setPos(2, 2);
			arrowBox = new ArrowButton(owner).setBackground(icon);
			title = new Label(owner);
			detail = new Label(owner);
			extra = new Label(owner);
			
			int warningLevel = summary.markVersionInList() ? summary.askToOpenWorld() ? 2 : 1 : 0;
			
			arrowBox.setWarningLevel(warningLevel);
			if (warningLevel > 0) {
				arrowBox.setToolTip(warningLevel == 1 ? 
						TextFormatting.GOLD + Locales.translateUnformatted("selectWorld.tooltip.snapshot1") + "\n" + TextFormatting.GOLD + Locales.translateUnformatted("selectWorld.tooltip.snapshot2")
						: 
						TextFormatting.RED + Locales.translateUnformatted("selectWorld.tooltip.fromNewerVersion1") + "\n" + TextFormatting.RED + Locales.translateUnformatted("selectWorld.tooltip.fromNewerVersion2"));
			}
			
			String s = summary.getDisplayName();
			if (s == null || s.isEmpty()) s = Locales.translateUnformatted("selectWorld.world") + (index + 1);
			title.setLabel(s);
			detail.setLabel(getDetails());
			extra.setLabel(getExtraData());
			
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
		
		public WorldSummary getSummary() {
			return summary;
		}
		
		@Override
		public String getName() {
			return null;
		}

		@Override
		public void setName(String name) {
			
		}

		@Override
		public String getValue() {
			return summary.getFileName();
		}

		@Override
		public void setValue(String value) {
			
		}
		
		@Override
		public UiContainer getContainer() {
			return owner;
		}
		
		@Override
		public String getDisplayString() {
			return summary.getDisplayName();
		}
		
		@Override
		public ListItem setAction(IGuiAction action) {
			arrowBox.setAction(action);
			return this;
		}
		
		@Override
		public void reposition(UiView container) {
			super.reposition(container);
			title.reposition(this);
			detail.reposition(this);
			extra.reposition(this);
			arrowBox.reposition(this);
		}
		
		@Override
		public void drawContents(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
	        GlStateManager.pushMatrix();
	        GlStateManager.translate(x, y, 0);
        	GlStateManager.color(1, 1, 1, 1);
			
			title.render(sender, mouseX, mouseY, partialTicks);
			detail.render(sender, mouseX, mouseY, partialTicks);
			extra.render(sender, mouseX, mouseY, partialTicks);
			
			if (mc.settings().touchscreen || isFocused(mouseX, mouseY)) {
				arrowBox.render(sender, mouseX - x, mouseY - y, partialTicks);
			} else {
				icon.render(sender, mouseX - x, mouseY - y, partialTicks);
			}
			
			GlStateManager.popMatrix();
		}
		
		@Override
		public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
			if (arrowBox.performAction(mouseX - x, mouseY - y, sender)) {
				return true;
			}
			return super.performAction(mouseX, mouseY, sender);
		}
		
		protected String getDetails() {
			return summary.getFileName() + " (" + DATE_FORMAT.format(summary.getLastTimePlayed()) + ")";
		}
		
		protected String getVersionString() {
			String version = summary.getVersionName();
			
            if (summary.markVersionInList()) {
            	version += TextFormatting.RESET;
                if (summary.askToOpenWorld()) {
                    return TextFormatting.RED + version;
                }
                return TextFormatting.ITALIC + version;
            }
            return version;
		}
		
		protected String getExtraData() {
			if (summary.requiresConversion()) {
	            return Locales.translateUnformatted("selectWorld.conversion");
	        }
			
			String line = Locales.translateUnformatted("gameMode." + summary.getEnumGameType().getName());
			
			if (summary.isHardcoreModeEnabled()) {
				line = TextFormatting.DARK_RED + Locales.translateUnformatted("gameMode.hardcore") + TextFormatting.RESET;
			}
			
			if (summary.getCheatsEnabled()) line += ", " + Locales.translateUnformatted("selectWorld.cheats");
			
			line += ", " + Locales.translateUnformatted("selectWorld.version") + " " + getVersionString();
			
			return line;
		}
	}
}
