package net.ddns.pinesmp.api.util;

import net.minecraft.server.MinecraftServer;

public interface MinecraftServerInterface {
	MinecraftServer instance = null;

	static MinecraftServer getInstance() {
		return instance;
	}
}
