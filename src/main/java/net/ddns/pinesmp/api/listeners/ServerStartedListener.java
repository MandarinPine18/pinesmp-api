package net.ddns.pinesmp.api.listeners;

import net.ddns.pinesmp.api.mod.PineSMPAPI;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class ServerStartedListener implements ServerLifecycleEvents.ServerStarted {
	@Override
	public void onServerStarted(MinecraftServer server) {
		PineSMPAPI.getInstance().onStart(server);
	}
}
