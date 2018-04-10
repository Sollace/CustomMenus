package com.sollace.custommenus.registry;

import java.util.Map;

import org.lwjgl.opengl.GLContext;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.container.UiContainer;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.visibility.IGuiVisibilityFilter;
import com.sollace.custommenus.privileged.IGame;
import com.sollace.custommenus.privileged.IGame.EnumGameState;
import com.sollace.custommenus.resources.JsonElementFactory;
import com.sollace.fml.IFML;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.world.World;

/**
 * Registry of all built-in element visibility tests.
 * <p>
 * <b>Usage:</b>
 * 
 * {@code UiFilters.register(<name>, (container, element) -> result);}
 */
public class UiFilters {
	
	private static final Map<String, IGuiVisibilityFilter> filters = Maps.newHashMap();
	
	static {
		register("MC_FORGE", (container, element) -> IFML.isForge());
		register("IN_GAME", (container, element) -> IGame.current().inGame());
		register("DEV_ENV", (container, element) -> "true".equals(System.getProperty("mcpenv")));
		register("GL_WARN", (container, element) -> !GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported());
		register("LAN_GAME", (container, element) -> IGame.current().currentState().isLan());
		register("IN_GAME_HARDCORE", (container, element) -> {
			World w = IGame.current().getNative().world;
			return w != null && w.getWorldInfo().isHardcoreModeEnabled();
		});
		register("IN_GAME_SINGLEPLAYER", (container, element) -> IGame.current().currentState() == EnumGameState.SINGLEPLAYER);
		register("IN_GAME_MULTIPLAYER", (container, element) -> IGame.current().currentState() == EnumGameState.MULTIPLAYER);
		register("IN_GAME_REALMS", (container, element) -> IGame.current().getNative().isConnectedToRealms());
		register("DEMO", (container, element) -> IGame.current().getNative().isDemo());
	}
	
	public static void register(String name, IGuiVisibilityFilter filter) {
		filters.put(name, filter);
	}
	
	public static IGuiVisibilityFilter getFilter(String condition) {
		return filters.get(condition);
	}
	
	public static Boolean testCondition(JsonObject condition, UiContainer container, UiElement element, JsonElementFactory factory) {
		String key = condition.get("condition").getAsString();
		boolean invert = condition.has("invert") && condition.get("invert").getAsBoolean();
		
		if (filters.containsKey(key)) {
			IGuiVisibilityFilter filter = getFilter(key);
			boolean answer = filter.filter(container, element);
			
			if (invert) answer = !answer;
			
			if (condition.has("apply")) {
				if (answer) {
					factory.populateElement(element, container, condition.get("apply").getAsJsonObject());
				}
			} else {
				element.setVisible(answer);
			}
		}
		return null;
	}
	
	public static void updateElementVisibility(String condition, UiContainer container, UiElement element) {
		if (filters.containsKey(condition)) {
			element.setVisible(getFilter(condition).filter(container, element));
		}
	}
}
