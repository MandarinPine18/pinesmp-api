package net.pinesmp.pinesmpapi.net;

import net.minecraft.server.MinecraftServer;
import net.pinesmp.pinesmpapi.util.Configuration;
import spark.Spark;

import static spark.Spark.get;

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
		Spark.port(configuration.port);

		if (configuration.ssl) {
			Spark.secure(configuration.keystoreFile, configuration.keystorePassword, configuration.truststoreFile, configuration.truststorePassword);
		}

		get("/api/test", (request, response) -> "Server running");      // just a test endpoint
	}
}
