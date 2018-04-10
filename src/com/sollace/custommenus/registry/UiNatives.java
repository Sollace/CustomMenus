package com.sollace.custommenus.registry;

import java.util.Map;

import com.google.common.collect.Maps;
import com.sollace.custommenus.GuiNull;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.natives.INativeGuiFactory;
import com.sollace.custommenus.locale.Locales;
import com.sollace.custommenus.gui.natives.INativeFerry;
import com.sollace.custommenus.reflection.Classes;
import com.sollace.custommenus.reflection.IInstantiator;
import com.sollace.custommenus.reflection.InstantiatorCreator;

import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiCreateFlatWorld;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.client.gui.GuiCustomizeWorldScreen;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiFlatPresets;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenAddServer;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.GuiSnooper;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.GuiWorldEdit;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.ScreenChatOptions;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.util.text.TextComponentString;

/**
 * Registry of native minecraft screens.
 * This allows for interfacing between layers, and for creating native guis from json.
 * <p>
 * <b>Usage:</b>
 * <br>
 * {@code UiNatives.register(<name>, <class>, <factory>);}
 * <br>
 * {@code UiNatives.register(<name>, <classname>, <instantiatorcreator>, <constructor argument types>...);}
 */
public final class UiNatives {
	private static final Map<String, INativeGuiFactory<?>> factories = Maps.newHashMap();
	private static final Map<Class<? extends GuiScreen>, String> classes = Maps.newHashMap();
	private static final Map<String, Class<? extends GuiScreen>> keys = Maps.newHashMap();
	
	static {
		register("mainmenu", GuiMainMenu.class, mc -> new GuiMainMenu());
		register("options", GuiOptions.class, mc -> new GuiOptions(mc.currentScreen(), mc.game().gameSettings));
		register("language", GuiLanguage.class, mc -> new GuiLanguage(mc.currentScreen(), mc.game().gameSettings, mc.game().getLanguageManager()));
		register("singleplayer", GuiWorldSelection.class, mc -> new GuiWorldSelection(mc.currentScreen()));
		register("multiplayer", GuiMultiplayer.class, mc -> new GuiMultiplayer(mc.currentScreen()));
		register("customizeskin", GuiCustomizeSkin.class, mc -> new GuiCustomizeSkin(mc.currentScreen()));
		register("videosettings", GuiVideoSettings.class, mc -> new GuiVideoSettings(mc.currentScreen(), mc.game().gameSettings));
		register("controls", GuiControls.class, mc -> new GuiControls(mc.currentScreen(), mc.game().gameSettings));
		register("chatoptions", ScreenChatOptions.class, mc -> new ScreenChatOptions(mc.currentScreen(), mc.game().gameSettings));
		register("snooper", GuiSnooper.class, mc -> new GuiSnooper(mc.currentScreen(), mc.game().gameSettings));
		register("resourcepacks", GuiScreenResourcePacks.class, mc -> new GuiScreenResourcePacks(mc.currentScreen()));
		register("soundoptions", GuiScreenOptionsSounds.class, mc -> new GuiScreenOptionsSounds(mc.currentScreen(), mc.game().gameSettings));
		register("credits", GuiWinGame.class, mc -> new GuiWinGame(false, mc.callback()));
		register("createworld", GuiCreateWorld.class, mc -> new GuiCreateWorld(mc.currentScreen()));
		register("createflatworld", GuiCreateFlatWorld.class, mc -> new GuiCreateFlatWorld(mc.callback(), mc.param("genSettings")));
		register("customizeworld", GuiCustomizeWorldScreen.class, mc -> new GuiCustomizeWorldScreen(mc.callback(), mc.param("genSettings")));
		register("editworld", GuiWorldEdit.class, mc -> new GuiWorldEdit(mc.currentScreen(), mc.param("worldId")));
		register("advancements", GuiScreenAdvancements.class, mc -> new GuiScreenAdvancements(mc.game().player.connection.getAdvancementManager()));
		register("stats", GuiStats.class, mc -> new GuiStats(mc.currentScreen(), mc.game().player.getStatFileWriter()));
		register("sharelan", GuiShareToLan.class, mc -> new GuiShareToLan(mc.currentScreen()));
		register("pause", GuiIngameMenu.class, mc -> new GuiIngameMenu());
		register("loading", GuiScreenWorking.class, mc -> new GuiScreenWorking());
		register("gameover", GuiGameOver.class, mc -> new GuiGameOver(mc.game().player.getCombatTracker().getDeathMessage()));
		register("connectserver", GuiScreenServerList.class, mc -> new GuiScreenServerList(mc.callback(), mc.getActiveServerData()));
		register("addserver", GuiScreenAddServer.class, mc -> new GuiScreenAddServer(mc.callback(), mc.getActiveServerData()));
		register("errorscreen", GuiErrorScreen.class, mc -> new GuiErrorScreen(Locales.translateUnformatted(mc.param("title")), Locales.translateUnformatted(mc.param("message"))));
		register("connecting", GuiConnecting.class, mc -> new GuiConnecting(mc.currentScreen(), mc.game(), mc.getActiveServerData()));
		register("disconnected", GuiDisconnected.class, mc -> new GuiDisconnected(mc.currentScreen(), mc.param("reason"), new TextComponentString(mc.param("message"))));
		register("yesno", GuiYesNo.class, mc -> {
			GuiYesNo yesno = new GuiYesNo(mc.yesno(), mc.param("title"), mc.param("question"), mc.param("yes"), mc.param("no"), 0);
			if (mc.has("delay")) yesno.setButtonDelay(Integer.parseInt(mc.param("delay")));
			return yesno;
		});
		register("mods", "net.minecraftforge.fml.client.GuiModList", instantiator -> instantiator.newInstance(GuiNull.currentScreen().getNative()), GuiScreen.class);
	}
	
