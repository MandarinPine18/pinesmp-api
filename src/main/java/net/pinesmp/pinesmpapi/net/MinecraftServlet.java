package net.pinesmp.pinesmpapi.net;

import com.google.gson.Gson;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.pinesmp.pinesmpapi.mod.PineSMPAPI;
import net.pinesmp.pinesmpapi.util.Configuration;
import net.pinesmp.pinesmpapi.util.ResponseMap;
import spark.QueryParamsMap;
import spark.Spark;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.routes;
import static spark.Spark.secure;
import static spark.Spark.unmap;

public class MinecraftServlet {
	private final MinecraftServer server;
	private final ServletConfiguration configuration;
	static class ServletConfiguration {
		private int port;
		private boolean ssl;
		private String keystoreFile;
		private String keystorePassword;
		private String truststoreFile;
		private String truststorePassword;
	}

	// this is a singleton
	private static MinecraftServlet instance = null;

	public MinecraftServer getServer() {
		return server;
	}

	private MinecraftServlet(MinecraftServer server) {
		this.server = server;
		this.configuration = new ServletConfiguration();
	}

	public static MinecraftServlet startServlet(MinecraftServer server, Configuration configuration) {
		// if we don't have an instance, make one
		if (instance == null) {
			instance = new MinecraftServlet(server);
		}

		// setting the configuration fields
		instance.configuration.port = Integer.parseInt(configuration.get("PORT"));
		instance.configuration.ssl = Boolean.parseBoolean(configuration.get("SSL_ENCRYPTION"));
		instance.configuration.keystoreFile = configuration.getFile("SSL_keystoreFile").getAbsolutePath();
		instance.configuration.keystorePassword = configuration.get("SSL_keystorePassword");
		instance.configuration.truststoreFile = configuration.getFile("SSL_truststoreFile").getAbsolutePath();
		instance.configuration.truststorePassword = configuration.get("SSL_truststorePassword");

		// implementing the given configuration
		instance.doConfiguration();

		return instance;
	}

	public static MinecraftServlet getInstance() {
		return instance;
	}

	private void doConfiguration() {
		port(configuration.port);

		if (configuration.ssl) {
			secure(configuration.keystoreFile, configuration.keystorePassword, configuration.truststoreFile, configuration.truststorePassword);
		}

		path("/api", () -> {
			before("/*", (request, response) -> {
				PineSMPAPI.LOGGER.info("Received API call from " + request.ip());
				response.type("application/json");
			});
			get("/test", (request, response) -> "Server running");      // just a test endpoint
			path("/players", () -> {
				get("", (request, response) -> {
					// initializing basic response stuff
					ResponseMap responseMap = new ResponseMap();

					// parameters
					boolean uuid = Boolean.parseBoolean(request.queryParamOrDefault("uuid", "false"));  // true: get UUIDs, false: get display names

					// list of players
					List<String> players = new LinkedList<String>();
					responseMap.result.put("players", players);

					for (ServerPlayerEntity player: PlayerLookup.all(server)) {
						players.add(uuid ? player.getUuidAsString() : player.getDisplayName().getString());
					}

					Gson gson = new Gson();

					// setting the response
					response.body(responseMap.serialize());
					response.status(200);

					return response.body();
				});
			});
		});
	}
}
