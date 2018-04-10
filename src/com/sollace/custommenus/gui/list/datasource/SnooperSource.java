package com.sollace.custommenus.gui.list.datasource;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.list.ListItemBase;
import com.sollace.custommenus.gui.list.UiList;
import com.sollace.custommenus.privileged.IGame;

public class SnooperSource implements IDataSource<SnooperSource.ListItem> {
	
	private final IGame mc;
	
	public SnooperSource(IGame mc) {
		this.mc = mc;
	}
	
	protected void addItems(UiList<ListItem> list, boolean isClient, Map<String, String> spoops) {
		for (Entry<String, String> i : new TreeMap<String, String>(spoops).entrySet()) {
			list.addChild(new ListItem(list, isClient, i));
		}
	}
	
	@Override
	public void populateList(JsonObject json, UiList<ListItem> list) {
		boolean dual = mc.getNative().getIntegratedServer() != null && mc.getNative().getIntegratedServer().getPlayerUsageSnooper() != null;
		
		addItems(list, dual, mc.getNative().getPlayerUsageSnooper().getCurrentStats());
		
		if (dual) addItems(list, false, mc.getNative().getIntegratedServer().getPlayerUsageSnooper().getCurrentStats());
	}

	@Override
	public void selectionChanged() {
		
	}
	
	protected class ListItem extends ListItemBase<ListItem> {
		
		private final Entry<String, String> spoop;
		
		private final boolean isC;
		
		protected ListItem(UiList<ListItem> owner, boolean isC, Entry<String, String> entry) {
			super(owner);
			this.isC = isC;
			spoop = entry;
			
		}
		
		@Override
		public boolean isSelected() {
			return false;
		}
		
		@Override
		public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
			owner.getFonts().drawString(owner.getFonts().trimStringToWidth((isC ? "C " : "") + spoop.getKey(), width / 2 - 2), x, y, 16777215);
			owner.getFonts().drawString(owner.getFonts().trimStringToWidth(spoop.getValue(), width / 2), x + width / 2, y, 16777215);
		}
	}
}
