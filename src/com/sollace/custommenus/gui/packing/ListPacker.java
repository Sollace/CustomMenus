package com.sollace.custommenus.gui.packing;

import java.util.Iterator;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.element.Label;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.geometry.UiBounded;
import com.sollace.custommenus.gui.geometry.Unit;
import com.sollace.custommenus.gui.list.UiList;
import com.sollace.custommenus.utils.JsonUtils;

public class ListPacker implements UiPacker {
	
	public Unit listWidth = new Unit(120);
	
	public int itemHeight;
	
	public ListPacker(UiList<?> owner) {
		itemHeight = owner.getFonts().FONT_HEIGHT;
	}
	
	@Override
	public void init(JsonObject json) {
		if (json.has("listWidth")) listWidth.loadJson("listWidth", json);
		itemHeight = JsonUtils.get(json, "itemHeight", itemHeight);
	}
	
	@Override
	public void pack(UiContainer container) {
		Iterator<UiElement> elements = container.getElementsIterator();
		
		int index = 0;
		
		listWidth.recalculate(container.getViewPort().getWidth());
		
		int w = listWidth.computed;
		int h = itemHeight;
		
		while (elements.hasNext()) {
			UiElement element = elements.next();
			
			if (element instanceof UiBounded) {
				((UiBounded)element).setSize(w, h);
				
				if (element instanceof Label) {
					((UiBounded)element).setPos(0, h / 2 + index * h - 1);
				} else {
					((UiBounded)element).setPos(-w / 2, h / 2 + index * h - 1);
				}
			}
			index++;
		}
	}

}
