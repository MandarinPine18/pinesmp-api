package net.pinesmp.pinesmpapi.listeners;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.pinesmp.pinesmpapi.mod.PineSMPAPI;

public class ServerStoppingListener implements ServerLifecycleEvents.ServerStopping {
	@Override
	public void onServerStopping(MinecraftServer server) {
		PineSMPAPI.getInstance().onStopping(server);
	}
}
