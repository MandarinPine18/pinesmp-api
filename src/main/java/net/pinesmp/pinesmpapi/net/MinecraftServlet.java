package net.pinesmp.pinesmpapi.net;

import net.minecraft.server.MinecraftServer;
import spark.Spark;

import static spark.Spark.get;

public class MinecraftServlet {
	private final MinecraftServer server;

	// this is a singleton
	private static MinecraftServlet instance = null;

	public MinecraftServer getServer() {
		return server;
	}

	private MinecraftServlet(MinecraftServer server) {
		this.server = server;
	}

	public static MinecraftServlet startServlet(MinecraftServer server, int port, boolean ssl, String keystoreFile, String keystorePassword, String truststoreFile, String truststorePassword) {
		if (instance == null) {
			instance = new MinecraftServlet(server);
			instance.configureServer(port, ssl, keystoreFile, keystorePassword, truststoreFile, truststorePassword);
		}
		return MinecraftServlet.getInstance();
	}

	public static MinecraftServlet getInstance() {
		return instance;
	}

	private void configureServer(int port, boolean ssl, String keystoreFile, String keystorePassword, String truststoreFile, String truststorePassword) {
		Spark.port(port);

		if (ssl) {
			Spark.secure(keystoreFile, keystorePassword, truststoreFile, truststorePassword);
		}

		get("/api/test", (request, response) -> "Server running");      // just a test endpoint
	}
}
