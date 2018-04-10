package com.sollace.custommenus.gui.natives;

import java.util.Map;

import com.google.common.collect.Maps;
import com.sollace.custommenus.gui.action.IGuiDecider;
import com.sollace.custommenus.locale.Locales;
import com.sollace.custommenus.mixin.MGuiGameOver;
import com.sollace.custommenus.mixin.MGuiMultiplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class NativeFerry implements INativeFerry {
	
	Minecraft mc;
	
	IGuiDecider callback;
	
	ServerData data = null;
	
	final Map<String, String> params = Maps.newHashMap();
	
	public NativeFerry(Minecraft mc) {
		this.mc = mc;
	}
	
	@Override
	public Minecraft game() {
		return mc;
	}

	@Override
	public IGuiDecider decider() {
		return callback;
	}
	
	@Override
	public INativeFerry decider(IGuiDecider decider) {
		callback = decider;
		return this;
	}
	
	@Override
	public ServerData getActiveServerData() {
		if (data == null) setActiveServerData(getOrCreateServerData(this));
		return data;
	}
	
	@Override
	public INativeFerry setActiveServerData(ServerData data) {
		this.data = data;
		return this;
	}
	
	@Override
	public ITextComponent getPlayerDeathMessage() {
		return getOrCreateDeathMessage(this);
	}
	
	protected static ITextComponent getOrCreateDeathMessage(INativeFerry caller) {
		return caller.currentScreen() instanceof MGuiGameOver ? ((MGuiGameOver)caller.currentScreen()).getPlayerDeathMessage() : new TextComponentString("");
	}
	
	protected static ServerData getOrCreateServerData(INativeFerry caller) {
		return caller.currentScreen() instanceof MGuiMultiplayer ? ((MGuiMultiplayer)caller.currentScreen()).getSelectedServer() : new ServerData(Locales.translateUnformatted("selectServer.defaultName"), "", false);
	}
	
	@Override
	public String param(String key) {
		return params.containsKey(key) ? params.get(key) : key;
	}

	@Override
	public INativeFerry param(String key, String value) {
		params.put(key, value);
		return this;
	}
	
	@Override
	public boolean has(String key) {
		return params.containsKey(key);
	}
}
