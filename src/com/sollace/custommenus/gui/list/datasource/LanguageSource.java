package com.sollace.custommenus.gui.list.datasource;

import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.action.ActionChangeLanguage;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.list.ListItemBase;
import com.sollace.custommenus.gui.list.UiList;
import com.sollace.custommenus.privileged.IGame;

import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;

public class LanguageSource implements IDataSource<LanguageSource.ListItem> {
	
	private final LanguageManager langs;
	
	public LanguageSource(IGame mc) {
		langs = mc.getNative().getLanguageManager();
	}
	
	@Override
	public void populateList(JsonObject json, UiList<ListItem> list) {
		for (Language lang : langs.getLanguages()) {
			list.addChild(new ListItem(list, lang));
		}
	}
	
	@Override
	public void selectionChanged() {
		
	}
	
	protected class ListItem extends ListItemBase<ListItem> {
		
		private final Language lang;
		
		protected ListItem(UiList<ListItem> owner, Language lang) {
			super(owner);
			this.lang = lang;
			setLabel(lang.toString());
			setAction(new ActionChangeLanguage(lang));
		}
		
		@Override
		public boolean isSelected() {
			return langs.getCurrentLanguage().equals(lang);
		}
		

		@Override
		public void render(UiRoot sender, int mouseX, int mouseY, float partialTicks) {
			owner.getFonts().setBidiFlag(true);
			super.render(sender, mouseX, mouseY, partialTicks);
			owner.getFonts().setBidiFlag(langs.getCurrentLanguage().isBidirectional());
		}
	}
}
