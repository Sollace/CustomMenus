package com.sollace.custommenus.gui.packing;

import java.util.Iterator;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.geometry.UiBounded;
import com.sollace.custommenus.gui.geometry.Unit;
import com.sollace.custommenus.utils.JsonUtils;

public class GridPacker implements UiPacker {
	protected Unit elementHeight = new Unit(20),
			elementWidth = new Unit(200);
	protected int columns = 1,
			columnSpacing = 4,
			rowSpacing = 4;
	
	public GridPacker(JsonObject json) {
		if (json.has("elementWidth")) elementWidth.loadJson("elementWidth", json);
		if (json.has("elementHeight")) elementHeight.loadJson("elementHeight", json);
		columns = JsonUtils.get(json, "columns", columns);
		columnSpacing = JsonUtils.get(json, "columnSpacing", columnSpacing);
		rowSpacing = JsonUtils.get(json, "rowSpacing", rowSpacing);
	}
	
	@Override
	public void pack(UiContainer container) {
		Iterator<UiElement> iterator = container.getElementsIterator();
		
		int i = 0;
		
		int column = 0;
		int row = 0;
		
		elementWidth.recalculate(container.getViewPort().getWidth());
		elementHeight.recalculate(container.getViewPort().getHeight());
		
		while (iterator.hasNext()) {
			UiElement element = iterator.next();
			
			if (element instanceof UiBounded) {
				
				
				column = i % columns;
				row = i / columns;
				
				((UiBounded)element).setPos(column * (elementWidth.computed + columnSpacing), row * (elementHeight.computed + rowSpacing));
				((UiBounded)element).setSize(elementWidth.computed, elementHeight.computed);
			}
			i++;
		}
	}
	
}
