package com.sollace.custommenus;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiRoot;
import com.sollace.custommenus.gui.natives.INativeFerry;
import com.sollace.custommenus.locale.Locales;
import com.sollace.custommenus.registry.UiNatives;
import com.sollace.custommenus.resources.JsonElementFactory;
import com.sollace.fml.IFML;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class GuiMenus implements IResourceManagerReloadListener {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Gson GSON = new GsonBuilder().create();
	private static final ParameterizedType OBJECT_TYPE = new ParameterizedType() {
        public Type[] getActualTypeArguments() {
            return new Type[] {};
        }
        
        public Type getRawType() {
            return JsonObject.class;
        }
        
        public Type getOwnerType() {
            return null;
        }
    };
    private static final ParameterizedType LIST_TYPE = new ParameterizedType() {
        public Type[] getActualTypeArguments() {
            return new Type[] {String.class};
        }
        
        public Type getRawType() {
            return ArrayList.class;
        }
        
        public Type getOwnerType() {
            return null;
        }
    };
	public static final GuiMenus INSTANCE = new GuiMenus();
	
	private final JsonElementFactory factory = new JsonElementFactory();
	
	public static JsonElementFactory jsonFactory() {
		return INSTANCE.factory;
	}
	
	private Map<String, JsonObject> guis = Maps.newHashMap();
	
	private String windowTitle = null;
	
	public boolean hasGuiConfigurationFor(String gui) {
		return gui != null && guis.containsKey(gui);
	}
	
	public void openMenu(Minecraft mc, String gui, INativeFerry ferry) {
		
		GuiProgressor.INSTANCE.completed();
		
		if ("realms".contentEquals(gui)) {
			new RealmsBridge().switchToRealms(mc.currentScreen);
			return;
		} else if ("createflatworld".contentEquals(gui) || "customizeworld".contentEquals(gui)) {
			return;
		}
		
		if (hasGuiConfigurationFor(gui)) {
			createCustomMenu(GuiNull.currentScreen(), ferry, gui).show();
		} else {
			mc.displayGuiScreen(UiNatives.create(gui, ferry));
		}
	}
	
	public UiRoot createCustomMenu(UiRoot parent, INativeFerry ferry, String gui) {
		return factory.loadGui(parent, ferry, guis.get(gui), gui);
	}
	
	private boolean preventRecall = false;
	
	public void displayGuiScreen(Minecraft sender, UiRoot screen, CallbackInfo info) {
		if (sender.world != null && screen.isNull()) return;
		
		if (!preventRecall && !screen.isNonNative()) {
			String type = screen.getViewPortId();
			
			UiRoot current = GuiNull.currentScreen();
			
			if (sender.world == null && screen.isNull() && !current.isNull()) {
				if (type.contentEquals(current.getViewPortId())) {
					info.cancel();
					return;
				}
			}
			
			if (hasGuiConfigurationFor(type)) {
				info.cancel();
				preventRecall = true;
				createCustomMenu(current, current.getFerry(), type).show();
				preventRecall = false;
			} else {
				LOGGER.info(String.format("Displaying Native Gui: %s", type));
			}
		}
	}
	
	private <T> T loadJsonData(InputStream stream, ParameterizedType type) {
		try {
			return JsonUtils.fromJson(GSON, new InputStreamReader(stream, StandardCharsets.UTF_8), type);
		} catch (Throwable e) {
			LOGGER.error("Exception whils reading guis.json", e);
		} finally {
            IOUtils.closeQuietly(stream);
        }
		return null;
	}

	@Override
	public void onResourceManagerReload(IResourceManager manager) {
		guis.clear();
		
		windowTitle = null;
		
		List<String> allGuiNames = new ArrayList<String>();
		
		for (String domain : manager.getResourceDomains()) {
			try {
				for (IResource resource : manager.getAllResources(new ResourceLocation(domain, "gui.json"))) {
					JsonObject theGui = loadJsonData(resource.getInputStream(), OBJECT_TYPE);
					
					List<String> guiNames = GSON.fromJson(theGui.get("interfaces"), LIST_TYPE);
					if (guiNames != null) {
						for (String i : guiNames) {
							if (!allGuiNames.contains(i)) allGuiNames.add(i);
						}
					}
					
					if (theGui.has("title")) windowTitle = theGui.get("title").getAsString();
				}
			} catch (IOException e) {  }
		}
		
		for (String domain : manager.getResourceDomains()) {
			try {
				for (String guiName : allGuiNames) {
					for (IResource resource : manager.getAllResources(new ResourceLocation(domain, String.format("interfaces/%s.json", guiName)))) {
						JsonObject gui = loadJsonData(resource.getInputStream(), OBJECT_TYPE);
						if (gui != null) guis.put(guiName, gui);
					}
				}
			} catch (IOException e) {  }
		}
		
		if (windowTitle != null) {
			Minecraft mc = Minecraft.getMinecraft();
			Display.setTitle(Locales.translateFormatted(windowTitle, mc.getVersion(), mc.getVersionType()));
		}
		
		GuiNull.currentScreen().refresh();
	}
	
	public void addCrashSections(CrashReportCategory category) {
		category.addDetail("Viewport id", new ICrashReportDetail<String>() {
			@Override
			public String call() throws Exception {
				return GuiNull.currentScreen().getViewPortId();
			}
		});
		category.addDetail("Native screen [Screen name]", new ICrashReportDetail<String>() {
			@Override
			public String call() throws Exception {
				return UiNatives.getNativeKeyFor(GuiNull.currentScreen().getViewPortId(), "~N/A~ / JSON Generated");
			}
		});
		category.addDetail("Has Forge?", new ICrashReportDetail<String>() {
			@Override
			public String call() throws Exception {
				return IFML.isForge() ? "yes :'(" : "no (:";
			}
		});
	}
}
