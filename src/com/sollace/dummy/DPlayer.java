package com.sollace.dummy;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;

public class DPlayer extends AbstractClientPlayer {
	
	private final NetworkPlayerInfo info;
	
	public DPlayer(GameProfile profile) {
		super(new DWorld(), profile);
		info = new NetworkPlayerInfo(profile);
	}

	@Override
	public boolean isSpectator() {
		return false;
	}

	@Override
	public boolean isCreative() {
		return true;
	}
	
	@Override
	protected NetworkPlayerInfo getPlayerInfo() {
        return info;
    }
}
