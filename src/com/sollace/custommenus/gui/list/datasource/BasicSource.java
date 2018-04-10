package com.sollace.custommenus.gui.list.datasource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.list.ListItemBase;
import com.sollace.custommenus.gui.list.UiList;
import com.sollace.custommenus.gui.list.UiListItem;

public class BasicSource<T extends UiListItem<T>> implements IDataSource<T> {
	
	private final JsonArray items;
	
	public BasicSource(JsonArray json) {
		items = json;
	}
	
	public BasicSource(JsonObject json) {
		items = json.get("values").getAsJsonArray();
	}
	
	@Override
	public void populateList(JsonObject json, UiList<T> list) {
		for (int i = 0; i < items.size(); i++) {
			ListItemBase<T> item = new ListItemBase<T>(list).setLabel(items.get(i).getAsString());
			list.addChild(item);
		}
	}
	
	@Override
	public void selectionChanged() {
		
	}
}
