package net.ddns.pinesmp.api.mod;

import com.mojang.brigadier.Command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.ddns.pinesmp.api.listeners.ServerStartedListener;
import net.ddns.pinesmp.api.net.PlayerServlet;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PineSMPAPI implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("pinesmp-api");
	public static MinecraftServer server = null;

	// this is a singleton
	private static PineSMPAPI instance = null;

	public static PineSMPAPI getInstance() {
		return instance;
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		PineSMPAPI.instance = this;

		ServerLifecycleEvents.SERVER_STARTED.register(new ServerStartedListener());

		Command<ServerCommandSource> command = context -> {
			assert server != null;
			server.sendMessage(Text.literal("test command called"));
			for (ServerPlayerEntity player : PlayerLookup.all(server)) {
				server.sendMessage(player.getDisplayName());
			}
			return 0;
		};
		CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
			LiteralCommandNode<ServerCommandSource> testNode = CommandManager
					.literal("test")
					.executes(command)
					.build();

			dispatcher.getRoot().addChild(testNode);
		}));
		LOGGER.info("Hello Fabric world!");
	}

	public void onStart(MinecraftServer server) {
		PineSMPAPI.server = server;
	}
}
