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

	public static MinecraftServlet startServlet(MinecraftServer server, int port) {
		if (instance == null) {
			instance = new MinecraftServlet(server);
			instance.configureServer(port);
		}
		return MinecraftServlet.getInstance();
	}

	public static MinecraftServlet getInstance() {
		return instance;
	}

	private void configureServer(int port) {
		Spark.port(port);
		get("pinesmp/api/test", (request, response) -> "this works");
	}
}
