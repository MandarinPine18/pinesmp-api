package net.pinesmp.pinesmpapi.listeners;

import net.pinesmp.pinesmpapi.mod.PineSMPAPI;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerStartedListener implements ServerLifecycleEvents.ServerStarted {
	@Override
	public void onServerStarted(MinecraftServer server) {
		PineSMPAPI.getInstance().onStart(server);
	}
}
