package net.pinesmp.pinesmpapi.mod;

import com.mojang.brigadier.Command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.pinesmp.pinesmpapi.listeners.ServerStartingListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.pinesmp.pinesmpapi.listeners.ServerStoppingListener;
import net.pinesmp.pinesmpapi.net.Application;
import net.pinesmp.pinesmpapi.net.PlayerController;
import net.pinesmp.pinesmpapi.util.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PineSMPAPI implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("pinesmp-api");
	public static MinecraftServer server = null;
	private static Configuration configuration = null;

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

		// spitting out defaults just in case there's missing stuff
		configuration = getConfiguration();
		configuration.fileExport();

		// building a configuration map for the spring application
		Map<String, Object> applicationProperties = new HashMap<String, Object>();
		applicationProperties.put("server.port", configuration.get("PORT"));
		applicationProperties.put("server.ssl.enabled", configuration.get("SSL_ENCRYPTION"));
		applicationProperties.put("server.ssl.key-alias", configuration.get("SSL_key-alias"));
		applicationProperties.put("server.ssl.key-store", configuration.getFile("SSL_key-store").getAbsolutePath());
		applicationProperties.put("server.ssl.key-store-password", configuration.get("SSL_key-store-password"));
		applicationProperties.put("server.ssl.trust-store", configuration.getFile("SSL_trust-store").getAbsolutePath());
		applicationProperties.put("server.ssl.trust-store-password", configuration.get("SSL_trust-store-password"));

		// running the spring application
		SpringApplication application = new SpringApplicationBuilder(Application.class)
				.web(WebApplicationType.SERVLET).build();
		application.setDefaultProperties(applicationProperties);
		application.run();

		ServerLifecycleEvents.SERVER_STARTING.register(new ServerStartingListener());
		ServerLifecycleEvents.SERVER_STOPPING.register(new ServerStoppingListener());

		Command<ServerCommandSource> command = ignored -> {
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
	}

	// listeners
	public void onStarting(MinecraftServer server) {
		PineSMPAPI.server = server;

		PlayerController.setServer(server);
	}

	public void onStopping(MinecraftServer server) {
		getConfiguration().fileExport();
	}

	public static Configuration getConfiguration() {
		if (configuration == null) {
			configuration = new Configuration(new File("config/pinesmp-api.properties"), new File("config/pinesmp-api"));
		}

		return configuration;
	}
}
