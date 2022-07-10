package net.pinesmp.pinesmpapi.net;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.pinesmp.pinesmpapi.util.Response;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping(value = "/api/player", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlayerController {
	private static MinecraftServer server;

	private static boolean isReady() {
		return server != null;
	}

	@RequestMapping("/list")
	public Response playerList(@RequestParam(name = "uuid", defaultValue = "false", required = false) boolean wantsUuid) {
		// make sure the controller is ready
		if (!isReady()) {
			return new Response(false, new LinkedList<String>(Collections.singleton("Too early, try again later")), new LinkedList<String>(), new LinkedHashMap<String, Object>());
		}

		// initializing an empty response
		Response response = new Response();

		// list of players
		List<String> players = new LinkedList<String>();
		response.getResult().put("players", players);

		for (ServerPlayerEntity player: PlayerLookup.all(server)) {
			players.add(wantsUuid ? player.getUuidAsString() : player.getDisplayName().getString());
		}

		return response;
	}

	public static MinecraftServer getServer() {
		return server;
	}

	public static void setServer(MinecraftServer server) {
		PlayerController.server = server;
	}
}