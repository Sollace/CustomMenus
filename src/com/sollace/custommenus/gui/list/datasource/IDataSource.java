package com.sollace.custommenus.gui.list.datasource;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.list.UiList;
import com.sollace.custommenus.gui.list.UiListItem;

public interface IDataSource<T extends UiListItem<T>> {
	
	void populateList(JsonObject json, UiList<T> list);
	
	default void updateContents(UiList<T> list) {
		
	}
	
	void selectionChanged();
}
