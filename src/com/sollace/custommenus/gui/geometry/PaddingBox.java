package com.sollace.custommenus.gui.geometry;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiView;

public class PaddingBox implements IPositioned {
	protected Unit top, bottom, left, right;
	
	public PaddingBox() {
		top = new Unit(0);
		bottom = new Unit(0);
		left = new Unit(0);
		right = new Unit(0);
	}
	
	public void reposition(UiView container) {
		int h = container.getViewPort().getHeight();
		int w = container.getViewPort().getWidth();
		
		top.recalculate(h);
		bottom.recalculate(h);
		
		left.recalculate(w);
		right.recalculate(w);
	}
	
	public PaddingBox setTop(int amount) {
		top.set(amount);
		return this;
	}
	
	public PaddingBox setBottom(int amount) {
		bottom.set(amount);
		return this;
	}
	
	public PaddingBox setLeft(int amount) {
		left.set(amount);
		return this;
	}
	
	public PaddingBox setRight(int amount) {
		right.set(amount);
		return this;
	}
	
	public PaddingBox init(JsonObject json) {
		top.loadJson("top", json);
		bottom.loadJson("bottom", json);
		left.loadJson("left", json);
		right.loadJson("right", json);
		return this;
	}
	
	public int getLeft() {
		return left.computed;
	}
	
	public int getRight() {
		return right.computed;
	}
	
	public int getTop() {
		return top.computed;
	}
	
	public int getBottom() {
		return bottom.computed;
	}
	
}
