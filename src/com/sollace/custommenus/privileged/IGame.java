package com.sollace.custommenus.privileged;

import com.mumfrey.liteloader.client.overlays.IMinecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.world.storage.ISaveFormat;

public interface IGame extends IMinecraft {
	String getPlayerState();
	
	default boolean inGame() {
		return getNative().world != null;
	}
	
	default EnumGameState currentState() {
		return EnumGameState.valueOf(getPlayerState().toUpperCase());
	}
	
	default Minecraft getNative() {
		return (Minecraft)this;
	}
	
	default FontRenderer fonts() {
		return ((Minecraft)this).fontRenderer;
	}
	
	default GameSettings settings() {
		return ((Minecraft)this).gameSettings;
	}
	
	default ISaveFormat saves() {
		return ((Minecraft)this).getSaveLoader();
	}
	
	default IReloadableResourceManager resources() {
		return (IReloadableResourceManager)((Minecraft)this).getResourceManager();
	}
	
	public static IGame current() {
		return (IGame)Minecraft.getMinecraft();
	}
	
	public static enum EnumGameState {
		HOSTING_LAN,
		PLAYING_LAN,
		SINGLEPLAYER,
		MULTIPLAYER,
		OUT_OF_GAME;
		
		public boolean isLan() {
			return this == HOSTING_LAN || this == PLAYING_LAN;
		}
	}
}
