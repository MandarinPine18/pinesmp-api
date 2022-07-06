package net.pinesmp.pinesmpapi.listeners;

import net.pinesmp.pinesmpapi.mod.PineSMPAPI;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerStartingListener implements ServerLifecycleEvents.ServerStarting {
	@Override
	public void onServerStarting(MinecraftServer server) {
		PineSMPAPI.getInstance().onStarting(server);
	}
}