	public static <T extends GuiScreen> void register(String name, Class<T> type, INativeGuiFactory<T> factory) {
		factories.put(name, factory);
		classes.put(type, name);
		keys.put(name, type);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends GuiScreen> void register(String name, String className, InstantiatorCreator<T> creator, Class<?> ...params) {
		Class<?> type = Classes.get(className);
		if (type == null || !GuiScreen.class.isAssignableFrom(type)) return;
		IInstantiator<T> instantiator = IInstantiator.create((Class<T>)type, params);
		factories.put(name, mc -> {
			try {
				return creator.call(instantiator);
			} catch (Throwable e) {
				factories.put(name, mc2 -> null);
				return null;
			}
		});
		classes.put((Class<T>)type, name);
		keys.put(name, (Class<T>)type);
	}
	
	private static <T extends GuiScreen> T registerReflexively(String key, INativeFerry ferry) {
		Class<T> type = Classes.get(key);
		if (type != null) {
			IInstantiator<T> instantiator = IInstantiator.createDynamic(type, GuiScreen.class);
			T result = instantiator.newInstance(ferry.currentScreen());
			if (result != null) {
				register(key, type, mc -> instantiator.newInstance(mc.currentScreen()));
			}
			return result;
		}
		return null;
	}
	
	public static String getKeyFor(GuiScreen gui) {
		if (gui instanceof GuiScreenRealmsProxy) {
			RealmsScreen realms = ((GuiScreenRealmsProxy)gui).getProxy();
			if (realms != null) return "realms {" + realms.getClass().getCanonicalName() + "}";
			return "realms";
		}
		if (gui instanceof GuiDownloadTerrain) return "terraingen";
		if (gui instanceof GuiFlatPresets) return "flatpresets";
		
		Class<?> c = gui.getClass();
		
		if (classes.containsKey(c)) return classes.get(c);
		
		if (keys.containsKey("mods") && keys.get("mods").isAssignableFrom(c)) return "mods";
		
		return c.getCanonicalName();
	}
	
	public static String getNativeKeyFor(String key, String none) {
		if (keys.containsKey(key)) return keys.get(key).getCanonicalName();
		if ("realms".contentEquals(key)) return GuiScreenRealmsProxy.class.getCanonicalName();
		if ("terraingen".contentEquals(key)) return GuiDownloadTerrain.class.getCanonicalName();
		if ("flatpresets".contentEquals(key)) return GuiFlatPresets.class.getCanonicalName();
		return none;
	}
	
	public static GuiScreen create(String key, INativeFerry mc) {
		if (key == "terraingen") key = "loading";
		
		if (factories.containsKey(key)) {
			return factories.get(key).newInstance(mc);
		}
		
		return registerReflexively(key, mc);
	}
	
	public static String getGuiViewportKey(GuiScreen screen) {
		return ((UiRoot)screen).getViewPortId();
	}
}
