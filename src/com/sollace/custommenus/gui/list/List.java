package com.sollace.custommenus.gui.list;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.sollace.custommenus.GuiMenus;
import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.container.ScrollContainer;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.element.Label;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.geometry.EnumEdge;
import com.sollace.custommenus.gui.list.datasource.IDataSource;
import com.sollace.custommenus.gui.packing.ListPacker;
import com.sollace.custommenus.privileged.IGame;
import com.sollace.custommenus.registry.UiActions;
import com.sollace.custommenus.registry.UiDataSources;
import com.sollace.custommenus.utils.JsonUtils;

import net.minecraft.client.gui.FontRenderer;

public class List<T extends UiListItem<T>> extends ScrollContainer implements UiList<T> {
	
	protected final FontRenderer fonts;
	
	protected int color = 16777215;
	
	private T selectedItem = null;
	private int selectedIndex = 0;
	
	private IGuiAction change = null;
	
	private IDataSource<T> dataSource = null;
	
	private String name = null;
	protected boolean enabled = true;
	
	private JsonObject itemTemplate = null, labelTemplate = null;
	
	protected final java.util.List<T> items = Lists.newArrayList();
	protected final java.util.List<T> moveable = Lists.newArrayList();
	
	public List(UiContainer container) {
		super(container);
		
		fonts = IGame.current().fonts();
		
		packer = new ListPacker(this);
	}
	
	@Override
	public int size() {
		return items.size();
	}
	
	@Override
	public FontRenderer getFonts() {
		return fonts;
	}
	
	@Override
	public UiElement init(JsonObject json) {
		color = JsonUtils.get(json, "itemColor", color);
		if (json.has("change")) setChangeAction(UiActions.create(json.get("change").getAsJsonObject(), this));
		selectedIndex = JsonUtils.get(json, "value", selectedIndex);
		
		if (json.has("itemTemplate")) itemTemplate = json.get("itemTemplate").getAsJsonObject();
		if (json.has("labelTemplate")) labelTemplate = json.get("labelTemplate").getAsJsonObject();
		
		if (json.has("datasource")) {
			dataSource = UiDataSources.create(json.get("datasource"));
			if (dataSource != null) dataSource.populateList(json, this);
		}
		
		super.init(json);
		return this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void addChild(UiElement child) {
		if (child instanceof UiListItem) {
			if (itemTemplate != null) GuiMenus.jsonFactory().populateElement(child, this, itemTemplate);
			items.add((T)child);
			if (((UiListItem<?>)child).isMoveable()) {
				moveable.add((T)child);
			}
		}
		if (labelTemplate != null && child instanceof Label) {
			GuiMenus.jsonFactory().populateElement(child, this, labelTemplate);
		}
		
		super.addChild(child);
	}
	
	protected void renderTranslated(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
		super.renderTranslated(sender, mouseX, mouseY, partialTicks);
		if (dataSource != null) {
			dataSource.updateContents(this);
		}
	}
	
	@Override
	public void setChangeAction(IGuiAction action) {
		change = action;
	}
	
	@Override
	public int getItemColour() {
		return color;
	}
	
	@Override
	public boolean isFocused(int mouseX, int mouseY) {
		return enabled && super.isFocused(mouseX, mouseY);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Integer getValue() {
		return selectedIndex;
	}
	
	@Override
	public T getSelectedItem() {
		return selectedItem;
	}
	
	@Override
	public String getDisplayString() {
		return selectedItem == null ? null : selectedItem.getDisplayString();
	}
	
	@Override
	public void setValue(Integer value) {
		setValue(value, GuiNull.currentScreen());
	}
	
	@Override
	public boolean setValue(int index, UiRoot sender) {
		selectedIndex = index % items.size();
		selectedItem = items.get(selectedIndex);
		if (change != null) {
			change.perform(sender);
			return true;
		}
		return false;
	}
	
	@Override
	public UiContainer getContainer() {
		return container;
	}
	
	@Override
	public boolean performAction(int mouseX, int mouseY, UiRoot sender) {
		boolean r = super.performAction(mouseX, mouseY, sender);
		if (dataSource != null) dataSource.selectionChanged();
		return r;
	}
	
	@Override
	public boolean keyPressed(UiRoot sender, char key, int keyCode) {
		if (super.keyPressed(sender, key, keyCode)) {
			if (dataSource != null) dataSource.selectionChanged();
			return true;
		}
		return false;
	}
	
	public boolean canMove(T item, EnumEdge direction) {
		if (!item.isMoveable()) return false;
		if (direction.unitLength() < 0) return moveable.indexOf(item) > 0;
		return moveable.indexOf(item) < moveable.size() - 1;
	}
	
	@Override
	public int moveItem(T item, EnumEdge direction) {
		int currentPos = moveable.indexOf(item);
		if (currentPos < 0) return currentPos;
		
		int newPos = currentPos + direction.unitLength();
		if (newPos < 0 || newPos >= moveable.size()) return currentPos;
		
		T to = moveable.get(newPos);
		if (to != null) {
			moveable.set(currentPos, to);
			moveable.set(newPos, item);
			
			currentPos = children.indexOf(item);
			newPos = children.indexOf(to);
			children.set(currentPos, to);
			children.set(newPos, item);
			
			to.itemMoved(currentPos);
			item.itemMoved(newPos);
			
			currentPos = items.indexOf(item);
			newPos = items.indexOf(to);
			
			items.set(currentPos, to);
			items.set(newPos, item);
			
			reposition(container);
		}
		
		return items.indexOf(item);
	}

	@Override
	public void clear() {
		for (UiListItem<T> i : items) children.remove(i);
		items.clear();
		moveable.clear();
	}
}
