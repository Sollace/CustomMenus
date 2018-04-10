package com.sollace.custommenus.registry;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sollace.custommenus.gui.action.ActionChangeOption;
import com.sollace.custommenus.gui.action.ActionCrash;
import com.sollace.custommenus.gui.action.ActionDelayed;
import com.sollace.custommenus.gui.action.ActionEndGame;
import com.sollace.custommenus.gui.action.ActionJoinGame;
import com.sollace.custommenus.gui.action.ActionAdjustOption;
import com.sollace.custommenus.gui.action.ActionAdjustSound;
import com.sollace.custommenus.gui.action.ActionAlterServer;
import com.sollace.custommenus.gui.action.ActionAlterWorld;
import com.sollace.custommenus.gui.action.ActionChangeDifficulty;
import com.sollace.custommenus.gui.action.ActionJoinServer;
import com.sollace.custommenus.gui.action.ActionLockDifficulty;
import com.sollace.custommenus.gui.action.ActionModifyElements;
import com.sollace.custommenus.gui.action.ActionOpenGui;
import com.sollace.custommenus.gui.action.ActionOpenLan;
import com.sollace.custommenus.gui.action.ActionOpenLink;
import com.sollace.custommenus.gui.action.ActionQuit;
import com.sollace.custommenus.gui.action.ActionRefresh;
import com.sollace.custommenus.gui.action.ActionResetControls;
import com.sollace.custommenus.gui.action.ActionRespawn;
import com.sollace.custommenus.gui.action.ActionReturn;
import com.sollace.custommenus.gui.action.ActionToggleSkin;
import com.sollace.custommenus.gui.action.ActionYesNo;
import com.sollace.custommenus.gui.action.IGuiAction;
import com.sollace.custommenus.gui.element.UiElement;
import com.sollace.custommenus.gui.input.UiField;
import com.sollace.custommenus.reflection.AbstractInstantiator;
import com.sollace.custommenus.reflection.IInstantiator;

/**
 * Registry of all standard-built in UiActions.
 * <p>
 * <b>Usage:</b>
 * 
 * {@code UiActions.register(<name>, <class>);}
 * 
 */
public final class UiActions {
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Map<String, IInstantiator<IGuiAction>> action_types = Maps.newHashMap();
	
	private static final Class<?>[]
			PARS_1 = new Class<?>[] {JsonObject.class, UiElement.class},
			PARS_2 = new Class<?>[]{JsonObject.class, UiField.class};
	
	static {
		register("openGui", ActionOpenGui.class);
		register("closeGui", ActionReturn.class);
		register("openLink", ActionOpenLink.class);
		register("openLan", ActionOpenLan.class);
		register("yesno", ActionYesNo.class);
		register("quit", ActionQuit.class);
		register("refresh", ActionRefresh.class);
		register("joinServer", ActionJoinServer.class);
		register("respawn", ActionRespawn.class);
		register("changeOption", ActionChangeOption.class);
		register("adjustOption", ActionAdjustOption.class);
		register("adjustSound", ActionAdjustSound.class);
		register("toggleSkinPart", ActionToggleSkin.class);
		register("endGame", ActionEndGame.class);
		register("joinGame", ActionJoinGame.class);
		register("changeDifficulty", ActionChangeDifficulty.class);
		register("lockDifficulty", ActionLockDifficulty.class);
		register("resetKeyBindings", ActionResetControls.class);
		register("delayed", ActionDelayed.class);
		register("modifyElements", ActionModifyElements.class);
		register("alterWorld", ActionAlterWorld.class);
		register("alterServer", ActionAlterServer.class);
		register("crash", ActionCrash.class);
	}
	
	public static void register(String name, Class<? extends IGuiAction> type) {
		try {
			if (action_types.containsKey(name)) LOGGER.warn(String.format("Duplicate registration %s", name));
			action_types.put(name, new Instantiator(type));
		} catch (Exception e) {
			LOGGER.error(String.format("Error registering action %s with type %s", name, type.getCanonicalName()), e);
		}
	}
	
	public static IGuiAction createAll(JsonElement json, UiElement owner) {
		if (json.isJsonObject()) return create(json.getAsJsonObject(), owner);
		JsonArray arr = json.getAsJsonArray();
		IGuiAction[] actions = new IGuiAction[arr.size()];
		for (int i = 0; i < actions.length; i++) {
			actions[i] = create(arr.get(i).getAsJsonObject(), owner);
		}
		return IGuiAction.all(actions);
	}
	
	public static IGuiAction create(JsonObject json, UiElement owner) {
		String key = json.get("type").getAsString();
		
		if (action_types.containsKey(key)) {
			return action_types.get(key).newInstance(json, owner);
		}
		
		LOGGER.warn(String.format("Invalid action type '%s'", key));
		return IGuiAction.NULL;
	}
	
	protected static class Instantiator extends AbstractInstantiator<IGuiAction> {
		
		public Instantiator(Class<? extends IGuiAction> type) {
			super(type, PARS_1, PARS_2);
		}
		
		@Override
		public IGuiAction newInstance(Object... pars) {
			try {
				if (altConstructorTwo != null && pars[1] instanceof UiField) {
					return altConstructorTwo.newInstance(pars);
				}
				if (altConstructorOne != null) {
					return altConstructorOne.newInstance(pars);
				}
				return defaultConstructor.newInstance();
				
			} catch (Exception e) {
				LOGGER.error("Unknown exception whilst creating action", e);
			}
			return null;
		}
	}
}
